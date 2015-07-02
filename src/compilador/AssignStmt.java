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
public class AssignStmt extends Stmt {
	private Id identifier;
	private Expression expr;
	
	public AssignStmt(Id id, Expression e) throws Exception {
		super();
		this.identifier = id;
		this.expr = e;
		if(this.identifier == null) {
			System.out.println(ANSI_RED + "ERRO NA LINHA: " + Parser.currentL + "  //////  variável não foi declarada" + ANSI_RESET);
		} else {
			if(this.identifier.type != expr.type) {
				System.out.println(ANSI_RED + "ERRO NA LINHA: " + Parser.currentL + "  //////  Tipo: Tipos não conferem." + ANSI_RESET);
			}
		}
	}

}
