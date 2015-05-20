package com.sdsu.spatialProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
public class ConnectionManager {
	private static Connection connectionObject = null;
	
	public static Connection getConnection(){
		try{
        	if(connectionObject == null){
        		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        		connectionObject = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","sashank","sashank");
        	}
        }catch(Exception e){
            System.out.println("Problem in openning connection : " + e);
            e.printStackTrace();
        }
        
        return connectionObject;
	}
}