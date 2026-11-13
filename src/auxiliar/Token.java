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
}