/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import static compilador.Parser.ANSI_RED;
import static compilador.Parser.ANSI_RESET;

/**
 *
 * @author caca
 */
public class SimpleExpr extends Expression {
	public Expression expr1;
	public SimpleExpr expr2;
	
	public SimpleExpr(Token addop, Expression e, SimpleExpr t){
		super(addop, e.type);
                
		this.expr1 = e;
		this.expr2 = t;
		if(t != null) {
			check();
		}
	}
	
	public SimpleExpr(Expression e, SimpleExpr t){
		super(t.op, e.type);
		this.expr1 = e;
		this.expr2 = t;
		check();
	}
	
	private void check(){
		
		if(this.expr2.op.tag == Tag.OR) {
			// se for OR, os operandos so podem ser ambos BOOL...
			if(!Type.same(this.expr1.type, this.expr2.type, Type.BOOL_TYPE)) {
				System.out.println(ANSI_RED + "ERRO NA LINHA: " + Parser.currentL + "  //////  Tipo: Tipos não conferem." + ANSI_RESET); //erro semantico
			}
		} else if(this.expr2.op.tag == '+' && ((this.expr1.type == Type.STRING_TYPE) || (this.expr2.type == Type.STRING_TYPE))) {
			this.type = Type.STRING_TYPE;
		} else {
			// se for * ou / , os operandos devem ser numericos (INT ou FLOAT)...
			if(!Type.same(this.expr1.type, this.expr2.type, Type.INT_TYPE)) {
				System.out.println(ANSI_RED + "ERRO NA LINHA: " + Parser.currentL + "  //////  Tipo: Tipos não conferem." + ANSI_RESET); //erro semantico
			}
		}
		
	}
}
