package Servicios;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Anotaciones.Columna;
import Anotaciones.Id;
import Anotaciones.Tabla;
import Utilidades.UBean;
import Utilidades.UConexion;

public class Consultas {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void guardar(Object o) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String query = "insert into TABLA (COLUMNAS) values(VALORES);";
		String columnas = "";
		String valores = "";
		
		//El nombre de la tabla es el nombre de la clase
		Class objectClass = o.getClass();
		Tabla anotationTabla = (Tabla) objectClass.getAnnotation(Tabla.class);
		if(anotationTabla != null) {
			query = query.replace("TABLA", anotationTabla.nombre().toLowerCase());
		}
		
		//Las columnas son los nombre de los atributos
		//Los valores son los valores de los atributos
		ArrayList<Field> atributos = UBean.obtenerAtributos(o);
		for(Field atributo:atributos) {
			Columna annotationColumna = (Columna)atributo.getAnnotation(Columna.class);
			Id annotationId = (Id)atributo.getAnnotation(Id.class);
			if(annotationColumna != null && annotationId == null)//Los atributos con @Id se ignoraran
			{
				columnas = columnas.concat(annotationColumna.nombre()).concat(",");
				if(atributo.getType() == String.class) {
					valores = valores.concat("'"+UBean.ejecutarGet(o, atributo.getName()).toString()+"'").concat(",");
				}
				else {
					valores = valores.concat(UBean.ejecutarGet(o, atributo.getName()).toString()).concat(",");
				}
			}
		}
		query = query.replace("COLUMNAS", columnas.substring(0, (columnas.length()-1)));
		query = query.replace("VALORES", valores.substring(0, (valores.length()-1)));
		
		//System.out.println(query);
		
