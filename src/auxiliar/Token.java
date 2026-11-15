package auxiliar;

public class Token {
	private String codToken;
	private String atributo;
	
	public Token(String codToken, String atributo) {
		this.codToken=codToken;
		this.atributo=atributo;
	}
	
	public String getCodToken() {
		return codToken;
	}
	
	public String getAtributo() {
		return atributo;
	}
	
	public String toString() {
		return "<"+codToken+","+atributo+">";
	}

	public String simbolo() {
		switch(codToken) {
		case "suma": return "+";
		case "resta": return "-";
		case "abrirParentesis": return "(";
		case "cerrarParentesis": return ")";
		case "abrirLlave": return "{";
		case "cerrarLlave": return "}";
		case "or": return "||";
		case "preIncremento": return "++";
		case "false": 
		case "true": return "veracidad";
		case "entero": return "ent";
		case "cadena": return "cad";
		case "coma": return ",";
		case "puntoComa": return ";";
		case "asignacion": return "=";
		case "igualdad": return "==";
		case "eof": return "$";
		default: return codToken;
		}
	}
}