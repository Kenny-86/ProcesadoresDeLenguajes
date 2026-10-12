package analizadores;

public class GestorErrores {
	private int nLinea;
	
	public GestorErrores() {
		nLinea=0;
	}
	public void siguienteLinea() {
		++nLinea;
	}
	public int getLinea() {
		return nLinea;
	}
//añadir los distintos errores
	public String error1(char c) {
		return "Se ha detectado el caracter "+c+" en la linea "+nLinea+". \n Este símbolo no es aceptado por este lenguaje en este contexto";
	}
	public String error2( String lexema) {
		return "Error de escritura en la linea "+nLinea+", el token "+ lexema+" no es válido";
	}
	
	public String error3 () {
		return "Error en la linea "+nLinea+": El formato de comentario no es válido";
	}
	public String error4() {
		return  "Error en la linea "+nLinea+": token no válido se esperaba otro dígito";
	}
}
