/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/** 
 * SymbolsTable: classe para a tabela de símbolos.
 * 
 * @author caca
 */
public class SymbolsTable {
	
	private Hashtable table;     // tabela de simbolos
	protected SymbolsTable prev; // tabela do nivel anterior
	
	public SymbolsTable(SymbolsTable prev) {
		this.table = new Hashtable();
		this.prev = prev;
	}
	
	/**
	 * Insere um token na tabela. 
	 * 
	 * @param w: token a ser inserido.
	 * @param i: informação relativa ao token.
	 */
	public void put(Token w, Id i) {
		table.put(w, i);
	}
	
	/** 
	 * Obtém as informações relativas a determinado token da tabela.
	 * @param w : token a ser pesquisado.
	 * @return informações sobre o token.
	 */
	public Id get(Token w) {
		for(SymbolsTable s = this; s != null; s = s.prev) {
			Id found = (Id) s.table.get(w);
			if(found != null) {
				return found;
			}
		}
		
		return null; // se token não estiver presente, retorna null
	}
	
	/**
	 * Imprime a tabela de simbolos
	 */
	public void printST() {
		System.out.println("\n== TABELA DE SÍMBOLOS ==\n");
		Set set = table.entrySet();
	    Iterator it = set.iterator();
	    while (it.hasNext()) {
	      Map.Entry<Token, Id> entry = (Map.Entry<Token, Id>) it.next();
	      System.out.println(entry.getKey() + " : " + entry.getValue().type.getLexeme());
	    }
	}
	
}