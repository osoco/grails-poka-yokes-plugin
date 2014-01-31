package es.osoco.pokayoke.grails;

import es.osoco.pokayoke.xpi.grails.GormPointCuts;
import es.osoco.pokayoke.xpi.grails.GrailsPointCuts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.runtime.callsite.CallSite;

public aspect GormLayeringViolation extends AbstractPokaYoke {

    private static final Log LOGGER = LogFactory.getLog(GormLayeringViolation.class);

    public pointcut violation() :
    	GrailsPointCuts.isDomain() &&
    	GormPointCuts.gormMethod() &&
    	!cflowbelow(GrailsPointCuts.serviceMethod());

    public Log getLogger() {
	return LOGGER;
    }

    public String getDescription() {
	return "Gorm method called outside the Service layer.";
    }

}
