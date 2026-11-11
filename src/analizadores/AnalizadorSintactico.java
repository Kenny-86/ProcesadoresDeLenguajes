package analizadores;

public class AnalizadorSintactico {
	private final String[] terminales= {"id",";", "ent","cad", "for","function", "int","", "",""};
	private final String[] noTerminales = {"P","A", "B"};
	private final String axioma = "P";
	private AnalizadorLexico aLexico;
	private final Pair<Sring, String> m;
}
