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

public class TermTemp extends Expression {
	public Expression expr1;
	public TermTemp term2;
	
	public TermTemp(Token mulop, Expression e, TermTemp t){
		super(mulop, e.type);
		this.expr1 = e;
		this.term2 = t;
		
		if(t != null) {
			check();
		}
	}
	
	private void check(){
		
		if(this.term2.op.tag == Tag.AND) {
			// se for AND, os operandos so podem ser ambos BOOL...
			if(!Type.same(this.expr1.type, this.term2.type, Type.BOOL_TYPE)) {
				System.out.println(ANSI_RED + "ERRO NA LINHA: " + Parser.currentL + "  //////  Tipo: Tipos não conferem." + ANSI_RESET); //erro semantico
			}
		} else {
			// se for * ou / , os operandos devem ser numericos (INT ou FLOAT)...
			if(!Type.same(this.expr1.type, this.term2.type, Type.INT_TYPE)) {
				System.out.println(ANSI_RED + "ERRO NA LINHA: " + Parser.currentL + "  //////  Tipo: Tipos não conferem." + ANSI_RESET); //erro semantico // caso negativo, nao pode existir: erro semantico
			}
		}
		
	}

}
