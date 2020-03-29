package ConnectDB_oracl;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        DBCredentials credentials = new DBCredentials();

        String url = "jdbc:oracle:thin:@localhost:1521:dborcl";
        String user = "company";
        String password = "123456";
        int numbN = 2000000;

        credentials.setUrl(url);
        credentials.setUser(user);
        credentials.setPassword(password);
        try {
            credentials.setNumberN(numbN);
        } catch (NumberNNotFitException e) {
            System.out.println("The number N must be greater than 0");
            return;
        }

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


            XML xml = new XML(temp);
            xml.createXML();
            xml.totalFieldXmlTwo();
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
        } catch (NumberNNotFitException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        long timeSpent =System.currentTimeMillis()- startTime;
        System.out.println("lead time: "+ timeSpent +" ms");
    }
}
