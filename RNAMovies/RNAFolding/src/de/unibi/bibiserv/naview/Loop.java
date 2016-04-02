package de.unibi.bibiserv.naview;

import java.awt.geom.Point2D.Double;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Loop {
  protected int number = 0;
  protected double x = 9999.0;
  protected double y = 9999.0;
  private double r = 0.0;
  protected List<Connection> connections;
  private boolean mark = false;
  final static double rt2_2 = 0.7071068;
  final static double lencut = 0.5;

  protected Loop(int number) {
    this.number = number;
    this.connections = new ArrayList<Connection>();
  }

  protected Connection connect(int start, int end, Loop loop, Region region) {
    Connection connection = new Connection(start, end, loop, region);
    connections.add(connection);
    return connection;
  }

  protected int getDepth() {
    int count, ret, d;

    if(this.connections.size() <= 1)
      return 0;

    if(this.mark)
      return -1;

    this.mark = true;
    count = ret = 0;
    for(Connection connection:this.connections) {
      d = connection.loop.getDepth();
      if(d >= 0) {
        if (count++ == 0)
          ret = d;
        else if(ret > d)
          ret = d;
      }
    }
    this.mark = false;
    return ret + 1;
  }

  protected double getRadius(int nb) {
    double mindit, ci, dt, sumn, sumd, radius, dit;
    int i, j, end, start, imindit;
    Connection connection, cnext;

    if(r > 0.0)
      return r;

    imindit = 0;
    do {
      mindit = 1.0e10;
      for(sumd = 0.0, sumn = 0.0, i = 0;
          i < connections.size();
          i++) {
        connection = connections.get(i);
        j = i + 1;
        if (j >= connections.size())
          j = 0;
        cnext = connections.get(j);
        end = connection.end;
        start = cnext.start;
        if(start < end)
          start += nb + 1;
        dt = cnext.angle - connection.angle;
        if(dt <= 0.0)
          dt += 2*Math.PI;
        if(!connection.extruded)
          ci = start - end;
        else
          ci = dt <= Math.PI/2 ? 2.0 : 1.5;
        sumn += dt*(1.0/ci + 1.0);
        sumd += dt*dt/ci;
        dit = dt/ci;
        if(dit < mindit && !connection.extruded && ci > 1.0) {
          mindit = dit;
          imindit = i;
        }
      }
      radius = sumn/sumd;
      if(radius < rt2_2)
        radius = rt2_2;
      if(mindit*radius < lencut) {
        connections.get(imindit).extruded = true;
      }
    } while(mindit*radius < lencut);
    r = radius;
    return radius;
  }

  public Point2D getPosition() {
    return new Point2D.Double(x, y);
  }

  public double getRadius() {
    return r;
  }

  public Connection[] getConnections() {
    return connections.toArray(new Connection[]{});
  }

  public String toString() {
    return("Loop #" + number + ": " + connections.size() + " connections");
  }
}
