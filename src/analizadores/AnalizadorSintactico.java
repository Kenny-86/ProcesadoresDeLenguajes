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
	private final int[][]nRegla= { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0 }, // A
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 5, 0, 9, 4, 0, 10, 9, 0, 9, 0, 0, 0, 0, 0, 0, 0 }, // B
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 24, 0, 24, 24, 0, 24, 24, 0, 24, 0, 0, 0, 0, 0, 25, 0 }, // C
        { 0, 0, 0, 0, 0, 0, 16, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // D
        { 29, 0, 0, 29, 0, 0, 0, 0, 0, 0, 29, 0, 29, 0, 0, 0, 29, 0, 0, 0, 0, 29, 0, 0, 29, 0, 0, 0, 0, 0 }, // E
        { 0, 31, 0, 0, 31, 0, 31, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 0, 0 }, // E1
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // F
        { 0, 8, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // G
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 18, 0, 0, 0, 0, 18, 0, 0, 0, 0, 18, 0, 19, 0, 0, 0, 0 }, // H
        { 0, 23, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // K
        { 39, 40, 0, 39, 0, 0, 0, 0, 0, 0, 39, 0, 39, 0, 0, 0, 39, 0, 0, 0, 0, 39, 0, 0, 39, 0, 0, 0, 0, 0 }, // L
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 2, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 3 }, // P
        { 0, 42, 0, 0, 41, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // Q
        { 32, 0, 0, 32, 0, 0, 0, 0, 0, 0, 32, 0, 32, 0, 0, 0, 32, 0, 0, 0, 0, 32, 0, 0, 32, 0, 0, 0, 0, 0 }, // R
        { 0, 34, 0, 0, 34, 0, 34, 0, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 34, 0, 0 }, // R1
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 45, 0, 0, 0, 0, 26, 0, 0, 0, 46, 0, 47, 0, 0, 0, 0, 0, 0, 0 }, // S
        { 28, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // S1
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0, 0, 0, 12, 0, 0, 0, 0, 11, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0 }, // T
        { 35, 0, 0, 35, 0, 0, 0, 0, 0, 0, 35, 0, 35, 0, 0, 0, 35, 0, 0, 0, 0, 35, 0, 0, 35, 0, 0, 0, 0, 0 }, // U
        { 0, 38, 36, 0, 38, 37, 38, 0, 38, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 38, 0, 0 }, // U1
        { 49, 0, 0, 54, 0, 0, 0, 0, 0, 0, 51, 0, 50, 0, 0, 0, 48, 0, 0, 0, 0, 52, 0, 0, 53, 0, 0, 0, 0, 0 }, // V
        { 55, 56, 56, 0, 56, 56, 56, 0, 56, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 56, 0, 0 }, // V1
        { 43, 0, 0, 43, 0, 0, 44, 0, 0, 0, 43, 0, 43, 0, 0, 0, 43, 0, 0, 0, 0, 43, 0, 0, 43, 0, 0, 0, 0, 0 }  // X};
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
