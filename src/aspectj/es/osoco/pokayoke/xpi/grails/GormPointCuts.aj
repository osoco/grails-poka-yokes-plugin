package es.osoco.aop.grails.xpi;

import org.codehaus.groovy.runtime.callsite.CallSite;

public aspect GormPointCuts {

    public pointcut saveMethod() :
	execution(public Object save()) ||
	execution(public Object save(java.util.Map)) ||
	execution(public Object save(boolean));

    public pointcut gormMethod() :
	saveMethod();

}
