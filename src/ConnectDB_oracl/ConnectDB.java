package ConnectDB_oracl;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;

public class ConnectDB {

    private int n;
    private String url;
    private String user;
    private String password;
    private Statement statement;
    private Connection cn;

    public void setN(int n) {
        this.n = n;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void connectDBOrcl() throws SQLException, IOException, SAXException, ParserConfigurationException {

        int[] temp = new int[n];

        try {
            DriverManager.registerDriver (
                    new oracle.jdbc.driver.OracleDriver());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }


        try {
            cn =  DriverManager.getConnection (url,user,password);

            cn.setAutoCommit(false);

            statement = cn.createStatement();
            statement.executeUpdate("delete from test");

            PreparedStatement preparedStatement = cn.prepareStatement("insert into test values (?)");

            int a = 1;
            while (a <= n) {
                preparedStatement.setInt(1, a);
                a++;
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

            ResultSet rs = statement.executeQuery ("SELECT FIELD FROM test");

            int b = 0;
            while (rs.next()) {
                if(b < temp.length) {
                    temp[b] = rs.getInt(1);
                    b++;
                }
            }
            cn.commit();
            statement.close();
            rs.close();
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
            }
        } catch (Exception ex){ System.out.println(ex); }

        XML xml = new XML();
        xml.createXML(temp);
        xml.XslTransform();
        xml.summaField();
    }

    public static void main(String[] args) throws SQLException, ParserConfigurationException, SAXException, IOException {
        ConnectDB connectDB = new ConnectDB();
        connectDB.setUrl("jdbc:oracle:thin:@localhost:1521:dborcl");
        connectDB.setUser("company");
        connectDB.setPassword("123456");
        connectDB.setN(50);
        connectDB.connectDBOrcl();
    }
}
