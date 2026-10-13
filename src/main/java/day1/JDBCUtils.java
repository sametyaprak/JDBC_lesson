package day1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtils {



    public static Connection connection;
    public static Statement statement;
    public static PreparedStatement prs;
    public static ResultSet rs;

    // create a connection
    public static Connection connectToDatabase() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://2.tcp.eu.ngrok.io:21975/jdbc_course?currentSchema=snack_attack,$user",
                    "student01", "kP5zkogLaZZ9ZWhD");
            System.out.println("Connected to database successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
      return  connection;
    }

    //create statement
    public static Statement getStatement() {
        try {
            statement = connection.createStatement();
            System.out.println("Statement created successfully");
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statement;
    }

    public static boolean executeQuery(String query) {
        boolean result = false;
        try {
            result = statement.execute(query);
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // TODO homework -> Yaklv explain what is wrong with this method
    // we used in 2nd way:
    public static PreparedStatement getPreparedStatement(String query) {
        try {
            prs = connection.prepareStatement(query);
            prs.setInt(1,2);
            prs.setString(2,"Student Menu");
            prs.setInt(3,20);
            prs.executeUpdate();
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prs;
    }

    // we used in 3rd way
    public static int executeUpdateWithPreSt(String query) {
        int result = 0;
        try {
            prs = connection.prepareStatement(query);
            prs.setInt(1,3);
            prs.setString(2,"Pizza Night");
            prs.setInt(3,15);
            result = prs.executeUpdate();
        }   catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

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
