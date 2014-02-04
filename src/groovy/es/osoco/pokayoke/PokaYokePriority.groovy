package es.osoco.pokayoke

enum PokaYokePriority {

	HIGH('fatal'), MEDIUM('error'), LOW('warn')

	private String logLevel

	public PokaYokePriority(String logLevel) {
		this.logLevel = logLevel
	}

	public String getLogLevel() {
		logLevel
	}
}