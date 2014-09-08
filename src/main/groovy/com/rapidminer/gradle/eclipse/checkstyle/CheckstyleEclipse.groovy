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
package com.rapidminer.gradle.eclipse.checkstyle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Creates proper checkstyle Eclipse Plugin config file that is placed in the project main folder.
 *
 * @author Nils Woehler
 *
 */
class CheckstyleEclipse extends DefaultTask {

	private static final String LOCATION = '_LOCATION_'

	private static final String CHECKSTYLE = 'checkstyle'

	String configDir
	String checkstyleFileName

	@TaskAction
	private void copyCheckstyleConfigFiles() {

		//		/rapidminer-license/config/checkstyle/checkstyle.xml
		String checkstyleConfigText = ''

		// load checkstyle config template
		getClass().getResource(CHECKSTYLE).withInputStream { ris ->  checkstyleConfigText = ris.text }

		String checkstyleFilePath = "/${project.rootProject.name}/${configDir}/checkstyle/${checkstyleFileName}".replaceAll('//', '/')
		checkstyleConfigText =	checkstyleConfigText.replaceAll(LOCATION, checkstyleFilePath)

		// write settings to file
		project.file('.' + CHECKSTYLE).write checkstyleConfigText
	}
}
