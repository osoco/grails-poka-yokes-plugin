package es.osoco.pokayoke.grails;

import es.osoco.pokayoke.PokaYokePriority;
import es.osoco.pokayoke.xpi.grails.GormPointCuts;
import es.osoco.pokayoke.xpi.grails.GrailsPointCuts;

import org.codehaus.groovy.runtime.callsite.CallSite;

public aspect GormLayeringViolation extends AbstractPokaYoke {

    public String getDescription() {
        return "Gorm method called outside the Service layer.";
    }

	public PokaYokePriority getPriority() {
		return PokaYokePriority.HIGH;
	}

    public pointcut violation() :
        GrailsPointCuts.isDomain() &&
        GormPointCuts.gormMethod() &&
        !cflowbelow(GrailsPointCuts.serviceMethod());

}
