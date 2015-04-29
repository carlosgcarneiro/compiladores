/*
 * Representa um token de palavras reservadas, identificadores e tokens compostos como: && e !=. Também é
 * útil para gerenciar a forma escrita dos operadores no código intermediário. Exemplo: -2 = minus 2
 */

package compilador;

public class Word extends Token {
    
    private java.lang.String lexeme = "";
    
    public Word(java.lang.String s, int tag) {
		super(tag); 
		lexeme = s;
    }
	public java.lang.String getLexeme() {
		return lexeme;
	}
    public java.lang.String toString() { 
        return lexeme;
    }
	public static final Word  
            //operadores de comparação
	    eq = new Word("==", Tag.EQ),
            gt = new Word(">", Tag.GT),
            ge = new Word(">=",Tag.GE),
            lt = new Word("<", Tag.LT),
	    le = new Word("<=", Tag.LE),
	    ne = new Word("<>", Tag.NE),
            
            // Atribuição
            at = new Word("=", Tag.AT),
            // Delimitadores
            pvg = new Word(";", Tag.PVG),
            vg = new Word(",", Tag.VG),
            dp = new Word(":", Tag.DP),
                
            left_par = new Word("(", Tag.LPAR),
	    right_par = new Word(")", Tag.RPAR),
            literal = new Word ("“", Tag.LIT),
            //left_key = new Word("{",Tag.LKEY),
	    //right_key = new Word("}", Tag.RKEY),
                
            scom = new Word("\\", Tag.SCOM),
            icom = new Word("/*", Tag.ICOM),
            fcom = new Word("*/", Tag.FCOM),
            
            //Operadores
	    plus = new Word("+", Tag.PLUS),
	    minus = new Word("-", Tag.MINUS),
	    times = new Word("*", Tag.TIMES),
	    div = new Word("/", Tag.DIV),
            and = new Word("and", Tag.AND),
            or = new Word("or", Tag.OR);
            
            
}
