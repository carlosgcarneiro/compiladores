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
public class StmtSuffix extends Stmt {
	private Condition c;
	
	public StmtSuffix(Condition c) {
		this.c = c;
	}
}
