package es.osoco.pokayoke

import org.aspectj.lang.JoinPoint

class PokaYokesListener {

	static PokaYokesConfigFactory configFactory

	static void onViolation(PokaYoke pokaYoke, JoinPoint joinPoint) {
		def config = configFactory.getConfig(pokaYoke)
		if (config.enabled) {
			def violation = new PokaYokeViolation(pokaYoke, config, joinPoint)
		 	config.actionOnViolation(violation)
		}
	}

}
