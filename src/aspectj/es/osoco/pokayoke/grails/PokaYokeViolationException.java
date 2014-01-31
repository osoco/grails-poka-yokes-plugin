package es.osoco.pokayoke.grails;

public class PokaYokeViolationException extends RuntimeException {

    public PokaYokeViolationException() {
	super();
    }

    public PokaYokeViolationException(String message) {
	super(message);
    }

    public PokaYokeViolationException(String message, Throwable cause) {
	super(message, cause);
    }

    public PokaYokeViolationException(Throwable cause) {
	super(cause);
    }

}
