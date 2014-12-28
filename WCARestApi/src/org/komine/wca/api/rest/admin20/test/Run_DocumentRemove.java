package org.komine.wca.api.rest.admin20.test;

import org.komine.wca.api.rest.admin20.DocumentRemove;

public class Run_DocumentRemove {

	private static String HOSTNAME = "yourserver.yourdomain";
	private static int PORT = 8390;
	private static String COLLECTION = "yourcollection";
	
	private static final String USERNAME = "esadmin";
	private static final String PASSWORD = "password";
	
	private static String[] wcaArgs = {
	    "-hostname",   HOSTNAME,
	    "-port",       Integer.toString(PORT),
	    "-collection", COLLECTION,
	    "-username",   USERNAME,
	    "-password",   PASSWORD,
	    "-documentid", "",
	};
	
	private static String[] docids = {
		"api:///aaa",
		"api:///bbb",
		"",
		"",
		"",
		"",
		};
	
	public static void main(String[] args) {
		for (int i=0; i<docids.length; i++) {
			String docid = docids[i];
			if ((null != docid) && (!docid.isEmpty())) {
				wcaArgs[11] = docid;
				try {
					DocumentRemove.main(wcaArgs);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
