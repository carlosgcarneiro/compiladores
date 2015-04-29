/*
 * subclasse de Word para os tipos básicos int e string. Todos eles tem o campo herdado tag definido como 
 * Tag.BASIC, de modo que o analisador sintático os trata da mesma forma.
 */

package compilador;

public class Type extends Word{
	
        //public int width = 0;
	public Type(java.lang.String s, int tag) {
		super(s, tag);
         
	}
	
	public static final Type
		INT_TYPE = new Type("int", Tag.BASIC),
		STRING_TYPE = new Type("string", Tag.BASIC);
	
        // Função para conversão de tipo
	public static boolean numeric(Type p) {
		if (p == Type.STRING_TYPE || p == Type.INT_TYPE ) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Type max(Type p1, Type p2) {
		if(!numeric(p1) || !numeric(p2)) {
			return null;
		} else if(p1 == Type.INT_TYPE || p2 == Type.INT_TYPE) {
			return Type.INT_TYPE;
		} else return Type.STRING_TYPE;
	}

}
   
