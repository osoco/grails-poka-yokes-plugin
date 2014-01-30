/*
 * ======================================================================
 *
 *    _ \   __|  _ \   __|  _ \ 
 *   (   |\__ \ (   | (    (   |    
 *  \___/ ____/\___/ \___|\___/ 
 *
 * Copyright (c) 2013 OSOCO. http://www.osoco.
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
includeTargets << grailsScript("_GrailsEvents")

def pokaYokesConfig

target('weaveAspects': "Weave poka-yoke aspects") {
	depends(pokaYokesConfiguration)
    if (isAspectWeavingEnabled()) {
        doWeaving()
    } else {
        event("StatusFinal", ["Weaving aspects...Disabled!"])
    }
}

target('doWeaving': "") {
    event("StatusUpdate", ["Weaving aspects..."])
    ant.taskdef(name: 'iajc', classname: 'org.aspectj.tools.ant.taskdefs.AjcTask')   
    ant.path(id: "iajc.classpath", runtimeClasspath)
	// TODO: Use pokaYokesConfig options
    ant.iajc(classpathref: "iajc.classpath", 
	        destdir: grailsSettings.classesDir, 
            source: '1.5', 
	        showWeaveInfo: true) {
    	inpath() {
    	    pathelement(path: grailsSettings.classesDir)
    	}
    	sourceRoots() {
    	    pathelement(path: "${pluginBasedir}/src/aspectj")
    	}
    }
    event("StatusFinal", ["Weaving aspects...OK!"])
}

target('pokaYokesConfiguration': 'Parse Poka Yokes configuration') {
    if (!pokaYokesConfig) {
		event('StatusFinal', ['Configuring Poka-Yokes plugin...'])
		// TODO: Merge DefaultPokaYokesConfig with custom PokaYokesConfig
		def configPath = "${grailsSettings.baseDir}/grails-app/conf/PokaYokesConfig.groovy"
		pokaYokesConfig = new ConfigSlurper().parse(new URL("file://${configPath}"))
    }
}

isAspectWeavingEnabled = {
    def isDisabledByProperty = 
	    Boolean.valueOf(System.getProperty('DISABLE_POKA_YOKE', 'false'))
	// TODO: Use pokaYokesConfig.enablePokaYokesClosure
    ((grailsSettings.grailsEnv != 'PRODUCTION') && (!isDisabledByProperty))
}
