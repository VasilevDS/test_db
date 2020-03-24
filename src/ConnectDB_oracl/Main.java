package ConnectDB_oracl;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        DBCredentials credentials = new DBCredentials();

        credentials.setUrl("jdbc:oracle:thin:@localhost:1521:dborcl");
        credentials.setUser("company");
        credentials.setPassword("123456");
        credentials.setNumber(15);

        long startTime =System.currentTimeMillis();
        ConnectDB connectDB = null;
        try {
            // подключение к БД
            connectDB = new ConnectDB(credentials);
            // вставка N записей в таблицу
            connectDB.dataInsertion();
            // запрос данных из таблицы
            int[] temp = connectDB.dataSelect();
            connectDB.close();
            XML xml = new XML();
            xml.createXML(temp);
            xml.XslTransform();
            xml.summaField();
        } catch (SQLException ex){
            Connection connection = connectDB.getCn();
            if (connection != null) {
                connection.rollback();
                connection.setAutoCommit(true);
                connection.close();
            }
            System.out.println("SQLException caught");
            System.out.println("---");
            while (ex != null){
                System.out.println("Message : " + ex.getMessage());
                System.out.println("SQLState : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (NumberNNotFitException e) {
            e.printStackTrace();
        }
        long timeSpent =System.currentTimeMillis()- startTime;
        System.out.println("lead time: "+ timeSpent +" ms");
    }
}
