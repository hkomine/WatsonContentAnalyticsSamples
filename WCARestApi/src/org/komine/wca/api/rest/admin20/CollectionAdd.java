package org.komine.wca.api.rest.admin20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.komine.wca.api.rest.util.ArgsUtil;
import org.komine.wca.api.rest.util.RestClient;

public class CollectionAdd {

	private static String METHOD_COLLECTION_ADD = "GET";
	private static String METHOD_COLLECTION_INDEXER_START = "GET";
	private static String METHOD_COLLECTION_SEARCH_START = "GET";
	private static final String PATH_COLLECTION_ADD = "/api/v20/admin/collections/add";
	private static final String PATH_COLLECTION_INDEXER_START = "/api/v20/admin/collections/indexer/start";
	private static final String PATH_COLLECTION_SEARCH_START = "/api/v20/admin/collections/search/start";
	private static final int DEFAULT_ADMINPORT = 8390;
	
	private String hostname;
	private int port;
	private String username;
	private String password;
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
		if (0 == port) {
			port = DEFAULT_ADMINPORT;
		}
		String username = argsUtil.getString("username");
		String password = argsUtil.getString("password");
		
		// Client instance
		CollectionAdd client = new CollectionAdd(hostname, port, username, password);
		
		String name = argsUtil.getString("name");
		String id = argsUtil.getString("id");
		String type = argsUtil.getString("type");

		System.out.println("Adding new collection.");
		
		String result = client.addCollection(name, id, type);
		System.out.println("Added: " + result);
		
		result = client.startIndex(id);
		System.out.println("Indexer started: " + result);
		
		result = client.startSearch(id);
		System.out.println("Search started: " + result);
	}
	
	public CollectionAdd(String hostname, int port, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	public String addCollection(String name, String id, String type) throws ClientProtocolException, URISyntaxException, IOException {
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
		params.put("name", name);
		if ((null != id) && (!id.isEmpty())) {
			params.put("id", id);
		}
		
		if ( (null != type) &&
		        ((0 == type.compareToIgnoreCase("Search")) || (0 == type.compareToIgnoreCase("Analytics"))) ) {
			params.put("type", type);
		}

        // Make a HTTP request
		String result = RestClient.makeRestRequest(METHOD_COLLECTION_ADD, hostname, port, PATH_COLLECTION_ADD, params, null, null, null);
		return result;
	}
	
	public String startIndex(String id) throws ClientProtocolException, URISyntaxException, IOException {
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
		params.put("collection", id);
		
        // Make a HTTP request
		String result = RestClient.makeRestRequest(METHOD_COLLECTION_INDEXER_START, hostname, port, PATH_COLLECTION_INDEXER_START, params, null, null, null);
		return result;
	}
	
	public String startSearch(String id) throws ClientProtocolException, URISyntaxException, IOException {
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
		params.put("collection", id);
		
        // Make a HTTP request
		String result = RestClient.makeRestRequest(METHOD_COLLECTION_SEARCH_START, hostname, port, PATH_COLLECTION_SEARCH_START, params, null, null, null);
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
		if (!argsUtil.isExist("username")) return false;
		if (!argsUtil.isExist("password")) return false;
		if (!argsUtil.isExist("name")) return false;
		return true;
	}
	
	private static void printUsage() {
		System.out.printf("Usage: %s -<key> <value>%n", CollectionAdd.class.getSimpleName());
		System.out.printf("    -%-10s : %s%n", "hostname", "Host name for Watso Content Analytics.");
		System.out.printf("    -%-10s : %s%n", "port", "<Optional> Admin port. Default port is 8390");
		System.out.printf("    -%-10s : %s%n", "username", "WCA Administrator username.");
		System.out.printf("    -%-10s : %s%n", "password", "Password for the user.");
		System.out.printf("    -%-10s : %s%n", "name", "Collection name to be created.");
		System.out.printf("    -%-10s : %s%n", "id", "<Optional> Collection id to be created.");
		System.out.printf("    -%-10s : %s%n", "type", "<Optional> Collection type. One of the following values: Search, Analytics");
		System.out.printf("%n");
		return;
	}
}

