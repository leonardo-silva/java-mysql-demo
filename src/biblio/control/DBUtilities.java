/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblio.control;

import java.sql.*;
/**
 *
 * @author leonardosilva
 */
public class DBUtilities {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public DBUtilities() {
        try {
            connection = DriverManager.getConnection(Main.connection_url, Main.DATABASE_USER_ID, Main.DATABASE_PASSWORD);

        } catch (SQLException ex) {
            System.out.println("The following error has occured: " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void executeSQLStatement(String sql_stmt) throws SQLException {
        statement = connection.createStatement();

        statement.executeUpdate(sql_stmt);
    }    

    public ResultSet executeSQLQuery(String sql_stmt) throws SQLException {
        statement = connection.createStatement();

        return statement.executeQuery(sql_stmt);
    }    
}
