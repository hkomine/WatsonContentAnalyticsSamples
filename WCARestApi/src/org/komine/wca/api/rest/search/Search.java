package org.komine.wca.api.rest.search;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.http.client.ClientProtocolException;
import org.komine.wca.api.rest.admin20.DocumentAddText;
import org.komine.wca.api.rest.util.ArgsUtil;
import org.komine.wca.api.rest.util.WCADocument;
import org.komine.wca.api.rest.util.AtomParser;
import org.komine.wca.api.rest.util.RestClient;

public class Search {

	private static String METHOD_SEARCH = "GET";
	private static final String PATH_SEARCH = "/api/v10/search";
	private static final int DEFAULT_SEARCHAPPPORT = 8390;
	
	private String hostname;
	private int port;
	private String collection;

	public static void main(String[] args) throws Exception {
		// Read arguments
		ArgsUtil argsUtil = new ArgsUtil(args);
		if (!validateArgs(argsUtil)) {
			printUsage();
			System.exit(0);
		}
		
		// Read server connection info
		String hostname = argsUtil.getString("hostname");
		int port = argsUtil.getInt("port");
		if (0 == port) {
			port = DEFAULT_SEARCHAPPPORT;
		}
		String collection = argsUtil.getString("collection");
		
		// Client instance
		Search client = new Search(hostname, port, collection);
		
		// Read query
		String query = argsUtil.getString("query");

		// Make a request
		System.out.println("Searching documents in the collection.");
		String result = client.searchDocument(query);
		
		// Show result
		LinkedList<WCADocument> list = AtomParser.parseFeed(result);
		System.out.printf("Searched: %d document(s).%n",list.size());
		
		Iterator<WCADocument> it = list.iterator();
		while (it.hasNext()) {
			WCADocument entry = it.next();
			System.out.println(entry.toString());
		}
	}
	
	public Search(String hostname, int port, String collection) {
		this.hostname = hostname;
		this.port = port;
		this.collection = collection;
	}
	
	private final String searchDocument(String query) throws ClientProtocolException, URISyntaxException, IOException {		
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("collection", collection);
		params.put("query", URLEncoder.encode(query,"UTF-8"));
		 
		String result = RestClient.makeRestRequest(METHOD_SEARCH, hostname, port, PATH_SEARCH, params, null, null, null);
		return result;
	}
	
	private static boolean validateArgs(ArgsUtil argsUtil) {
		if (!argsUtil.isExist("hostname")) return false;
		if (!argsUtil.isExist("collection")) return false;
		if (!argsUtil.isExist("query")) return false;
		return true;
	}
	
	private static void printUsage() {
		System.out.printf("Usage: %s -<key> <value>%n", DocumentAddText.class.getSimpleName());
		System.out.printf("    -%-10s : %s%n", "hostname", "Host name for Watso Content Analytics.");
		System.out.printf("    -%-10s : %s%n", "port", "<Optional> Admin port. Default port is 8393");
		System.out.printf("    -%-10s : %s%n", "collection", "WCA collection a document to be added to.");
		System.out.printf("    -%-10s : %s%n", "query", "Query string for searching documents in the collection.");
		System.out.printf("%n");
		return;
	}
}
