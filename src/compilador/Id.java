/*
 * Será completada futuramente
*/

package compilador;

public class Id extends Expression{
    public int offset;
    
	public Id(Word id, Type p, int offset) {
		super(id, p);
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
