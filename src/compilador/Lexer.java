/*
 * Classe que implementa o Analisador Lexico, o construtor insere as palavras reservadas na 
 * tabela de simbolos
 */
package compilador;

import java.io.*;
import java.util.*;

public class Lexer {

    public static int line = 1;  // contador de linhas
    private char ch = ' '; 		 // caractere lido do arquivo
    private boolean eof = false; // indica fim de arquivo
    private FileReader file;	 // arquivo a ser compilado

    private Hashtable words = new Hashtable();
    private java.lang.String erros = "";

    // Método para inserir palavras reservadas na HashTable
    private void reserve(Word w) {
        words.put(w.getLexeme(), w); // lexema é a chave para entrada na HashTable
    }

    //Construtor recebe o arquivo que conterá os tokens
    public Lexer(java.lang.String fileName) throws FileNotFoundException {

        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }

        // Insere palavras reservadas na HashTable
        reserve(new Word("declare", Tag.DECLARE));
        reserve(new Word("start", Tag.START));
        reserve(new Word("end", Tag.END));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("string", Tag.STRING));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("then", Tag.THEN));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("read", Tag.READ));
        reserve(new Word("write", Tag.WRITE));
        reserve(new Word("or", Tag.OR));
        reserve(new Word("and", Tag.AND));

        reserve(Type.INT_TYPE);
        reserve(Type.STRING_TYPE);
    }

    // Função para ler o próximo caractere
    private void readch() throws IOException {
        int charInt = file.read();

        // Se não terminou de ler o arquivo, o próximo caractere é colocado em "ch"
        if ((charInt != -1) && (!eof)) {
            ch = (char) charInt;
        } else {
            ch = ' '; // indica fim de arquivo
            eof = true;
        }
    }

    /**
     * Lê o próximo caractere do arquivo e verifica se é igual a c
     */
    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c) {
            return false;
        }
        ch = ' ';
        return true;
    }

    /**
     * Retorna os tokens lidos do arquivo
     */
    public Token scan() throws IOException {
        // Desconsidera delimitadores na entrada e espaços em branco
        for (;; readch()) {
            if (eof) {
                return new Token(Tag.EOF);
            }
            if (ch == ' ' || ch == '\t' || ch == '\b' || ch == '\r') {
                continue;
            } else if (ch == '\n') {
                Lexer.line++;
            } else {
                break;
            }
        }

        // Tratamento de comentários
        if (ch == '/') {
            readch();

            // Comentario no formato "/* ... */"
            if (ch == '*') {
                int linhasComment = 0; // usado para reportar o erro, se existente,
                // na primeira linha em que o comentário incorreto aparece
                while (true) {
                    readch();

                    // Se atingir o fim de arquivo e o comentário não for fechado,
                    // encontrou-se um erro léxico
                    if (eof) {
                        erros += "Comentário em formato incorreto na linha " + Lexer.line + "\n";
                        break;
                    } else if (ch == '*') {
                        if (readch('/')) {
                            Lexer.line += linhasComment;
                            return scan(); // comentário finalizado : ignora-se o comentário
                            // retornando-se o token do próximo scan
                        } else if (eof) {
                            erros += "Comentário em formato incorreto na linha " + Lexer.line + "\n";
                            break;
                        }
                    }
                    if (ch == '\n') {
                        linhasComment++;  // podem existir varias linhas neste tipo de comentario
                    }

                }

            } /* Comentário no formato "// ... " */ else if (ch == '/') {
                while (true) {
                    readch();

                    if (ch == '\n') {
                        return scan(); // comentário finalizado : ignora-se o comentário
                        // retornando-se o token do próximo scan
                    }
                }
            } /* Se não for comentário, a única possibilidade é de se retornar um operador
             * do tipo div, que será tratado pelo analisador sintático */ else {
                return Word.div; // retorna /
            }
        }
        // Fim do tratamento de comentários 

        switch (ch) {

            case '=':
                if (readch('=')) {
                    return Word.eq; // retorna ==
                } else {
                    return Word.at; // retorna =
                }
            case '>':
                if (readch('=')) {
                    return Word.ge; // retorna >=
                } else {
                    return Word.gt; // retorna >
                }
            case '<':
                if (readch('=')) {
                    return Word.le; // retorna <=
                } else if (ch == '>') {
                    return Word.ne; // retorna <>
                } else {
                    return Word.lt; // retorna <
                }
            case '+':
                ch = ' ';
                return Word.plus;	// retorna +
            case '-':
                ch = ' ';
                return Word.minus;	// retorna -
            case '*':
                ch = ' ';
                return Word.times; // retorna *
            case ';':
                ch = ' ';
                return Word.pvg;   // retorna ;
            case ':':
                ch = ' ';
                return Word.dp;   // retorna :
            case ',':
                ch = ' ';
                return Word.vg;	   // retorna ,
            case '(':
                ch = ' ';
                return Word.left_par; // retorna (
            case ')':
                ch = ' ';
                return Word.right_par; // retorna )
        }

        // Constantes Numéricas
        if (Character.isDigit(ch)) {
            long value = 0;
            while (Character.isDigit(ch)) {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            }
            if (!Character.isDigit(ch)) {
                // ch = ' ';
                if (value > 2147483647) // valor ultrapassou o tamanho de um int Java
                {
                    erros += "Valor numerico lido ultrapassou o suportado na linha " + Lexer.line + "\n";
                }

                return new Int(value); // retorna o inteiro

            }
        }

        // Literais
        if (ch == '"') {
            java.lang.String literal = "";

            while (true) {
                readch();
                if (eof) {
                    // se a string não for fechada e chegar-se ao fim do arquivo,
                    // tem-se um erro léxico...
                    erros += "A string não foi fechada na linha" + Lexer.line + "\n";
                } else if (ch == '"') {
                    ch = ' ';
                    return new Word(literal, Tag.STRING); // retorna o literal bem formado
                } else if (((ch >= 0) && (ch <= 255)) && (ch != '"') && (ch != '\n')) {
                    literal += ch; // literal é formado pela concatenação dos caracteres lidos
                } else {
                    // Não está entre 0 e 255
                    erros += "A string não foi fechada na linha" + Lexer.line + "\n";
                }
            }
        }
        // Palavras reservadas
        if ((ch >= 65 && ch <= 122)) {
            StringBuffer b = new StringBuffer();
            do {
                b.append(ch);
                readch();
            } while ((48 <= ch && ch <= 57) || (ch >= 65 && ch <= 122));

            java.lang.String s = b.toString();
            Word w = (Word) words.get(s);// é != de null se for palavra reservada

            if (w != null) {
                return w;// retorna a palavra reservada
            }
            if (s.length() > 20) {
                erros += "Identificador com mais de 20 caracteres na linha " + Lexer.line + "\n";
                Word ident_errado = new Word(s, Tag.ID);
                return ident_errado; // lexema é identificado como identificador, mas nao é adicionado à TS
            }
            // Não é palavra reservada
            if (s.toUpperCase() == "OR") {
                w = new Word(s, Tag.OR);
            } else if (s.toUpperCase() == "AND") {
                w = new Word(s, Tag.AND);
            } else {
                w = new Word(s, Tag.ID);
            }
            words.put(s, w);
            return w;
        }

        // Qualquer caracter entre os 256 acii nao utilizados pela gramatica
        Word caractere = new Word("" + ch, Tag.CH);
        //words.put(ch, caractere);   caracter é uma constante, por isso nao entra na TS.
        ch = ' ';
        return caractere;
    }

    /**
     * Simula a análise léxica e imprime a TS e os erros encontrados.
     */
    public Hashtable simulateAnalysis() throws IOException {

        Token t = scan(); // busca novo token

        System.out.println("\n \t Tokens encontrados no arquivo \n");

        // Lê tokens enquanto não encontra fim de arquivo
        while (t.tag != Tag.EOF) {
            if (t instanceof Word) {
                System.out.println(((Word) t).getLexeme() + " ");
            } else if (t instanceof Int) {
                System.out.println(((Int) t).toString() + " ");
            } else {
                System.out.println((char) t.tag + " ");
            }
            t = scan(); // busca outro token
        }

        System.out.println("\n \t \t Tabela de Símbolos\n");
        Enumeration tokensTable = words.elements(); // para iterar na tabela// para iterar na tabela
        while (tokensTable.hasMoreElements()) {
            Word tt = (Word) tokensTable.nextElement();
            System.out.println("Lexema: \t" + tt.getLexeme() + " \t\t\t\t Tag:   " + tt.tag); // imprime-se o token
        }

        if (erros.length() > 0) {
            System.out.println("\n------ ERROS ----- ");
            System.out.println(erros);
        }
        return words;
    }

}
