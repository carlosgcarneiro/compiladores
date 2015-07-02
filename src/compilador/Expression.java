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


/**
 * Expression: classe que representa uma expressão.
 */
public class Expression extends Node {
	public Token op;
	public Type type;
	
	public Expression(Token op, Type type) {
		super();
		this.op = op;
		this.type = type;
	}
	
	public Expression gen() {
		return this;
	}
	
	public Expression reduce() {
		return this;
	}
	
	public void jumping(int t, int f) {
		this.emitJumps(toString(), t, f);
	}
	
	public void emitJumps(java.lang.String test, int t, int f) {
		if (t != 0 && f != 0) {
			emit("if "+ test + " goto L" + t);
			emit("goto L" + f);
		} else if (t != 0) {
			emit("if " + test + " goto L" + t);
		} else if (f != 0) {
			emit("iffalse " + test + " goto L" + f);
		}
	}
	
	public java.lang.String toString() {
		return this.op.toString();
	}
}