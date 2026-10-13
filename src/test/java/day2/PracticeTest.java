package day2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// Day 2, part 1 practice: solutions. Practice database (jdbc_course), your own login.
// Every test: Gherkin comment first, then one code block per step.
public class PracticeTest {

    static final String HOST = "2.tcp.eu.ngrok.io";
    static final int PORT = 21975;
    static final String USER = "student01"; // change to YOUR login: student01 ... student40
    static final String PASSWORD = "kP5zkogLaZZ9ZWhD";

    /*
        Given User connects to the database
        When  User counts the products of the "ICECEKLER" category
        Then  Verify that the number is 5
        And   User closes the connection
     */
    @Test
    public void p1DrinksCategoryTest() throws SQLException {
        JDBCUtils.connectToDatabase(HOST, PORT, "jdbc_course", USER, PASSWORD);
        JDBCUtils.getStatement();

        // The category name is in category_entity, the link is in product_category: JOIN three tables
        ResultSet rs = JDBCUtils.statement.executeQuery(
                "SELECT COUNT(*) FROM product p " +
                "JOIN product_category pc ON pc.product_id = p.id " +
                "JOIN category_entity c ON c.id = pc.category_id " +
                "WHERE c.name = 'ICECEKLER'");
        rs.next();
        int drinks = rs.getInt(1);
        System.out.println("ICECEKLER products = " + drinks);

        Assertions.assertEquals(5, drinks, "wrong number of drinks");
        JDBCUtils.closeStatementAndConnection();
    }

    /*
        Given User connects to the database
        When  User reads the most expensive product
        Then  Verify that it is "Pizza Dört Peynir" with price 125.0
        And   User closes the connection
     */
    @Test
    public void p2MostExpensiveProductTest() throws SQLException {
        JDBCUtils.connectToDatabase(HOST, PORT, "jdbc_course", USER, PASSWORD);
        JDBCUtils.getStatement();

        ResultSet rs = JDBCUtils.statement.executeQuery("SELECT name, price FROM product ORDER BY price DESC LIMIT 1");
        rs.next();
        System.out.println(rs.getString("name") + " " + rs.getDouble("price"));

        Assertions.assertEquals("Pizza Dört Peynir", rs.getString("name"));
        Assertions.assertEquals(125.0, rs.getDouble("price"));
        JDBCUtils.closeStatementAndConnection();
    }

    /*
        Given User connects to the database
        When  User calculates the average product price
        Then  Verify that the average is 67.75
        And   User closes the connection
     */
    @Test
    public void p3AveragePriceTest() throws SQLException {
        JDBCUtils.connectToDatabase(HOST, PORT, "jdbc_course", USER, PASSWORD);
        JDBCUtils.getStatement();

        ResultSet rs = JDBCUtils.statement.executeQuery("SELECT AVG(price) FROM product");
        rs.next();
        double average = rs.getDouble(1);
        System.out.println("average = " + average);

        // doubles: the third argument is the allowed difference (delta)
        Assertions.assertEquals(67.75, average, 0.001);
        JDBCUtils.closeStatementAndConnection();
    }

    /*
        Given User connects to the database
        When  User gets all product names
        Then  Verify that "Ayran 300ml" is in the list
        And   User closes the connection
     */
    @Test
    public void p4AyranIsOnTheMenuTest() {
        JDBCUtils.connectToDatabase(HOST, PORT, "jdbc_course", USER, PASSWORD);
        JDBCUtils.getStatement();

        List<Object> names = JDBCUtils.getColumnData("name", "product");
        System.out.println(names);

        Assertions.assertTrue(names.contains("Ayran 300ml"), "Ayran 300ml is not on the menu");
        JDBCUtils.closeStatementAndConnection();
    }

    /*
        Given User connects to the database
        When  User counts the rows of "product" and of "product_category"
        Then  Verify that every product has exactly one category (both counts are equal)
        And   User closes the connection
     */
    @Test
    public void p5EveryProductHasACategoryTest() {
        JDBCUtils.connectToDatabase(HOST, PORT, "jdbc_course", USER, PASSWORD);
        JDBCUtils.getStatement();

        int products = JDBCUtils.countRows("product");
        int links = JDBCUtils.countRows("product_category");
        System.out.println("product = " + products + ", product_category = " + links);

        Assertions.assertEquals(products, links, "not every product has exactly one category");
        JDBCUtils.closeStatementAndConnection();
    }

    /*
        Given User connects to the database
        When  User adds the meze "Humus" (id 20, price 5.0, category 3 = Mezeler)
        Then  Verify that product_additions has one row more
        And   User deletes "Humus" again and verifies the original count
        And   User closes the connection
     */
    @Test
    public void p6AddAndRemoveAMezeTest() throws SQLException {
        JDBCUtils.connectToDatabase(HOST, PORT, "jdbc_course", USER, PASSWORD);
        JDBCUtils.getStatement();
        int before = JDBCUtils.countRows("product_additions");

        // 1st way: the values belong to this test
        PreparedStatement insert = JDBCUtils.connection.prepareStatement(
                "INSERT INTO product_additions (id, name, price, active, addition_category_id) VALUES (?, ?, ?, true, 3)");
        insert.setInt(1, 20);
        insert.setString(2, "Humus");
        insert.setDouble(3, 5.0);
        System.out.println("inserted rows = " + insert.executeUpdate());
        Assertions.assertEquals(before + 1, JDBCUtils.countRows("product_additions"));

        PreparedStatement delete = JDBCUtils.connection.prepareStatement("DELETE FROM product_additions WHERE id = ?");
        delete.setInt(1, 20);
        System.out.println("deleted rows = " + delete.executeUpdate());
        Assertions.assertEquals(before, JDBCUtils.countRows("product_additions"));

        insert.close();
        delete.close();
        JDBCUtils.closeStatementAndConnection();
    }
}
