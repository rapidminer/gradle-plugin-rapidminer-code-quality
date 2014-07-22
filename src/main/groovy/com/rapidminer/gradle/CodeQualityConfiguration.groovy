package com.rapidminer.gradle;

/**
 *
 * @author Nils Woehler
 *
 */
public class CodeQualityConfiguration {

	/**
	 * The basic root project's configuration directory.
	 */
	String configDir = "config/"

	// ############### Java Header ##################
	
	/**
	 * Defines whether the header check tasks should be added.
	 */
	boolean headerCheck = true

	/**
	 * The path to the file that contains the license header. 
	 * The path should be relative to the root folder of the root project. 
	 * It will be used to check the file header if {@link #addHeaderChecks} is 
	 * set to <code>true</code>.
	 */
	String headerFile = "config/HEADER"
	
	/**
	 * Defines whether to ignore header errors
	 */
	boolean headerCheckIgnoreErrors = false

	// ############### Codenarc ##################
	
	/**
	 * Defines whether the CodeNarc plugin should be applied. Default: true.
	 * Can be overwritten by defining the project property 
	 * 'codenarc' (e.g. 'gradle check -P codenarc=false').
	 *
	 * CodeNarc will only be  applied for project that also apply the Groovy plugin.
	 */
	boolean codenarc = true
	
	/**
	 * Defines whether to use the shipped Codenarc config file.
	 */
	boolean codenarcUseDefaultConfig = true

	/**
	 * Defines the name of the Codenarc configuration file.
	 * It has to be placed in $configDir/codenarc/
	 */
	String codenarcConfigFileName = "codenarc.conf"

	/**
	 * Defines whether a build should fail on Codenarc errors.
	 */
	boolean codenarcIgnoreErrors = true

	// ############### Checkstyle ##################
	
	/**
	 * Defines if the checkstyle plugin should be applied.
	 * Can be overwritten by defining the project property 
	 * 'checkstyle' (e.g. 'gradle check -P checkstyle=false').
	 */
	boolean checkstyle = true
	
	/**
	 * Defines whether to use the shipped checkstyle config file.
	 */
	boolean checkstyleUseDefaultConfig = true

	/**
	 * Defines the name of the checkstyle configuration file.
	 * It has to be placed in $configDir/checkstyle/
	 */
	String checkstyleConfigFileName = "checkstyle.xml"

	/**
	 * Defines whether a build should fail on checkstyle errors.
	 */
	boolean checkstyleIgnoreErrors = true
	
	// ############### JDepend ##################

	/**
	 * Defines if the JDepend plugin should be applied. Default is: true.
	 * Can be overwritten by defining the project property 
	 * 'jdepend' (e.g. 'gradle check -P jdepend=true').
	 */
	boolean jdepend = false
	
	/**
	 * Defines whether JDepend errors should be ignored. Default is: true
	 */
	boolean jdependIgnoreErrors = true

	
	// ############### FindBugs ##################
	
	/**
	 * Defines if the JDepend plugin should be applied. Default is: true.
	 * Can be overwritten by defining the project property 
	 * 'findbugs' (e.g. 'gradle check -P findbugs=true').
	 */
	boolean findbugs = false
	
	/**
	 * Defines whether FindBugs errors should be ignored. Default is: true
	 */
	boolean findbugsIgnoreErrors = true
}
