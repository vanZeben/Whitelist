package com.imdeity.whitelist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.config.Configuration;

@SuppressWarnings("unused")
public class Settings {
	private static Configuration config;
	private static Configuration language;
	
	public static void loadConfig(String filepath, String defaultRes) throws IOException {
		File file = FileMgmt.CheckYMLexists(filepath, defaultRes);
		if (file != null) {
				
			// read the config.yml into memory
			config = new Configuration(file);
			config.load();
			file = null;
		}	
	}
	
	// Functions to pull data from the config and language files


    private static String[] parseString(String str) {
		return parseSingleLineString(str).split("@");
	}
	
	public static String parseSingleLineString(String str) {
		return str.replaceAll("&", "\u00A7");
	}
	
	public static Boolean getBoolean(String root){
        return config.getBoolean(root.toLowerCase(), true);
    }
    private static Double getDouble(String root){
        return config.getDouble(root.toLowerCase(), 0);
    }
    private static Integer getInt(String root){
        return config.getInt(root.toLowerCase(), 0);
    }
    private static Long getLong(String root){
        return Long.parseLong(getString(root).trim());
    }
    
    /*
     * Public Functions to read data from the Configuration
     * and Language data
     * 
     * 
     */
    
    public static String getString(String root){
        return config.getString(root.toLowerCase());
    }
    public static String getLangString(String root){
        return parseSingleLineString(language.getString(root.toLowerCase()));
    }
    
 // read a comma delimited string into an Integer list
	public static List<Integer> getIntArr(String root) {
		
		String[] strArray = getString(root.toLowerCase()).split(",");
		List<Integer> list = new ArrayList<Integer>();
		if (strArray != null) {
		for (int ctr=0; ctr < strArray.length; ctr++)
			if (strArray[ctr] != null)
				list.add(Integer.parseInt(strArray[ctr].trim()));
		}	
		return list;
	}
	
	// read a comma delimited string into a trimmed list.
	public static List<String> getStrArr(String root) {
		
		String[] strArray = getString(root.toLowerCase()).split(",");
		List<String> list = new ArrayList<String>();
		if (strArray != null) {
		for (int ctr=0; ctr < strArray.length; ctr++)
			if (strArray[ctr] != null)
				list.add(strArray[ctr].trim());
		}
		return list;
	}
	
    ///////////////////////////////////
    
	
	public static String getMySQLServerAddress() {
		return getString("mysql.SERVER_ADDRESS");
	}
	
	public static int getMySQLServerPort() {
		return getInt("mysql.SERVER_PORT");
	}
	
	public static String getMySQLDatabaseName() {
		return getString("mysql.DATABASE_NAME");
	}
	
	public static String getMySQLUsername() {
		return getString("mysql.USERNAME");
	}
	
	public static String getMySQLPassword() {
		return getString("mysql.PASSWORD");
	}
	
	public static String getMySQLDatabaseTableName() {
		return getString("mysql.DATABASE_TABLE_NAME");
	}
	
	///////////////////////

	public static boolean isWhitelistActive() {
		return getBoolean("whitelist.ENABLED");
	}

	public static String getWhitelistMessage() {
        return getString("whitelist.MESSAGE");
    }
}
