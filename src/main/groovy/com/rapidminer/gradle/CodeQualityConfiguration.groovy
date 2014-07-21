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
	
	/**
	 * The name of the file that contains the license header. 
	 * It has to be placed in the folder defined by {@link #configDir}.
	 */
	String headerFileName = "HEADER.txt"

	/**
	 * Defines whether to use the shipped Codenarc config file.
	 */
	boolean codenarcUseDefaultConfig = true
	
	/**
	 * Defines the name of the Codenarc configuration file.
	 * It has to be placed in $configDir/codenarc/
	 */
	String codenarcFileName = "codenarc.conf"
	
	/**
	 * Defines whether a build should fail on Codenarc errors.
	 */
	boolean codenarcIgnoreErrors = true
	
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
	
	boolean jdependDefaultConfig = true
	String jdependFileName = "jdepend.conf"
	boolean jdependIgnoreErrors = true
	
	boolean findBugsUseDefaultConfig = true
	String findbugsFileName = "findbugs.conf"
	boolean findbugsIgnoreErrors = true
}
