package org.komine.wca.api.rest.admin20.test;
import org.komine.wca.api.rest.admin20.DocumentAddFile;

public class Run_DocumentAddFile {
	
	private static String HOSTNAME = "yourserver.yourdomain";
	private static int PORT = 8390;
	private static String COLLECTION = "yourcollection";
	private static String LANGUAGE = "ja";
	
	private static final String USERNAME = "esadmin";
	private static final String PASSWORD = "password";
	
	private static String[] wcaArgs = {
	    "-hostname",   HOSTNAME,
	    "-port",       Integer.toString(PORT),
	    "-collection", COLLECTION,
	    "-username",   USERNAME,
	    "-password",   PASSWORD,
	    "-filepath",   "C:\\temp\\test.txt",
	    "-language",   LANGUAGE,
	};
	
	public static void main(String[] args) {
		try {
			DocumentAddFile.main(wcaArgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
