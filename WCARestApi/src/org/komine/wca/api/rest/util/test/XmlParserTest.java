package org.komine.wca.api.rest.util.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.komine.wca.api.rest.util.XmlParser;
import org.xml.sax.SAXException;

public class XmlParserTest {

	String xmlText = "<es:apiResponse xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:es=\"http://www.ibm.com/discovery/es/rest/1.0\" xmlns:ibmsc=\"http://www.ibm.com/search/content/2010\" xmlns:ibmbf=\"http://www.ibm.com/browse/facets/2010\" xmlns:ibmsa=\"http://www.ibm.com/search/capabilities/2010\">"
			+ "<es:securityToken>f15683ae-dee2-471d-944c-7f17b6c992b4</es:securityToken>"
			+ "</es:apiResponse>";
	String expected = "f15683ae-dee2-471d-944c-7f17b6c992b4";
	
	@Test
	public void testString() throws ParserConfigurationException, SAXException, IOException {
        String actual= XmlParser.getSecurityTokenFromString(xmlText);
        Assert.assertEquals(expected, actual);
	}

	@Test
	public void testInputStream() throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new ByteArrayInputStream(xmlText.getBytes());  
        String actual= XmlParser.getSecurityToken(is);
        Assert.assertEquals(expected, actual);
	}
}
