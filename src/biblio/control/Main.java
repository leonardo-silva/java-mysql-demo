/*
 * Programa principal para gerenciamento de uma biblioteca. A classe Main é
 * responsável por acionar a tela principal do sistema e armazenar informações 
 * sobre o usuário ativo (logado).
 */
package biblio.control;

import biblio.ui.*;

/**
 *
 * @author leonardosilva
 */
public class Main {
    private static String userLogin="";
    // Constantes
    public static final String DATABASE_NAME = "testdb";
    public static final String DATABASE_SERVER = "localhost";
    public static final String DATABASE_USER_ID = "testuser";
    public static final String DATABASE_PASSWORD = "test123";
    
    public static final String connection_url = "jdbc:mysql://" + DATABASE_SERVER + "/" + DATABASE_NAME;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Cria e exibe a tela principal */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BibliotecaUI().setVisible(true);
            }
        });        
    }
    
    public static String getUserLogin() {
        return Main.userLogin;
    }

    public static void setUserLogin(String login) {
        Main.userLogin = login;
    }
}
