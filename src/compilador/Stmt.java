/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 * Stmt: classe que representa um comando.
 * @author caca
 */

public class Stmt extends Node {

	public Stmt() {}
	
	public static Stmt Null = new Stmt();
	
	public void gen(int b, int a) {}
	
	int after = 0;
	
	public static Stmt Enclosing = Stmt.Null;
	
}
