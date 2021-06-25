package Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UConexion {
	private static Connection conexion;
	private UConexion() {};
	
	public static Connection ObtenerConexion() throws IOException {
		
		ResourceBundle rb = ResourceBundle.getBundle("framework");
		
		String urlBD = rb.getString("urlBD");
		String usuario = rb.getString("usuario");
		String clave = rb.getString("clave").equals("SIN_CLAVE") ? "" : rb.getString("clave");
		
		
		if(UConexion.conexion == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				UConexion.conexion = DriverManager.getConnection("jdbc:mysql:".concat(urlBD), usuario,clave);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return UConexion.conexion;
	}
}
