package com.neevtech.droolsweb.rs.webservices.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ApacheHttpClient {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage:  Please pass the user name and item id for which you want to test");
			System.exit(-1);
		}
		try{
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet("http://localhost:8080/DroolsWeb/rest/discount/"+ args[0] +"/"+ args[1]);
			request.addHeader("accept", "application/xml");
			HttpResponse response = client.execute(request);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatusLine().getStatusCode());
			}
	
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			    
			String output = "";
			System.out.println("Output from Server.... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			client.getConnectionManager().shutdown();
		} catch(ClientProtocolException ex) {
			ex.printStackTrace();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
