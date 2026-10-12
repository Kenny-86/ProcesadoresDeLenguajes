package analizadores;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

import tslib.*;

public class AnalizadorLexico {

    private String direccion;
    private String lexema;
    private int entero;
    private float real;
    private String fichero;
    private int contadorDecimal;
    private int estado;
    private int columnaActual;

    private TS_Gestor gestor = new TS_Gestor("TablaSimbolos.txt");

    private static int[][] transiciones = {
        {1,2,1,7,22,8,6,0,9,21,5,0,22,22,22,22,22,22,0,0},
        {1,1,1,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12},
        {13,2,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,3,13},
        {0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {14,4,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14},
        {5,5,5,5,5,5,5,5,5,0,15,5,5,5,5,5,5,5,5,5},
        {17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17},
        {18,18,18,19,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18},
        {0,0,0,0,0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0},
        {10,10,10,10,10,10,10,11,10,0,10,10,10,10,10,10,10,10,10,10},
        {10,10,10,10,10,10,10,11,0,0,10,10,10,10,10,10,10,10,10,10},
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
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };

    private static String[][] acciones = {
        {"C;L","C;L","C;L","L","L;GT6","L","L","E1","L","L;GT17","L","L","L;GT11","L;GT12","L;GT13","L;GT14","L;GT15","L;GT16","E1","E1"},
        {"C;L","C;L","C;L","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3","L;GT3"},
        {"L;GT1","N;L","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;GT1","L;R","L;GT1"},
        {"E2","D;L","E2","E2","E2","E2","E2","E2","E2","E3","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2"},
        {"L;GT2","D;L","L;GT2","L;GT2","L;GT2","L;GT2","L;GT2","L;GT2","L;GT2","GT2","L;GT2","L;GT2","L;GT2","L;GT2","L;GT2","L;GT2","L;GT2","L;GT2","L;GT2","L;GT2"},
        {"C;L","C;L","C;L","C;L","C;L","C;L","C;L","C;L","C;L","E3","L;GT10","C;L","C;L","C;L","C;L","C;L","C;L","C;L","C;L","C;L"},
        {"L;GT9","L;GT9","L;GT9","L;GT9","L;GT9","L;GT9","L;GT8","L;GT9","L;GT9","GT9","L;GT9","L;GT9","L;GT9","L;GT9","L;GT9","L;GT9","L;GT9","L;GT9","L;GT9","L;GT9"},
        {"L;GT4","L;GT4","L;GT4","L;GT5","L;GT4","L;GT4","L;GT4","L;GT4","L;GT4","GT4","L;GT4","L;GT4","L;GT4","L;GT4","L;GT4","L;GT4","L;GT4","L;GT4","L;GT4","L;GT4"},
        {"E2","E2","E2","E2","E2","L;GT7","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2","E2"},
        {"E3","E3","E3","E3","E3","E3","E3","L","E3","E3","E3","E3","E3","E3","E3","E3","E3","E3","E3","E3"},
        {"L","L","L","L","L","L","L","L","L","E3","L","L","L","L","L","L","L","L","L","L"},
        {"L","L","L","L","L","L","L","L","L","E3","L","L","L","L","L","L","L","L","L","L"},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""},
        {"","","","","","","","","","","","","","","","","","","",""}
    };

    public AnalizadorLexico(String dir) {
        direccion = dir;
        lexema = "";
        entero = 0;
        real = 0;
        fichero = "tokens.txt";
        contadorDecimal = 0;
        estado = 0;

        // Crear primero la tabla de palabras reservadas.
        crearTPalRes(gestor);

        // Después, crear la tabla global.
        gestor.createTSGlobal();
    }

    public ArrayList<Character> lector() {
        File archivo = new File(direccion);
        ArrayList<Character> caracteres = new ArrayList<>();

        try (Scanner reader = new Scanner(archivo)) {
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
            System.out.println(
                "Error al crear la tabla de palabras reservadas, ya existe"
            );
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
            case 1:
                System.out.println(
                    "Se está intentando crear la tabla global de nuevo, ya está creada"
                );
                break;
            case 2:
                System.out.println(
                    "Se tiene que crear primero la tabla de palabras reservadas"
                );
                break;
            case 3:
                System.out.println("No se han definido los atributos");
                break;
        }
    }

