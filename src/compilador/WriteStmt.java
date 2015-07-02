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
public class WriteStmt extends Stmt {
	private Expression e;
	
	public WriteStmt(Expression expr) {
		super();
		this.e = expr;
	}
	
}