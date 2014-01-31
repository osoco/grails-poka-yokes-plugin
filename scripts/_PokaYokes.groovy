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

target('weavePokaYokes': "Weave poka-yoke aspects") {
	depends(configurePokaYokes)
    if (isPokaYokingEnabled()) {
        doWeaving()
    } else {
        event("StatusFinal", ["Weaving aspects...Disabled!"])
    }
}

/*
 * Avoid the weaving error caused when aspects are applied to already weaved classes.
 *
 * The root cause is eventCompileEnd event is fired more than once while running 
 * integration tests (GRAILS-9741).
 */
target('preCompilePokaYokes': "Clean compiled classes to avoid double-weaving") {
	depends(configurePokaYokes)
    if (isPokaYokingEnabled()) {
        cleanPokaYokes()
    }
}

target('doWeaving': "Compile-time weaving from aspect sources") {
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
    	    pathelement(path: "${pokaYokesPluginDir}/src/aspectj")
    	}
    }
    event("StatusFinal", ["Weaving aspects...OK!"])
}

target('configurePokaYokes': 'Parse Poka Yokes configuration') {
    if (!pokaYokesConfig) {
		event('StatusFinal', ['Configuring Poka-Yokes plugin...'])
		// TODO: Merge DefaultPokaYokesConfig with custom PokaYokesConfig
		def configPath = "${grailsSettings.baseDir}/grails-app/conf/PokaYokesConfig.groovy"
		pokaYokesConfig = new ConfigSlurper().parse(new URL("file://${configPath}"))
    }
}

isPokaYokingEnabled = {
	// TODO: Use pokaYokesConfig.enablePokaYokesClosure
    !isPokaYokingDisabledByProperty()
}

isPokaYokingDisabledByProperty = {
    Boolean.valueOf(System.getProperty('DISABLE_POKA_YOKING', 'false'))
}

cleanPokaYokes = {
    event("StatusUpdate", ["Cleaning classes before poka-yoking"])
    clean()
    ant.delete(failonerror: false, includeemptydirs: false) {
        fileset(dir: grailsSettings.projectTargetDir, includes: "**/*") 
    }
    event("StatusFinal", ["Cleaned up target dir before poka-yoking"])
}