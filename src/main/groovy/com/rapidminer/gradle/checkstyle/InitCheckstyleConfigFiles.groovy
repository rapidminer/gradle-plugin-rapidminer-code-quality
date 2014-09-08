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
package com.rapidminer.gradle.checkstyle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class InitCheckstyleConfigFiles extends DefaultTask {

	private static final CHECKSTYLE_XML = 'checkstyle.xml'
	private static final CHECKSTYLE_XSL = 'checkstyle.xsl'

	File configDir
	String checkstyleFileName

	@TaskAction
	private void copyCheckstyleConfigFiles() {
		// check if configDir folder exists, otherwise create it
		if(!configDir.exists()) {
			configDir.mkdirs()
		}

		File checkstyleFile = new File(configDir.absolutePath, checkstyleFileName)

		// write checkstyle.xml
		getClass().getResource(CHECKSTYLE_XML).withInputStream { ris -> checkstyleFile.write ris.text }

		// write checkstyle.xsl
		def checkstyleTransformerFile = new File(configDir.absolutePath, CHECKSTYLE_XSL)
		getClass().getResource(CHECKSTYLE_XSL).withInputStream { ris -> checkstyleTransformerFile.write ris.text }
	}
}
