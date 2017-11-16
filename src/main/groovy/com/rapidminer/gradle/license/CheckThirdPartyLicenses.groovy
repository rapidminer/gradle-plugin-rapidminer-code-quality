package com.rapidminer.gradle.license

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

import java.security.MessageDigest

class CheckThirdPartyLicenses extends DefaultTask {

    def excludedArtifacts
    def excludeArtifactsFromFolder
    def thirdPartyLicenseDir = "$project.projectDir/third-party-licenses/"
    def generatedThirdPartyLicenses = "$project.buildDir/reports/license/thirdparty.licenses"
    def savedThirdPartyLicenses = "$project.projectDir/src/test/resources/thirdparty.licenses"

    static calculateHash(file) {
        MessageDigest.getInstance("MD5").digest(file.text.bytes).encodeHex().toString()
    }

    @TaskAction
    private void runCheck() {
        loadLicenses()
        checkThirdPartyLicenses()
    }

    def loadLicenses() {

        if (excludeArtifactsFromFolder) {
            excludeArtifactsFromFolder.eachFile() { file ->
                excludedArtifacts << file.name - ".license"
            }
        }

        def dependenciesJSON = project.file("$project.buildDir/reports/license/dependency-license.json")
        if (dependenciesJSON.exists()) {
            def list = new JsonSlurper().parseText(dependenciesJSON.text)

            def licenseSet = new TreeSet<String>()
            list.dependencies.each { dependency ->
                def nameParts = dependency.name.split(":")
                // using the artifact name without group and version
                def artifactName = nameParts[1]
                def isIncludedArtifact = !(artifactName in excludedArtifacts)
                if (isIncludedArtifact) {
                    def licenseNames = dependency.licenses.name
                    def licensesAsStr = licenseNames.join("; ")
                    def licenseString = "${artifactName} = ${licensesAsStr}"
                    licenseSet.add(licenseString)
                }
            }

            project.logger.info "Creating file with ${licenseSet.size()} third party licenses..."
            def thirdPartyLicenses = project.file(generatedThirdPartyLicenses)
            thirdPartyLicenses.createNewFile()
            thirdPartyLicenses.text = licenseSet.join("\n")
            project.logger.info "Done!"
        }
    }

    def checkThirdPartyLicenses() {

        def actualLicenses = project.file(generatedThirdPartyLicenses)
        def lines = actualLicenses.readLines()
        def violations = new LinkedList<String>()
        lines.each { String line ->
            def prefix = line.split("=")[0].trim()
            def licenseFile = project.file("${thirdPartyLicenseDir}/${prefix}.license")
            if (!licenseFile.exists()) {
                def shorterPath = "- ${licenseFile.parentFile.parentFile.name}/${licenseFile.parentFile.name}/${licenseFile.name}"
                violations.add(shorterPath)
            }
        }
        if (!violations.isEmpty()) {
            def missingLicenses = violations.join("\n")
            throw new GradleException("\nLicenses check failed! There are missing ${violations.size()} third party license files:\n" +
                    "${missingLicenses}\n\n" +
                    "See ${generatedThirdPartyLicenses} for more details.")
        }

        def savedLicenses = project.file(savedThirdPartyLicenses)
        if (!savedLicenses.exists()) {
            throw new GradleException("Licenses check failed! Could not check third party licenses because of missing file:\n${savedThirdPartyLicenses}")
        }

        def actualLicensesHash = calculateHash(actualLicenses)
        def savedLicensesHash = calculateHash(savedLicenses)
        if (savedLicensesHash != actualLicensesHash) {
            throw new GradleException("Licenses check failed! Manual check of the licenses' content is required because there are a differences between\n" +
                    "${savedThirdPartyLicenses}\nand\n${generatedThirdPartyLicenses}")
        }
        project.logger.info "Third party licenses check was successful!"

    }

}
