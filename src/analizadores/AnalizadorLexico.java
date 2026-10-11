//puto el que lo lea
package analizadores;
import java.io.File;                  // Para manejar el archivo
import java.io.FileNotFoundException; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;          
import java.io.IOException;
import java.io.FileWriter;  //para escribir el fichero de tokens

public class AnalizadorLexico {
	private static String direccion;
	private String lexema;
	private int entero;
	private float real;
	private String fichero;
	private int contadorDecimal;
	private int estado;
	private static Map<String, Integer> columnas = new HashMap<>();//se usará para recorrer la matriz de transiciones
	
	private static int[][] transiciones = new int[23][20];  //Matriz que marca todos los cambios de estado
	/*={{1,2,1,7,22,8,6,-1,9,21,5,0,22,22,22,22,22,22, -1, -1},
	{1, 1, 1,12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12},
	{13, 2, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 3, 13},
	{-1,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
	{14,4,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14},
	{5,5,5,5,5,5,5,5,5,-1,15,5,5,5,5,5,5,5,5},
	{17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17},
	{18,18,18,19,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18},
	{-1,-1,-1,-1,-1,20,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
	{-1,-1,-1,-1,-1,-1,-1,10,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
	{10,10,10,10,10,10,10,11,10,-1,10,10,10,10,10,10,10,10,10,10},
	{10,10,10,10,10,10,10,11,0,-1,10,10,10,10,10,10,10,10,10,10},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}}*/
	private static String[][] acciones= new String[23][20];//Matriz que marca todas las acciones semánticas a ser realizadas, no es final, seguramente se cambie
	public AnalizadorLexico(String dir) {
		direccion=dir;
		lexema="";
		entero=0;
		real=0;
		this.fichero="tokens.txt";
		contadorDecimal=0;
		estado=0;
		columnas.put(" ",0);//Aqui irian todas las letras
		columnas.put("_", 2);
		columnas.put("+", 3);
		columnas.put("-", 4);
		columnas.put("|", 5);
		columnas.put("=", 6);
		columnas.put("*", 7);
		columnas.put("/", 8);
		columnas.put(" ", 9);//Aqui irian el E0f
		columnas.put(" ",10);//Aqui : "
		columnas.put(" ", 11);//delimitador
		columnas.put("\t", 2);
		columnas.put("\n", 2);
		columnas.put(",", 12);
		columnas.put(";", 13);
		columnas.put("(", 14);
		columnas.put(")", 15);
		columnas.put("{", 16);
		columnas.put("}", 17);
		columnas.put(".", 18);
		columnas.put(" ", 19);// o.c.
		columnas.put(" ", 1);// digitos
	}
	//devuelve una lista con cada caracter del fichero de texto
	public ArrayList<Character> lector( ) {
		File fichero = new File(direccion);
		ArrayList<Character> caracteres= new ArrayList<>();
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
		entero=0;
		contadorDecimal=0;
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
	
	  public void concatenarNumeroEnt(char num) {
        int res=(int)num;
        entero=entero*10+res;
    }

    public void concatenarNumeroDecimal(char num) {
        float res=(int)num;
        contadorDecimal-=1;
        res=res*(float)Math.pow(10, contadorDecimal);
        real=real+res;
    }

    public void concatenarCaracteres(char caracter) {
       lexema.concat(""+caracter);
    }

	public static void main (String[] args) {
		
		
	}
}

