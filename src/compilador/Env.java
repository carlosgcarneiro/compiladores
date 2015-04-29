/*
 * Mapeia tokens de palavra a objetos da classe Id que é definida no pacote inter com 
 * as classes para expressões e comandos
*/

package compilador;

import java.util.*;

public class Env {
	
    private Hashtable table;     // tabela de simbolos
    protected Env prev; // tabela do nivel anterior

    public Env(Env n) {
            table = new Hashtable();
            prev = n;
    }
 
    public void put(Token w, Id i) {
            table.put(w, i);
    }

    public Id get(Token w) {
            for(Env s = this; s != null; s = s.prev) {
                    Id found = (Id) s.table.get(w);
                    if(found != null) {
                            return found;
                    }
            }
            return null; // se token não estiver presente, retorna null
    }

  	public void printEnv() {// Imprime a tabela de símbolos
            System.out.println("\n TABELA DE SÍMBOLOS \n");
            Set set = table.entrySet();
	    Iterator it = set.iterator();
	    while (it.hasNext()) {
	      Map.Entry<Token, Id> entry = (Map.Entry<Token, Id>) it.next();
	      System.out.println(entry.getKey() + " : " + entry.getValue().type.getLexeme());
	    }
	}  
}

