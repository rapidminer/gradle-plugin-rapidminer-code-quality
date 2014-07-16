## Introduction

The 'rapidminer-code-quality' plugin is designed to add tasks for automatic code quality checks. The checks are performed via checkstyle (http://checkstyle.sourceforge.net/).

The plugin ships with a preconfigured checkstyle configuration which is used by default.

## How to use
	buildscript { 
		dependencies { 
			classpath 'com.rapidminer.gradle:code-quality:$VERSION'
		} 
	}
	 
	apply plugin: 'rapidminer-code-quality'
	 
	codeQuality {
	 
		/*
		 * Defines the directory which contains the checkstyle
		 * config file and java header file
		 */
		configDir = "config/checkstyle/"
	 
		/*
		 * Defines the name of the checkstyle config file
		 */
		configFileName = "checkstyle.xml"
	 
		/*
		 * Defines the name of the file that contains the Java header
		 */
		javaHeaderFileName = "java.header"
	 
		/*
		 *  If set to true, the default checkstyle config file will copied 
		 *  to the location specified by configDir + configFileName each time 
		 *  before the code quality the test is performed
		 */
		useDefaultConfig = true
		
		/*
		 * If set to true, errors found by code quality check will not cause 
		 * the build to fail 
		 */
		ignoreErrors = false
	}