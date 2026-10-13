package day2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Day 2 homework: solutions. Live Snack Attack database, read-only user.
public class HomeworkTest {

    static final String HOST = "2.tcp.eu.ngrok.io";
    static final int PORT = 21975;
    static final String DATABASE = "snack_attack_db";
    static final String USER = "db_readonly";
    static final String PASSWORD = "ReadOnly2024SecurePass";

    /*
        Given User connects to the Snack Attack database
        When  User gets the id of each given product: "Kola 330ml", "Pizza Margherita", "Ayran 300ml"
        Then  Verify that every id is unique
        And   User closes the connection
     */
    @Test
    public void h1ThreeProductsHaveUniqueIdsTest() throws SQLException {
        JDBCUtils.connectToDatabase(HOST, PORT, DATABASE, USER, PASSWORD);
        PreparedStatement prs = JDBCUtils.connection.prepareStatement("SELECT id FROM product WHERE name = ?");

        // one PreparedStatement, many values: only setString changes in the loop
        for (String name : new String[]{"Kola 330ml", "Pizza Margherita", "Ayran 300ml"}) {
            prs.setString(1, name);
            ResultSet rs = prs.executeQuery();
            int rows = 0;
            while (rs.next()) {
                System.out.println(name + " -> id " + rs.getLong("id"));
                rows++;
            }
            Assertions.assertEquals(1, rows, name + " does not have a unique id");
        }
        prs.close();
        JDBCUtils.connection.close();
    }

    /*
        Given User connects to the Snack Attack database
        When  User counts the products with a price of 0 or less
        Then  Verify that there are none
        And   User closes the connection
     */
    @Test
    public void h2NoProductIsFreeTest() {
        JDBCUtils.connectToDatabase(HOST, PORT, DATABASE, USER, PASSWORD);
        JDBCUtils.getStatement();
        try {
            ResultSet rs = JDBCUtils.statement.executeQuery("SELECT COUNT(*) FROM product WHERE price <= 0");
            rs.next();
            System.out.println("products with price <= 0: " + rs.getInt(1));
            Assertions.assertEquals(0, rs.getInt(1), "a product has no valid price");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        JDBCUtils.closeStatementAndConnection();
    }

    /*
        Given User connects to the Snack Attack database
        When  User looks for product names that appear more than once
        Then  Verify that no name appears twice
        And   User closes the connection
     */
    @Test
    public void h3NoDuplicateProductNamesTest() throws SQLException {
        JDBCUtils.connectToDatabase(HOST, PORT, DATABASE, USER, PASSWORD);
        JDBCUtils.getStatement();
        ResultSet rs = JDBCUtils.statement.executeQuery(
                "SELECT name, COUNT(*) FROM product GROUP BY name HAVING COUNT(*) > 1");
        int duplicates = 0;
        while (rs.next()) {
            System.out.println("duplicate: " + rs.getString(1) + " x" + rs.getInt(2));
            duplicates++;
        }
        System.out.println("duplicate names: " + duplicates);
        Assertions.assertEquals(0, duplicates, "some product names appear more than once");
        JDBCUtils.closeStatementAndConnection();
    }

        /*
        Given User connects to the Snack Attack database and counts the products
        When  User connects to the practice database and counts the products
        Then  Verify that both counts are equal
        And   User closes both connections
     */
        @Test
        public void productCountConsistencyTest() throws SQLException {
            // Count products in Snack Attack database
            JDBCUtils.connectToDatabase(HOST, PORT, DATABASE, USER, PASSWORD);
            PreparedStatement prsSnack = JDBCUtils.connection.prepareStatement("SELECT count(*) FROM product");
            ResultSet rsSnack = prsSnack.executeQuery();
            rsSnack.next();
            int snackCount = rsSnack.getInt(1);
            System.out.println("Product count in Snack Attack DB: " + snackCount);

            // Connect to practice database
            day1.JDBCUtils.connectToDatabase();
            PreparedStatement prsPractice = JDBCUtils.connection.prepareStatement("SELECT count(*) FROM product");
            ResultSet rsPractice = prsPractice.executeQuery();
            rsPractice.next();
            int practiceCount = rsPractice.getInt(1);
            System.out.println("Product count in Practice DB: " + practiceCount);

            // Verify counts are equal
            Assertions.assertEquals(snackCount, practiceCount, "Product counts should be equal in both databases");

            // Close practice database connection
            JDBCUtils.closeStatementAndConnection();
        }

}
