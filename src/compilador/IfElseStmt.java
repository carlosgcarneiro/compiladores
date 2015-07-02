/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;

/**
 *
 * @author caca
 */
public class IfElseStmt extends IfStmt {
	private Else elseStmt;
	
	public IfElseStmt(Condition c, ArrayList<Stmt> a, Else e) {
		super(c, a);
		this.elseStmt = e;
	}
}
