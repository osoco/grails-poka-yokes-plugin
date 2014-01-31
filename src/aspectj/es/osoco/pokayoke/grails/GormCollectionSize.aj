package es.osoco.pokayoke.grails;

import es.osoco.pokayoke.xpi.grails.GroovyPointCuts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.hibernate.collection.PersistentCollection;

public aspect GormCollectionSize extends AbstractPokaYoke {

    private static final Log LOGGER = LogFactory.getLog(GormCollectionSize.class);

    public pointcut violation() :
	sizeOnPersistentCollection(Object, CallSite);

    public pointcut sizeOnPersistentCollection(Object receiver, CallSite callsite) :
        within(es.osoco.bbva.betatesting..*) && GroovyPointCuts.groovyCall(receiver, callsite) &&
    	    if ((receiver instanceof PersistentCollection) && ("size".equals(callsite.getName())));

    public Log getLogger() {
    	return LOGGER;
    }

    public String getDescription() {
    	return "The size() method should be avoided on Hibernate mapped collections. " +
    	    "Instead you can use the xxxCount property injected into the domain class.";
    }

}
