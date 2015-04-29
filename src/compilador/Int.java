/*
 * Representa um token número
 */

package compilador;


public class Int extends Token {
    public final long value;  // para caso o valor ultrapasse o tamanho de um int Java

    public Int(long v) {
        super(Tag.INT);
        value = v;
    }
    
   
    public java.lang.String toString() { 
        return "" + value;
    }
    
}
