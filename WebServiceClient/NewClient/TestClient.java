package com.test.webservice.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fedbid.msgprocessor.domain.fbo.AuthenticationData;
import com.fedbid.msgprocessor.domain.fbo.CancelNotice;
import com.fedbid.msgprocessor.domain.fbo.CancelNoticeData;

public class TestClient {
	
	public static void main(String k[]) {
		TestClient client = new TestClient();
		client.test2();		
	}
	
	public void test2(){
		try{
		String endpointUrl = "http://fbo-test.symplicity.com/ws/fbo_api.php";
		
		QName serviceName = new QName("https://fbo-test.symplicity.com/", "FBOWebServiceService");
		QName portName = new QName("https://fbo-test.symplicity.com/", "FBOWebServicePort");
				
		/** Create a service and add at least one port to it. **/ 
		Service service = Service.create(serviceName);
		service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointUrl);
				
		/** Create a Dispatch instance from a service.**/ 
		Dispatch<SOAPMessage> dispatch = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);
			
		/** Create SOAPMessage request. **/
		// compose a request message
		MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);

		// Create a message.  This example works with the SOAPPART.
		SOAPMessage request = mf.createMessage();
		SOAPPart part = request.getSOAPPart();

		// Obtain the SOAPEnvelope and header and body elements.
		SOAPEnvelope env = part.getEnvelope();
		SOAPHeader header = env.getHeader();
		
		/*
		SOAPElement authElement = header.addChildElement(new QName("https://fbo-test.symplicity.com/", "AuthenticationData"));
		SOAPElement user = authElement.addChildElement(new QName("username"));
		user.setValue("fedbid1234");
		SOAPElement pwd = authElement.addChildElement(new QName("password"));
		pwd.setValue("Password123%");
		*/
		
		AuthenticationData auth = new AuthenticationData();
		auth.setUsername("fedbid1234");
		auth.setPassword("Password123%");
		
		JAXBContext jaxbContext = JAXBContext.newInstance(AuthenticationData.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(auth, sw);
		System.out.println(sw);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document document = builder.parse(new ByteArrayInputStream(sw.toString().getBytes()));	 
	    	    
	    DocumentFragment docFrag = document.createDocumentFragment();
	    docFrag.appendChild(document.getDocumentElement());
	    Document headerOwner = header.getOwnerDocument();
	    org.w3c.dom.Node replacingNode = headerOwner.importNode(docFrag, true);
	    header.appendChild(replacingNode);
		
		
		SOAPBody soapBody = env.getBody();
		
		CancelNotice cancelNotice = new CancelNotice();
		CancelNoticeData data = new CancelNoticeData();
		data.setSolnbr("1231231233333");
		XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
		data.setDate(new Date());
		data.setDesc("test");
		cancelNotice.setData(data);
		jaxbContext = JAXBContext.newInstance(CancelNotice.class);
		jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		sw = new StringWriter();
		
		jaxbMarshaller.marshal(cancelNotice, sw);
		
		factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    builder = factory.newDocumentBuilder();
	    document = builder.parse(new ByteArrayInputStream(sw.toString().getBytes()));	        
	    soapBody.addDocument(document);	    
		request.saveChanges();
		
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		request.writeTo(out);
		String strMsg = new String(out.toByteArray());
		System.out.println(strMsg);

		/** Invoke the service endpoint. **/
		SOAPMessage response = dispatch.invoke(request);
		System.out.println("=========="+response.getSOAPBody().getValue());
		NodeList nodeList = response.getSOAPPart().getDocumentElement().getChildNodes();
		printNodeValue(nodeList);
		//printResponseXml(response.getSOAPPart().getDocumentElement().getFirstChild());
		/** Process the response. **/
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
	private void printResponseXml(Node node){
		try{
			StringWriter writer = new StringWriter();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();			
			transformer.transform(new DOMSource(node), new StreamResult(writer));			
			String xml = writer.toString();
			System.out.println(xml);
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
	private void printNodeValue(NodeList list){
		for(int k=0; k<list.getLength(); k++){
			if(k==1){
				Node node = list.item(k);
				
				node = node.getFirstChild();
				
				printResponseXml(node.getFirstChild());				
			}
		}
	}
	
}
