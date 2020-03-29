package ConnectDB_oracl;

import java.sql.*;

public class ConnectDB {

    private int n;
    private Statement statement;
    private Connection cn;
    private DBCredentials credentials;
    private final String tableName = "test_java";
    private final String queryINSERT = "insert into " + tableName + " values (?)";
    private final String querySELECT = "SELECT * FROM " + tableName;

    public void setN(int n) { this.n = n; }

    public Connection getCn() { return cn; }

    public ConnectDB(DBCredentials credentials) throws SQLException, NumberNNotFitException {
        this.credentials = credentials;
        setN(credentials.getNumberN());
        cn = connection();
        cn.setAutoCommit(false);
        statement = cn.createStatement();
    }

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
            }
            return null;
        } catch (Exception ex){
            System.out.println(ex);
            return null;
        }
        return cn;
    }

    public boolean tableCheck (String tableName) throws SQLException {
        final String query = "select 1 from dual where EXISTS (select * from "+ tableName +")";
        boolean result;
        ResultSet rs = statement.executeQuery(query);
        result = rs.next();
        rs.close();
        return result;
    }

    public void dataInsertion () throws SQLException {

        System.out.println("Start data insertion");

        // Если в таблице находились записи, то они удаляются перед вставкой
        // (Это условие можно удалить и оставить удаление в любом случае)
        if (tableCheck(tableName)) {
            System.out.println("clean table");
            statement.executeUpdate("delete from " + tableName);
        }

        PreparedStatement preparedStatement = cn.prepareStatement(queryINSERT);
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
        int[] result = new int[n];

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
