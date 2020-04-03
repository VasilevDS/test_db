package WorkWithXML;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class XMLFile {
    private Document document;
    private File fileXMLOne = new File("test_1.xml");
    private File fileXMLTwo = new File("test_2.xml");
    private File template = new File("template.xsl");


    public void createXML(int[] recList) throws TransformerException, FileNotFoundException, ParserConfigurationException {
        System.out.println("create XML_1 file");
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element entries = document.createElement("entries");
        document.appendChild(entries);
        Text text;

        for(int n: recList) {
            Element entry = document.createElement("entry");
            Element field = document.createElement("field");
            text = document.createTextNode(Integer.toString(n));
            entries.appendChild(entry);
            entry.appendChild(field);
            field.appendChild(text);
        }

        saveXML();
    }

    public void saveXML() throws TransformerException, FileNotFoundException {
        System.out.println("save file" + fileXMLOne + "\n");
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(document), new StreamResult(new FileOutputStream(fileXMLOne, false)));
    }

    public void creatingXml2FromXl1() {
        System.out.println("Transform " + fileXMLOne + " file to " + fileXMLTwo + "\n");
        TransformerFactory factory = TransformerFactory.newInstance();
        StreamSource xslStream = new StreamSource(template); // загрузка шаблона xsl

        StreamSource in = new StreamSource(fileXMLOne);
        StreamResult out = new StreamResult(fileXMLTwo);

        try {
            Transformer transformer = factory.newTransformer(xslStream);
            transformer.transform(in, out);
        } catch (TransformerException e){
            System.out.println("Error converting " + fileXMLOne + " file to " + fileXMLTwo);
            e.printStackTrace();
        }
    }

    public void totalFieldXmlTwo() {
        System.out.println("parsing " + fileXMLTwo);
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileXMLTwo);
            Element element = document.getDocumentElement();
            System.out.println("\n-------------------------------------------------------");
            System.out.println("The sum of the values of all \"field\" attributes:\n" +
                                parsingXmlTwo(element.getElementsByTagName("entry")));

        } catch (SAXException e) {
            System.out.println("Error parsing file " + fileXMLTwo);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error parsing file " + fileXMLTwo);
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            System.out.println("Error parsing file " + fileXMLTwo);
            e.printStackTrace();
        }
    }

    public BigInteger parsingXmlTwo(NodeList nodeList) {
        BigInteger totalField = new BigInteger("0");
        for (int i = 0; i < nodeList.getLength(); i++) {
            if(nodeList.item(i) instanceof Element) {
                totalField = totalField.add(BigInteger.valueOf(
                        Integer.parseInt(((Element) nodeList.item(i)).getAttribute("field"))));
            }
        }
        return totalField;
    }

}
