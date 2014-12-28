package org.komine.wca.api.rest.admin20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.komine.wca.api.rest.util.RestClient;
import org.komine.wca.api.rest.util.XmlParser;
import org.xml.sax.SAXException;

public class Login {
	private static final String METHOD_LOGIN = "GET";
	private static final String PATH_LOGIN = "/api/v20/admin/login";
	
	public static final String login(String hostname, int port, String username, String password) throws ClientProtocolException, URISyntaxException, IOException, ParserConfigurationException, SAXException {
		
		HashMap<String,String> params = new HashMap<String,String>();
		
		params.put("username", username);
		params.put("password", password);
		
		String result = RestClient.makeRestRequest(METHOD_LOGIN, hostname, port, PATH_LOGIN, params, null, null, null);
		String securityToken = XmlParser.getSecurityTokenFromString(result);

		return securityToken;
	}
}
