package com.rapidminer.gradle

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.OutputFile;
import java.nio.file.Files;

class InitCheckstyleFiles extends DefaultTask {

	@OutputFile
	checkstyleConfigOutput = project.file(project.buildDir.absolutePath + "/checkstyle/checkstyle.xml")
	
	@OutputFile
	checkstyleTransformerFile = project.file(project.buildDir.absolutePath + "/checkstyle/checkstyle.xsl")
	
	@TaskAction
	private void copyCheckstyleConfigFiles() {
		// check if $buildDir/checkstyle folder exists, otherwise create it
		def parent = new File(checkstyleConfigOutput.parent)
		if(!parent.exists()) {
			parent.mkdirs()
		}
		// write checkstyle.xml
		getClass().getResource("checkstyle.xml").withInputStream { ris -> checkstyleConfigOutput.write ris.text }

		// write checkstyle.xsl		
		getClass().getResource("checkstyle.xsl").withInputStream { ris -> checkstyleTransformerFile.write ris.text }
	}
}
