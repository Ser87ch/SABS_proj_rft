package ru.sabstest;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.xerces.impl.dv.util.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class XML {

	/**
	 * @return новый XML документ
	 */
	public static Document createNewDoc()
	{
		Document doc = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
			Log.msg(e);
		}

		return doc;
	}

	/**
	 * создает XML файл
	 * @param doc документ
	 * @param filename полный путь к файлу
	 */
	public static void createXMLFile(Document doc, String filename)
	{

		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File xml = new File(filename);
			StreamResult result = new StreamResult(xml);
			//StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			Log.msg(e);
		} catch (TransformerException e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

	/**
	 * @param filename полный путь к файлу
	 * @return главный элемент xml
	 */
	public static Element getXMLRootElement(String filename)
	{
		Element root = null;
		try {
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);	
			
			root = doc.getDocumentElement();
			root.normalize();
		} catch (Exception e) {

			e.printStackTrace();
			Log.msg(e);
		}
		return root;

	}

	public static Element getXMLRootElementFromString(String src)
	{
		Element root = null;
		try {
			//InputSource is = new InputSource(new StringReader(src));
			InputStream is = new ByteArrayInputStream(src.getBytes("Windows-1251"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);				
			
			root = doc.getDocumentElement();
			root.normalize();
		} catch (Exception e) {

			e.printStackTrace();
			Log.msg(e);
		}
		return root;

	}
	
	/**
	 * валидация xml файла xsd
	 * @param xsd полный путь к xsd
	 * @param xml полный путь к xml
	 */
	public static void validate(String xsd, String xml)
	{
		try {
			Source schemaFile = new StreamSource(new File(xsd));
			Source xmlFile = new StreamSource(new File(xml));
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();

			validator.validate(xmlFile);

			Log.msg("XML файл " + xml + " соответствует схеме " + xsd + ".");
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg("XML файл " + xml + " не соответствует схеме " + xsd + ".");
			Log.msg(e);
		}
	}

	/**
	 * валидация xml файла xsd
	 * @param xsd полный путь к xsd
	 * @param xml полный путь к xml
	 */
	public static void validate(String[] xsd, String xml)
	{
		try {
			Source[] schemaFile = new Source[xsd.length];
			for(int i = 0; i < xsd.length; i++)
				schemaFile[i] = new StreamSource(new File(xsd[i]));
			Source xmlFile = new StreamSource(new File(xml));
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();

			validator.validate(xmlFile);

			Log.msg("XML файл " + xml + " соответствует схеме " + xsd + ".");
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg("XML файл " + xml + " не соответствует схеме " + xsd + ".");
			Log.msg(e);
		}
	}

	public static String getChildValueString(String sTag, Element root) 
	{
		try{
			String s;
			s = root.getElementsByTagNameNS("*",sTag).item(0).getFirstChild().getNodeValue();
			return s == null ? "" : s;
		}catch(Exception e){
			return "";
		}
	}

	public static int getChildValueInt(String sTag, Element root) 
	{
		try{
			String s;
			s = root.getElementsByTagNameNS("*",sTag).item(0).getFirstChild().getNodeValue();
			return s == null ? 0 : Integer.parseInt(s);
		}catch(Exception e){
			return 0;
		}
	}

	public static Date getChildValueDate(String sTag, Element root) 
	{
		try{
			String s;
			s = root.getElementsByTagNameNS("*",sTag).item(0).getFirstChild().getNodeValue();
			return s == null ? new Date(0) : Date.valueOf(s);
		}catch(Exception e){
			return new Date(0);
		}
	}

	public static float getChildValueFloat(String sTag, Element root) 
	{
		try{
			String s;
			s = root.getElementsByTagNameNS("*",sTag).item(0).getFirstChild().getNodeValue();
			return s == null ? 0 : Float.parseFloat(s);
		}catch(Exception e){
			return 0;
		}
	}

	public static Element createNode(Document doc, Element rootElement, String tag, String value)
	{
		Element el = doc.createElement(tag);
		if(value != null)
			el.appendChild(doc.createTextNode(value));
		rootElement.appendChild(el);
		return el;
	}

	public static Element createNode(Document doc, Element rootElement, String tag, Date value)
	{
		Element el = doc.createElement(tag);
		if(value != null)
			el.appendChild(doc.createTextNode(new SimpleDateFormat("yyyy-MM-dd").format(value)));
		rootElement.appendChild(el);
		return el;
	}

	public static Element createNode(Document doc, Element rootElement, String tag, int value)
	{
		Element el = doc.createElement(tag);
		el.appendChild(doc.createTextNode(Integer.toString(value)));
		rootElement.appendChild(el);
		return el;
	}

	public static Element createNode(Document doc, Element rootElement, String tag, float value)
	{
		Element el = doc.createElement(tag);
		el.appendChild(doc.createTextNode(Float.toString(value)));
		rootElement.appendChild(el);
		return el;
	}

	public static Date getOptionalDateAttr(String attr, Element el)
	{
		String s = el.getAttribute(attr);
		if(!s.equals(""))
			return Date.valueOf(s);
		else
			return null;
	}

	public static int getOptionalIntAttr(String attr, Element el)
	{
		String s = el.getAttribute(attr);
		if(!s.equals(""))
			return Integer.parseInt(s);
		else
			return 0;
	}

	public static void setOptinalAttr(Element el, String attrName, Date value)
	{
		if(value != null)
			el.setAttribute(attrName, new SimpleDateFormat("yyyy-MM-dd").format(value));
	}

	public static void setOptinalAttr(Element el, String attrName, int value)
	{
		if(value != 0)
			el.setAttribute(attrName, Integer.toString(value));
	}

	public static void setOptinalAttr(Element el, String attrName, String value)
	{
		if(value != null && !value.equals(""))
			el.setAttribute(attrName, value);
	}

	public static void createXMLFromBase64(String src, String trg)
	{
		String xml = decodeBase64(src);

		try{
			FileWriter fstream = new FileWriter(trg);
			BufferedWriter out = new BufferedWriter(fstream);	
			out.write(xml);
			out.flush();
			fstream.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			Log.msg(e);
		}
	}	
	
	public static String decodeBase64(String src)
	{
		Element root = XML.getXMLRootElement(src);
		Element n = (Element) root.getElementsByTagName("sen:Object").item(0);
		String encodedxml = n.getTextContent();
		
		String xml = new String(Base64.decode(encodedxml));
		
		return xml;
	}
}
