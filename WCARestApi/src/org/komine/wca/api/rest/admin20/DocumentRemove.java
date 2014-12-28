package org.komine.wca.api.rest.admin20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.komine.wca.api.rest.util.ArgsUtil;
import org.komine.wca.api.rest.util.RestClient;

public class DocumentRemove {
	
	private static String METHOD_REMOVE = "GET";
	private static final String PATH_REMOVE = "/api/v20/admin/collections/indexer/document/remove";
	
	private String hostname;
	private int port;
	private String username;
	private String password;
	private String collection;
	private String securityToken;

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
		String username = argsUtil.getString("username");
		String password = argsUtil.getString("password");
		String collection = argsUtil.getString("collection");
		
		// Client instance
		DocumentRemove client = new DocumentRemove(hostname, port, username, password, collection);
		
		// Read document info
		String documentId = argsUtil.getString("documentid");
		
		System.out.printf("Removing a document, %s.%n", documentId);
		
		String result = client.removeDocument(documentId);
		
		System.out.println("Deleted: " + result);
	}
	
	public DocumentRemove(String hostname, int port, String username, String password, String collection) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
		this.collection = collection;
	}
	
	private final String removeDocument(String documentId) throws ClientProtocolException, URISyntaxException, IOException {
		// Check secutiryToken
		if ((null == securityToken) || (securityToken.isEmpty())) {
			securityToken = getSecurityToken();
			if ((null == securityToken) || (securityToken.isEmpty())) {
				System.out.println("Failed on logging in.");
				System.exit(0);
			}
		}
		
		HashMap<String, String> params = new HashMap<String, String>();
		 
		params.put("securityToken", securityToken);
		params.put("collection", collection);
		params.put("documentId", URLEncoder.encode(documentId,"UTF-8"));
		 
		String result = RestClient.makeRestRequest(METHOD_REMOVE, hostname, port, PATH_REMOVE, params, null, null, null);
		return result;
	}
	
	// get SecurityToken
	private String getSecurityToken() {
		System.out.println("Trying logging in to get securityToken.");
		try {
			return Login.login(hostname, port, username, password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private static boolean validateArgs(ArgsUtil argsUtil) {
		if (!argsUtil.isExist("hostname")) return false;
		if (!argsUtil.isExist("port")) return false;
		if (!argsUtil.isExist("collection")) return false;
		if (!argsUtil.isExist("username")) return false;
		if (!argsUtil.isExist("password")) return false;
		if (!argsUtil.isExist("documentid")) return false;
		return true;
	}
	
	private static void printUsage() {
		System.out.printf("Usage: %s -<key> <value>%n", DocumentRemove.class.getSimpleName());
		System.out.printf("    -%-10s : %s%n", "hostname", "Host name for Watso Content Analytics.");
		System.out.printf("    -%-10s : %s%n", "port", "Admin port.");
		System.out.printf("    -%-10s : %s%n", "collection", "WCA collection a document to be added to.");
		System.out.printf("    -%-10s : %s%n", "username", "WCA Administrator username.");
		System.out.printf("    -%-10s : %s%n", "password", "Password for the user.");
		System.out.printf("    -%-10s : %s%n", "documentid", "Document ID for the new document.");
		System.out.printf("%n");
		return;
	}
}
