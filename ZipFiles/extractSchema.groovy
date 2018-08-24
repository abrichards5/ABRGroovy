// Groovy Script to analyze the ISS zip file
// Author: Adam Richards

// import dependencies
import java.util.zip.CRC32
import java.util.zip.Checksum
import java.nio.file.Path
import java.nio.file.Paths

def configFile="groovy/sp11.groovy.config"


// parse lines from config file, based on section markers [ sectionname ], ignore comments
def parseConfigFile(cf) {
def rv=[:]
println "Reading config file: $cf"
def section=""
new File(cf).eachLine { line ->
	// check not a comment, and not whitespace
	if (!(line =~ /\s*#/) && !(line =~ /^\s*$/)) {
        // check for section flags in config file
        // looking for patterh [ word ] 
        def match = (line =~ /^\s*\[\s*(\w+)\s*\]\s*$/)
        if (match.hasGroup() && match.size() == 1) {
            // found section marker
            section = match[0][1]
            println "found section:'$section'"
            return // go to next line
        }
        if (section!="") {
            
            if(rv.containsKey(section))
                {
                  //rv.get(section).add(line)
                  rv.get(section) << line
                  
                }
                else
                {
                  rv.put(section, [line])
                }
        }
	}
}
return rv
}



// FUNCTIONS

def searchZipFile(File zipFile, String fn, List incList, excList, Closure c) {
    def zf = new java.util.zip.ZipFile(zipFile)
    zf.entries().findAll {entry -> !entry.directory }.each { item ->
        def inIncludeList=0;
        def inExcludeList=0;
        def regexMatch=""
        incList.each { regex ->
            def match = item.name =~ /$regex/
            if (match.count == 1)
            {
                inIncludeList=1
                regexMatch=regexMatch + regex +", "
            }
        }
        if (inIncludeList == 1) {
            excList.each { regex ->
                def match = item.name =~ /$regex/
                if (match.count == 1)
                {
                    inExcludeList=1
                    regexMatch=regexMatch + regex +", "
                }
            }
            if (inExcludeList == 0) {
            itemfn = Util.getFileName(item.name)
            if (fn.equals(itemfn))
                {
                    println "MATCH filename: $itemfn regex: $regexMatch fullpath: $item.name"
                    c(zf,item)
                }
            }
        }
    }
}

// search zip file for any filename that matched and run closure
def searchZipFile(File zipFile, String fn, Closure c) {
    def zf = new java.util.zip.ZipFile(zipFile)
    zf.entries().findAll {entry -> !entry.directory }.each { item ->

        itemfn = Util.getFileName(item.name)
        if (fn.equals(itemfn))
        {
            c(zf,item)
        }

    }
}

// function search zip file for items based on regex list
def searchZipFile(File zipFile, List regexArray) {
    def zf = new java.util.zip.ZipFile(zipFile)
    zf.entries().findAll {entry -> !entry.directory }.each { item ->
        regexArray.each { regex ->
            def match = item.name =~ /$regex/
            if (match.count == 1)
            {
                println item.name
                println Util.crc(zf.getInputStream(item).text)
                return item.name
            }
        }
    }
}

// check if path string is a directory in the zip file
def isDirectoryInZip(File zipFile, String path) {
    def zf = new java.util.zip.ZipFile(zipFile)
    def rv = false
    zf.entries().findAll {entry -> entry.directory }.each { item ->
	if (item.name.equals(path)) {
	   rv = true
	}
    }
    return rv
}

// check if path string is a directory in the zip file
def extractZipDirectory(File zipFile, String dirname,String targetPath) {
    def zf = new java.util.zip.ZipFile(zipFile)
    def rv = false
    zf.entries().findAll {entry -> !entry.directory }.each { item ->
    if (item.name.startsWith(dirname)) {
       //println "will extract: " + item.name
       extractAndSaveZipFileEntry(zipFile,item,targetPath)
    }
    }
    return rv
}

// list all files in zip file, exclude directories
def listZipFileContents(File zipFile) {
    zf = new java.util.zip.ZipFile(zipFile)
    zf.entries().findAll { !it.directory }.each { println it.name }
}

// dump all text file content of zip file
def dumpZipFileContents(File zipFile) {
    def zf = new java.util.zip.ZipFile(zipFile)
    zf.entries().findAll { !it.directory }.each {
        println it.name
        println zf.getInputStream(it).text
    }
}
// ******************************************************************
// BEGIN MAIN

// init map
def map = [:];
// init empty list
def config=parseConfigFile(configFile)
// output lists for reference
def extractList = config['extract']
def includeList = config['include']
def excludeList = config['exclude']

extractList.each { item ->
println "extractList: " + item
}
includeList.each { item ->
println "includeList: " + item
}
excludeList.each { item ->
println "excludeList: " + item
}


class FileData {
    int cnt =0
    String fullPath
    String checksum
    String content
}

def itemClosure = { z, i->
    //println i.name
    c = Util.crc(z.getInputStream(i).text)
    t = z.getInputStream(i).text
    if (map.containsKey(c)) {
        // increment counter
        map[c].cnt= map[c].cnt+1
    }
    else {
    map[c] = new FileData(cnt:1,fullPath:i.name,checksum:c,content:t)
    }
}

def dumpMap(m) {
    m.each { k,v->
        println "checksum: $k : count: $v.cnt path: $v.fullPath"
    }
}

def saveFiles(m,d) {
    new File(d).mkdirs()
    m.each { k,v->
        p = Util.getFilePath(v.fullPath)
        
       
        np = (Paths.get(d,v.fullPath)).toString()
        println "Saving $k : $v.fullPath : $p To: $np"

        //println "$v.content"
        new File(Util.getFilePath(np)).mkdirs()
        new File(np).write(v.content)
    }
}

def extractAndSaveZipFileEntry(zipFile,entry,targetDir) {
        entry_filename = Util.getFileName(entry.name)
        entry_path = Util.getFilePath(entry.name)
        np = (Paths.get(targetDir,entry_path)).toString() + "/" + entry_filename
        //println "Saving $entry_path  : $entry_filename To: $np"
        zf = new java.util.zip.ZipFile(zipFile)
        content = zf.getInputStream(entry).text
        new File(Util.getFilePath(np)).mkdirs()
        new File(np).write(content)    
}



def tmpPath="stage/tmp/files"
// purge tmp directory
def tmpPathFile = new File(tmpPath)
tmpPathFile.mkdirs()
printf "Delete directory $tmpPath: ${tmpPathFile.deleteDir()} \n"
// create tmpdir
tmpPathFile.mkdirs()

def tmpDirPath="stage/tmp/dirs"
// purge tmp directory
def tmpDirPathFile = new File(tmpDirPath)
tmpDirPathFile.mkdirs()
printf "Delete directory $tmpDirPath: ${tmpDirPathFile.deleteDir()} \n"
// re-create but now empty 
tmpDirPathFile.mkdirs()


try {
	File zipFile = new File(args[0])

	extractList.each { t ->
		if (isDirectoryInZip(zipFile,t)) {
			println "Found directory match: " + t
            println "Extracting directory: " + t + " to " + tmpPath
            extractZipDirectory(zipFile,t,tmpDirPath)
		}
		else
		{
			def fn = Util.getFileName(t)
			// search zip file for fn, then execute itemClosure
			searchZipFile(zipFile, fn, includeList, excludeList, itemClosure);
		}
	}
}
catch (ex)
{
    println "Exception: " + ex
    System.exit(1);
}

dumpMap(map)
saveFiles(map,tmpPath)

System.exit(0)
// END

/**
 * Utility Class
 */
public class Util {

    public static String crc(String input) {

        byte[] bytes = input.getBytes();

        Checksum checksum = new CRC32();

        // update the current checksum with the specified array of bytes
        checksum.update(bytes, 0, bytes.length);

        // get the current checksum value
        long checksumValue = checksum.getValue();

        return checksumValue.toString();
    }

    public static String getFileName(String fp) {
        Path p = Paths.get(fp);
        String fileName = p.getFileName().toString();
        return fileName;
    }
    public static String getFilePath(String fp) {
        Path p = Paths.get(fp);
        String path = p.getParent().toString();
        return path;
    }
}

