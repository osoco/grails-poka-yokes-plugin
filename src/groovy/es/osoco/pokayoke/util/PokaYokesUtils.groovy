package es.osoco.pokayoke.util

import grails.util.Environment
import groovy.lang.GroovyClassLoader
import groovy.util.ConfigObject
import groovy.util.ConfigSlurper

class PokaYokesUtils {

	private static ConfigObject pokaYokesConfig

	public static synchronized ConfigObject getPokaYokesConfig() {
		if (pokaYokesConfig== null) {
			loadPokaYokesConfig()
		}
		pokaYokesConfig
	}

	public static void loadPokaYokesConfig() {
		pokaYokesConfig = mergeConfig("DefaultPokaYokesConfig", "PokaYokesConfig")
	}


public static ConfigObject mergeConfig(String defaultConfigClassName, String appConfigClassName) {
		mergeConfig(loadConfig(defaultConfigClassName), loadConfig(appConfigClassName))
	}

	public static ConfigObject mergeConfig(ConfigObject currentConfig, ConfigObject overridenConfig) {
		ConfigObject config = new ConfigObject();
		if (overridenConfig == null) {
			config.putAll(currentConfig);
		} else {
			config.putAll(currentConfig.merge(overridenConfig));
		}
		config
	}

	public static ConfigObject loadConfig(String configClassName) {
		ConfigObject config
		GroovyClassLoader classLoader = new GroovyClassLoader(PokaYokesUtils.class.getClassLoader())
		ConfigSlurper slurper = new ConfigSlurper(Environment.getCurrent().getName())
		try {
			config = slurper.parse(classLoader.loadClass(configClassName))
		} catch (ClassNotFoundException e) {
			// TODO fix this
			throw new RuntimeException(e);
		}
		config
	}

}