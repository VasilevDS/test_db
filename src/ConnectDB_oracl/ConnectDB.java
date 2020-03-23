package ConnectDB_oracl;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;

public class ConnectDB {

    private int n;
    private Statement statement;
    private Connection cn;
    private DBCredentials credentials;
    private final String tableName = "test_java";

    public void setN(int n) {
        this.n = n;
    }

    public ConnectDB(DBCredentials credentials) throws SQLException {

        this.credentials = credentials;
        setN(credentials.getNumber());
        cn = connection();
        cn.setAutoCommit(false);
    }

    public void connectDBOrcl() throws SQLException, IOException, SAXException, ParserConfigurationException {

        setN(credentials.getNumber());
        int[] temp = new int[n];

        /*try {
            DriverManager.registerDriver (
                    new oracle.jdbc.driver.OracleDriver());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }*/


        try {
//            Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
//            DriverManager.setLogStream(System.out);

            cn =  DriverManager.getConnection (credentials.getUrl(), credentials.getUser(), credentials.getPassword());

            DatabaseMetaData dma = cn.getMetaData ();
            // Печать сообщения об успешном соединении
            System.out.println("\nConnected to " + dma.getURL());
            System.out.println("Driver " + dma.getDriverName());
            System.out.println("Version " + dma.getDriverVersion());
            cn.setAutoCommit(false);

            statement = cn.createStatement();
            statement.executeUpdate("delete from test_java");

            PreparedStatement preparedStatement = cn.prepareStatement("insert into test_java values (?)");

            int a = 1;
            while (a <= n) {
                preparedStatement.setInt(1, a);
                a++;
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

            ResultSet rs = statement.executeQuery ("SELECT * FROM test_java");

            int b = 0;
            while (rs.next()) {
                if(b < temp.length) {
                    temp[b] = rs.getInt(1);
                    b++;
                }
            }

            statement.close();
            rs.close();
            cn.commit();
            cn.setAutoCommit(true);
            cn.close();

        } catch (SQLException ex){
            if (cn != null) {
                cn.rollback();
                cn.setAutoCommit(true);
                cn.close();
            }
            System.out.println("SQLException caught");
            System.out.println("---");
            while (ex != null){
                System.out.println("Message : " + ex.getMessage());
                System.out.println("SQLState : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
                return;
            }
        } catch (Exception ex){
            System.out.println(ex);
            return;
        }

        XML xml = new XML();
        xml.createXML(temp);
        xml.XslTransform();
        xml.summaField();
    }

//    public static void main(String[] args) throws SQLException, ParserConfigurationException, SAXException, IOException {
//        ConnectDB connectDB = new ConnectDB();
//        connectDB.setUrl("jdbc:oracle:thin:@localhost:1521:dborcl");
//        connectDB.setUser("company");
//        connectDB.setPassword("123456");
//        connectDB.setN(10);
//        connectDB.connectDBOrcl();
//    }

    public Connection connection() throws SQLException {

        try {
            cn =  DriverManager.getConnection (credentials.getUrl(), credentials.getUser(), credentials.getPassword());
            DatabaseMetaData dma = cn.getMetaData ();
            // Печать сообщения об успешном соединении
            System.out.println("\nConnected to " + dma.getURL());
            System.out.println("Driver " + dma.getDriverName());
            System.out.println("Version " + dma.getDriverVersion()+"\n");
        } catch (SQLException ex){
            if (cn != null) {
            cn.rollback();
            cn.setAutoCommit(true);
            cn.close();
            }
            System.out.println("SQLException caught");
            System.out.println("---");
            while (ex != null){
                System.out.println("Message : " + ex.getMessage());
                System.out.println("SQLState : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
                return null;
            }
        } catch (Exception ex){
            System.out.println(ex);
            return null;
        }
        return cn;
    }

    public boolean tableCheck (String tableName) throws SQLException {
        final String query = "select 1 from dual where EXISTS (select * from "+ tableName +")";
        boolean result = false;
        ResultSet rs = null;
        Statement stat = cn.createStatement();
        rs = stat.executeQuery(query);
        result = rs.next();
        return result;
    }

    public void dataInsertion () throws SQLException {
        statement = cn.createStatement();
        System.out.println("Start data insertion");

        if (tableCheck(tableName)) {
            statement.executeUpdate("delete from " + tableName);
            System.out.println("clean table");
        }

        PreparedStatement preparedStatement = cn.prepareStatement("insert into " + tableName + " values (?)");

        int a = 1;
        while (a <= n) {
            preparedStatement.setInt(1, a);
            a++;
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        System.out.println("Finish data insertion\n");
    }

    public int[] dataSelect () throws SQLException {
        String query = "SELECT * FROM " + tableName;
        int[] result = new int[n];

        System.out.println("Start read data");
        ResultSet rs = statement.executeQuery (query);

        int b = 0;
        while (rs.next()) {
            if(b < result.length) {
                result[b] = rs.getInt(1);
                b++;
            }
        }
        System.out.println("Finish data read\n");
        rs.close();
        return result;
    }

    public void close() throws SQLException {
        System.out.println("Close connected\n");
        statement.close();
        cn.commit();
        cn.setAutoCommit(true);
        cn.close();
    }
}
