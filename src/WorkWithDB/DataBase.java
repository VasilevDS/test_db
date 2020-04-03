package WorkWithDB;

import java.sql.*;

public class DataBase {

    private int numberN;
    private Statement statement;
    private Connection cn;
    private DBCredentials credentials;
    private final String tableName = "test_java";
    private final String queryINSERT = "insert into " + tableName + " values (?)";
    private final String querySELECT = "SELECT * FROM " + tableName;

    public void setNumberN(int numberN) throws NumberNNotFitException{
        if(numberN>0) this.numberN = numberN;
        else throw new NumberNNotFitException("number n does not match");
    }

    public void setCredentials(DBCredentials credentials) {
        this.credentials = credentials;
    }

    public Connection getCn() { return cn; }

    public void connection() throws SQLException {

        try {
            cn =  DriverManager.getConnection (credentials.getUrl(), credentials.getUser(), credentials.getPassword());
            DatabaseMetaData dma = cn.getMetaData ();
            cn.setAutoCommit(false);
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
            }
            return;
        } catch (Exception ex){
            System.out.println(ex);
            return;
        }

    }

    public boolean tableCheck (String tableName) throws SQLException {
        final String query = "select 1 from dual where EXISTS (select * from "+ tableName +")";
        statement = cn.createStatement();
        boolean result;
        ResultSet rs = statement.executeQuery(query);
        result = rs.next();
        rs.close();
        return result;
    }

    public void insertNRecordsInTable () throws SQLException {

        System.out.println("Start data insertion");
        statement = cn.createStatement();
        // Если в таблице находились записи, то они удаляются перед вставкой
        // (Это условие можно удалить и оставить удаление в любом случае)
        if (tableCheck(tableName)) {
            System.out.println("clean table");
            statement.executeUpdate("delete from " + tableName);
        }

        PreparedStatement preparedStatement = cn.prepareStatement(queryINSERT);
        int a = 1;
        while (a <= numberN) {
            preparedStatement.setInt(1, a);
            a++;
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        System.out.println("Finish data insertion\n");
    }

    public int[] getDataFromTable () throws SQLException {
        int[] result = new int[numberN];
        statement = cn.createStatement();

        System.out.println("Start read data");
        ResultSet rs = statement.executeQuery (querySELECT);

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
