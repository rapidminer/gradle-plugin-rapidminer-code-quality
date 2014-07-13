package com.rapidminer.gradle

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.SourceSet;

class PrependHeaderTask extends DefaultTask {
	
	SourceSet sourceSet
	File headerFile
	
	@TaskAction
	private void copyCheckstyleConfigFiles() {
		// load header text
		def headerText = headerFile.text
		
		// iterate over .java files
		def javaFiles = sourceSet.allJava.include '**/*.java'
		javaFiles.visit { element ->
			if(!element.file.isDirectory()) {
				print element.file.name + "... "
				
				// load java file and search for package definition
				def fileText = element.file.text
				int index = fileText.indexOf("package")
				if(index == -1) {
					println "No package definition"
					return
				} else {
					def newContent = headerText + fileText.substring(index)
					element.file.write newContent
					println "Done"
				}
			}
		}
	}
}
