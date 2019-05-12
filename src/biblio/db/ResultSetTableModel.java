/*
 * Esta classe é utilizada para povoar componentes JTable com o conteúdo de tabelas
 * do banco de dados
 */
package biblio.db;

import biblio.control.*;
import java.sql.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author leonardosilva
 */
public class ResultSetTableModel extends AbstractTableModel {
    private Connection connection;
    private final Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numberOfRows;

    private boolean connectedToDatabase = false;

    private void SetDatabaseURL() {
        try {
            connection = DriverManager.getConnection(Main.connection_url, Main.DATABASE_USER_ID, Main.DATABASE_PASSWORD);
        } catch (SQLException sex) {
            System.out.println(sex.getMessage());
        }
    }

    public ResultSetTableModel(String query) throws SQLException {

        SetDatabaseURL();
        connection = DriverManager.getConnection(Main.connection_url, Main.DATABASE_USER_ID, Main.DATABASE_PASSWORD);

        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        connectedToDatabase = true;

        if (!connectedToDatabase) {
            throw new IllegalStateException("Falha na conexão com o banco de dados");
        }

        resultSet = statement.executeQuery(query);
        metaData = resultSet.getMetaData();
        resultSet.last();
        numberOfRows = resultSet.getRow();

        fireTableStructureChanged();

    }

    @Override
    public Class getColumnClass(int column) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Falha na conexão com o banco de dados");
        }

        try {
            String className = metaData.getColumnClassName(column + 1);
            return Class.forName(className);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return Object.class;
    }

    @Override
    public int getColumnCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Falha na conexão com o banco de dados");
        }

        try {
            return metaData.getColumnCount();
        } catch (SQLException sex) {
            System.out.println(sex.getMessage());
        }

        return 0;
    }

    @Override
    public String getColumnName(int column) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Falha na conexão com o banco de dados");
        }

        try {
            return metaData.getColumnName(column + 1);
        } catch (SQLException sex) {
            System.out.println(sex.getMessage());
        }

        return "";
    }

    @Override
    public int getRowCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Falha na conexão com o banco de dados");
        }

        return numberOfRows;
    }

    @Override
    public Object getValueAt(int row, int column)
            throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Falha na conexão com o banco de dados");
        }

        try {
            resultSet.absolute(row + 1);
            return resultSet.getObject(column + 1);
        } catch (SQLException sex) {
            System.out.println(sex.getMessage());
        }

        return "";
    }

    public void disconnectFromDatabase() {
        if (connectedToDatabase) {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException sex) {
                System.out.println(sex.getMessage());
            } finally {
                connectedToDatabase = false;
            }
        }
    }    
}
