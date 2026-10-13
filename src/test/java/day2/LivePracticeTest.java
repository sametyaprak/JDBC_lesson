package day2;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivePracticeTest {


    static final String HOST = "2.tcp.eu.ngrok.io";
    static final int PORT = 21975;
    static final String DATABASE = "snack_attack_db";
    static final String USER = "db_readonly";
    static final String PASSWORD = "ReadOnly2024SecurePass";

    // helper inside the test class: the ids of all products with the given name
    static List<Long> idsOf (String productName) throws SQLException {
        PreparedStatement prs = JDBCUtils.connection.prepareStatement("SELECT id FROM product WHERE name = ?");
        prs.setString(1, productName);
        ResultSet rs = prs.executeQuery();
        List<Long> ids = new ArrayList<>();
        while (rs.next()) {
            ids.add(rs.getLong("id"));
        }
        prs.close();
        return ids;
    }

    /*
        Given User connects to the Snack Attack database
        When  User gets the id of "Pizza Margherita"
        Then  Verify that the id is unique
        And   User closes the connection
     */
    @Test
    public void MargheritaUniqueIdTest() throws SQLException {
        //call util helper method from Util class
        JDBCUtils.connectToDatabase(HOST, PORT, DATABASE, USER, PASSWORD);
        //use idsOf helper method from the same class
        System.out.println("Margherita unique ID" + idsOf("Margherita"));
        Assertions.assertEquals(1, idsOf("Margherita"),"Product id os not unique");
        JDBCUtils.connection.close();
    }

        /*
        Given User connects to the Snack Attack database
        When  User counts the products of the given category "PIZZA"
        Then  Verify that the number is 6
        And   User closes the connection
     */
    @Test
    public void pizzaCategoryTest() throws SQLException {
        JDBCUtils.connectToDatabase(HOST, PORT, DATABASE, USER, PASSWORD);
        PreparedStatement prs = JDBCUtils.connection.prepareStatement("SELECT count(*)\n" +
                "FROM snack_attack.product p\n" +
                "JOIN snack_attack.product_category pc\n" +
                "    ON p.id = pc.product_id\n" +
                "JOIN snack_attack.category_entity ce\n" +
                "    ON ce.id = pc.category_id\n" +
                "    WHERE ce.name = ?;");
        prs.setString(1, "PIZZA");
        ResultSet rs = prs.executeQuery();
        rs.next();
        System.out.println("PIZZA products " + rs.getInt(1));
        Assertions.assertEquals(6, rs.getInt(1));
        prs.close();
        JDBCUtils.connection.close();
    }

        /*
        Given User connects to the Snack Attack database
        When  User gets the price of "Kola 330ml"
        Then  Verify that the price is 25.0
        And   User closes the connection
     */

        @Test
        public void kolaPriceTest() throws SQLException {
            JDBCUtils.connectToDatabase(HOST, PORT, DATABASE, USER, PASSWORD);
            PreparedStatement prs = JDBCUtils.connection.prepareStatement("SELECT price FROM product WHERE name = ?");
            prs.setString(1,"Kola 330ml");
            ResultSet rs = prs.executeQuery();
            rs.next();
            System.out.println("Price of Kola 330ml: " + rs.getDouble("price"));
            Assertions.assertEquals(25.0,rs.getDouble("price"),"Price is not correct");
            prs.close();
        }

        /*
        Given User connects to the Snack Attack database
        When  User searches for "kola 330ml" written in lower case
        Then  Verify that "name = ?" finds nothing (= is case-sensitive)
        And   Verify that "LOWER(name) = LOWER(?)" finds exactly one product
        And   User closes the connection
     */

    @Test
    public void caseSensitiveSearchTest() throws SQLException {
        JDBCUtils.connectToDatabase(HOST, PORT, DATABASE, USER, PASSWORD);
        PreparedStatement prs = JDBCUtils.connection.prepareStatement("SELECT * FROM product WHERE name = ?");
        prs.setString(1,"kola 330ml");
        ResultSet rs = prs.executeQuery();
        Assertions.assertFalse(rs.next(),"Should not find any product with lower case name");

        prs = JDBCUtils.connection.prepareStatement("SELECT * FROM product WHERE LOWER(name) = LOWER(?)");
        prs.setString(1,"kola 330ml");
        rs = prs.executeQuery();
        int count = 0;
        while(rs.next()){
            count++;
        }
        Assertions.assertEquals(1,count,"Should find exactly one product with case-insensitive search");
    }

        /*
        Given User connects to the Snack Attack database and counts the products
        When  User connects to the practice database and counts the products
        Then  Verify that both counts are equal
        And   User closes both connections
     */






}
