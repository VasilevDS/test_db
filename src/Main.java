import WorkWithDB.DBCredentials;
import WorkWithDB.DataBase;
import WorkWithDB.NumberNNotFitException;
import WorkWithXML.XMLFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        String url = "jdbc:oracle:thin:@localhost:1521:dborcl";
        String user = "company";
        String password = "123456";
        int numbN = 50;

        DBCredentials credentials = new DBCredentials(url, user, password);

        long startTime =System.currentTimeMillis();
        DataBase dataBase = new DataBase();

        try {
            dataBase.setCredentials(credentials);
            dataBase.setNumberN(numbN);
        } catch (NumberNNotFitException e) {
            System.out.println("The number N must be greater than 0");
            return;
        }
        try {
            dataBase.connection();                      // подключение к БД
            dataBase.insertNRecordsInTable();           // вставка N записей в таблицу
            int[] temp = dataBase.getDataFromTable();   // запрос данных из таблицы
            dataBase.close();


            XMLFile xmlFile = new XMLFile();
            xmlFile.createXML(temp);        // создание файла xml1
            xmlFile.creatingXml2FromXl1();  // создание xml2 из трасформации xml1
            xmlFile.totalFieldXmlTwo();     // арифм. сумма поля Field

        } catch (SQLException ex){
            Connection connection = dataBase.getCn();
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
        } catch (ParserConfigurationException|FileNotFoundException|TransformerException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        long timeSpent =System.currentTimeMillis()- startTime;
        System.out.println("lead time: "+ timeSpent +" ms");
    }
}
