package ConnectDB_oracl;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        DBCredentials credentials = new DBCredentials();

        credentials.setUrl("jdbc:oracle:thin:@localhost:1521:dborcl");
        credentials.setUser("company");
        credentials.setPassword("123456");
        credentials.setNumber(5000000);

        long startTime =System.currentTimeMillis();
        try {
            ConnectDB connectDB = new ConnectDB(credentials);
            //connectDB.connectDBOrcl();
            connectDB.dataInsertion();
            int[] temp = connectDB.dataSelect();
            connectDB.close();
            XML xml = new XML();
            xml.createXML(temp);
            xml.XslTransform();
            xml.summaField();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        long timeSpent =System.currentTimeMillis()- startTime;
        System.out.println("lead time: "+ timeSpent +" ms");
    }
}
