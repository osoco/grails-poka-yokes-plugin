package es.osoco.pokayoke.xpi.grails;

import org.codehaus.groovy.runtime.callsite.CallSite;

public aspect GrailsPointCuts {

    public pointcut inService() :
	within(es.osoco..*Service);

    public pointcut isDomain() :
	@within(grails.persistence.Entity);

    public pointcut serviceMethod() :
	inService() && execution(* *.*(..));

    public pointcut publicServiceMethod() :
	inService() && execution(public * *.*(..));

    public pointcut inTest() :
	within(es.osoco..*Spec) || within(es.osoco..*Test);

}
