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
public class NegFactor extends Expression {
	
	public NegFactor(Expression e, int l){
		super(null, null);
		if((e.type == Type.INT_TYPE)) {
			this.op = e.op;
			this.type = e.type;
		} else {
			System.out.println(ANSI_RED + "ERRO NA LINHA: " + l + "  //////  Tipo: Tipos não conferem." + ANSI_RESET); //erro semantico
		}
	}

}
