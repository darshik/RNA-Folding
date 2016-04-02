package de.unibi.bibiserv.rnamovies.configuration;

import java.util.EventListener;

public interface ConfigListener extends EventListener {
		public void configurationChanged(ConfigChangedEvent e);
}
