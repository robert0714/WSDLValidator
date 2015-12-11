package com.iisigroup.utils.xml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.eclipse.wst.xml.core.internal.validation.core.ValidationMessage;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationReport;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WsdlValidatorTest {
	WsdlValidator commponent ;
	@Before
	public void setUp() throws Exception {
		commponent =new WsdlValidator();
	}
	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testValidateFile() throws Exception {
		URL wsdlURL = getClass().getResource("/test/xmlsamples/multireference.wsdl");
		File wsdlFile =new File(wsdlURL.toURI());
		ValidationReport report = commponent.validate(wsdlFile);
		final	ValidationMessage[] vmses = report.getValidationMessages();
		
		Assert.assertSame(1, vmses.length);
		
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
	}
	@Test
	public void testValidateInputStream() throws Exception {
		InputStream is = getClass().getResourceAsStream("/test/xmlsamples/multireference.wsdl");
		ValidationReport report = commponent.validate(is);
		final	ValidationMessage[] vmses = report.getValidationMessages();
		
		Assert.assertSame(1, vmses.length);
		
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
	}
}
