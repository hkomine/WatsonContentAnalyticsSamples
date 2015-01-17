package org.komine.wca.api.rest.admin20.test;

import org.komine.wca.api.rest.admin20.CollectionAdd;

public class Run_CollectionAdd {
	
	private static String HOSTNAME = "yourserver.yourdomain";
	private static int PORT = 8390;;
	
	private static final String USERNAME = "esadmin";
	private static final String PASSWORD = "password";
	
	private static String[] wcaArgs = {
	    "-hostname",   HOSTNAME,
	    "-port",       Integer.toString(PORT),
	    "-username",   USERNAME,
	    "-password",   PASSWORD,
	    "-name",       "あいうえお Collection",
	    "-id",         "my_collection",
	    "-type",       "Analytics",
	};
	
	public static void main(String[] args) {
		try {
			CollectionAdd.main(wcaArgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
