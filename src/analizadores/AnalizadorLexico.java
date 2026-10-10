//puto el que lo lea
package analizadores;
import java.io.File;                  // Para manejar el archivo
import java.io.FileNotFoundException; 
import java.util.ArrayList;
import java.util.Scanner;          
import java.io.IOException;
import java.io.FileWriter;  //para escribir el fichero de tokens

public class AnalizadorLexico {
	private static String direccion;
	private String lexema;
	private int entero;
	private float real;
	private String fichero;
	public AnalizadorLexico(String dir, String fichero) {
		direccion=dir;
		lexema="";
		entero=0;
		real=0;
		this.fichero=fichero;
	}
	//devuelve una lista con cada caracter del fichero de texto
	public ArrayList<Character> lector( ) {
		File fichero = new File(direccion);
		ArrayList<Character> caracteres= new ArrayList<Character>();
		try (Scanner reader = new Scanner(fichero)) {//try with resources
			while (reader.hasNextLine()) {
				String data = reader.nextLine(); //leemos de linea en liena
				for (int i=0; i<data.length(); i++) {
					char car = data.charAt(i);//transformamos en caracter
					caracteres.addLast(car);
				}
				caracteres.addLast(' ');//añadimos el salto de linea que no se lee explicitamente
			}
		} catch (FileNotFoundException e) {
			System.out.println("Fichero no encontrado.");
			e.printStackTrace();
		}
		return caracteres;
	}

	public void tokenStringEntero() {
		escribirTexto("< entero, "+entero+" >");
		entero=0;
	}

	public void tokenStringReal() {
		escribirTexto("< real, "+real+" >");
		real=0;
	}

	public void tokenStringId(int posTS) {
		escribirTexto("< id, "+posTS+" >");
		lexema="";
	}
 
	public void tokenStringCadCAr() {
		lexema="";
		escribirTexto("< cadena, "+lexema+" >");
	}

	public void tokenStringPalabraReservada(String lexema){
		escribirTexto("< "+ lexema + ", ->");
	}

	private void escribirTexto(String string) {
		try(FileWriter escritor =new FileWriter(fichero, true)){//try with resourcess
			escritor.write(string+"\n");//concatena el token y cambia de linea
		}catch(IOException e) {
			e.printStackTrace();
		}

	}
//pedo pedet

	public static void main (String[] args) {
		
	}
}