		try {
			
			Connection conexion = UConexion.ObtenerConexion();
			PreparedStatement ps = conexion.prepareStatement(query);
			
			System.out.println(ps.executeUpdate());
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void modificar(Object o) {
		String query = "UPDATE TABLA SET COLUMNASVALOR WHERE CONDICION";
		String columnasConValor = "";
		String condicion = "";
		
		
		//El nombre de la tabla es el nombre de la clase
		Class objectClass = o.getClass();
		Tabla anotationTabla = (Tabla) objectClass.getAnnotation(Tabla.class);
		if(anotationTabla != null) {
			query = query.replace("TABLA", anotationTabla.nombre().toLowerCase());
		}
		
		
		//Las columnas son los nombre de los atributos
		//Los valores son los valores de los atributos
		
		try {
			ArrayList<Field> atributos = UBean.obtenerAtributos(o);
			for(Field atributo:atributos) {
				Columna annotationColumna = (Columna)atributo.getAnnotation(Columna.class);
				Id annotationId = (Id)atributo.getAnnotation(Id.class);
				if(annotationColumna != null && annotationId == null)//Los atributos con @Id se ignoraran
				{
					columnasConValor = columnasConValor
							.concat(annotationColumna.nombre())
							.concat("=")
							.concat((atributo.getType() == String.class) ? "'"+UBean.ejecutarGet(o, atributo.getName()).toString()+"'" : UBean.ejecutarGet(o, atributo.getName()).toString())
							.concat(",");
				}
				else if(annotationId != null) {
					condicion = condicion.concat(
							annotationColumna.nombre()
							.concat("=")
							.concat(Integer.toString(atributo.getInt(o))));
				}
			}
			query = query.replace("COLUMNASVALOR", columnasConValor.substring(0, (columnasConValor.length()-1)));
			query = query.replace("CONDICION", condicion.substring(0, (condicion.length())));
		}
		catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		

		
		System.out.println(query);
		
		try {
			
			Connection conexion = UConexion.ObtenerConexion();
			PreparedStatement ps = conexion.prepareStatement(query);
			
			System.out.println(ps.executeUpdate());
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void eliminar(Object o) {
		String query = "DELETE FROM TABLA WHERE CONDICION;";
		String condicion = "";
		
		//El nombre de la tabla es el nombre de la clase
		Class objectClass = o.getClass();
		Tabla anotationTabla = (Tabla) objectClass.getAnnotation(Tabla.class);
		if(anotationTabla != null) {
			query = query.replace("TABLA", anotationTabla.nombre().toLowerCase());
		}
		
		
		try {
			ArrayList<Field> atributos = UBean.obtenerAtributos(o);
			for(Field atributo:atributos) {
				Columna annotationColumna = (Columna)atributo.getAnnotation(Columna.class);
				Id annotationId = (Id)atributo.getAnnotation(Id.class);
				if(annotationId != null) {
					condicion = condicion.concat(
							annotationColumna.nombre()
							.concat("=")
							.concat(Integer.toString(atributo.getInt(o))));
				}
			}
			query = query.replace("CONDICION", condicion.substring(0, (condicion.length())));
		}
		catch(IllegalArgumentException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
		}
		
		System.out.println(query);

		try {
			Connection conexion = UConexion.ObtenerConexion();
			PreparedStatement ps = conexion.prepareStatement(query);
			System.out.println(ps.executeUpdate());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public static Object obtenerPorId(Class c,Object id) {
		String query = "SELECT * FROM TABLA WHERE CONDICION;";
		String condicion = "";
		Object resultObject = null;
		ArrayList<Field> atributos;
		try {
			//El nombre de la tabla es el nombre de la clase
			Tabla anotationTabla = (Tabla) c.getAnnotation(Tabla.class);
			if(anotationTabla != null) {
				query = query.replace("TABLA", anotationTabla.nombre().toLowerCase());
			}
			
			
			atributos = UBean.obtenerAtributos(c.getConstructor().newInstance());
			Class objId = id.getClass();

			for(Field atributo:atributos) {
				Columna annotationColumna = (Columna)atributo.getAnnotation(Columna.class);
				Id annotationId = (Id)atributo.getAnnotation(Id.class);
				if(annotationId != null) {
					condicion = condicion.concat(
							annotationColumna.nombre()
							.concat("=")
							.concat(objId == String.class ? "'"+((String)id)+"'" : String.valueOf(id)));
				}
			}
			query = query.replace("CONDICION", condicion.substring(0, (condicion.length())));
			
			System.out.println(query);
			
			
			resultObject = c.getConstructor().newInstance();
			Connection conexion = UConexion.ObtenerConexion();
			PreparedStatement ps = conexion.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				
				//Obtener valores de las columnas por cada atributo
				for(Field atributo : atributos) {
					String nombreColumna = atributo.getAnnotation(Columna.class).nombre().toLowerCase();
					Object valorColumna = rs.getObject(nombreColumna);
					atributo.set(resultObject, valorColumna);
					//System.out.println((valorColumna));
				}
				
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//El nombre de la tabla es el nombre de la clase
		Tabla anotationTabla = (Tabla) c.getAnnotation(Tabla.class);
		if(anotationTabla != null) {
			query = query.replace("TABLA", anotationTabla.nombre().toLowerCase());
		}		
				
		return resultObject;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public static ArrayList<Object> obtenerTodos(Class c) throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		String query = "SELECT * FROM TABLA;";
		ArrayList<Object> lista = new ArrayList<>();
		Tabla anotationTabla = (Tabla) c.getAnnotation(Tabla.class);
		if(anotationTabla != null) {
			query = query.replace("TABLA", anotationTabla.nombre().toLowerCase());
		}
		
		System.out.println(query);
		
		try {
			Connection conexion = UConexion.ObtenerConexion();
			PreparedStatement ps = conexion.prepareStatement(query);
			ResultSet  rs = ps.executeQuery();
			
			while(rs.next()) {
				//Crear un objeto
				Object object;
				object = c.getDeclaredConstructor().newInstance();
				
				//Obtener atributos
				//Class objectClass = object.getClass();
				ArrayList<Field> atributos = UBean.obtenerAtributos(object);
				System.out.println(atributos.size());
				//Obtener valores de las columnas por cada atributo
				for(Field atributo : atributos) {
					String nombreColumna= atributo.getAnnotation(Columna.class).nombre().toLowerCase();
					Object valorColumna = rs.getObject(nombreColumna);
					atributo.set(object, valorColumna);
					System.out.println(nombreColumna);
				}
				
				//Agregar el objeto con los valores cargados a la lista
				lista.add(object);
			}
			
		} catch (InstantiationException | IllegalAccessException |IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return lista;
	}
}
