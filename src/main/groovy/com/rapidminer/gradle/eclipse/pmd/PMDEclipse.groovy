/*
 * Copyright 2013-2014 RapidMiner GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rapidminer.gradle.eclipse.pmd

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Copies PMD Eclipse Plugin config files to their appropriate locations.
 *
 * @author Nils Woehler
 *
 */
class PMDEclipse extends DefaultTask {

	private static final String LOCATION = '_LOCATION_'
	private static final String PMD = 'pmd'
	public static final String RULESET_FILENAME = 'ruleset'

	File rulesetFile

	@TaskAction
	private void copyCheckstyleConfigFiles() {

		String pmdConfigText = ''

		// load PMD config template
		getClass().getResource(PMD).withInputStream { ris ->  pmdConfigText = ris.text }

		// Check if config folder exists
		File rulesetParentDir = new File(rulesetFile.parent)
		if(!rulesetParentDir.exists()) {
			rulesetParentDir.mkdirs()
		}

		// Write ruleset file to disk
		getClass().getResource(RULESET_FILENAME).withInputStream { ris ->  rulesetFile.write ris.text }

		// add PMD ruleset location to config text
		pmdConfigText =	pmdConfigText.replaceAll(LOCATION, rulesetFile.absolutePath.replaceAll('\\\\', '\\\\\\\\'))

		// write PMD settings to file
		project.file('.' + PMD).write pmdConfigText

	}
}
