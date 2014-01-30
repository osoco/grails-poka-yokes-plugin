package es.osoco.aop.grails.xpi;

import org.codehaus.groovy.runtime.callsite.CallSite;

public aspect GroovyPointCuts {

    public pointcut groovyCall(Object receiver, CallSite callsite) :
        call(public Object CallSite.call*(..)) && target(callsite) && args(receiver);

}
