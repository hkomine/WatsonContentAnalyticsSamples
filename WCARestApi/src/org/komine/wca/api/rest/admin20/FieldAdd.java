package org.komine.wca.api.rest.admin20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.komine.wca.api.rest.util.ArgsUtil;
import org.komine.wca.api.rest.util.RestClient;

public class FieldAdd {

	private static String METHOD_COLLECTION_ADD = "GET";
	private static final String PATH_COLLECTION_ADD = "/api/v20/admin/collections/indexer/fields/add";
	private static final int DEFAULT_ADMINPORT = 8390;
	
	private String hostname;
	private int port;
	private String collection;
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
		String collection = argsUtil.getString("collection");
		
		// Client instance
		FieldAdd client = new FieldAdd(hostname, port, collection, username, password);
		
		String name = argsUtil.getString("name");
		boolean returnable = argsUtil.isExist("returnable");
		boolean fieldSearchable = argsUtil.isExist("fieldSearchable");
		boolean freeTextSearchable = argsUtil.isExist("freeTextSearchable");

		System.out.println("Adding new collection.");
		
		String result = client.addField(name, returnable, fieldSearchable, freeTextSearchable);
		
		System.out.println("Added: " + result);
	}
	
	public FieldAdd(String hostname, int port, String collection, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
		this.collection = collection;
	}
	
	private String addField(String name, boolean returnable, boolean fieldSearchable, boolean freeTextSearchable) throws ClientProtocolException, URISyntaxException, IOException {
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
		params.put("name", name);
		if (returnable) {
			params.put("returnable", "true");
		}
		if (fieldSearchable) {
			params.put("fieldSearchable", "true");
		}
		if (freeTextSearchable) {
			params.put("freeTextSearchable", "true");
		}

        // Make a HTTP request
		String result = RestClient.makeRestRequest(METHOD_COLLECTION_ADD, hostname, port, PATH_COLLECTION_ADD, params, null, null, null);
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
		if (!argsUtil.isExist("name")) return false;
		return true;
	}
	
	private static void printUsage() {
		System.out.printf("Usage: %s -<key> <value>%n", FieldAdd.class.getSimpleName());
		System.out.printf("    -%-10s : %s%n", "hostname", "Host name for Watso Content Analytics.");
		System.out.printf("    -%-10s : %s%n", "port", "<Optional> Admin port. Default port is 8390.");
		System.out.printf("    -%-10s : %s%n", "username", "WCA Administrator username.");
		System.out.printf("    -%-10s : %s%n", "password", "Password for the user.");
		System.out.printf("    -%-10s : %s%n", "collection", "WCA collection a field to be added to.");
		System.out.printf("    -%-10s : %s%n", "name", "Field name to be created.");
		System.out.printf("    -%-10s : %s%n", "returnable", "<Optional> Shows the value of this field in the search results.");
		System.out.printf("    -%-10s : %s%n", "fieldSearchable", "<Optional> Enables this field to be searched by field name.");
		System.out.printf("    -%-10s : %s%n", "freeTextSearchable", "<Optional> Enables this field to be searched with a free text query.");
		System.out.printf("%n");
		return;
	}
}

