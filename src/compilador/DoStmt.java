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

public class DoStmt extends Stmt {
	private ArrayList<Stmt> stmtList;
	private StmtSuffix stmtSuffix;
	
	public DoStmt(ArrayList<Stmt> stmts, StmtSuffix ss) {
		super();
		this.stmtList = new ArrayList<Stmt>(stmts);
		this.stmtSuffix = ss;
	}
}    

