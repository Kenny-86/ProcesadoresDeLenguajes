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
			{"err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "id:T:K", "err", "err", "err", "err", "err", "err", "err", "err", "void", "err", "err", "err", "err"},
			// B
			{"err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "S", "err", "err", "for:(:id:=:U:;:E:;:G:):{:C:}", "err", "S", "if:(:E:):S", "err", "let:id:T:D:;", "S", "err", "S", "err", "err", "err", "err", "err", "err", "err"},
			// C
			{"err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "B:C", "err", "err", "B:C", "err", "B:C", "B:C", "err", "B:C", "B:C", "err", "B:C", "err", "err", "err", "err", "err", "", "err"},
			// D
			{"err", "err", "err", "err", "err", "err", "", "=:U", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err"},
			// E
			{"R:E1", "err", "err", "R:E1", "err", "err", "err", "err", "err", "err", "R:E1", "err", "R:E1", "err", "err", "err", "R:E1", "err", "err", "err", "err", "R:E1", "err", "err", "R:E1", "err", "err", "err", "err", "err"},
			// E1
			{"err", "", "err", "err", "", "err", "", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "||:R:E1", "err", "err"},
			// F
			{"err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "function:id:H:(:A:):{:C:}", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err"},
			// G
			{"err", "", "err", "++:id", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "id:=:E", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err"},
			// H
			{"err", "err", "err", "err", "err", "err", "err", "err", "err", "T", "err", "err", "err", "T", "err", "err", "err", "err", "T", "err", "err", "err", "err", "T", "err", "void", "err", "err", "err", "err"},
			// K
			{"err", "", "err", "err", ",:id:T:K", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err"},
			// L
			{"E:Q", "", "err", "E:Q", "err", "err", "err", "err", "err", "err", "E:Q", "err", "E:Q", "err", "err", "err", "E:Q", "err", "err", "err", "err", "E:Q", "err", "err", "E:Q", "err", "err", "err", "err", "err"},
			// P
			{"err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "B:P", "err", "err", "B:P", "F:P", "B:P", "B:P", "err", "B:P", "B:P", "err", "B:P", "err", "err", "err", "err", "err", "err", ""},
			// Q
			{"err", "", "err", "err", ",:E:Q", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err"},
			// R
			{"U:R1", "err", "err", "U:R1", "err", "err", "err", "err", "err", "err", "U:R1", "err", "U:R1", "err", "err", "err", "U:R1", "err", "err", "err", "err", "U:R1", "err", "err", "U:R1", "err", "err", "err", "err", "err"},
			// R1
			{"err", "", "err", "err", "", "err", "", "err", "==:U:R1", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "", "err", "err"},
			// S
			{"err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "echo:E:;", "err", "err", "err", "err", "id:S1", "err", "err", "err", "prompt:id:;", "err", "return:X:;", "err", "err", "err", "err", "err", "err", "err"},
			// S1
			{"(:L:):;", "err", "err", "err", "err", "err", "err", "=:E:;", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err"},
			// T
			{"err", "err", "err", "err", "err", "err", "err", "err", "err", "boolean", "err", "err", "err", "float", "err", "err", "err", "err", "int", "err", "err", "err", "err", "string", "err", "err", "err", "err", "err", "err"},
			// U
			{"V:U1", "err", "err", "V:U1", "err", "err", "err", "err", "err", "err", "V:U1", "err", "V:U1", "err", "err", "err", "V:U1", "err", "err", "err", "err", "V:U1", "err", "err", "V:U1", "err", "err", "err", "err", "err"},
			// U1
			{"err", "", "+:V:U1", "err", "", "-:V:U1", "", "err", "", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "", "err", "err"},
			// V
			{"(:E:)", "err", "err", "++:id", "err", "err", "err", "err", "err", "err", "cad", "err", "ent", "err", "err", "err", "id:V1", "err", "err", "err", "err", "real", "err", "err", "veracidad", "err", "err", "err", "err", "err"},
			// V1
			{"(:L:)", "", "", "err", "", "", "", "err", "", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "err", "", "err", "err"},
			// X
			{"E", "err", "err", "E", "err", "err", "", "err", "err", "err", "E", "err", "E", "err", "err", "err", "E", "err", "err", "err", "err", "E", "err", "err", "E", "err", "err", "err", "err", "err"}
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
					System.out.println(fila+", "+columna);
					llamadaError(fila);
					return null;
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



	private void llamadaError(int fila) {
		switch (fila) {
		case 0: gestErr.errorA(); break;
		case 1: gestErr.errorB(); break;
		case 2: gestErr.errorC(); break;
		case 3: gestErr.errorD(); break;
		case 4: gestErr.errorExp(); break;
		case 5: gestErr.errorPC(); break;
		case 6: gestErr.errorF(); break;
		case 7: gestErr.errorG(); break;
		case 8: gestErr.errorH(); break;
		case 9: gestErr.errorK(); break;
		case 10: gestErr.errorL(); break;
		case 11: gestErr.errorP(); break;
		case 12: gestErr.errorQ(); break;
		case 13: gestErr.errorExp(); break;
		case 14: gestErr.errorPC(); break;
		case 15: gestErr.errorS(); break;
		case 16: gestErr.errorS1(); break;
		case 17: gestErr.errorT(); break;
		case 18: gestErr.errorExp(); break;
		case 19: gestErr.errorPC(); break;
		case 20: gestErr.errorExp(); break;
		case 21: gestErr.errorV1(); break;
		case 22: gestErr.errorX(); break;
		}
	}



	public static void main(String[] args) {
		AnalizadorSintactico aS = new AnalizadorSintactico("ficherotexto.txt");
		ArrayList<Integer>parse=aS.analisisSi();
		if (parse ==null) {
			System.out.println("La ejecución devolvió error, consulta el fichero de errores :)");
		}
		else {
		aS.escribirTexto("D");
		for(Integer ent: parse) {
			aS.escribirTexto(""+ent);
		}
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
