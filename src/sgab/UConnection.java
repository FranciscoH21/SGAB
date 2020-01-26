package sgab;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UConnection {
    private static Connection con = null;
    private static Connection conv2 = null;
    
    // Librería de MySQL
    private static String driver = "com.mysql.jdbc.Driver";

    // Nombre de la base de datos
    private static String database = "Biblioteca";

    // Host
    private static String hostname = "localhost";

    // Puerto
    private static String port = "3306";

    // Ruta de nuestra base de datos 
    public static String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
    public static String urlV2 = "jdbc:mysql://" + hostname + ":" + port;

    // Nombre de usuario
    public static String username = "root";

    // Clave de usuario
    public static String password = "sistemas";
    
    private static int login = 0;
    
    public static Connection getConnection(String user,String pwd){
        try {
                Class.forName(driver);
                con = DriverManager.getConnection(url, user, pwd);
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException ();
        }
    }
        /*static class MiShDwnHook extends Thread{
        public void run(){
            try {
                Connection con = UConnection.getConnection();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }*/
        public static Connection getConnectionSimple(String user,String pwd){
            try {
                 Class.forName(driver);
                conv2 = DriverManager.getConnection(urlV2, user, pwd);
            } catch (Exception e) {
            }
            return conv2;
        }
    public static void cerrarV2() throws SQLException{
        conv2.close();
    }
    public static int getLogin(String user,String pwd){
        try {   
                Class.forName(driver);
                con = DriverManager.getConnection(urlV2, user, pwd);
            return login = 1;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
            throw new RuntimeException ();
        }
    }
}
