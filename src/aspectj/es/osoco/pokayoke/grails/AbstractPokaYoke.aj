package es.osoco.pokayoke.grails;

import es.osoco.pokayoke.PokaYoke;
import es.osoco.pokayoke.PokaYokesListener;
import es.osoco.pokayoke.util.StackTraceUtil;

import org.apache.commons.logging.Log;
import org.aspectj.lang.JoinPoint;

public abstract aspect AbstractPokaYoke implements PokaYoke {

    public abstract pointcut violation();

    public abstract String getDescription();

    public String getName() {
        return getClass().getSimpleName();
    }

    before() : violation() {
        warn(thisJoinPoint);
    }

    protected void warn(JoinPoint joinPoint) {
        PokaYokesListener.onViolation(this, joinPoint);
    }

    // protected boolean shouldThrowExceptionOnViolation() {
    //     return false;
    // }

    // protected void warn(JoinPoint joinPoint) {
    //     String msg = "Poka-Yoke Violation: " + getDescription();
    //     warn(msg);
    // }

    // protected void warn(String msg) {
    //     if (shouldThrowExceptionOnViolation()) {
    //         RuntimeException ex = new PokaYokeViolationException(msg);
    //         getLogger().error(msg, ex);
    //         throw ex;
    //     } else {
    //         getLogger().error(msg + "\n" + StackTraceUtil.sanitizedStack());
    //     }
    // }

}
