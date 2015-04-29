/*
 * Representa um token String
 */

package compilador;

public class String extends Token {
    public final String value;

    public String(String v) {
        super(Tag.STRING);
        value = v;
    }
}
