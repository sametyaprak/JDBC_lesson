package day2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SnackAttackRemoteTest {

    // Live Snack Attack database (session 4): same host and port as the course database, valid on 12 Oct 2026;
    // they change when the server tunnel restarts, the instructor tells you the new ones.
    static final String HOST = "2.tcp.eu.ngrok.io";
    static final int PORT = 21975;
    static final String DATABASE = "snack_attack_db";
    static final String USER = "db_readonly";
    static final String PASSWORD = "ReadOnly2024SecurePass";

    /*
        Given
          User connects to the Snack Attack database
        When
          User sends the query to get the id of the product with the given name from the "product" table
        Then
          Verify that the product id is unique
        And
          User closes the connection
     */

    @Test
    public void productIdIsUniqueTest() throws SQLException {

        // Given
        JDBCUtils.connectToDatabase(HOST, PORT, DATABASE, USER, PASSWORD);

        // When: parameterised query -> PreparedStatement stays in the test class
        String query = "SELECT * FROM product WHERE name = ?";
        PreparedStatement prs = JDBCUtils.connection.prepareStatement(query);
        prs.setString(1, "Kola 330ml");

        ResultSet rs = prs.executeQuery();
        List<Long> ids = new ArrayList<>();
        while (rs.next()) {
            ids.add(rs.getLong("id"));
        }
        System.out.println("product id = " + ids);

        // Then: unique = exactly one row came back
        Assertions.assertEquals(1, ids.size(), "product id is not unique");

        // And: we used a PreparedStatement, not a Statement -> close it directly
        prs.close();
        JDBCUtils.connection.close();
    }
}
