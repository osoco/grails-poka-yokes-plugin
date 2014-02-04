package es.osoco.pokayoke

import groovy.util.ConfigObject

class PokaYokesConfigFactory {

	static ConfigObject pokaYokesConfig

	static PokaYokeConfig getConfig(PokaYoke pokaYoke) {
	    new PokaYokeConfig(pokaYokesConfig, pokaYoke)
	}

}
