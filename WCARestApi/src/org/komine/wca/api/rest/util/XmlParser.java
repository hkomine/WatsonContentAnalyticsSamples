package org.komine.wca.api.rest.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser {
	
	private static final String XMLTAG_ES_SECURITYTOKEN = "es:securityToken";

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
}
