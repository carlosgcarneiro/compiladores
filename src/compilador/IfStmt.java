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

import java.util.ArrayList;

public class IfStmt extends Stmt {
	private Condition condition;
	private ArrayList<Stmt> stmtList = new ArrayList<Stmt>();
	
	public IfStmt(Condition c, ArrayList<Stmt> a) {
		this.condition = c;
		this.stmtList = new ArrayList<Stmt>(a);
	}

}