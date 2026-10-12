package day1;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Runner {


    public static void main(String[] args) throws SQLException {
        // I will call JDBC util class from here

        // we need to connect to DB
        JDBCUtils.connectToDatabase();

        // we need to create statement
        JDBCUtils.getStatement();

        // Execute query
        System.out.println("execute method usage 1 -> SELECT "+ JDBCUtils.executeQuery("SELECT * from product"));
        // I want to delete product:additions higher than 12
        System.out.println("execute method usage 2 -> DELETE "+ JDBCUtils.executeQuery("DELETE FROM product_additions WHERE id > 12;"));

        // insert data into DB
        String query1 = "INSERT INTO product_additions (id, name, price, active, addition_category_id) VALUES (?, ?, ?, true, 1);";

        //1st way: in the same class (prepare, set, execute update)
        PreparedStatement prs = JDBCUtils.connection.prepareStatement(query1);
        prs.setInt(1,13);
        prs.setString(2,"BBQ Sos");
        prs.setDouble(3,5.0);
        System.out.println("1st way rows = " + prs.executeUpdate()); // 1

        //2nd way: reusable method with values inside JDBC_utils
        // TODO -> this query fails whenever we run it, with info: Caused by: org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint "product_additions_pkey"
        //  Detail: Key (id)=(2) already exists.
        // WHAT WE CAN DO FOR IT ??
        JDBCUtils.getPreparedStatement(query1);

        //3rd way: reusable method that also returns the number of rows affected
        System.out.println("3rd way rows = " + JDBCUtils.executeUpdateWithPreSt(query1)); // 1

        // read one column of a table
        System.out.println(JDBCUtils.getColumnData("name", "product_additions")); // 12 original sauces + the 3 new ones
        System.out.println(JDBCUtils.getColumnData("name", "category_entity"));

        //always to not forget to close the connection

        //this closes the class level variable and connection
        prs.close();

        //this closes the UTIL level connection
        JDBCUtils.closeStatementAndConnection();


    }
}
