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


public class Condition extends Expression {

	public Condition(Expression e) {
		super(e.op, e.type);
		
		if(e.type != Type.BOOL_TYPE) {
			                 System.out.println("Linha "+Parser.currentL+"Tipo: Tipos não conferem."); // caso negativo, nao pode existir: erro semantico
		}
	}
}