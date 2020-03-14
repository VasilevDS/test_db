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
        credentials.setNumber(50);

        ConnectDB connectDB = new ConnectDB(credentials);
        try {
            connectDB.connectDBOrcl();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
