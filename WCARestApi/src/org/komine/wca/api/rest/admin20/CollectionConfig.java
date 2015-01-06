package org.komine.wca.api.rest.admin20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.komine.wca.api.rest.util.ArgsUtil;
import org.komine.wca.api.rest.util.RestClient;

public class CollectionConfig {

	private static String METHOD_COLLECTION_CONFIG = "GET";
	private static final String PATH_COLLECTION_CONFIG = "/api/v20/admin/collections/config";
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
		CollectionConfig client = new CollectionConfig(hostname, port, username, password);
		
		String collection = argsUtil.getString("collection");

		System.out.println("Getting collection config.");
		
		String result = client.getConfig(collection);
		System.out.println("Gotten: " + result);
	}
	
	public CollectionConfig(String hostname, int port, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	public String getConfig(String collection) throws ClientProtocolException, URISyntaxException, IOException {
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

        // Make a HTTP request
		String result = RestClient.makeRestRequest(METHOD_COLLECTION_CONFIG, hostname, port, PATH_COLLECTION_CONFIG, params, null, null, null);
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
		if (!argsUtil.isExist("collection")) return false;
		return true;
	}
	
	private static void printUsage() {
		System.out.printf("Usage: %s -<key> <value>%n", CollectionConfig.class.getSimpleName());
		System.out.printf("    -%-10s : %s%n", "hostname", "Host name for Watso Content Analytics.");
		System.out.printf("    -%-10s : %s%n", "port", "<Optional> Admin port. Default port is 8390");
		System.out.printf("    -%-10s : %s%n", "username", "WCA Administrator username.");
		System.out.printf("    -%-10s : %s%n", "password", "Password for the user.");
		System.out.printf("    -%-10s : %s%n", "collection", "Collection ID to be retrieved.");
		System.out.printf("%n");
		return;
	}
}

