package es.osoco.pokayoke.grails;

import es.osoco.pokayoke.PokaYokePriority;
import es.osoco.pokayoke.xpi.grails.GroovyPointCuts;

import org.codehaus.groovy.runtime.callsite.CallSite;
import org.hibernate.collection.PersistentCollection;

public aspect GormCollectionSize extends AbstractPokaYoke {

    public PokaYokePriority getPriority() {
        return PokaYokePriority.HIGH;
    }

    public String getDescription() {
        return "The size() method should be avoided on Hibernate mapped collections. " +
            "Instead you can use the xxxCount property injected into the domain class.";
    }

    public pointcut violation() :
        sizeOnPersistentCollection(Object, CallSite);

    public pointcut sizeOnPersistentCollection(Object receiver, CallSite callsite) :
        within(es.osoco.bbva.betatesting..*) && GroovyPointCuts.groovyCall(receiver, callsite) &&
        if ((receiver instanceof PersistentCollection) && ("size".equals(callsite.getName())));

}
