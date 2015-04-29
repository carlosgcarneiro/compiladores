

package compilador;

public class Expr extends Node {
	public Token op;
	public Type type;
	
	public Expr(Token op, Type type) {
		super();
		this.op = op;
		this.type = type;
	}
}
