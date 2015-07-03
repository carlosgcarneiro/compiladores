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

    private SymbolsTable top = null;

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
        System.out.println(ANSI_RED + "ERRO NA LINHA: " + this.lexer.line + "  //////  TOKEN ENCONTRADO: " + this.look + ANSI_RESET);

    }

    private void must_be_unique_error(Word w) {

        System.out.println(ANSI_RED + "ERRO NA LINHA: " + this.lexer.line + "  //////  TOKEN ENCONTRADO: " + w.getLexeme() + " deve ser unico" + ANSI_RESET);

    }

    private void didnt_declare_error(Token t) {

        System.out.println(ANSI_RED + "ERRO NA LINHA: " + this.lexer.line + "  //////  variável não foi declarada" + ANSI_RESET);

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
        SymbolsTable savedTable = this.top; //tinhamos feito com contxtualizacao de variavel, para nao alterar muito mantivemos, entao savedTable=this.top
        this.top = new SymbolsTable();
        declList();
        match(Tag.START);
        stmtList();
        this.top = savedTable; //retornando o contexto anterior à TS
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
        ArrayList<Word> ids = identList();
        match(Tag.DP);

        Type t = type();
        // adiciona-se os ids à tabela de simbolos, junto a seu respectivo tipo
        for (Word w : ids) {
            // verificando se o identificador é único...	
            if (this.top.get(w) != null) {
                must_be_unique_error(w);
            } else {
                Id id = new Id(w, t, this.used);
                this.top.put(w, id);
            }
        }

    }

    /**
     * ident-list
     */
    private ArrayList<Word> identList() throws Exception {
        ArrayList<Word> ids = new ArrayList<Word>();
        if (this.look.tag == Tag.ID) { //se a tag a ser lida for um ID, adicionar ao array
            ids.add((Word) this.look);
        }
        match(Tag.ID); // eh necessario que se tenha ao menos um id (identifier)

        // pode haver mais identificadores a seguir
        // e eles devem ser precedidos de uma virgula
        while (this.look.tag == Tag.VG) {
            match(Tag.VG);
            if (this.look.tag == Tag.ID) {
                ids.add((Word) this.look);
            }
            match(Tag.ID); // identifier
        }
        return ids;
    }

    /**
     * type
     */
    private Type type() throws Exception {
        Type t = null;
        try {
            t = (Type) look;
        } catch (java.lang.ClassCastException cc) {
            t = new Type(look + "", look.tag);
        }
        //if (this.look.tag == Tag.BASIC) {
        match(Tag.BASIC);
        return t;
        //}
    }

    /**
     * stmt-list
     */
    private ArrayList<Stmt> stmtList() throws Exception {

        ArrayList<Stmt> stmts = new ArrayList<Stmt>();

        do {
            Stmt s = stmt();
            stmts.add(s);
            match(Tag.PVG);
        } while ((this.look.tag == Tag.IF) || (this.look.tag == Tag.ID)
                || (this.look.tag == Tag.DO) || (this.look.tag == Tag.READ)
                || (this.look.tag == Tag.WRITE));
        return stmts;
    }

    /**
     * stmt
     */
    private Stmt stmt() throws Exception {

        this.currentL = this.lexer.line;
        switch (this.look.tag) {
            case Tag.ID:
                return assignStmt();
            case Tag.IF:
                return ifStmt();
            case Tag.DO:
                return doStmt();
            case Tag.READ:
                return readStmt();
            case Tag.WRITE:
                return writeStmt();
            default:
                error();
                return null;
        }
    }

    /**
     * assign-stmt
     */
    private AssignStmt assignStmt() throws Exception {
        Id identifier = this.top.get(this.look);
        match(Tag.ID);
        match(Tag.AT);
        Expression e = simpleExpr();
        return new AssignStmt(identifier, e);
    }

    /**
     * if-stmt
     */
    private IfStmt ifStmt() throws Exception {
        match(Tag.IF);
        Condition c = condition();
        match(Tag.THEN);
        ArrayList<Stmt> stmts = stmtList();
        Stmt s = ifStmt2();

        if (s == null) {
            return new IfStmt(c, stmts);
        } else {
            return new IfElseStmt(c, stmts, (Else) s);
        }

    }

    /**
     * if-stmt2
     */
    private Stmt ifStmt2() throws Exception {
        switch (this.look.tag) {
            case Tag.END:
                match(Tag.END);
                return null;
            case Tag.ELSE:
                match(Tag.ELSE);
                ArrayList<Stmt> stmts = stmtList();
                match(Tag.END);
                return new Else(stmts);
            default:
                error();
                return null;
        }
    }

    /**
     * condition
     */
    private Condition condition() throws Exception {
        Expression e = expression();
        return new Condition(e);
    }

    /**
     * while-stmt
     */
    private DoStmt doStmt() throws Exception {
        match(Tag.DO);
        ArrayList<Stmt> stmts = stmtList();
        StmtSuffix ss = stmtSuffix();
        return new DoStmt(stmts, ss);
    }

    /**
     * stmt-suffix
     */
    private StmtSuffix stmtSuffix() throws Exception {
        match(Tag.WHILE);
        Condition c = condition();
        match(Tag.END);
        return new StmtSuffix(c);
    }

    /**
     * read-stmt
     */
    private ReadStmt readStmt() throws Exception {
        match(Tag.READ);
        match(Tag.LPAR);
        Id id = this.top.get(this.look);
        match(Tag.ID);
        match(Tag.RPAR);
        return new ReadStmt(id);
    }

    /**
     * write-stmt
     */
    private WriteStmt writeStmt() throws Exception {
        match(Tag.WRITE);
        match(Tag.LPAR);
        Expression w = writable();
        match(Tag.RPAR);
        return new WriteStmt(w);
    }

    /**
     * writable
     */
    private Expression writable() throws Exception {
        return simpleExpr();
    }

    /**
     * expression
     */
    private Expression expression() throws Exception {
        Expression e1 = simpleExpr();
        Expression e2 = expression2();
        if (e2 == null) {
            return e1;
        } else {
            return new Expr(e1, e2);
        }
    }

    /**
     * expression = simple-expr expression2 expression2 = lamda | relop
     * simple-expr
     *
     */
    private Expression expression2() throws Exception {
        switch (this.look.tag) {
            case Tag.EQ: //first de relop
            case Tag.GT:
            case Tag.GE:
            case Tag.LT:
            case Tag.LE:
            case Tag.NE:
                Token op = relop();
                Expression s = simpleExpr();
                return new Expr(op, s, null);
            case Tag.THEN:
            case Tag.END:
            case Tag.RPAR:
                return null;
            default:
                return null; //lambda                
        }
    }

    /**
     * simple-expr
     */
    public Expression simpleExpr() throws Exception {
        Expression e1 = term();
        SimpleExpr e2 = simpleExpr2();
        if (e2 == null) {
            return e1;
        } else {
            return new SimpleExpr(e1, e2);
        }
    }

    /**
     * simple-expr2 simple-expr ::= term simple-expr2 simple-expr2::= lambda |
     * addop term simple-expr2
     */
    public SimpleExpr simpleExpr2() throws Exception {
        switch (this.look.tag) {
            case Tag.PVG:
            case Tag.THEN:
            case Tag.END:
            case Tag.RPAR:
            case Tag.EQ:
            case Tag.GT:
            case Tag.GE:
            case Tag.LT:
            case Tag.LE:
            case Tag.NE:
                return null;
            case Tag.MINUS:
            case Tag.PLUS:
            case Tag.OR:
                Token op = addop();
                Expression t = term();
                if (this.look.tag == 311 && top.get(this.look) == null) {
                    match(this.look.tag);
                    return null;
                }
                SimpleExpr s = simpleExpr2();
                return new SimpleExpr(op, t, s);
            default:
                error();
                return null; //lambda
        }

    }

    /**
     * term
     */
    public Expression term() throws Exception {
        Expression e1 = factorA();

        TermTemp e2 = term2();
        if (e2 == null) {
            return e1;
        } else {
            return new Term(e1, e2);
        }
    }

    /**
     * term2 term ::= factor-a term2 term2::= lambda | mulop factor-a term2
     */
    public TermTemp term2() throws Exception {
        switch (this.look.tag) {
            case Tag.TIMES:
            case Tag.DIV:
            case Tag.AND:
                Token op = mulop();
                if (this.look.tag == 311 && top.get(this.look) == null) {
                    match(this.look.tag);
                    return null;
                }
                    Expression e = factorA();
                
                TermTemp t = term2();
                return new TermTemp(op, e, t);

            default:
                return null; //lambda
        }
    }

    /**
     * factor-a
     */
    public Expression factorA() throws Exception {
        switch (this.look.tag) {
            case Tag.NOT:
                match(Tag.NOT);
                Expression e1 = factor();
                return new NotFactor(e1, this.currentL);
            case Tag.MINUS:
                match(Tag.MINUS);
                Expression e2 = factor();
                return new NegFactor(e2, this.currentL);
            case Tag.ID:
            case Tag.LPAR:
            case Tag.INT:
            case Tag.STRING:
                Expression e3 = factor();
                return e3;
            default:
                error();
                return null;
        }
    }

    /**
     * factor
     */
    public Expression factor() throws Exception {
        switch (this.look.tag) {
            case Tag.ID:

                Id id = this.top.get(this.look);
                match(Tag.ID);
                if (id == null) {
                    didnt_declare_error(this.look); // caso id nao exista, erro semantico
                }
                return id;
            case Tag.INT:
            case Tag.STRING:
                Constant c = constant();
                return c;
            case Tag.LPAR:
                match(Tag.LPAR);
                Expression e = expression();
                match(Tag.RPAR);
                return e;

            default:
                error();
                return null;
        }
    }

    /**
     * relop
     */
    private Token relop() throws Exception {
        Word op = (Word) this.look;
        switch (this.look.tag) {
            case Tag.EQ:
                match(Tag.EQ);
                return op;
            case Tag.GT:
                match(Tag.GT);
                return op;
            case Tag.GE:
                match(Tag.GE);
                return op;
            case Tag.LT:
                match(Tag.LT);
                return op;
            case Tag.LE:
                match(Tag.LE);
                return op;
            case Tag.NE:
                match(Tag.NE);
                return op;
            default:
                error();
                return null;
        }
    }

    /**
     * addop
     */
    private Token addop() throws Exception {
        Word op = (Word) this.look;
        switch (this.look.tag) {
            case Tag.PLUS:
                match(Tag.PLUS);
                return op;
            case Tag.MINUS:
                match(Tag.MINUS);
                return op;
            case Tag.OR:
                match(Tag.OR);
                return op;
            default:
                error();
                return null;
        }
    }

    /**
     * mulop
     */
    private Token mulop() throws Exception {
        Word op = (Word) this.look;
        switch (this.look.tag) {
            case Tag.TIMES:
                match(Tag.TIMES);
                return op;
            case Tag.DIV:
                match(Tag.DIV);
                return op;
            case Tag.AND:
                match(Tag.AND);
                return op;
            default:
                error();
                return null;
        }
    }

    /**
     * constant
     */
    private Constant constant() throws Exception {
        switch (this.look.tag) {
            case Tag.INT:
                int a = 0;
                try{
                    a = Integer.parseInt(this.look.toString());
                }catch(NumberFormatException nfe){
                }
                IntegerConst i = new IntegerConst(a);
                Constant intc = new Constant(i);
                match(Tag.INT);
                return intc;
            case Tag.STRING:
                Word w = (Word) this.look;
                Constant literal = new Constant(w);
                match(Tag.STRING);
                return literal;
            default:
                error();
                return null;
        }
    }
}
