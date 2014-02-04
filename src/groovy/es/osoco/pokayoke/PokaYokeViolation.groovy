package es.osoco.pokayoke

import org.aspectj.lang.JoinPoint

class PokaYokeViolation {

	private PokaYoke pokaYoke
	private PokaYokeConfig config
	private JoinPoint joinPoint

	public PokaYokeViolation(PokaYoke pokaYoke, PokaYokeConfig config, JoinPoint joinPoint) {
		this.pokaYoke = pokaYoke
		this.config = config
		this.joinPoint = joinPoint
	}

	public PokaYoke getPokaYoke() {
		pokaYoke
	}

	public PokaYokeConfig getConfig() {
		config
	}

	public JoinPoint getJoinPoint() {
		joinPoint
	}

}
