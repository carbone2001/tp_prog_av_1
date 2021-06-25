import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Servicios.Consultas;
public class Program {

	public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub

		try {
			
			//probado
//			Persona p1 = new Persona("Natalio","Ruiz",1);
//			Persona p2 = new Persona("Juan","Perez",1);
//			Persona p3 = new Persona("Nicolas","Mora",1);
//			Consultas.guardar(p1);
//			Consultas.guardar(p2);
//			Consultas.guardar(p3);
			
			//probado
//			ArrayList<Object> list = Consultas.obtenerTodos(Persona.class);
//			for(Object o : list) {
//				Persona p = (Persona) o;
//				System.out.println(p.toString());
//			}
			
			//probado. Recordar que el dni no coincide con el id de la db
			//Persona p4 = new Persona("Natalio Modificado","Ruiz",7);
//			Consultas.modificar(p4);
			
			//probado.
			//Consultas.eliminar(p4);
			
			//probado. 
//			Persona p5 = (Persona)Consultas.obtenerPorId(Persona.class, 11);
//			System.out.println(p5.toString());
			
			
		} catch (IllegalArgumentException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
