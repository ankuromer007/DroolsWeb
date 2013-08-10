package com.neevtech.droolsweb.rs.webservices.clients;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class JerseyClient {
	
	private static WebResource webResource;
	
	public JerseyClient(String service){
		Client client = Client.create();
		webResource = client.resource("http://localhost:8080/DroolsWeb/rest");
		webResource = webResource.path(service);
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage:  Please pass the user name and item id for which you want to test");
			System.exit(-1);
		}
		new JerseyClient("discount");
		try {
			ClientResponse response = webResource.path(args[0]).path(args[1])
					.accept(MediaType.APPLICATION_XML)
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}

			System.out.println("Output from Server.... \n");
			System.out.println(response.getEntity(String.class));
		} catch(UniformInterfaceException ex) {
			ex.printStackTrace();
		} catch(ClientHandlerException ex) {
			ex.printStackTrace();
		}
	}
}
