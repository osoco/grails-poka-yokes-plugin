package es.osoco.pokayoke.util;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public abstract class StackTraceUtil {

    private static final int DEFAULT_STACK_DEPTH = 4;

    private static final List<String> IGNORED_PACKAGES = Arrays.asList(
        "com.springsource.loaded.",
        "groovy.lang.",
        "java.lang.",
        "sun.",
        "org.codehaus.groovy.reflection.",
        "org.codehaus.groovy.runtime.",
        "es.osoco.pokayoke.grails.",
        "es.osoco.pokayoke.util.",
        "es.osoco.pokayoke.xpi.",
		"es.osoco.pokayoke.PokaYokesListener",
		"DefaultPokaYokesConfig"
    );


    public static String sanitizedStack() {
	return sanitizedStack(DEFAULT_STACK_DEPTH);
    }

    public static String sanitizedStack(int depth) {
	List<StackTraceElement> elements = new ArrayList<StackTraceElement>();
	for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
	    if (element.getLineNumber() < 1) {
		continue;
	    }
	    boolean ignore = false;
	    for (String ignoredPackage : IGNORED_PACKAGES) {
		if (element.getClassName().startsWith(ignoredPackage)) {
		    ignore = true;
		    break;
		}
	    }
	    if (!ignore) {
		elements.add(element);
	    }
	}
	StringBuilder sb = new StringBuilder();
	int i = 0;
	for (StackTraceElement element : elements) {
	    if (i++ > depth) {
		break;
	    }
	    sb.append("\tat ").append(element).append('\n');
	}
	sb.append("\t...").append('\n');
	return sb.toString();
    }

}
