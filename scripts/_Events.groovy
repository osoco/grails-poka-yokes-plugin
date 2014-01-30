/*
 * ======================================================================
 *
 *    _ \   __|  _ \   __|  _ \ 
 *   (   |\__ \ (   | (    (   |    
 *  \___/ ____/\___/ \___|\___/ 
 *
 * Copyright (c) 2014 OSOCO. http://osoco.es
 *
 * ======================================================================
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.codehaus.gant.GantBinding

includeTargets << new File(pokaYokesPluginDir, "scripts/_PokaYokes.groovy")

eventCompileStart = { GantBinding compileBinding ->
	
    if (isAspectWeavingEnabled()) {
        // Avoid the weaving error caused when aspects are applied to already weaved classes. 
        // The root cause is eventCompileEnd event is fired more than once while running integration tests (GRAILS-9741).
        fullClean(false)
    }
}

eventCompileEnd = { GantBinding compileBinding ->
	weaveAspects()
}

// Private methods --------------------------------------------------------------------------------

private void fullClean(boolean deleteDirs) {
    event("StatusUpdate", ["Cleaning before compile"])    
    clean()
    ant.delete(failonerror: false, includeemptydirs: deleteDirs) {
        fileset(dir: grailsSettings.projectTargetDir, includes: "**/*") 
    }
    event("StatusFinal", ["Cleaned up target dir"])
}

private void weaveAspects() {
    event("StatusUpdate", ["Weaving aspects..."])

    ant.taskdef(name: 'iajc', classname: 'org.aspectj.tools.ant.taskdefs.AjcTask')   
    ant.path(id: "iajc.classpath", runtimeClasspath)
    ant.iajc(classpathref: "iajc.classpath", destdir: grailsSettings.classesDir, 
            source: '1.5', showWeaveInfo: true) {
    	inpath() {
    	    pathelement(path: grailsSettings.classesDir)
    	}
    	sourceRoots() {
    	    pathelement(path: grailsSettings.baseDir.path + '/src/aspect')
    	}
    }

    event("StatusFinal", ["Weaving aspects...OK!"])
}

private boolean isAspectWeavingEnabled() {
    def isDisabledByProperty = Boolean.valueOf(System.getProperty('DISABLE_POKA_YOKE', 'false'))
    ((grailsSettings.grailsEnv != 'PRODUCTION') && (!isDisabledByProperty))
}








eventTestPhaseStart = { phaseName ->
    if ((phaseName == 'functional') && argsMap['zap']) {
	event('StatusFinal', ['Running Security Tests using OWASP ZAP Proxy...'])
	runningSecurityTests = true
	customFunctionalTestPhaseCleanUp()
	setZapProxyProperties()
	argsMap.daemon ? startZapDaemon() : startZapUI()
    }
}

eventTestPhaseEnd = { phaseName ->
    if ((phaseName == 'functional') && runningSecurityTests) {
	def baseUrl = System.getProperty(grailsSettings.FUNCTIONAL_BASE_URL_PROPERTY)
	spiderUrl(baseUrl)
	activeScanUrl(baseUrl)
	storeSession()

	try {
	    checkAlerts()
	} catch (e) {
	    securityTestsFailed = true
	    throw e
	} finally {
	    zapTestPhaseCleanUp()

	    String label = securityTestsFailed ? "Security Tests FAILED" : "Security Tests PASSED"
	    String msg = ""
	    if (createTestReports) {
		event("TestProduceReports", [])
		msg += " - ZAP session stored"
	    }
	    if (securityTestsFailed) {
		grailsConsole.error(label, msg)
	    }
	    else {
		grailsConsole.addStatus("$label$msg")
	    }
	}
    }
}

zapTestPhaseCleanUp = {
    stopZapProcess()
    originalFunctionalTestPhaseCleanUp()
    runningSecurityTests = false
}

voidFunctionalTestPhaseCleanUp = {}

customFunctionalTestPhaseCleanUp = {
    originalFunctionalTestPhaseCleanUp = owner.functionalTestPhaseCleanUp
    owner.functionalTestPhaseCleanUp = voidFunctionalTestPhaseCleanUp
}
