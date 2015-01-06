package org.komine.wca.api.rest.admin20;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.komine.wca.api.rest.util.ArgsUtil;
import org.komine.wca.api.rest.util.RestClient;

public class DocumentAddText {

	private static String METHOD_REMOVE = "POST";
	private static final String PATH_ADDTEXT = "/api/v20/admin/collections/indexer/document/add/text";
	private static final int DEFAULT_ADMINPORT = 8390;

	private static String DEFAULT_LANGUAGE = "en";
	
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
		if (0 == port) {
			port = DEFAULT_ADMINPORT;
		}
		String username = argsUtil.getString("username");
		String password = argsUtil.getString("password");
		String collection = argsUtil.getString("collection");
		
		// Client instance
		DocumentAddText client = new DocumentAddText(hostname, port, username, password, collection);
		
		// Read document info
		String documentId = argsUtil.getString("documentid");
		String title = argsUtil.getString("title");
		String content = argsUtil.getString("content");
		String language = argsUtil.getString("language");
		if ((null == language) || (language.isEmpty())) {
			language = DEFAULT_LANGUAGE;
		}

		System.out.println("Adding new document from text.");
		
		String result = client.addDocument(documentId, title, content, language);
		
		System.out.println("Added(text): " + result);
	}
	
	public DocumentAddText(String hostname, int port, String username, String password, String collection) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
		this.collection = collection;
	}
	
	public String addDocument(String documentId, String title, String content, String language) throws ClientProtocolException, URISyntaxException, IOException {
		// Check secutiryToken
		if ((null == securityToken) || (securityToken.isEmpty())) {
			securityToken = getSecurityToken();
			if ((null == securityToken) || (securityToken.isEmpty())) {
				System.out.println("Failed on logging in.");
				System.exit(0);
			}
		}
		
		// Create title field
		final String fields = "{\"title\":\"" + title + "\"}";
		
        // Create content-type
        ContentType textContentType = ContentType.create("text/plain", Consts.UTF_8);
  
        // Post parameters and the file with Multipart
        MultipartEntityBuilder mpEntityBuilder = MultipartEntityBuilder.create();
        mpEntityBuilder.setCharset(Consts.UTF_8);
        mpEntityBuilder.addTextBody("collection", this.collection);
        mpEntityBuilder.addTextBody("documentId", documentId, textContentType);
        mpEntityBuilder.addTextBody("language", language);
        mpEntityBuilder.addTextBody("fields", fields, textContentType);
        mpEntityBuilder.addTextBody("securityToken", securityToken);
        mpEntityBuilder.addTextBody("text", content, textContentType);
        HttpEntity entity = mpEntityBuilder.build();

        // Make a HTTP request
		String result = RestClient.makeRestRequest(METHOD_REMOVE, hostname, port, PATH_ADDTEXT, null, entity, null, null);
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
		if (!argsUtil.isExist("collection")) return false;
		if (!argsUtil.isExist("username")) return false;
		if (!argsUtil.isExist("password")) return false;
		if (!argsUtil.isExist("documentid")) return false;
		if (!argsUtil.isExist("title")) return false;
		if (!argsUtil.isExist("content")) return false;
		return true;
	}
	
	private static void printUsage() {
		System.out.printf("Usage: %s -<key> <value>%n", DocumentAddText.class.getSimpleName());
		System.out.printf("    -%-10s : %s%n", "hostname", "Host name for Watso Content Analytics.");
		System.out.printf("    -%-10s : %s%n", "port", "<Optional> Admin port. Default port is 8390");
		System.out.printf("    -%-10s : %s%n", "collection", "WCA collection a document to be added to.");
		System.out.printf("    -%-10s : %s%n", "username", "WCA Administrator username.");
		System.out.printf("    -%-10s : %s%n", "password", "Password for the user.");
		System.out.printf("    -%-10s : %s%n", "documentid", "Document ID for the new document.");
		System.out.printf("    -%-10s : %s%n", "title", "Title for the new document.");
		System.out.printf("    -%-10s : %s%n", "content", "Content for the new document.");
		System.out.printf("    -%-10s : %s%n", "language", "<Optional> Language code for the new document. Default value is \"en\".");
		System.out.printf("%n");
		return;
	}
}

