package com.rapidminer.gradle

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.OutputFile;
import java.nio.file.Files;

class InitCheckstyleFiles extends DefaultTask {

	File configDir
	File checkstyleFile
	
	@TaskAction
	private void copyCheckstyleConfigFiles() {
		// check if configDir folder exists, otherwise create it
		if(!configDir.exists()) {
			configDir.mkdirs()
		}
		
		// write checkstyle.xml
		getClass().getResource("checkstyle.xml").withInputStream { ris -> checkstyleFile.write ris.text }

		// write checkstyle.xsl
		def checkstyleTransformerFile = new File(configDir.absolutePath, "checkstyle.xsl")
		getClass().getResource("checkstyle.xsl").withInputStream { ris -> checkstyleTransformerFile.write ris.text }
	}
}
