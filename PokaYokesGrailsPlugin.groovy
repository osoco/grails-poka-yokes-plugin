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
import es.osoco.pokayoke.PokaYokesConfigFactory
import es.osoco.pokayoke.PokaYokesListener
import es.osoco.pokayoke.util.PokaYokesUtils

class PokaYokesGrailsPlugin {

    def version = "0.1"
    def grailsVersion = "2.0 > *"
    def author = 'Rafael Luque'
    def authorEmail = 'info@osoco.es'
    def title = 'Grails Poka-Yokes Plugin'
    def description = 'TODO'
    def documentation = 'http://grails.org/plugin/poka-yokes'

    def license = 'APACHE'
    def organization = [ name: "OSOCO", url: "http://osoco.es" ]
    def developers = [ [ name: "Rafael Luque" ]]
    def scm = [url: "https://github.com/osoco/grails-poka-yokes-plugin/"]
    def issueManagement = [
        system: "GitHub", 
        url: "https://github.com/osoco/grails-poka-yokes-plugin/issues"
    ]

	def doWithSpring = {
		
		def conf = PokaYokesUtils.pokaYokesConfig
		if (!conf || !conf.pokaYokes.global.enabled) {
			println '\n\nGrails Poka-Yokes are disabled, not loading.\n\n'
			return
		}
		println 'Configuring Grails Poka-Yokes...'		

		pokaYokesConfigFactory(PokaYokesConfigFactory) {
			pokaYokesConfig = conf
		}

		pokaYokesListener(PokaYokesListener) {
			configFactory = ref('pokaYokesConfigFactory')
		}

	}

}
