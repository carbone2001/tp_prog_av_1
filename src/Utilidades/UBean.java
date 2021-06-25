package Utilidades;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class UBean {
	public static ArrayList<Field> obtenerAtributos(Object o){
		ArrayList<Field> fieldsArrLs = new ArrayList<Field>();
		Field[] fieldsArr = o.getClass().getFields();
		//System.out.println(o.getClass());
		for(Field f:fieldsArr) {
			//System.out.println("Nombre: "+f.getName());
			fieldsArrLs.add(f);
		}
		return fieldsArrLs;
	}
	
	public static void ejecutarSet(Object o, String att, Object valor) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		o.getClass().getField(att).set(o, valor);
	}
	
	public static Object ejecutarGet(Object o, String att) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return o.getClass().getField(att).get(o);
	}
}
