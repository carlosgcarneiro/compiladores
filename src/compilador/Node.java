
package compilador;
import compilador.Lexer.*;

public class Node {

public int lexline = 0;
static int labels = 0;
	
	public Node() {
		lexline = Lexer.line;
	}
	
	public void error(String s) throws Exception {
		throw new Exception("na linha " + lexline + " "+s);
	}
	
	public int newLabel() {
		return ++labels;
	}
	
	public void emitLabel(int i) {
		System.out.println("L" + i + ":");
	}
	
	public void emit(java.lang.String s) {
		System.out.println("\t" + s);
	}

}