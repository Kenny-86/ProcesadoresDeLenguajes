package analizadores;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import tslib.*;

public class AnalizadorLexico {
	private String direccion;
	private String lexema;
	private int entero;
	private int real;
	private String fichero;
	private int estado;
	private int columnaActual;
	private TS_Gestor gestor = new TS_Gestor("TablaSimbolos.txt");

	/* Columnas: 0 letra, 1 digito, 2 '_', 3 '+', 4 '-', 5 '|', 6 '=', 7 '*', 8 '/',
	 * 9 EOF, 10 '"', 11 blanco/tab/salto, 12 ',', 13 ';', 14 '(', 15 ')', 16 '{', 17 '}',
	 * 18 '.', 19 otro.
	 * Estados >= 12 son finales: tras la accion se vuelve al estado 0. */
	private static int[][] transiciones = {
			/*0*/  {1,2,1,7,22,8,6,0,9,21,5,0,22,22,22,22,22,22,0,0},
			/*1*/  {1,1,1,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12},
			/*2*/  {13,2,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,3,13},
			/*3*/  {0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			/*4*/  {14,4,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14},
			/*5*/  {5,5,5,5,5,5,5,5,5,0,15,5,5,5,5,5,5,5,5,5},
			/*6*/  {17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17},
			/*7*/  {18,18,18,19,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18},
			/*8*/  {0,0,0,0,0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			/*9*/  {0,0,0,0,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0},
			/*10*/ {10,10,10,10,10,10,10,11,10,0,10,10,10,10,10,10,10,10,10,10},
			/*11*/ {10,10,10,10,10,10,10,11,0,0,10,10,10,10,10,10,10,10,10,10}
	};


	private static String[][] acciones = {
			/*0*/ {"C;L","N;L","C;L","L","L;GT6","L","L","E1","L","GT17","L","L","L;GT11","L;GT12","L;GT13","L;GT14","L;GT15","L;GT16","E1","E1"},
			/*1*/ {"C;L","C;L","C;L","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3","GT3"},
			/*2*/ {"GT1","N;L","GT1","GT1","GT1","GT1","GT1","GT1","GT1","GT1","GT1","GT1","GT1","GT1","GT1","GT1","GT1","GT1","L","GT1"},
			/*3*/ {"E4","D;L","E4","E4","E4","E4","E4","E4","E4","E4","E4","E4","E4","E4","E4","E4","E4","E4","E4","E4"},
			/*4*/ {"GT2","D;L","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2","GT2"},
			/*5*/ {"C;L","C;L","C;L","C;L","C;L","C;L","C;L","C;L","C;L","E5","L;GT10","C;L","C;L","C;L","C;L","C;L","C;L","C;L","C;L","C;L"},
			/*6*/ {"GT8","GT8","GT8","GT8","GT8","GT8","L;GT9","GT8","GT8","GT8","GT8","GT8","GT8","GT8","GT8","GT8","GT8","GT8","GT8","GT8"},
			/*7*/ {"GT4","GT4","GT4","L;GT5","GT4","GT4","GT4","GT4","GT4","GT4","GT4","GT4","GT4","GT4","GT4","GT4","GT4","GT4","GT4","GT4"},
			/*8*/ {"E2","E2","E2","E2","E2","L;GT7","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2"},
			/*9*/ {"E3","E3","E3","E3","E3","E3","E3","L","E3","E3","E3","E3","E3","E3","E3","E3","E3","E3","E3","E3"},
			/*10*/{"L","L","L","L","L","L","L","L","L","E3","L","L","L","L","L","L","L","L","L","L"},
			/*11*/{"L","L","L","L","L","L","L","L","L","E3","L","L","L","L","L","L","L","L","L","L"}
	};

	public AnalizadorLexico(String dir) {
		direccion = dir;
		lexema = "";
		entero = 0;
		real = 0;
		this.fichero = "tokens.txt";
		new File(fichero).delete();
		estado = 0;
		crearTPalRes(gestor); // primero las palabras reservadas
		crearAtributos(gestor);
		crearTablaGlobal(gestor); // despues la tabla global
	}

	public void crearAtributos(TS_Gestor gestor) {
		int r = gestor.createAtributo("despl",
				TS_Gestor.DescripcionAtributo.DIR,
				TS_Gestor.TipoDatoAtributo.ENTERO);
		if (r != 0) {
			System.out.println("Error al definir el atributo, código " + r);
		}
	}

	public ArrayList<Character> lector() {
		File f = new File(direccion);
		System.out.println("Abriendo: " + new File(direccion).getAbsolutePath());
		ArrayList<Character> caracteres = new ArrayList<Character>();
		try (Scanner reader = new Scanner(f)) {
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				for (int i = 0; i < data.length(); i++) {
					caracteres.add(data.charAt(i));
				}
				caracteres.add('\n');
			}
		} catch (FileNotFoundException e) {
			System.out.println("Fichero no encontrado.");
			e.printStackTrace();
		}
		return caracteres;
	}

	private void escribirTexto(String string) {
		try (FileWriter escritor = new FileWriter(fichero, true)) {
			escritor.write(string + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void crearTPalRes(TS_Gestor gestor) {
		int comprobar = gestor.createTPalabrasReservadas();
		if (comprobar != 0) {
			System.out.println("Error al crear la tabla de palabras reservadas, ya existe");
		}
		gestor.addEntradaTPalabrasReservadas("boolean");
		gestor.addEntradaTPalabrasReservadas("echo");
		gestor.addEntradaTPalabrasReservadas("float");
		gestor.addEntradaTPalabrasReservadas("for");
		gestor.addEntradaTPalabrasReservadas("function");
		gestor.addEntradaTPalabrasReservadas("if");
		gestor.addEntradaTPalabrasReservadas("int");
		gestor.addEntradaTPalabrasReservadas("let");
		gestor.addEntradaTPalabrasReservadas("prompt");
		gestor.addEntradaTPalabrasReservadas("return");
		gestor.addEntradaTPalabrasReservadas("string");
		gestor.addEntradaTPalabrasReservadas("void");
		gestor.addEntradaTPalabrasReservadas("false");
		gestor.addEntradaTPalabrasReservadas("true");
	}

	public void crearTablaGlobal(TS_Gestor gestor) {
		int comprobar = gestor.createTSGlobal();
		switch (comprobar) {
		case 1: System.out.println("Se está intentando crear la tabla global de nuevo, ya está creada"); break;
		case 2: System.out.println("Se tiene que crear primero la tabla de palabras reservadas"); break;
		case 3: System.out.println("No se han definido los atributos"); break;
		}
	}

	public void crearTablaLocal(TS_Gestor gestor) {
		int comprobar = gestor.createTSLocal();
		switch (comprobar) {
		case 2: System.out.println("Se tiene que crear primero la tabla global"); break;
		case 14: System.out.println("No se ha creado la tabla de palabras reservadas"); break;
		}
	}

	public void mostrarTabla(TS_Gestor gestor, TS_Gestor.Tabla tabla) {
		int comprobar = gestor.show(tabla);
		if (comprobar == 10) {
			System.out.println("La tabla pedida no existe");
		}
	}

	public void escribirTabla(TS_Gestor gestor, TS_Gestor.Tabla tabla) {
		int comprobar = gestor.write(tabla);
		if (comprobar == 10) {
			System.out.println("La tabla pedida no existe");
		}
	}

	public void tokenStringEntero() {
		escribirTexto("< entero, " + entero + " >");
		entero = 0;
	}

	public void tokenStringReal() {
		//hace falta cambiar el formato del real para que se ajuste a la especificación de la páctica
		escribirTexto("< real, " +  entero+"."+real+ ">");
		entero=0;
		real = 0;
	}

	public void tokenStringId(int posTS) {
		escribirTexto("< id, " + posTS + " >");
	}

	public void tokenStringCadCAr() {
		escribirTexto("< cadena, " + lexema + " >");
	}

	public void tokenStringPalabraReservada(String nombre) {
		escribirTexto("< " + nombre + ", >");
	}

	public void concatenarNumeroEnt(char num) {
		int res = num - '0';
		entero = entero * 10 + res;
	}

	public void concatenarNumeroDecimal(char num) {
		int res = num -'0';
		real=real*10+res;
		
	}

	public void concatenarCaracteres(char caracter) {
		lexema = lexema.concat("" + caracter);
	}

	private void reiniciarAcumuladores() {
		lexema = "";
		entero = 0;
		real = 0;
	}

	public static void main(String[] args) {
		AnalizadorLexico anl = new AnalizadorLexico("ficheroTexto.txt");
		GestorErrores gestorErr = new GestorErrores();
		ArrayList<Character> texto = anl.lector();

		int i = 0;
		while (i < texto.size()) {
			char caracter = texto.get(i);
			anl.columnaActual = obtenerColumna(caracter);

			int estadoSiguiente = transiciones[anl.estado][anl.columnaActual];
			String accionActual = acciones[anl.estado][anl.columnaActual];
			anl.ejecutarAcciones(accionActual, caracter, gestorErr);
			anl.estado = (estadoSiguiente >= 12) ? 0 : estadoSiguiente;

			// Solo se avanza si la accion lee o da error
			boolean consume = accionActual.contains("L")|| accionActual.contains("E");
			if (consume) {
				if (caracter == '\n') {
					gestorErr.siguienteLinea();
				}
				i++;
			}
		}

		// Fin de fichero (columna 9). Puede hacer falta dos pasadas: la primera cierra el
		// token pendiente (si lo hay) y la segunda emite el token eof.
		anl.columnaActual = 9;
			String accionActual = acciones[anl.estado][9];
			int estadoSiguiente = transiciones[anl.estado][9];
			anl.ejecutarAcciones(accionActual, ' ', gestorErr);
			anl.estado = (estadoSiguiente >= 12) ? 0 : estadoSiguiente;
			//anl.ejecutarAcciones("GT17", ' ', gestorErr);

		anl.escribirTabla(anl.gestor, TS_Gestor.Tabla.GLOBAL);
		anl.gestor.destroy(TS_Gestor.Tabla.GLOBAL);
	}

	private static int obtenerColumna(char c) {
		if (Character.isLetter(c)) return 0;
		if (c >= '0' && c <= '9') return 1;
		switch (c) {
		case '_': return 2;
		case '+': return 3;
		case '-': return 4;
		case '|': return 5;
		case '=': return 6;
		case '*': return 7;
		case '/': return 8;
		case '"': return 10;
		case ' ':
		case '\t':
		case '\n': return 11;
		case ',': return 12;
		case ';': return 13;
		case '(': return 14;
		case ')': return 15;
		case '{': return 16;
		case '}': return 17;
		case '.': return 18;
		default: return 19;
		}
	}

	public void ejecutarAcciones(String accion, char caracter, GestorErrores gestorErr) {
		if (accion.isEmpty()) {
			return;
		}
		String[] listaAcciones = accion.split(";");
		for (String accionPosible : listaAcciones) {
			switch (accionPosible.trim()) {
			case "L": break; // el avance lo gestiona main
			case "C": concatenarCaracteres(caracter); break;
			case "N": concatenarNumeroEnt(caracter); break;
			case "D": concatenarNumeroDecimal(caracter); break;
			case "GT1": tokenStringEntero(); break;
			case "GT2": tokenStringReal(); break;
			case "GT3":
				if (gestor.getEntradaTPalabrasReservadas(lexema) == 0) {
					int pos = gestor.addEntradaTSGlobal(lexema);
					if (pos == 0) {                        // ya estaba en la tabla
						pos = gestor.getEntradaTSGlobal(lexema);
					}
					tokenStringId(pos);
				} else {
					tokenStringPalabraReservada(lexema);
				}
				lexema = "";
				break;
			case "GT4": tokenStringPalabraReservada("suma"); break;
			case "GT5": tokenStringPalabraReservada("preIncremento"); break;
			case "GT6": tokenStringPalabraReservada("resta"); break;
			case "GT7": tokenStringPalabraReservada("or"); break;
			case "GT8": tokenStringPalabraReservada("asignacion"); break;
			case "GT9": tokenStringPalabraReservada("igualdad"); break;
			case "GT10":tokenStringCadCAr();lexema = "";break;
			case "GT11": tokenStringPalabraReservada("coma"); break;
			case "GT12": tokenStringPalabraReservada("puntoComa"); break;
			case "GT13": tokenStringPalabraReservada("abrirParentesis"); break;
			case "GT14": tokenStringPalabraReservada("cerrarParentesis"); break;
			case "GT15": tokenStringPalabraReservada("abrirLlave"); break;
			case "GT16": tokenStringPalabraReservada("cerrarLlave"); break;
			case "GT17": tokenStringPalabraReservada("eof"); break;
			case "E1": gestorErr.error1(caracter); reiniciarAcumuladores(); break;
			case "E2": gestorErr.error2(lexema, caracter); reiniciarAcumuladores(); break;
			case "E3": gestorErr.error3(); reiniciarAcumuladores(); break;
			case "E4": gestorErr.error4(entero); reiniciarAcumuladores(); break;
			case "E5": gestorErr.error5(); reiniciarAcumuladores(); break;
			}
		}
	}
}