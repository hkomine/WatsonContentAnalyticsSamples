package org.komine.wca.api.rest.util.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.komine.wca.api.rest.util.XmlParser;
import org.xml.sax.SAXException;

public class XmlParserTest {

	String xmlSecurityToken = "<es:apiResponse xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:es=\"http://www.ibm.com/discovery/es/rest/1.0\" xmlns:ibmsc=\"http://www.ibm.com/search/content/2010\" xmlns:ibmbf=\"http://www.ibm.com/browse/facets/2010\" xmlns:ibmsa=\"http://www.ibm.com/search/capabilities/2010\">"
			+ "<es:securityToken>f15683ae-dee2-471d-944c-7f17b6c992b4</es:securityToken>"
			+ "</es:apiResponse>";
	String expectedSecurityToken = "f15683ae-dee2-471d-944c-7f17b6c992b4";
	
	@Test
	public void testSecurityTokenFromString() throws ParserConfigurationException, SAXException, IOException {
        String actual = XmlParser.getSecurityTokenFromString(xmlSecurityToken);
        Assert.assertEquals(expectedSecurityToken, actual);
	}

	@Test
	public void testSecurityTokenFromInputStream() throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new ByteArrayInputStream(xmlSecurityToken.getBytes("UTF-8"));  
        String actual = XmlParser.getSecurityToken(is);
        Assert.assertEquals(expectedSecurityToken, actual);
	}
	
	String xmlCollectionCreated = "<es:apiResponse xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:es=\"http://www.ibm.com/discovery/es/rest/1.0\" xmlns:ibmsc=\"http://www.ibm.com/search/content/2010\" xmlns:ibmbf=\"http://www.ibm.com/browse/facets/2010\" xmlns:ibmsa=\"http://www.ibm.com/search/capabilities/2010\">"
			+ "<es:collectionID>my_collection</es:collectionID>"
			+ "</es:apiResponse>";
	String expectedCollectionId_01 = "my_collection";
	
	@Test
	public void testCollectionIdString() throws ParserConfigurationException, SAXException, IOException {
		String actual = XmlParser.getCollectionIdString(xmlCollectionCreated);
		Assert.assertEquals(expectedCollectionId_01, actual);
	}
	
	@Test
	public void testCollectionIdFromStream() throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new ByteArrayInputStream(xmlCollectionCreated.getBytes("UTF-8"));  
        String actual   = XmlParser.getCollectionId(is);
        Assert.assertEquals(expectedCollectionId_01, actual);
	}
	
	String xmlCollectionInfo = "<es:apiResponse xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:es=\"http://www.ibm.com/discovery/es/rest/1.0\" xmlns:ibmsc=\"http://www.ibm.com/search/content/2010\" xmlns:ibmbf=\"http://www.ibm.com/browse/facets/2010\" xmlns:ibmsa=\"http://www.ibm.com/search/capabilities/2010\">"
			+ "<es:collection id=\"my_collection\" label=\"‚ ‚¢‚¤‚¦‚¨\" description=\"\" type=\"Analytics\" security=\"false\" bigInsights=\"false\">"
			+ "<es:collectionConfig documentCache=\"true\" thumbnail=\"false\" dupDocDetection=\"false\" optionalFacet=\"false\" termsInterest=\"false\" sentiment=\"false\" queryLog=\"true\" ruleCategorization=\"true\" docClustering=\"false\" docFlagging=\"true\" social=\"false\" overlayIndex=\"false\" contentAssessment=\"false\" timeZone=\"Asia/Tokyo\" export=\"false\" deepInspection=\"false\" report=\"false\" exportToAnalytics=\"false\" rdf=\"false\" alert=\"false\">"
			+ "<es:documentImportanceConfig model=\"NONE\"/>"
			+ "<es:ngramConfig mode=\"Default\" policy=\"Normal\"/>"
			+ "</es:collectionConfig>"
			+ "</es:collection>"
			+ "</es:apiResponse>";
	String expectedCollectionId_02 = "my_collection";
	String expectedCollectionLabel = "‚ ‚¢‚¤‚¦‚¨";
	String expectedCollectionType = "Analytics";

	@Test
	public void testCollectionInfoFromString() throws ParserConfigurationException, SAXException, IOException {
		Map<String,String> map = XmlParser.getCollectionInfoFromString(xmlCollectionInfo);
		Assert.assertEquals(expectedCollectionId_02, map.get("id"));
		Assert.assertEquals(expectedCollectionLabel, map.get("label"));
		Assert.assertEquals(expectedCollectionType, map.get("type"));
	}
	
	@Test
	public void testCollectionInfoFromStream() throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new ByteArrayInputStream(xmlCollectionInfo.getBytes("UTF-8"));  
        Map<String,String> map  = XmlParser.getCollectionInfo(is);
		Assert.assertEquals(expectedCollectionId_02, map.get("id"));
		Assert.assertEquals(expectedCollectionLabel, map.get("label"));
		Assert.assertEquals(expectedCollectionType, map.get("type"));
	}
}
