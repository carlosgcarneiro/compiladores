
import compilador.Env;
import compilador.Lexer;
import compilador.Parser;
import compilador.Token;
import java.util.Scanner;
import java.util.*;

public class Compilador {

    private static String fileName;          // arquivo a ser compilado
    public static Env currentTable; // tabela de simbolos do programa

    public static void startCompiler() {
        Scanner keyboardInput = new Scanner(System.in);
        System.out.print("Insira o nome do arquivo a ser compilado: ");
        fileName = keyboardInput.next();
        keyboardInput.close();
    }

    public static void main(String[] args) {
        try {
            startCompiler();
            int parser = 1;
            if (parser == 1) {
                Parser p = new Parser(fileName);
            } else {
                Lexer l = new Lexer(fileName);
                //Token t;

                l.simulateAnalysis();
            }
            System.out.println("\n\n Compiler: Compilação do arquivo " + fileName + " feita com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
