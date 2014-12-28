package org.komine.wca.api.rest.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.abdera.parser.Parser;

public class AtomParser {

	private static final String XMLNS_ES ="http://www.ibm.com/discovery/es/rest/1.0";
	
	private static final String LOCALPART_COLLECTION ="collection";	// <es:collection>
	private static final String LOCALPART_LANGUAGE ="language";		// <es:language>
	private static final String LOCALPART_DOCUMENTSOURCE ="documentSource";	// <es:documentSource>
	private static final String LINKREL_VIA = "via";
	
	
	public static LinkedList<WCADocument> parseFeed(String atomText) throws UnsupportedEncodingException {
		InputStream is = new ByteArrayInputStream(atomText.getBytes("UTF-8"));
		return parseFeed(is);
	}
	
	public static LinkedList<WCADocument> parseFeed(InputStream in) {
		LinkedList<WCADocument> list = new LinkedList<WCADocument>();
		
		Abdera abdera = new Abdera();
		Parser parser = abdera.getParser();
		
		Document<Feed> doc = parser.parse(in);
		Feed feed = doc.getRoot();
		for (Entry entry : feed.getEntries()) {
			WCADocument wcaDoc = new WCADocument();
			
			wcaDoc.setId(entry.getId().toASCIIString());
			wcaDoc.setCollection(getEsEntry(entry,XMLNS_ES, LOCALPART_COLLECTION));
			wcaDoc.setUpdated(entry.getUpdated());
			wcaDoc.setTitle(entry.getTitle());
			wcaDoc.setSummary(entry.getSummary());
			wcaDoc.setLanguage(getEsEntry(entry, XMLNS_ES, LOCALPART_LANGUAGE));
			wcaDoc.setDocumentSource(getEsEntry(entry,XMLNS_ES, LOCALPART_DOCUMENTSOURCE));
			wcaDoc.setLink_via(getLinkHref(entry, LINKREL_VIA));

			list.add(wcaDoc);
		}
		
		return list;
	}
	
	private static String getEsEntry(Entry entry, String nameSpace, String localPart) {
		Element child = entry.getFirstChild(new QName(nameSpace, localPart));
		if (null != child) {
			return child.getText();
		} else {
			return null;
		}
	}
	
	private static String getLinkHref(Entry entry, String rel) {
		Link link = entry.getLink(rel);
		if (null != link) {
			return link.getHref().toASCIIString();
		} else {
			return null;
		}
	}
}
