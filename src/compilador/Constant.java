/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author caca
 */
public class Constant extends Expression {
	
	public Constant(Token token, Type p) {
		super(token, p);
	}
	
	public Constant(IntegerConst i) {
		super(i, Type.INT_TYPE);
	}
	
	public Constant(Word w) {
		super(w, Type.STRING_TYPE);
	}

}
