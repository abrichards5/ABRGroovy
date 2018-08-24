// Groovy Script to verify the notification classes are listed in the template RawData.java file
// Author: Adam Richards

// import dependencies
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Matcher

import groovy.io.FileType

// read in passed arguments
def notifClassPath=args[0]
def templateFilePath=args[1]

// get list of notifiction classes
def fileList = []
def dir = new File(notifClassPath)
dir.eachFile (FileType.FILES) { file ->
  tmpName = file.getName()
  // only get classes that end in Notification
  match = tmpName =~ /Notification\.java$/
  if (match.count == 1) {
      // replace extension
      tmpName=tmpName.replaceFirst(/\.java$/,".class")
      fileList << tmpName
  }
}

def joinedList=[];
if (fileList.size() > 0)
{
joinedList = fileList.sort().join(",\n")
}
println "Found "+fileList.size()+" Notification Classes:"
//println joinedList
println "Verify notification classes listed in RawData template file:  $templateFilePath"
def templateText=new File(templateFilePath).getText()
missingList=[]
fileList.each({item-> 
    // match classname preceeded by white space or a , 
    def match=templateText=~/[\s,',']{1}${item}/
    //println "$item found: $match.count"
    if (match.count == 0 ) {
        missingList.add(item)
    }
})
if (missingList.size() > 0 ) {
    println "ERROR: The following notification classes missing from the RawData template file:"
    println missingList
    println "Failing build"
    System.exit(1)
}
System.exit(0)


