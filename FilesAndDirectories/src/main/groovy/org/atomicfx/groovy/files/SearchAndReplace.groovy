package org.atomicfx.groovy.files;

// File: LogSlf4j.groovy
// Add dependencies for Slf4j API and Logback
//@Grapes([
//	@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1'),
//	@Grab(group='ch.qos.logback', module='logback-classic', version='0.9.28')
//])

import groovy.util.logging.Slf4j
/**
 * Structured to run as Java Application
 * @author arichards
 *
 */
@Slf4j
class GroovyClass {

	static main(args) {

		def currentDir = new File(".");

		def backupFile;
		def fileText;

		//Replace the contents of the list below with the
		//extensions to search for
		def exts = [".txt", ".foo", ".test"]

		//Replace the value of srcExp to a String or regular expression
		//to search for.
		def srcExp = "awesome"

		//Replace the value of replaceText with the value new value to
		//replace srcExp
		def replaceText = "-awesome-"

		// recursively descend directory processing files
		currentDir.eachFileRecurse(
				{file ->
					log.info("FILE: {}",file.getCanonicalPath());
					for (ext in exts){
						if (file.name.endsWith(ext)) {
							log.info("Processing file: {}", file.getName());
							fileText = file.text;
							backupFile = new File(file.path + ".bak");
							backupFile.write(fileText);
							fileText = fileText.replaceAll(srcExp, replaceText)
							file.write(fileText);
						}
					}
				}
				);
		currentDir.eachDirRecurse { dir ->
			log.info("DIR: {} ",dir.getCanonicalPath())
		}


	}

}
