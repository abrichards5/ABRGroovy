// unique to soapui tool environment.  will not run standalone
try {
// Import the class into the context
def currentProject = testRunner.testCase.testSuite.project
currentProject
    .testSuites["ASWLibrary"]
       .testCases["ASWLibrary"]
          .testSteps["ASWLibClass"]
             .run(testRunner, context)

// Now use it
version =  context.ASWLibClass.getVersion()
com.eviware.soapui.support.UISupport.showInfoMessage(version,"success");
} catch (Exception e) { com.eviware.soapui.support.UISupport.showInfoMessage(e); }