    public void crearTablaLocal(TS_Gestor gestor) {
        int comprobar = gestor.createTSLocal();

        switch (comprobar) {
            case 2:
                System.out.println("Se tiene que crear primero la tabla global");
                break;
            case 14:
                System.out.println("No se ha creado la tabla de palabras reservadas");
                break;
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
        escribirTexto("< real, " + real + " >");
        real = 0;
        contadorDecimal = 0;
    }

    public void tokenStringId(int posTS) {
        escribirTexto("< id, " + posTS + " >");
        lexema = "";
    }

    public void tokenStringCadCAr() {
        escribirTexto("< cadena, " + lexema + " >");
        lexema = "";
    }

    public void tokenStringPalabraReservada(String lexema) {
        escribirTexto("< " + lexema + ", ->");
        this.lexema = "";
    }

    public void concatenarNumeroEnt(char num) {
        int res = (int) num - 48;
        entero = entero * 10 + res;
    }

    public void concatenarNumeroDecimal(char num) {
        float res = (int) num - 48;
        contadorDecimal--;
        res = res * (float) Math.pow(10, contadorDecimal);
        real = real + res;
    }

    public void inicioReal() {
        real = (float) entero;
        entero = 0;
    }

    public void concatenarCaracteres(char caracter) {
        lexema = lexema.concat("" + caracter);
    }

    public static void main(String[] args) {
        AnalizadorLexico anl = new AnalizadorLexico("texto.txt");
        GestorErrores gestorErr = new GestorErrores();

        ArrayList<Character> texto = anl.lector();

        int i = 0;

        while (i < texto.size()) {
            char caracter = texto.get(i);
            int columna = obtenerColumna(caracter, gestorErr);

            /*
             * Si el carácter termina un token, se genera ese token
             * sin incrementar i. Así, el mismo carácter se procesa
             * de nuevo desde el estado inicial.
             */
            if (anl.terminarTokenYReprocesar(
                    columna, caracter, gestorErr)) {
                continue;
            }

            // El carácter no termina un token anterior:
            // se consulta la matriz de transiciones.
            anl.columnaActual = columna;

            if (columna >= 9) {
                anl.columnaActual = columna + 1;
            }

            int estadoSiguiente =
                transiciones[anl.estado][anl.columnaActual];

            String accionActual =
                acciones[anl.estado][anl.columnaActual];

            anl.ejecutarAcciones(accionActual, caracter, gestorErr);

            anl.estado = estadoSiguiente;

            // El carácter actual ya se ha procesado.
            i++;
        }

        // Procesar el fin de fichero.
        int estadoAnterior = anl.estado;

        String accionEOF = acciones[estadoAnterior][9];
        anl.ejecutarAcciones(accionEOF, '\0', gestorErr);

        anl.estado = 0;

        // Si no estábamos en el estado inicial, emitimos también EOF.
        if (estadoAnterior != 0) {
            String accionEOFFinal = acciones[0][9];
            anl.ejecutarAcciones(accionEOFFinal, '\0', gestorErr);
        }
    }

    /*
     * Devuelve true si se ha terminado un token y el carácter actual
     * debe volver a procesarse desde el estado 0.
     *
     * En ese caso, main utiliza continue sin incrementar i.
     */
    private boolean terminarTokenYReprocesar(
            int columna, char caracter, GestorErrores gestorErr) {

        switch (estado) {

            // Un identificador termina si el carácter no es
            // una letra, un dígito o un guion bajo.
            case 1:
                if (columna != 0 && columna != 1 && columna != 2) {
                    ejecutarAcciones("GT3", caracter, gestorErr);
                    estado = 0;
                    return true;
                }
                break;

            // Un entero termina cuando aparece algo que no sea
            // un dígito ni el punto que inicia la parte decimal.
            case 2:
                if (columna != 1 && columna != 18) {
                    ejecutarAcciones("GT1", caracter, gestorErr);
                    estado = 0;
                    return true;
                }
                break;

            // Un real termina cuando aparece un carácter
            // que no es un dígito.
            case 4:
                if (columna != 1) {
                    ejecutarAcciones("GT2", caracter, gestorErr);
                    estado = 0;
                    return true;
                }
                break;

            // Si se ha leído '+' y el siguiente carácter no es '+',
            // se genera el token de suma y se reprocesa el carácter.
            case 7:
                if (columna != 3) {
                    ejecutarAcciones("GT4", caracter, gestorErr);
                    estado = 0;
                    return true;
                }
                break;

            // Si se ha leído '=' y el siguiente carácter no es '=',
            // se genera el token de igualdad según tu matriz.
            case 6:
                if (columna != 6) {
                    ejecutarAcciones("GT9", caracter, gestorErr);
                    estado = 0;
                    return true;
                }
                break;
        }

        return false;
    }

    private static int obtenerColumna(char c, GestorErrores gestorErr) {
        if (Character.isAlphabetic(c)) {
            return 0;
        }

        if (Character.isDigit(c)) {
            return 1;
        }

        switch (c) {
            case '_': return 2;
            case '+': return 3;
            case '-': return 4;
            case '|': return 5;
            case '=': return 6;
            case '*': return 7;
            case '/': return 8;
            case '"': return 9;
            case ' ':
            case '\t':
                return 10;
            case '\n':
                gestorErr.siguienteLinea();
                return 10;
            case ',':
                return 11;
            case ';':
                return 12;
            case '(':
                return 13;
            case ')':
                return 14;
            case '{':
                return 15;
            case '}':
                return 16;
            case '.':
                return 17;
            default:
                return 18;
        }
    }

    public void ejecutarAcciones(
            String accion, char caracter, GestorErrores gestorErr) {

        if (accion == null || accion.isEmpty()) {
            return;
        }

        String[] listaAcciones = accion.split(";");

        for (String accionPosible : listaAcciones) {
            switch (accionPosible.trim()) {

                case "L":
                    // El avance del índice se controla en main.
                    break;

                case "C":
                    concatenarCaracteres(caracter);
                    break;

                case "N":
                    concatenarNumeroEnt(caracter);
                    break;

                case "D":
                    concatenarNumeroDecimal(caracter);
                    break;

                case "R":
                    inicioReal();
                    break;

                case "GT1":
                    tokenStringEntero();
                    break;

                case "GT2":
                    tokenStringReal();
                    break;

                case "GT3":
                    if (gestor.getEntradaTPalabrasReservadas(lexema) == 0) {
                        int pos = gestor.addEntradaTSGlobal(lexema);
                        tokenStringId(pos);
                    } else {
                        tokenStringPalabraReservada(lexema);
                    }
                    break;

                case "GT4":
                    tokenStringPalabraReservada("suma");
                    break;

                case "GT5":
                    tokenStringPalabraReservada("preIncremento");
                    break;

                case "GT6":
                    tokenStringPalabraReservada("resta");
                    break;

                case "GT7":
                    tokenStringPalabraReservada("or");
                    break;

                case "GT8":
                    tokenStringPalabraReservada("asignacion");
                    break;

                case "GT9":
                    tokenStringPalabraReservada("igualdad");
                    break;

                case "GT10":
                    tokenStringCadCAr();
                    break;

                case "GT11":
                    tokenStringPalabraReservada("coma");
                    break;

                case "GT12":
                    tokenStringPalabraReservada("puntoComa");
                    break;

                case "GT13":
                    tokenStringPalabraReservada("abrirParentesis");
                    break;

                case "GT14":
                    tokenStringPalabraReservada("cerrarParentesis");
                    break;

                case "GT15":
                    tokenStringPalabraReservada("abrirLlave");
                    break;

                case "GT16":
                    tokenStringPalabraReservada("cerrarLlave");
                    break;

                case "GT17":
                    tokenStringPalabraReservada("eof");
                    break;

                case "E1":
                    gestorErr.error1(caracter);
                    break;

                case "E2":
                    gestorErr.error2(lexema);
                    break;

                case "E3":
                    gestorErr.error3();
                    break;

                case "E4":
                    gestorErr.error4();
                    break;
            }
        }
    }
}
