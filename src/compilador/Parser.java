/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author caca
 */
public class Parser {

    public static final java.lang.String ANSI_RESET = "\u001B[0m";
    public static final java.lang.String ANSI_BLACK = "\u001B[30m";
    public static final java.lang.String ANSI_RED = "\u001B[31m";
    public static final java.lang.String ANSI_GREEN = "\u001B[32m";

    private Lexer lexer; // analisador léxico
    private Token look; // simbolo lookahead
    private int used = 0; // memoria usada para declaracoes
    public static int currentL = 0; //linha atual

    public Parser(java.lang.String filename) throws IOException, Exception {
        this.lexer = new Lexer(filename);
        this.advance();
        this.program();

    }

    /**
     * advance: metodo parmatch(Word.pvg.tag);a permitir mover no arquivo, lendo
     * o proximo token (lookahead)
     *
     * @throws IOException
     * @throws LexerException
     */
    private void advance() throws IOException {
        this.look = this.lexer.scan();

    }

    /**
     * error: metodo para gerar os erros sintaticos
     */
    private void error() {
        System.out.println(ANSI_RED + "ERRO NA LINHA: " + this.lexer.line + "  //////  TOKEN ENCONTRADO: " + this.look + ANSI_RESET); // REMOVER...

    }

    /**
     * match: permite verificar se o lookahead corresponde ao esperado
     */
    private void match(int t) throws IOException {

        if (this.look.tag == t) {

            this.advance();
        } else {
            error();
            this.advance();
        }
    }

    /**
     * program: producao para o simbolo inicial da gramatica -> program
     */
    public void program() throws Exception {
        match(Tag.DECLARE);
        // inicializada
        declList();
        match(Tag.START);
        stmtList();
        match(Tag.END);
        match(Tag.EOF); // se o token apos exit nao for eof, da problema (programa termina com "fim de arquivo")
    }

    /**
     * decl-list
     */
    private void declList() throws Exception {
        decl();
        match(Tag.PVG);
        while (this.look.tag == Tag.ID) {
            decl();
            match(Tag.PVG);
        }
    }

    /**
     * decl
     */
    private void decl() throws Exception {
        identList();
        match(Tag.DP);
        type();

    }

    /**
     * ident-list
     */
    private void identList() throws Exception {
        match(Tag.ID); // eh necessario que se tenha ao menos um id (identifier)

        // pode haver mais identificadores a seguir
        // e eles devem ser precedidos de uma virgula
        while (this.look.tag == Tag.VG) {
            match(Tag.VG);
            match(Tag.ID); // identifier
        }
    }

    /**
     * type
     */
    private void type() throws Exception {
        if (this.look.tag == Tag.BASIC) {

            match(Tag.BASIC);
        }
    }

    /**
     * stmt-list
     */
    private void stmtList() throws Exception {

        do {
            stmt();
            match(Tag.PVG);
        } while ((this.look.tag == Tag.IF) || (this.look.tag == Tag.ID)
                || (this.look.tag == Tag.DO) || (this.look.tag == Tag.READ)
                || (this.look.tag == Tag.WRITE));
    }

    /**
     * stmt
     */
    private void stmt() throws Exception {

        this.currentL = this.lexer.line;
        switch (this.look.tag) {
            case Tag.ID:
                assignStmt();
                break;
            case Tag.IF:
                ifStmt();
                break;
            case Tag.DO:
                doStmt();
                break;
            case Tag.READ:
                readStmt();
                break;
            case Tag.WRITE:
                writeStmt();
                break;
            default:
                error();
        }
    }

    /**
     * assign-stmt
     */
    private void assignStmt() throws Exception {
        match(Tag.ID);
        match(Tag.AT);
        simpleExpr();
    }

    /**
     * if-stmt
     */
    private void ifStmt() throws Exception {
        match(Tag.IF);
        condition();
        match(Tag.THEN);
        stmtList();
        ifStmt2();
    }

    /**
     * if-stmt2
     */
    private void ifStmt2() throws Exception {
        switch (this.look.tag) {
            case Tag.END:
                match(Tag.END);
            case Tag.ELSE:
                match(Tag.ELSE);
                stmtList();
                match(Tag.END);
            default:
                error();
        }
    }

    /**
     * condition
     */
    private void condition() throws Exception {
        expression();
    }

