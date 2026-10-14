
package analizadores;

import java.io.FileWriter;
import java.io.IOException;

public class GestorErrores {
	private int nLinea;
	private static String fichero= "errores.txt";
	
	public GestorErrores() {
		nLinea=1;
	}
	public void siguienteLinea() {
		++nLinea;
	}
	public int getLinea() {
		return nLinea;
	}
//añadir los distintos errores
	public void error1(char c) {
		escribirTexto( "Error en la linea: "+nLinea+" Error léxico: Se leyó el caracter: "+c+" que no es aceptado en este lenguaje en este contexto");
	}
	public void error2( String lexema, char caracter) {
		escribirTexto( "Error en la linea "+nLinea+"Error léxico: El token "+ lexema+ caracter+" no es válido");
	}
	
	public void error3 () {
		escribirTexto( "Error en la linea "+nLinea+": Error léxico: El formato de comentario no es válido");
	}
	public void error4(int num) {
		escribirTexto(  "Error en la linea "+nLinea+": Error léxico: token "+num+"."+" no es válido se esperaba otro dígito");
	}
	public void error5(String cadena) {
		escribirTexto( "Error en la linea "+nLinea+": Error léxico: El formato de cadena "+cadena+" no es válido");
	}
	private void escribirTexto(String string) {
		try (FileWriter escritor = new FileWriter(fichero, true)) {
			escritor.write(string + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

