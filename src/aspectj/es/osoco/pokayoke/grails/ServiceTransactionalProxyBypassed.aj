package es.osoco.aop.grails.pokayoke;

import es.osoco.aop.grails.pokayoke.util.StackTraceUtil;
import es.osoco.aop.grails.xpi.GrailsPointCuts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.codehaus.groovy.runtime.callsite.CallSite;
import org.hibernate.collection.PersistentSet;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

public aspect ServiceTransactionalProxyBypassed extends AbstractPokaYoke {

    private final Log LOGGER = LogFactory.getLog(ServiceTransactionalProxyBypassed.class);

    public pointcut annotatedPublicServiceMethod(Object receiver, Transactional tx) :
	GrailsPointCuts.publicServiceMethod() && @annotation(tx) && this(receiver);

    public pointcut violation() :
	proxyByPassed(Object, Transactional, Object, Transactional);

    public pointcut proxyByPassed(Object caller,
			 Transactional callerTxAnnotation,
			 Object callee,
			 Transactional calleeTxAnnotation) :
	cflowbelow(annotatedPublicServiceMethod(caller, callerTxAnnotation)) &&
	annotatedPublicServiceMethod(callee, calleeTxAnnotation) &&
	if ((caller.getClass() == callee.getClass()) && (!callerTxAnnotation.equals(calleeTxAnnotation)) && !isCalledThroughProxy());

    // before(Object caller, Transactional callerTxAnnotation, Object callee, Transactional calleeTxAnnotation) : test(caller, callerTxAnnotation, callee, calleeTxAnnotation) {

    // 	StackTraceElement[] elements = Thread.currentThread().getStackTrace();
    // 	StackTraceElement callerElement = elements[3];
    // 	boolean isCglibProxy = ClassUtils.isCglibProxyClassName(callerElement.getClassName());

    // 	System.out.println("Caller is -> " + callerElement);
    // 	System.out.println("Caller is CGLIB proxy? -> " + isCglibProxy);
    // 	if (!isCglibProxy) {
    // 	    logger.debug("--> Method in " + caller.getClass() + " with annotation " + callerTxAnnotation +
    // 	        " calls to "+ thisJoinPointStaticPart.getSignature() + " with annotation " + calleeTxAnnotation);
    // 	    warn(thisJoinPoint);
    // 	}
    // }

    public Log getLogger() {
	return LOGGER;
    }

    public String getDescription() {
	return "Service transactional proxy has been bypassed. \n" +
	    "Be careful when calling annotated methods within a service when the annotation settings are different. " +
	    "Because you’re underneath the proxy, it’s a direct method call, and any checks that the proxy would have " +
	    "done will be bypassed.";
    }

    private static boolean isCalledThroughProxy() {
	StackTraceElement[] elements = Thread.currentThread().getStackTrace();
	StackTraceElement callerElement = elements[3];
	boolean isCglibProxy = ClassUtils.isCglibProxyClassName(callerElement.getClassName());
	return isCglibProxy;
    }

}





