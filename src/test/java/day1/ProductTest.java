package day1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ClientInfoStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductTest {

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
    public void productTest() throws SQLException {
        JDBCUtils.connectToDatabase();
        JDBCUtils.getStatement();

        // When (1st way): query, ResultSet and loop inside the test class
        String query = "SELECT price FROM product";
        ResultSet rs = JDBCUtils.statement.executeQuery(query);
        List<Double> prices = new ArrayList<>();
        while (rs.next()) {
            prices.add(rs.getDouble(1));
        }
        System.out.println("list = " + prices);
        System.out.println("list size = " + prices.size());

        // When (2nd way): one line with the reusable method
        System.out.println(JDBCUtils.getColumnData("price","product"));
        // When (2nd way): one line with the reusable method
        int counter = 0;
        for (Double price : prices) {
            if( price>100){
                counter++;
            }
        }
        System.out.println("number of products with price > 100: " + counter);
        Assertions.assertEquals(6, counter, "result is not the same");
        JDBCUtils.closeStatementAndConnection();
    }

}
