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
    private Lexer lexer; // analisador léxico
	private Token look; // simbolo lookahead
	private Env top = null; // tabela de simbolos atual
	private int used = 0; // memoria usada para declaracoes
	public static int currentL = 0; //linha atual
    
        
        public Parser(java.lang.String filename) throws IOException {
		this.lexer = new Lexer(filename);
		this.advance();
	}
        
        
        /**
	 * advance: metodo para permitir mover no arquivo, lendo o proximo token
 (lookahead)
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
	private void error()    {
		//System.out.println("ERRO AQUI: " + this.look.tag + " L" + this.lexer.line); // REMOVER...
		throw new ParserException(this.lexer.line);
	}

	/**
	 * match: permite verificar se o lookahead corresponde ao esperado
	 */
	private void match(int t) throws IOException {
		if (this.look.tag == t) {
			//System.out.println("" + this.look.tag + " L" + this.lexer.line); // REMOVER...
			this.advance();
		} else {
			error(); 
		}
	}
    
    /**
	 * program: producao para o simbolo inicial da gramatica -> program
	 */
    
    
	public void program() throws Exception {
                match(Tag.DECLARE);
                Env savedTable = this.top;
		this.top = new Env(this.top); // tabela de simbolos eh
												// inicializada
                declList();
		match(Tag.START);
		stmtList();

		this.top = savedTable; // retorna à tabela de simbolos do contexto
								// anterior

		match(Tag.END);
		match(Tag.EOF); // se o token apos exit nao for eof, da problema (programa termina com "fim de arquivo")
	}

	/**
	 * decl-list 
	 */
	private void declList() throws Exception {
		while (this.look.tag == Tag.BASIC) {
			decl();
		}
	}

	/**
	 * decl
	 */
	private void decl() throws Exception {
		Type p = type();
		ArrayList<Word> ids = identList();
		int currentLine = this.lexer.line;
		match(Word.pvg.tag);

		// adiciona-se os ids à tabela de simbolos, junto a seu respectivo tipo
		for (Word w : ids) {
			// verificacao de unicidade...	
			if(this.top.get(w) != null) {
				throw new UniqueVarException(currentLine);
			} else {
				Id id = new Id(w, p, this.used);
				this.top.put(w, id);
			}
		}
	}

	/**
	 * ident-list
	 */
	private ArrayList<Word> identList() throws Exception {
		ArrayList<Word> ids = new ArrayList<Word>();

		if (this.look.tag == Tag.ID) {
			ids.add((Word) this.look);
		}
		match(Tag.ID); // eh necessario que se tenha ao menos um id (identifier)

		// pode haver mais identificadores a seguir
		// e eles devem ser precedidos de uma virgula
		while (this.look.tag == Word.vg.tag) {
			match(Word.vg.tag); // ,
			if (this.look.tag == Tag.ID) {
				ids.add((Word) this.look);
			}
			match(Tag.ID); // identifier
		}

		return ids; // retorna a lista de id's
	}

	/**
	 * type 
	 */
	private Type type() throws Exception {
		Type p = (Type) look;
		match(Tag.BASIC);
		return p;
	}

	/**
	 * stmt-list
	 */
	private ArrayList<Stmt> stmtList() throws Exception {
		ArrayList<Stmt> stmts = new ArrayList<Stmt>();
		
		do {
			Stmt s = stmt();
			stmts.add(s);
		} while ((this.look.tag == Tag.IF) || (this.look.tag == Tag.DO)
				|| (this.look.tag == Tag.SCAN) || (this.look.tag == Tag.PRINT)
				|| (this.look.tag == Tag.ID));
		
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
			return whileStmt();
		case Tag.SCAN:
			return readStmt();
		case Tag.PRINT:
			return writeStmt();
		default:
			this.error();
			return null;
		}
	}

	/**
	 * assign-stmt 
	 */
	private AssignStmt assignStmt() throws Exception {
		Id identifier = this.top.get(this.look);
		match(Tag.ID);
		match('=');
		Expression e = simpleExpr();
		match(';');
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
		
		if(s == null) {
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
			this.error();
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
	private WhileStmt whileStmt() throws Exception {
		match(Tag.DO);
		ArrayList<Stmt> stmts = stmtList();
		StmtSuffix ss = stmtSuffix();
		return new WhileStmt(stmts, ss);
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
	private ReadStmt readStmt() throws Exception  {
		match(Tag.SCAN);
		match(Word.left_par.tag);
		Id id = this.top.get(this.look);
		match(Tag.ID);
		match(Word.right_par.tag);
		match(';');
		return new ReadStmt(id);
	}

	/**
	 * write-stmt
	 */
	private WriteStmt writeStmt() throws Exception {
		match(Tag.PRINT);
		match(Word.left_par.tag);
		Expression w = writable();
		match(Word.right_par.tag);
		match(';');
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
		
		if(e2 == null) {
			return e1;
		} else {
			return new Expr(e1, e2);
		}
	}

	/**
	 * expression2
	 */
	private Expression expression2() throws Exception {
		switch (this.look.tag) {
		case Tag.EE:
		case '>':
		case Tag.GE:
		case '<':
		case Tag.LE:
		case Tag.NE:
			Token op = relop();
			Expression s = simpleExpr();
			return new Expr(op, s, null);
		case Tag.THEN:
		case Tag.END:
		case ')':
			return null;
		default:
			this.error();
			return null;
		}
	}

	/**
	 * simple-expr
	 */
	public Expression simpleExpr() throws Exception {
		Expression e1 = term();
		SimpleExpr e2 = simpleExpr2();
		
		if(e2 == null) {
			return e1;
		} else {
			return new SimpleExpr(e1,e2);
		}
	}

	/**
	 * simple-expr2 
	 */
	public SimpleExpr simpleExpr2() throws Exception {
		switch (this.look.tag) {
		case ';':
		case Tag.THEN:
		case Tag.END:
		case ')':
		case Tag.EE:
		case '>':
		case Tag.GE:
		case '<':
		case Tag.LE:
		case Tag.NE:
			return null;
		case '-':
		case '+':
		case Tag.OR:
			Token op = addop();
			Expression t = term();
			SimpleExpr s = simpleExpr2();
			return new SimpleExpr(op, t, s);
		default:
			this.error();
			return null;
		}
	}

	/**
	 * term
	 */
	public Expression term() throws Exception {
		Expression e1 = factorA();
		TermTemp e2 = term2();
		
		if(e2 == null) {
			return e1;
		} else {
			return new Term(e1,e2);
		}
	}

	/**
	 * term2
	 */
	public TermTemp term2() throws Exception {
		switch (this.look.tag) {
		case '*':
		case '/':
		case Tag.AND:
			Token op = mulop();
			Expression e = factorA();
			TermTemp t = term2();
			return new TermTemp(op, e, t);
		case ';':
		case Tag.THEN:
		case Tag.END:
		case ')':
		case '-':
		case '+':
		case Tag.EE:
		case '>':
		case Tag.GE:
		case '<':
		case Tag.LE:
		case Tag.NE:
		case Tag.OR:
			return null;
		default:
			this.error();
			return null;
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
		case '-':
			match(Word.minus.tag);
			Expression e2 = factor();
			return new NegFactor(e2, this.currentL);
		case Tag.ID:
		case '(':
		case Tag.INT:
		case Tag.FLOAT:
		case Tag.STRING:	 
			Expression e3 = factor();
			return e3;
		default:
			this.error();
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
			if(id == null) {
				throw new NotExistentIdErrorException(this.currentL); // caso id nao exista, erro semantico
			}
			return id;
		case '(':
			match(Word.left_par.tag);
			Expression e = expression();
			match(Word.right_par.tag);
			return e;
		case Tag.INT:
		case Tag.FLOAT:
		case Tag.STRING:
			Constant c = constant();
			return c;
		default:
			this.error();
			return null;
		}
	}

	/**
	 * relop
	 */
	private Token relop() throws Exception {
		switch (this.look.tag) {
		case Tag.EE:
			Word eetok = (Word) this.look;
			match(Tag.EE);
			return eetok;
		case '>':
			Word gttok = (Word) this.look;
			match('>');
			return gttok;
		case Tag.GE:
			Word getok = (Word) this.look;
			match(Tag.GE);
			return getok;
		case '<':
			Word lttok = (Word) this.look;
			match('<');
			return lttok;
		case Tag.LE:
			Word letok = (Word) this.look;
			match(Tag.LE);
			return letok;
		case Tag.NE:
			Word netok = (Word) this.look;
			match(Tag.NE);
			return netok;
		default:
			this.error();
			return null;
		}
	}

	/**
	 * addop
	 */
	private Token addop() throws Exception {
		switch (this.look.tag) {
		case '+':
			Word plstok = (Word) this.look;
			match('+');
			return plstok;
		case '-':
			Word mntok = (Word) this.look;
			match('-');
			return mntok;
		case Tag.OR:
			Word ortok = (Word) this.look;
			match(Tag.OR);
			return ortok;
		default:
			this.error();
			return null;
		}
	}

	/**
	 * mulop
	 */
	private Token mulop() throws Exception {
		switch (this.look.tag) {
		case '*':
			Word tmstok = (Word) this.look;
			match('*');
			return tmstok;
		case '/':
			Word divtok = (Word) this.look;
			match('/');
			return divtok;
		case Tag.AND:
			Word andtok = (Word) this.look;
			match(Tag.AND);
			return andtok;
		default:
			this.error();
			return null;
		}
	}

	/**
	 * constant
	 */
	private Constant constant() throws Exception {
		switch (this.look.tag) {
		case Tag.INT:
			IntegerConst i = (IntegerConst) this.look;
			Constant intc = new Constant(i);
			match(Tag.INT);
			return intc;
		case Tag.FLOAT:
			FloatConst f = (FloatConst) this.look;
			Constant floatc = new Constant(f);
			match(Tag.FLOAT);
			return floatc;
		case Tag.STRING:
			Word w = (Word) this.look;
			Constant literal = new Constant(w);
			match(Tag.STRING);
			return literal;
		default:
			this.error();
			return null;
		}
	}
}