import es.osoco.pokayoke.PokaYokeViolation
import es.osoco.pokayoke.PokaYokeViolationException
import es.osoco.pokayoke.util.StackTraceUtil

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

pokaYokes {

	global {
		// Enable globally all poka-yokes
		enabled = true

		loggingOnViolation = true
		loggingMessage = { PokaYokeViolation violation -> 
			"Poka-Yoke violation: ${violation.pokaYoke.description} \n" + StackTraceUtil.sanitizedStack()
		}
		loggingCategoryName = { PokaYokeViolation violation -> 
			"GRAILS-POKA-YOKES.${violation.pokaYoke.name}" 
		}
		loggingPriority = { PokaYokeViolation violation -> 
			violation.pokaYoke.priority.logLevel
		}
		
		exceptionOnViolation = true
		exceptionMessage = { PokaYokeViolation violation -> 
			"Poka-Yoke violation: ${violation.pokaYoke.name}"
		}
		
		jmxOnViolation = true

		actionOnViolation = { PokaYokeViolation violation ->
			def config = violation.config
			if (config.exceptionOnViolation) {
				RuntimeException pokaYokeEx = 
				    new PokaYokeViolationException(config.exceptionMessage(violation))
				LogFactory.getLog(config.loggingCategoryName(violation)).
				    error(config.loggingMessage(violation), pokaYokeEx)
				throw pokaYokeEx
			} else {
				if (config.loggingOnViolation) {
					LogFactory.getLog(config.loggingCategoryName(violation)).
                        "${config.loggingPriority(violation)}"(config.loggingMessage(violation))
				}
			}
			if (config.jmxOnViolation) {
			}
		}

	}

	rules {
		
	}

}