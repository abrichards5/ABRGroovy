#!/usr/bin/env groovy

def currentDir = new File(".");

def backupFile;
def fileText;

//Replace the contents of the list below with the 
//extensions to search for
def exts = [".txt", ".foo"]

//Replace the value of srcExp to a String or regular expression
//to search for.
def srcExp = "dummy"

//Replace the value of replaceText with the value new value to 
//replace srcExp
def replaceText = "awesome"

currentDir.eachFileRecurse(
  {file ->
    for (ext in exts){
      if (file.name.endsWith(ext)) {
        fileText = file.text;
        backupFile = new File(file.path + ".bak");
        backupFile.write(fileText);
        fileText = fileText.replaceAll(srcExp, replaceText)
        file.write(fileText);
      }
    }
  }
)

