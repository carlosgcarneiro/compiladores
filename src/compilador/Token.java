/*
 /* Representa um token genérico. Contém a constante que representa o token
 */
package compilador;
public class Token {

    public final int tag;

    public Token(int t) {
        tag = t;
    }

    @Override
    public java.lang.String toString() {
        return "" + (char)tag;
    }
}