    /**
     * while-stmt
     */
    private void doStmt() throws Exception {
        match(Tag.DO);
        stmtList();
        stmtSuffix();
    }

    /**
     * stmt-suffix
     */
    private void stmtSuffix() throws Exception {
        match(Tag.WHILE);
        condition();
    }

    /**
     * read-stmt
     */
    private void readStmt() throws Exception {
        match(Tag.READ);
        match(Tag.LPAR);
        match(Tag.ID);
        match(Tag.RPAR);
    }

    /**
     * write-stmt
     */
    private void writeStmt() throws Exception {
        match(Tag.WRITE);
        match(Tag.LPAR);
        writable();
        match(Tag.RPAR);
    }

    /**
     * writable
     */
    private void writable() throws Exception {
        simpleExpr();
    }

    /**
     * expression
     */
    private void expression() throws Exception {
        simpleExpr();
        expression2();
    }

    /**
     * expression = simple-expr expression2 expression2 = lamda | relop
     * simple-expr
     *
     */
    private void expression2() throws Exception {
        switch (this.look.tag) {
            case Tag.EQ: //first de relop
            case Tag.GT:
            case Tag.GE:
            case Tag.LT:
            case Tag.LE:
            case Tag.NE:
                relop();
                simpleExpr();
                break;
            default:
                break; //lambda
        }
    }

    /**
     * simple-expr
     */
    public void simpleExpr() throws Exception {
        term();
        simpleExpr2();

    }

    /**
     * simple-expr2 simple-expr ::= term simple-expr2 simple-expr2::= lambda |
     * addop term simple-expr2
     */
    public void simpleExpr2() throws Exception {
        switch (this.look.tag) {
            case Tag.PLUS: //first de relop
            case Tag.MINUS:
            case Tag.OR:
                addop();
                term();
                simpleExpr2();
                break;
            default:
                break; //lambda
        }

    }

    /**
     * term
     */
    public void term() throws Exception {
        factorA();
        term2();
    }

    /**
     * term2 term ::= factor-a term2 term2::= lambda | mulop factor-a term2
     */
    public void term2() throws Exception {
        switch (this.look.tag) {
            case Tag.TIMES:
            case Tag.DIV:
            case Tag.AND:
                mulop();
                factorA();
                term2();
                break;
            default:
                break; //lambda
        }
    }

    /**
     * factor-a
     */
    public void factorA() throws Exception {
        switch (this.look.tag) {
            case Tag.NOT:
                match(Tag.NOT);
                factor();
                break;
            case '-':
                match(Tag.MINUS);
                factor();
            default:
                factor();
                break; //lambda
        }
    }

    /**
     * factor
     */
    public void factor() throws Exception {
        switch (this.look.tag) {
            case Tag.ID:
                match(Tag.ID);
                break;
            case Tag.INT:
            case Tag.STRING:
                constant();
                break;
            case Tag.LPAR:
                match(Tag.LPAR);
                expression();
                match(Tag.RPAR);
                break;
            default:
                error();
        }
    }

    /**
     * relop
     */
    private void relop() throws Exception {
        switch (this.look.tag) {
            case Tag.EQ:
                match(Tag.EQ);
            case Tag.GT:
                match(Tag.GT);
            case Tag.GE:
                match(Tag.GE);
            case Tag.LT:
                match(Tag.LT);
            case Tag.LE:
                match(Tag.LE);
            case Tag.NE:
                match(Tag.NE);
            default:
                error();
        }
    }

    /**
     * addop
     */
    private void addop() throws Exception {
        switch (this.look.tag) {
            case Tag.PLUS:
                match(Tag.PLUS);
                break;
            case Tag.MINUS:
                match(Tag.MINUS);
                break;
            case Tag.OR:
                match(Tag.OR);
                break;
            default:
                error();
        }
    }

    /**
     * mulop
     */
    private void mulop() throws Exception {
        switch (this.look.tag) {
            case Tag.TIMES:
                match(Tag.TIMES);
                break;
            case Tag.DIV:
                match(Tag.DIV);
                break;
            case Tag.AND:
                match(Tag.AND);
                break;
            default:
                error();
        }
    }

    /**
     * constant
     */
    private void constant() throws Exception {
        switch (this.look.tag) {
            case Tag.INT:
                match(Tag.INT);
                break;
            case Tag.STRING:
                match(Tag.STRING);
                break;
            default:
                error();
        }
    }
}
