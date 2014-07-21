package com.rapidminer.gradle.checkstyle

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.OutputFile;
import java.nio.file.Files;

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
