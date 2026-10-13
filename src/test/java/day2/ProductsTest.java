package day2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsTest {

    // Course database login (session 1, "Log in" slide). Host and port valid on 12 Oct 2026:
    // they change when the server tunnel restarts, the instructor tells you the new ones.
    static final String HOST = "2.tcp.eu.ngrok.io";
    static final int PORT = 21975;
    static final String USER = "student01"; // change to YOUR login: student01 ... student40
    static final String PASSWORD = "kP5zkogLaZZ9ZWhD";

    /*
        Given
          User connects to the database
        When
          User sends the query to get the "price" from the "product" table
        Then
          Verify that the number of products with a price greater than 100 is 6
        And
          User closes the connection
     */

    @Test
    public void productsTest() throws SQLException {

        // Given: User connects to the database
        JDBCUtils.connectToDatabase(HOST, PORT, "jdbc_course", USER, PASSWORD);
        JDBCUtils.getStatement();

        // When (1st way): query, ResultSet and loop inside the test class
        String query = "SELECT price FROM product;";
        ResultSet rs = JDBCUtils.statement.executeQuery(query);

        List<Double> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getDouble(1));
        }
        System.out.println("list = " + list);
        System.out.println("list size = " + list.size());

        // When (2nd way): one line with the reusable method
        System.out.println(JDBCUtils.getColumnData("price", "product"));

        // Then: Verify that the number of products with a price greater than 100 is 6
        int counter = 0;
        for (Double w : list) {
            if (w > 100) {
                counter++;
            }
        }
        System.out.println("number of products with price > 100: " + counter);
        Assertions.assertEquals(6, counter, "result is not the same");

        // And: User closes the connection
        JDBCUtils.closeStatementAndConnection();
    }
}
