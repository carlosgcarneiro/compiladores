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
public class IntegerConst extends Token {
	
	public final int value;	// valor do numero

	public IntegerConst(int value) {
		super(Tag.INT);
		this.value = value;
	}

	public java.lang.String toString() {
		return "" + value;
	}

}
