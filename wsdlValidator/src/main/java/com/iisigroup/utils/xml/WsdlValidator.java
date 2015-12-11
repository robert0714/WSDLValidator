/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package com.iisigroup.utils.xml;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
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

/**
 * The Class WsdlValidator.
 */
public class WsdlValidator {
	
	/**
	 * Convert report to xml report.
	 *
	 * @param report the report
	 * @return the validation report
	 */
	protected ValidationReport convertReportToXMLReport(IValidationReport report) {
		ValidationInfo convertedReport = new ValidationInfo(report.getFileURI());
		IValidationMessage[] messages = report.getValidationMessages();
		int numMessages = messages.length;
		for (int i = 0; i < numMessages; i++) {
			convertMessage(messages[i], convertedReport);
		}

		return convertedReport;
	}

	/**
	 * Convert message.
	 *
	 * @param message the message
	 * @param convertedReport the converted report
	 */
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
	
	/**
	 * Validate.
	 *
	 * @param wsdlFile the wsdl file
	 * @return the validation report
	 * @throws MalformedURLException the malformed url exception
	 */
	public ValidationReport validate(File wsdlFile) throws MalformedURLException{
		
		XMLGrammarPool xsdGrammarPool = new InlineSchemaModelGrammarPoolImpl();
		XMLGrammarPool xmlGrammarPool = new XMLGrammarPoolImpl();
		org.eclipse.wst.wsdl.validation.internal.WSDLValidator validator = org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator.getInstance();
		
		ValidationReport report = null;
		
		String uri = wsdlFile
				.toURI().toURL().toExternalForm();
		
		WSDLValidationConfiguration configuration = new WSDLValidationConfiguration();
		configuration.setProperty(Constants.XMLSCHEMA_CACHE_ATTRIBUTE,
				xsdGrammarPool);
		configuration
				.setProperty(Constants.XML_CACHE_ATTRIBUTE, xmlGrammarPool);;
		IValidationReport valreport = validator.validate(uri, null,
				configuration);
		
		report = 	convertReportToXMLReport(valreport);		
		return report ;
	}
	
	/**
	 * Validate.
	 *
	 * @param wsdlFile the wsdl file
	 * @return the validation report
	 */
	public ValidationReport validate(InputStream wsdlFile){		
		XMLGrammarPool xsdGrammarPool = new InlineSchemaModelGrammarPoolImpl();
		XMLGrammarPool xmlGrammarPool = new XMLGrammarPoolImpl();
		org.eclipse.wst.wsdl.validation.internal.WSDLValidator validator = org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator.getInstance();
		
		ValidationReport report = null;
		URL wsdlUri =    getClass().getResource("");
		String wsdlSystemId = wsdlUri.toExternalForm();
		 
		
		WSDLValidationConfiguration configuration = new WSDLValidationConfiguration();
		configuration.setProperty(Constants.XMLSCHEMA_CACHE_ATTRIBUTE,
				xsdGrammarPool);
		configuration
				.setProperty(Constants.XML_CACHE_ATTRIBUTE, xmlGrammarPool);;
		IValidationReport valreport = validator.validate(wsdlSystemId, wsdlFile,
				configuration);		
		report = 	convertReportToXMLReport(valreport);		
		return report ;
	}
}
