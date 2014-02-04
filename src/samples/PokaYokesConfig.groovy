import es.osoco.pokayoke.PokaYokeViolation
import es.osoco.pokayoke.PokaYokeViolationException

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

pokaYokes {

	global {
		// Enable globally all poka-yokes
		enabled = false

		loggingOnViolation = true
		loggingMessage = { PokaYokeViolation violation -> 
			"Poka-Yoke violation: ${violation.description} \n" + StackTraceUtil.sanitizedStack()
		}
		loggingCategoryName = { PokaYokeViolation violation -> 
			"GRAILS-POKA-YOKES.${violation.name}" 
		}
		loggingPriority = { PokaYokeViolation violation -> 
			violation.priority
		}
		
		exceptionOnViolation = true
		exceptionMessage = { PokaYokeViolation violation -> 
			"Poka-Yoke violation: ${violation.name}"
		}
		
		jmxOnViolation = true

		actionOnViolation = { PokaYokeViolation violation ->
			LogFactory.getLog("GRAILS-POKA-YOKES").error("violation received!!!!")
		}

	}

	rules {
		
	}

}