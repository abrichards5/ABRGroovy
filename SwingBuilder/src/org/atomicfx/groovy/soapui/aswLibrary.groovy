// Groovy support library
// Author: Adam Richards
// Description: Library of reusable groovy code for ASW soapui testing

// This script is designed to be accessed via a groovy test step withing a SoapUI test Case
// Other scripts elsewhere in SoapUI will access the reused class through their context
// This is the magic: put an instance of this class into the soapui exposed context object
context.setProperty( "ASWLibClass", new ASWLibClass(log, context, testRunner));

// Commnted code for reference use
// create instance of AswLib
//def aswLib = new AswLib();

// call method example
// aswLib.loadAswProperties



//// EXAMPLE of use in a groovy test step
//// unique to soapui tool environment.  will not run standalone
//try {
//// Import the class into the context
//def currentProject = testRunner.testCase.testSuite.project
//currentProject
//    .testSuites["ASWLibrary"]
//       .testCases["ASWLibrary"]
//          .testSteps["ASWLibClass"]
//             .run(testRunner, context)
//
//// Now use it
//version =  context.ASWLibClass.getVersion()
//com.eviware.soapui.support.UISupport.showInfoMessage(version,"success");
//} catch (Exception e) { com.eviware.soapui.support.UISupport.showInfoMessage(e); }



// define the Groovy Class with the methods we wish to use in groovy test steps
class ASWLibClass {
	def LIB_VERSION="1.0.1"
	// The three following fields are MANDATORY
	// save pointers to various soapui objects of interest
	def log
	def context
	def testRunner
	def ui
	def gu

	// The constructor below is MANDATORY
	def ASWLibClass(logIn, contextIn, testRunnerIn) {
		this.log = logIn
		this.context = contextIn
		this.testRunner = testRunnerIn
		this.gu = new com.eviware.soapui.support.GroovyUtils(this.context);
		this.ui = com.eviware.soapui.support.UISupport;
	}

	// simple version tracker
	String getVersion() {
		return LIB_VERSION;
	}


	def loadGlobalProperties(propFilePath) {
		log.info "Attempting to load properties from file: " + propFilePath
		def props = new Properties()
		new File(propFilePath).withInputStream {  stream ->
			props.load(stream)
		}
		log.info props.size()

		List<String> keys = new ArrayList<String>()
		for(String key : props.stringPropertyNames()) {
			keys.add(key)
		}
		Collections.sort(keys);

		props.each {
			// set project level property
			// project.setPropertyValue(it.key,it.value)

			// set global level
			gprop = com.eviware.soapui.model.propertyexpansion.PropertyExpansionUtils.globalProperties.getPropertyValue(it.key)
			if (gprop == null) {
				log.info "  setting global property: " + it.key
				com.eviware.soapui.model.propertyexpansion.PropertyExpansionUtils.globalProperties.setPropertyValue(it.key, it.value)
			}
			else {
				log.info " skipping existing globabl property: " + it.key
			}
		}
	}

	// used to get a soapui project object.
	def getProjectObject() {
		// get the soapui project object based on available objects
		// available objects changed depending on where the groovy code is defined.
		// a test step groovy has different objects available than say a project startup groovy script

		// if project object is defined then we are pobably in a project startup script
		if (binding.hasVariable('project')) {
			return project;
		}
		if (binding.hasVariable('context')) {
			log.info "ThreadIndex: "  + context.ThreadIndex;
			log.info "Project Name: " +  context.testCase.testSuite.project.name;
			log.info "Test Suite: " + context.testCase.testSuite.name;
			log.info "Test Case: " + context.testCase.getName();
			log.info "Test Step: " + context.testCase.getTestStepAt(context.getCurrentStepIndex()).getLabel();
			return context.testCase.testSuite.project;
		}

	}

	// used to log groovy error information if readable format
	def getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		Exception santizedException = org.codehaus.groovy.runtime.StackTraceUtils.sanitize(new Exception(e));
		santizedException.printStackTrace(pw);
		return sw.toString();
	}


	// reusable dialog to prompt user to set a specific global property
	// propName is the name of the global property
	// propOptionsName is the global property name of  a
	// comma delimited list of options to choose from (this is optional)
	// description with be the title of the dialog and text in prompt
	def UISetPropertyDialog(propName,propOptionsName,description) {

		try {
			def cenv = com.eviware.soapui.model.propertyexpansion.PropertyExpansionUtils.globalProperties.getPropertyValue(propName)
			def cenvOptions = com.eviware.soapui.model.propertyexpansion.PropertyExpansionUtils.globalProperties.getPropertyValue(propOptionsName)
			def cenvOptionsArray=null
			if (cenvOptions != null) {
				cenvOptionsArray = cenvOptions.split(',')
			} else cenvOptionsArray = null;

			ui.showInfoMessage("Current property: '" + propName + "' value is: '" + cenv + "'");

			String value = null
			if (cenv == null)
				cenv = ""
			if (cenvOptionsArray != null) {
				// if found options use pulldown prompt syntax
				value = ui.prompt("Select " + description, description, cenvOptionsArray, cenv);
			} else {
				// else use text box syntax
				value = ui.prompt("Enter " + description, description, cenv);
			}


			if (value != null) {
				com.eviware.soapui.model.propertyexpansion.PropertyExpansionUtils.globalProperties.setPropertyValue(propName, value)
			}

			def nenv = com.eviware.soapui.model.propertyexpansion.PropertyExpansionUtils.globalProperties.getPropertyValue(propName)
			ui.showInfoMessage("New property: '" + propName + "' value is: '" + nenv + "'");
		} catch (Exception e) {
			log.error(getStackTrace(e));
			ui.showErrorMessage(e);
		} finally {

		}
	}

}