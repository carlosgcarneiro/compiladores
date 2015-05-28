/*
 * Classe que define as constantes para o token
 */

package compilador;

public class Tag {
    public final static int
            
            // Palavras reservadas da linguagem
            DECLARE = 256,
            START = 257,
            END = 258,
            INT = 259,
            STRING = 260,
            IF =261,
            THEN = 262,
            ELSE = 263,
            DO = 264,
            WHILE = 265,
            READ = 266,
            WRITE = 267,
               
    
            // Tags para operadores de comparação
            EQ = 280, // ==
            GT = 281, // >
            GE = 282, // >=
            LT = 283, // <
	    LE = 284, // <=
	    NE = 285, // <>
            
            // Atribuição 
            AT = 286, // =
            
            // Delimitadores
            PVG = 287, //;
            VG = 288, // ,
            DP = 289, // :
                
            LPAR = 290, // (
	    RPAR = 291, // )
            LIT = 292, // “
	    //LITR = 293, // }
          
            SCOM = 294, // \\
            ICOM = 295, // /*
            FCOM = 296, // */
            
            //Operadores
	    PLUS = 297, // +
	    MINUS = 298,// -
	    TIMES = 299,// *
	    DIV = 300, // /
            OR = 301, // or e OR
            AND = 302, // and e AND
            NOT = 303,
    
            BASIC = 310,
            ID = 311,
            EOF = 999,
    
            CH = 400;
}
