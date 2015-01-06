package org.komine.wca.api.rest.util;

import java.util.HashMap;

public class ArgsUtil {
	
	private HashMap<String, String> params = new HashMap<String, String>();
	
	public ArgsUtil(String[] args) {
		for (int i=0; i<args.length; i++) {
			if (args[i].startsWith("-")) {
				String key = args[i].substring(1).toLowerCase();
				String value = null;
						
				// Check if next arg is value
				if ((i+1 < args.length) && (!args[i+1].startsWith("-"))) {
					i++;
					value = args[i];
				}

				if (params.containsKey(key)) {
					params.remove(key);
				}
				
				params.put(key, value);
			}
		}
	}
	
	public String get(String key) {
		return params.get(key.toLowerCase());
	}
	
	public String getString(String key) {
		return params.get(key.toLowerCase());
	}
	
	public int getInt(String key) {
		try {
			String value = params.get(key.toLowerCase());
			if ((null != value) && (!value.isEmpty())) {
				return Integer.parseInt(params.get(key.toLowerCase()));
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public boolean isExist(String key) {
		return params.containsKey(key.toLowerCase());
	}
}
