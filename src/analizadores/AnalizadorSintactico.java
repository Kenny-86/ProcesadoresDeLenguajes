package analizadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class AnalizadorSintactico {
	private final String[] terminales= {"if","(", ")","for","id", "=",";","{", "}", "let", "int", "float", "boolean", "string", "function", "void", "||",
															"==", "+", "-", ",", "echo", "prompt", "return", "ent", "cad", "real", "veracidad", "++"};
	private final String[] noTerminales = {"P", "B", "F", "E","E1","G","T","D","U","U1","H","A","C","S","S1","L","R","R1","V","V1","Q","X","K" };
	private final String axioma = "P";
	private AnalizadorLexico aLexico;
	private Stack<String> pila;
	private final String[][][] M= {};
	private final int[][]nRegla= {};
	private ArrayList<Integer> parse;

	private static HashMap<String, Integer> traductorNoTerminales= new HashMap<>();
	private static HashMap<String, Integer> traductorTerminales= new HashMap<>();
	
	public AnalizadorSintactico(String dir) {
		aLexico=new AnalizadorLexico(dir);
		pila=new Stack<>();
		//rellenarlo 
		rellenarTraductores();
		//rellenar M
	parse= new ArrayList<>();
	}
	
public ArrayList<Integer>analizar() {
	pila.add("$");
	pila.add(axioma);
	//se sustituirá por la condición de salida que es cuando se lea $ y se quipare con eof
	boolean condicion=false;
	while(condicion) {
		
	String token = aLexico.analisis().getCodToken(); //el token nos lo tiene que dar el analizador léxico
	int fila = traductorNoTerminales.get(pila.pop());
	int columna = traductorTerminales.get(token);
	/*if(M[fila][columna]=={""}) {
		//llamar al gestor de errores
		//seguramente no sea una cadena vacía sino un identificador del error específico
	}*/
	//coger los elementos en M[fila][columna] y añadirlos a la pila
	//para esta práctica también nos tiene que devolver el número de regla que se ha ejecutado, lo guardaremos nRegla
	parse.add(nRegla[fila][columna]);
	}
	return parse;
}
	
	
	private void rellenarTraductores() {
//hacer todos los puts
	}

}
