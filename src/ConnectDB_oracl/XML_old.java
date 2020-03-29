package ConnectDB_oracl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XML_old
{

	public void createXML(int[] n)
	{
		try
		{
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element entries = document.createElement("entries");
			document.appendChild(entries);
			
			for (int i : n)
			{
				String c = Integer.toString(i);
				
				Element entry = document.createElement("entry");
				entries.appendChild(entry);
			
				Element field = document.createElement("field");
				field.setTextContent(c);
				entry.appendChild(field);
			}

			
			//��������� ��������� ������������� XML ��������� � ���� 
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			
			//-------------�������� XML � ������������ ������---------------------------------
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			//---------------------------------------------------------------------------------
			
			DOMSource source = new DOMSource(document);
			//StreamResult result = new StreamResult(new File(System.getProperty("user.id")+File.separator+FILENAME));
			StreamResult result = new StreamResult(new File("1.xml"));
		
			transformer.transform(source, result);

		}
		catch(ParserConfigurationException | TransformerConfigurationException ex)
		{
			Logger.getLogger(ConnectDB.class.getName())
				.log(Level.SEVERE, null, ex);
		}
		catch (TransformerException ex)
		{
			Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void XslTransform() {
		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource xslStream = new StreamSource(new File("template.xsl")); // ��� ������ xsl

		StreamSource in = new StreamSource("1.xml");
		StreamResult out = new StreamResult("2.xml");

		try {
			Transformer transformer = factory.newTransformer(xslStream);
			transformer.transform(in, out);
		} catch (TransformerException e){ e.printStackTrace(); }
	}

	public static void summaField() throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();

		XMLHandler handler = new XMLHandler();
		parser.parse(new File("2.xml"), handler);

		System.out.println(handler.result);
	}

	private static class XMLHandler extends DefaultHandler {
		BigInteger result;
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (qName.equals("entry")) {
				//result += Integer.parseInt(attributes.getValue("field"));
				result.add(BigInteger.valueOf(20));
				result.add(BigInteger.valueOf(Integer.parseInt(attributes.getValue("field"))));
			}
		}
	}

}
