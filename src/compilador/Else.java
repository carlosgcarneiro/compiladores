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
public class Else extends Stmt {
	private ArrayList<Stmt> stmtList = new ArrayList<Stmt>();
	
	public Else(ArrayList<Stmt> stmtList) {
		super();
		this.stmtList = new ArrayList<Stmt>(stmtList);
	}
}
