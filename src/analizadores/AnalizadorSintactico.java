package analizadores;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class AnalizadorSintactico {
	/*private final String[] terminales= {"if","(", ")","for","id", "=",";","{", "}", "let", "int", "float", "boolean", "string", "function", "void", "||",
			"==", "+", "-", ",", "echo", "prompt", "return", "ent", "cad", "real", "veracidad", "++"};
	private final String[] noTerminales = {"P", "B", "F", "E","E1","G","T","D","U","U1","H","A","C","S","S1","L","R","R1","V","V1","Q","X","K" };
	*/
	private final String axioma = "P";
	private AnalizadorLexico aLexico;
	private GestorErrores gestErr;
	private Stack<String> pila;
	private final String[][] M= {
			// A
			{"err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "id:T:K", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "void", "err1", "err1", "err1", "err1"},
			// B
			{"err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "S", "err1", "err1", "for:(:id:=:U:;:E:;:G:):{:C:}", "err1", "S", "if:(:E:):S", "err1", "let:id:T:D:;", "S", "err1", "S", "err1", "err1", "err1", "err1", "err1", "err1", "err1"},
			// C
			{"err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "B:C", "err1", "err1", "B:C", "err1", "B:C", "B:C", "err1", "B:C", "B:C", "err1", "B:C", "err1", "err1", "err1", "err1", "err1", "", "err1"},
			// D
			{"err1", "err1", "err1", "err1", "err1", "err1", "", "=:U", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1"},
			// E
			{"R:E1", "err1", "err1", "R:E1", "err1", "err1", "err1", "err1", "err1", "err1", "R:E1", "err1", "R:E1", "err1", "err1", "err1", "R:E1", "err1", "err1", "err1", "err1", "R:E1", "err1", "err1", "R:E1", "err1", "err1", "err1", "err1", "err1"},
			// E1
			{"err1", "", "err1", "err1", "", "err1", "", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "||:R:E1", "err1", "err1"},
			// F
			{"err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "function:id:H:(:A:):{:C:}", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1"},
			// G
			{"err1", "", "err1", "++:id", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "id:=:E", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1"},
			// H
			{"err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "T", "err1", "err1", "err1", "T", "err1", "err1", "err1", "err1", "T", "err1", "err1", "err1", "err1", "T", "err1", "void", "err1", "err1", "err1", "err1"},
			// K
			{"err1", "", "err1", "err1", ",:id:T:K", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1"},
			// L
			{"E:Q", "", "err1", "E:Q", "err1", "err1", "err1", "err1", "err1", "err1", "E:Q", "err1", "E:Q", "err1", "err1", "err1", "E:Q", "err1", "err1", "err1", "err1", "E:Q", "err1", "err1", "E:Q", "err1", "err1", "err1", "err1", "err1"},
			// P
			{"err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "B:P", "err1", "err1", "B:P", "F:P", "B:P", "B:P", "err1", "B:P", "B:P", "err1", "B:P", "err1", "err1", "err1", "err1", "err1", "err1", ""},
			// Q
			{"err1", "", "err1", "err1", ",:E:Q", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1"},
			// R
			{"U:R1", "err1", "err1", "U:R1", "err1", "err1", "err1", "err1", "err1", "err1", "U:R1", "err1", "U:R1", "err1", "err1", "err1", "U:R1", "err1", "err1", "err1", "err1", "U:R1", "err1", "err1", "U:R1", "err1", "err1", "err1", "err1", "err1"},
			// R1
			{"err1", "", "err1", "err1", "", "err1", "", "err1", "==:U:R1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "", "err1", "err1"},
			// S
			{"err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "echo:E:;", "err1", "err1", "err1", "err1", "id:S1", "err1", "err1", "err1", "prompt:id:;", "err1", "return:X:;", "err1", "err1", "err1", "err1", "err1", "err1", "err1"},
			// S1
			{"(:L:):;", "err1", "err1", "err1", "err1", "err1", "err1", "=:E:;", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1"},
			// T
			{"err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "boolean", "err1", "err1", "err1", "float", "err1", "err1", "err1", "err1", "int", "err1", "err1", "err1", "err1", "string", "err1", "err1", "err1", "err1", "err1", "err1"},
			// U
			{"V:U1", "err1", "err1", "V:U1", "err1", "err1", "err1", "err1", "err1", "err1", "V:U1", "err1", "V:U1", "err1", "err1", "err1", "V:U1", "err1", "err1", "err1", "err1", "V:U1", "err1", "err1", "V:U1", "err1", "err1", "err1", "err1", "err1"},
			// U1
			{"err1", "", "+:V:U1", "err1", "", "-:V:U1", "", "err1", "", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "", "err1", "err1"},
			// V
			{"(:E:)", "err1", "err1", "++:id", "err1", "err1", "err1", "err1", "err1", "err1", "cad", "err1", "ent", "err1", "err1", "err1", "id:V1", "err1", "err1", "err1", "err1", "real", "err1", "err1", "veracidad", "err1", "err1", "err1", "err1", "err1"},
			// V1
			{"(:L:)", "", "", "err1", "", "", "", "err1", "", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "err1", "", "err1", "err1"},
			// X
			{"E", "err1", "err1", "E", "err1", "err1", "", "err1", "err1", "err1", "E", "err1", "E", "err1", "err1", "err1", "E", "err1", "err1", "err1", "err1", "E", "err1", "err1", "E", "err1", "err1", "err1", "err1", "err1"}
	};
	private final int[][]nRegla= {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0 }, // A
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
			{ 43, 0, 0, 43, 0, 0, 44, 0, 0, 0, 43, 0, 43, 0, 0, 0, 43, 0, 0, 0, 0, 43, 0, 0, 43, 0, 0, 0, 0, 0 }  // X
	};
	private ArrayList<Integer> parse;
	private final HashMap<String, Integer> traductorNoTerminales= new HashMap<>();
	private final HashMap<String, Integer> traductorTerminales= new HashMap<>();

	public AnalizadorSintactico(String dir) {
		gestErr=new GestorErrores();
		aLexico=new AnalizadorLexico(dir, gestErr);
		pila=new Stack<>();
		//rellenarlo 
		rellenarTraductores();
		parse= new ArrayList<>();
	}



	public ArrayList<Integer>analisisSi() {
		pila.clear();
		parse.clear();
		pila.push("$");
		pila.push(axioma);
		//el token nos lo tiene que dar el analizador léxico
		String token = aLexico.analisisL().simbolo();
	
		while(!pila.peek().equals("$")) {
			String tope = pila.peek();
			System.out.println(tope);
			int columna = traductorTerminales.get(token);
			if (traductorTerminales.containsKey(tope)) { //el tope es terminal
				pila.pop();
				if (tope.equals(token)) {
					token=aLexico.analisisL().simbolo();
				}
				else {
					gestErr.error9(token, tope);
				}
			}
			else {//el tope no es terminal
				int fila = traductorNoTerminales.get(tope);
				pila.pop();
				if(M[fila][columna].contains("err")) {
					llamadaError(M[fila][columna]);
				}
				parse.addLast(nRegla[fila][columna]);
				String consecuente=M[fila][columna];
				if(!consecuente.isEmpty()) {
					String[]elementos = consecuente.split(":");
				for(int i=elementos.length; i>0; --i) {
					pila.push(elementos[i-1]);
				}
				}
			}
		}
		if(!token.equals("$")) {
			gestErr.errorN(token);
		}
		return parse;
	}



	private void llamadaError(String error) {
		switch(error) {
		default: throw new IllegalArgumentException("Se leyó el error: "+error);
		}
	}



	public static void main(String[] args) {
		AnalizadorSintactico aS = new AnalizadorSintactico("ficherotexto.txt");
		ArrayList<Integer>parse=aS.analisisSi();
		aS.escribirTexto("D");
		for(Integer ent: parse) {
			aS.escribirTexto(""+ent);
		}
	}
	private void escribirTexto(String string) {
		try (FileWriter escritor = new FileWriter("parse.txt", true)) {
			escritor.write(string + " ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void rellenarTraductores() {
		//hacer todos los puts
		traductorNoTerminales.put("A", 0);
		traductorNoTerminales.put("B", 1);
		traductorNoTerminales.put("C", 2);
		traductorNoTerminales.put("D", 3);
		traductorNoTerminales.put("E", 4);
		traductorNoTerminales.put("E1", 5);
		traductorNoTerminales.put("F", 6);
		traductorNoTerminales.put("G", 7);
		traductorNoTerminales.put("H", 8);
		traductorNoTerminales.put("K", 9);
		traductorNoTerminales.put("L", 10);
		traductorNoTerminales.put("P", 11);
		traductorNoTerminales.put("Q", 12);
		traductorNoTerminales.put("R", 13);
		traductorNoTerminales.put("R1", 14);
		traductorNoTerminales.put("S", 15);
		traductorNoTerminales.put("S1", 16);
		traductorNoTerminales.put("T", 17);
		traductorNoTerminales.put("U", 18);
		traductorNoTerminales.put("U1", 19);
		traductorNoTerminales.put("V", 20);
		traductorNoTerminales.put("V1", 21);
		traductorNoTerminales.put("X", 22);

		traductorTerminales.put("(", 0);
		traductorTerminales.put(")", 1);
		traductorTerminales.put("+", 2);
		traductorTerminales.put("++", 3);
		traductorTerminales.put(",", 4);
		traductorTerminales.put("-", 5);
		traductorTerminales.put(";", 6);
		traductorTerminales.put("=", 7);
		traductorTerminales.put("==", 8);
		traductorTerminales.put("boolean", 9);
		traductorTerminales.put("cad", 10);
		traductorTerminales.put("echo", 11);
		traductorTerminales.put("ent", 12);
		traductorTerminales.put("float", 13);
		traductorTerminales.put("for", 14);
		traductorTerminales.put("function", 15);
		traductorTerminales.put("id", 16);
		traductorTerminales.put("if", 17);
		traductorTerminales.put("int", 18);
		traductorTerminales.put("let", 19);
		traductorTerminales.put("prompt", 20);
		traductorTerminales.put("real", 21);
		traductorTerminales.put("return", 22);
		traductorTerminales.put("string", 23);
		traductorTerminales.put("veracidad", 24);
		traductorTerminales.put("void", 25);
		traductorTerminales.put("{", 26);
		traductorTerminales.put("||", 27);
		traductorTerminales.put("}", 28);
		traductorTerminales.put("$", 29);
	}


}
