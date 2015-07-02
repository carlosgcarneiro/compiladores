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
public class ReadStmt extends Stmt {
	private Id identifier;
	
	public ReadStmt(Id id) {
		super();
		this.identifier = id;
		
		if(this.identifier == null) {
			System.out.println(ANSI_RED + "ERRO NA LINHA: " + Parser.currentL + "  //////  variável não foi declarada" + ANSI_RESET);
		}
	}
}
