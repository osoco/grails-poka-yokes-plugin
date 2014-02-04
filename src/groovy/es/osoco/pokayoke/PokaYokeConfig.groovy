package es.osoco.pokayoke

import groovy.util.ConfigObject

class PokaYokeConfig {

	private ConfigObject config
	private PokaYoke pokayoke

	public PokaYokeConfig(ConfigObject config, PokaYoke pokayoke) {
		this.config = config
		this.pokayoke = pokayoke
	}

	public ConfigObject getConfig() {
		config
	}

	public ConfigObject getPokaYokeConfig() {
		config.rules."${pokayoke.name}"
	}

	public boolean isEnabled() {
		getConfigProperty('enabled')
	}

	public boolean isLoggingOnViolation() {
		getConfigProperty('loggingOnViolation')
	}

	public boolean isExceptionOnViolation() {
		getConfigProperty('exceptionOnViolation')
	}

	public boolean isJmxOnViolation() {
		getConfigProperty('jmxOnViolation')
	}

	public Closure getLoggingMessage() {
		getConfigProperty('loggingMessage')
	}

	public Closure getLoggingCategoryName() {
		getConfigProperty('loggingCategoryName')
	}

	public Closure getLoggingPriority() {
		getConfigProperty('loggingPriority')
	}

	public Closure getExceptionMessage() {
		getConfigProperty('exceptionMessage')
	}

	public Closure getActionOnViolation() {
		getConfigProperty('actionOnViolation')
	}

	public Object getConfigProperty(String propertyName) {
		pokaYokeConfig."$propertyName" ?: config.pokaYokes.global."$propertyName"
	}

}