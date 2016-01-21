package com.fedbid.mobility.buyer.client.rest;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fedbid.mobility.auth.model.DeviceUser;
import com.fedbid.mobility.auth.model.MarketSector;
import com.fedbid.mobility.auth.model.User;
import com.fedbid.mobility.buyer.model.Buy;
import com.fedbid.mobility.utils.Constants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class BuyerClient {

	public static void main(String[] args) {
		BuyerClient client = new BuyerClient();
		//client.login();
		client.extendBuy();
	}

	private String marshal(Object input, Class type) throws Exception{
		JAXBContext context = JAXBContext.newInstance(type);
		Marshaller jaxbMarshaller = context.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		StringWriter writer = new StringWriter();
		jaxbMarshaller.marshal(input, writer);
		String strUser = writer.toString();
		
		return strUser;
	}
	
	private Object unmarshal(String xml, Class type, String topackage) throws Exception{
		JAXBContext context = JAXBContext.newInstance(topackage);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller.unmarshal(new StringReader(xml));		
	}
	
	public ClientResponse getResponse(Object input, Class requestType, String url) throws Exception{
		Client client = Client.create();
		WebResource webResource = client.resource(url);			
		//WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON);
		WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_XML);
		
		String strUser = marshal(input, requestType);
		System.out.println(strUser);
		builder.entity(strUser);
		
		//builder.header("Accept", MediaType.APPLICATION_XML);
		//builder.header("Content-Type", "application/xml;charset=UTF-8");
		builder.header("Content-Type", MediaType.APPLICATION_XML);
		
		if(input instanceof DeviceUser){
			DeviceUser du = (DeviceUser)input;
			builder.header("sessionKey", du.getSessionKey());
			builder.header("user_id", du.getUser_id());
			builder.header("deviceToken", du.getDeviceToken());
			builder.header("deviceUuid", du.getDeviceUuid());
			
		}

		ClientResponse response = builder.post(ClientResponse.class);
		
		return response;
	}
	
	
		
	public DeviceUser login() {
		try {
			User user = new User();
			MarketSector ms = new MarketSector();
			ms.setSectorType(Constants.BUYER);
			ms.setMarketSectorId(Constants.DBFED + "");
			ms.setMarketSector("GOV");
			List<MarketSector> marketSectors = new ArrayList<MarketSector>();
			marketSectors.add(ms);
			user.setMarketSectors(marketSectors);
			user.setUser_id(94021);
			user.setDeviceToken("4bb9d307db2a2fca9d2e98cba63c85a83fed3d39cba9865f8c4333f3fa56b80f");
			user.setDeviceUuid("7349B8E4-89B7-4E8A-875E-AE8800522326");
			user.setUsername("buyer_94021@fedbidqa.com");
			user.setPassword("password");
			
			ClientResponse response = getResponse(user, User.class, "http://localhost:8080/mobility/rest/auth/login");

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			
			String output = response.getEntity(String.class);
			System.out.println("Server response : \n");
			System.out.println(output);
			
			com.fedbid.mobility.auth.model.DeviceUser deviceUser = (com.fedbid.mobility.auth.model.DeviceUser)unmarshal(output, DeviceUser.class, "com.fedbid.mobility.auth.model");
			System.out.println(deviceUser);
			
			return deviceUser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public void extendBuy() {
		try {
						
			DeviceUser deviceUser = login();
			deviceUser.setDeviceToken("4bb9d307db2a2fca9d2e98cba63c85a83fed3d39cba9865f8c4333f3fa56b80f");
			deviceUser.setDeviceUuid("7349B8E4-89B7-4E8A-875E-AE8800522326");			
			deviceUser.setExtendDate("28-01-2015");
			deviceUser.setExtendTime("10:00");
			
			
			
			ClientResponse response = getResponse(deviceUser, DeviceUser.class, "http://localhost:8080/mobility/rest/buyer/buy/detail/687150/extend");

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			
			String output = response.getEntity(String.class);
			System.out.println("Server response : \n");
			System.out.println(output);
			
			Buy buy = (Buy)unmarshal(output, Buy.class, "com.fedbid.mobility.buyer.model");
			System.out.println(deviceUser);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loginHttpPost(){
		try {
			User user = new User();
			MarketSector ms = new MarketSector();
			ms.setSectorType(Constants.BUYER);
			ms.setMarketSectorId(Constants.DBFED + "");
			ms.setMarketSector("GOV");
			List<MarketSector> marketSectors = new ArrayList<MarketSector>();
			marketSectors.add(ms);
			user.setMarketSectors(marketSectors);
			user.setUser_id(94021);
			user.setDeviceToken("3d658e72bdd70c6d6fb12dca9ca9ba73568aa817983587d85a67c91af9c9d95f");
			user.setDeviceUuid("F47E20A6-6EF4-4432-A655-893CC9F16E43");
			user.setUsername("buyer_94021@fedbidqa.com");
			user.setPassword("password");
			
			JAXBContext context = JAXBContext.newInstance(User.class);
			Marshaller jaxbMarshaller = context.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			StringWriter writer = new StringWriter();
			jaxbMarshaller.marshal(user, writer);
			String strUser = writer.toString();
			System.out.println(strUser);
			
			HttpPost httppost = new HttpPost("http://localhost:8080/FBMobileServices-SNAPSHOT/rest/auth/login");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder = builder.addTextBody("user", strUser, ContentType.APPLICATION_XML);			
			HttpEntity reqEntity = builder.build();
			           
            CloseableHttpClient httpclient = HttpClients.createDefault();
            httppost.setEntity(reqEntity);
            httppost.addHeader("Accept", "application/xml");
            httppost.addHeader("Content-Type","text/plain; charset=UTF-8");
            //httppost.addHeader("Authorization", "Bearer 1");
            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
                httpclient.close();
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void getOrganizations(){
		DeviceUser deviceUser = new DeviceUser();
		try{
			
			Client client = Client.create();

			WebResource webResource = client.resource("http://localhost:8080/FBMobileServices-SNAPSHOT/rest/buyer/buy/organizations");
			
			System.out.println("-------------organizations------------- ");
			webResource.header("Accept", MediaType.APPLICATION_XML);
			webResource.header("Content-Type", "application/xml;charset=UTF-8");
			webResource.header("Authorization", "Bearer 1");
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_XML);
			builder.post(deviceUser);

			/*
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);
			*/
			System.out.println("Server response : \n");
			//System.out.println(output);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	private HttpHeaders getHttpHeadersWithUserCredentials(HttpHeaders headers) {

		String username = "b2buyweb";
		// String password = "spring";
		String password = "w3Bb2Buy$";

		String combinedUsernamePassword = username + ":" + password;
		//byte[] base64Token = Base64.encode(combinedUsernamePassword.getBytes());
		//String base64EncodedToken = new String(base64Token);
		// adding Authorization header for HTTP Basic authentication
		//headers.add("Authorization", "Basic  " + base64EncodedToken);

		return headers;
	}
	
	public void testJsonClient() {
		try {
			// TODO Auto-generated method stub
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
			User user = new User();
			MarketSector ms = new MarketSector();
			ms.setSectorType(Constants.BUYER);
			ms.setMarketSectorId(Constants.DBFED + "");
			ms.setMarketSector("GOV");
			List<MarketSector> marketSectors = new ArrayList<MarketSector>();
			marketSectors.add(ms);
			user.setMarketSectors(marketSectors);
			user.setUser_id(102482);

			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getFeatures().put(
					JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			Client client = Client.create(clientConfig);

			WebResource resource = client
					.resource("http://localhost:8080/FBMobileServices-SNAPSHOT/rest/auth/login");
			// Builder builder = resource.accept("application/xml");
			WebResource.Builder builder = resource.accept(MediaType.APPLICATION_XML);
			// builder.type("application/xml");
			builder.type(MediaType.APPLICATION_XML);
			ClientResponse response = builder.post(ClientResponse.class, user);

			// resource.accept(MediaType.APPLICATION_JSON_TYPE);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code 123: "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Server response .... \n");
			System.out.println(output);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}
