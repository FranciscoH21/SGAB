package sgab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionMySQL {

    public static Connection cn = null;
    public static Statement stmt = null;
    public static ResultSet rs = null;
    public static PreparedStatement pstm = null;

// Librería de MySQL
    public String driver = "com.mysql.jdbc.Driver";

    // Nombre de la base de datos
    public String database = "Biblioteca";

    // Host
    public String hostname = "localhost";

    // Puerto
    public String port = "3306";

    // Ruta de nuestra base de datos (desactivamos el uso de SSL con "?useSSL=false")
    public String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;

    // Nombre de usuario
    public String username = "root";

    // Clave de usuario
    public String password = "";

    public Connection getConnection() {
        Connection con = null;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return con;
    }
}
