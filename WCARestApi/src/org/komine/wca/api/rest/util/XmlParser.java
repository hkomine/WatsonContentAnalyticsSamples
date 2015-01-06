package org.komine.wca.api.rest.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser {
	
	private static final String XMLTAG_ES_SECURITYTOKEN = "es:securityToken";
	private static final String XMLTAG_ES_COLLECTIONID = "es:collectionID";
	private static final String XMLTAG_ES_COLLECTION = "es:collection";

	/**
	 * Return securityToken from the response of Rest Admin 2.0 /login call
	 * 
	 * @param xmlText
	 * @return securityToken
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static String getSecurityTokenFromString(String xmlText) throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new ByteArrayInputStream(xmlText.getBytes("UTF-8")); 
        return getSecurityToken(is);
	}
	
	public static String getSecurityToken(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.parse(is);
        return getSecurityToken(doc);
	}
	
	private static String getSecurityToken(Document doc) {
		String securityToken = null;
		
		Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();

        for( int i=0; i<children.getLength(); i++ ) {
             Node child = children.item(i);
             if( child instanceof Element ) {
                  Element childElement = (Element) child;
                  if (0 == childElement.getTagName().compareTo(XMLTAG_ES_SECURITYTOKEN)) {
                	  securityToken = childElement.getTextContent();
                	  break;
                  }
             }
        }
		return securityToken;
	}
	

	/**
	 * Return collection Id in Map from the response of Rest Admin 2.0 /collections/add call
	 * 
	 * @param xmlText
	 * @return collectionid
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static String getCollectionIdString(String xmlText) throws ParserConfigurationException, SAXException, IOException {
		InputStream is = new ByteArrayInputStream(xmlText.getBytes("UTF-8")); 
		return getCollectionId(is);
	}

	public static String getCollectionId(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.parse(is);
        return geCollectionId(doc);
	}
	
	private static String geCollectionId(Document doc) {
		String collectionId = null;
		
		Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();

        for( int i=0; i<children.getLength(); i++ ) {
             Node child = children.item(i);
             if( child instanceof Element ) {
                  Element childElement = (Element) child;
                  if (0 == childElement.getTagName().compareTo(XMLTAG_ES_COLLECTIONID)) {
                	  collectionId = childElement.getTextContent();
                	  break;
                  }
             }
        }
		return collectionId;
	}
	
	
	/**
	 * Return collection info in Map from the response of Rest Admin 2.0 /collections/config call
	 * 
	 * @param xmlText
	 * @return Map of collection info
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Map<String,String> getCollectionInfoFromString(String xmlText) throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new ByteArrayInputStream(xmlText.getBytes("UTF-8")); 
        return getCollectionInfo(is);
	}
	
	public static Map<String,String> getCollectionInfo(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.parse(is);
        return geCollectionInfo(doc);
	}
	
	private static Map<String,String> geCollectionInfo(Document doc) {
		HashMap<String,String> map = new HashMap<String,String>();
		
		Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();

        for( int i=0; i<children.getLength(); i++ ) {
             Node child = children.item(i);
             if( child instanceof Element ) {
                  Element childElement = (Element) child;
                  if (0 == childElement.getTagName().compareTo(XMLTAG_ES_COLLECTION)) {
                	  NamedNodeMap nnMap = childElement.getAttributes();
                	  int len = nnMap.getLength();
                	  for (int j=0; j<len; j++) {
                		   Node node = nnMap.item(j);
                		   if (node instanceof Attr) {
                			   Attr attr = (Attr) node;
                			   map.put(attr.getNodeName(), attr.getNodeValue());
                		   }
                	  }
                  }
             }
        }
        
		return map;
	}
}
