package com.rapidminer.gradle.codenarc

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.OutputFile;
import java.nio.file.Files;

class InitCodenarcConfigFiles extends DefaultTask {

	private static final CODENARC_CONF = 'codenarc.conf'
	
	File configDir
	String codenarcFileName
	
	@TaskAction
	private void copyCheckstyleConfigFiles() {
		// check if configDir folder exists, otherwise create it
		if(!configDir.exists()) {
			configDir.mkdirs()
		}
		
		File codenarcFile = new File(configDir.absolutePath, codenarcFileName)
		
		// write codenarc.conf
		getClass().getResource(CODENARC_CONF).withInputStream { ris -> codenarcFile.write ris.text }
	}
}
