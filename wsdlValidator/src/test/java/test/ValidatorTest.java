package test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.eclipse.wst.wsdl.validation.internal.Constants;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidationConfiguration;
import org.eclipse.wst.wsdl.validation.internal.eclipse.InlineSchemaModelGrammarPoolImpl; 
import org.eclipse.wst.xml.core.internal.validation.core.ValidationInfo;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationMessage;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationReport;

public class ValidatorTest {

	public static void main(String[] args) throws MalformedURLException {
			new ValidatorTest().testMethos();
		
	}
	protected void testMethos() throws MalformedURLException{
		XMLGrammarPool xsdGrammarPool = new InlineSchemaModelGrammarPoolImpl();
		XMLGrammarPool xmlGrammarPool = new XMLGrammarPoolImpl();
//		org.eclipse.wst.wsdl.validation.internal.WSDLValidator validator = org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator.getInstance();
		WSDLValidator validator = WSDLValidator.getInstance();

		WSDLValidationConfiguration configuration = new WSDLValidationConfiguration();
		configuration.setProperty(Constants.XMLSCHEMA_CACHE_ATTRIBUTE,
				xsdGrammarPool);
		configuration
				.setProperty(Constants.XML_CACHE_ATTRIBUTE, xmlGrammarPool);

		String uri = new File(
				"/sharedData/robertData/Documents/workspaces/eclipse-SDK-4.4.2-linux-gtk-x86_64/wsdl/multireference.wsdl")
				.toURL().toExternalForm();
		;
		IValidationReport valreport = validator.validate(uri, null,
				configuration);
		final ValidationReport report = 	convertReportToXMLReport(valreport);
		
		final	ValidationMessage[] vmses = report.getValidationMessages();
		
		if(vmses != null && vmses.length > 0 ){
			for(int i =0 ; i < vmses.length  ;++i ){
				int columnNumber = vmses[i].getColumnNumber();
				
				int lineNumber = vmses[i].getLineNumber();
				String key = vmses[i].getKey();
				String msg = vmses[i].getMessage();
				Object[]  msgs = vmses[i].getMessageArguments();
				List nms = vmses[i].getNestedMessages();
				int sec = vmses[i].getSeverity();
				 
				System.out.println(String.format("msg: %s, line:%s",new Object[]{ msg ,String.valueOf(lineNumber)}));
			}
		}
		
		System.out.println("end...");
	}
	protected ValidationReport convertReportToXMLReport(IValidationReport report) {
		ValidationInfo convertedReport = new ValidationInfo(report.getFileURI());
		IValidationMessage[] messages = report.getValidationMessages();
		int numMessages = messages.length;
		for (int i = 0; i < numMessages; i++) {
			convertMessage(messages[i], convertedReport);
		}

		return convertedReport;
	}

	protected void convertMessage(IValidationMessage message,
			ValidationInfo convertedReport) {
		List nestedMessages = message.getNestedMessages();
		if (nestedMessages != null && nestedMessages.size() > 0) {
			Iterator messageIter = nestedMessages.iterator();
			while (messageIter.hasNext()) {
				convertMessage((IValidationMessage) messageIter.next(),
						convertedReport);
			}
		} else {
			if (message.getSeverity() == IValidationMessage.SEV_WARNING)
				convertedReport.addWarning(message.getMessage(),
						message.getLine(), message.getColumn(),
						message.getURI());
			else
				convertedReport.addError(message.getMessage(),
						message.getLine(), message.getColumn(),
						message.getURI());
		}
	}

}
