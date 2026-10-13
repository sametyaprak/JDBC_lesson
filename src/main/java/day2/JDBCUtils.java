package day2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtils {

    public static Connection connection;
    public static Statement statement;
    public static PreparedStatement prs;
    public static ResultSet rs;

    // step-1
    public static Connection connectToDatabase(String hostName, int port, String database, String user, String password) {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://"+hostName+":"+port+"/"+database+"?currentSchema=snack_attack,$user",
                    user, password);
            System.out.println("Connected to database successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  connection;
    }

    //step-2 create statement
    public static Statement getStatement() {
        try {
            statement = connection.createStatement();
            System.out.println("Statement created successfully");
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statement;
    }

    //util 1 -> query helper
    public static boolean executeQuery(String query) {
        boolean result = false;
        try {
            result = statement.execute(query);
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //util 2 -> column data helper
    public static List<Object> getColumnData(String columnName, String tableName) {
        List<Object> list = new ArrayList<>();
        String sql = "select " + columnName + " FROM " + tableName;
        try {
            rs = statement.executeQuery(sql);
            while (rs.next()){
                list.add(rs.getObject(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //util 3 -> count helper
    public static int countRows(String tableName) {
        int result = 0;
        try {
            rs = statement.executeQuery("SELECT COUNT (*) FROM " + tableName);
            rs.next();
            result = rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //step-3 close connection
    public static void closeStatementAndConnection() {
        try {
            statement.close();
            connection.close();
            System.out.println("Statement closed successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





}
