
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
	public void error1(char c) {
		escribirTexto( "Error en la linea: "+nLinea+" Error léxico: Se leyó el caracter: "+c+" que no es aceptado en este lenguaje en este contexto.");
	}
	public void error2() {
		escribirTexto( "Error en la linea "+nLinea+": Error léxico: El formato de comentario no es válido.");
	}
	public void error3(int num, char c) {
		escribirTexto(  "Error en la linea "+nLinea+": Error léxico: token "+num+"."+c+" no es válido se esperaba otro dígito.");
	}
	public void error4(String cadena) {
		escribirTexto( "Error en la linea "+nLinea+": Error léxico: El formato de cadena "+cadena+" no es válido, debe de cerrarse.");
	}
	public void error5() {
		error1('/');
	}
	public void error6(String cadena) {
		escribirTexto( "Error en la linea "+nLinea+": Error léxico: El formato de cadena "+cadena+" no es válido, ocupa más de 64 caracteres.");
	}
	public void error7(int entero) {
		escribirTexto( "Error en la linea "+nLinea+": Error léxico: El valor del int "+entero+" se sale de rango (mayor que 32767 o menor que -32767).");
	}
	public void error8(int entero, long real) {
		escribirTexto( "Error en la linea "+nLinea+": Error léxico: El valor del float "+entero+"."+real+" se sale del rango máximo (mayor que 117549436.0 o menor que -117549436.0).");
	}
	
	public void error9(String leido, String esperado) {
		escribirTexto("Error en la linea "+nLinea+": Error sintáctico: Se leyó el token "+ leido+ " pero se esperaba el token " +esperado+".");
	}
	public void errorK() {
		escribirTexto("Error en la linea "+nLinea+" Error sintáctico: Se esperaba ',' seguido de otro parámetro, o ')' para cerrar la lista de parámetros");
	}
	
	public void errorC() {
		escribirTexto("Error en la linea "+nLinea+" Error sintáctico: Se esperaba una sentencia o '}' para cerrar el bloque");
	}
	
	public void errorS() {
		escribirTexto("Error en la linea "+nLinea+" Error sintáctico: Se esperaba una asignación, una llamada a función, echo, prompt o return");
	}
	
	public void errorS1() {
		escribirTexto("Error en la linea "+nLinea+" Error sintáctico: Se esperaba '='  o '('  después del identificador");
	}
	
	public void errorExp() {
		escribirTexto("Error en la linea "+nLinea+" Error sintáctico: Se esperaba una expresión (identificador, constante, operando,...)");
	}
	
	public void errorPC() {
		escribirTexto("Error en la linea "+nLinea+" Error sintáctico: Se esperaba fin de expresión");
	}
	private void escribirTexto(String string) {
		try (FileWriter escritor = new FileWriter(fichero, true)) {
			escritor.write(string + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

