package org.komine.wca.api.rest.search.test;

import org.komine.wca.api.rest.search.Search;

public class Run_Search {

	private static String HOSTNAME = "yourserver.yourdomain";
	private static int PORT = 8393;
	private static String COLLECTION = "yourcollection";
	
	private static String[] wcaArgs = {
	    "-hostname",   HOSTNAME,
	    "-port",       Integer.toString(PORT),
	    "-collection", COLLECTION,
	    "-query", "*:*",
	};
	
	public static void main(String[] args) {
		try {
			Search.main(wcaArgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
