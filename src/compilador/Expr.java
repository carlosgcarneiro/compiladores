package compilador;

import static compilador.Parser.ANSI_RED;
import static compilador.Parser.ANSI_RESET;

public class Expr extends Expression {

    public Expression expr1;
    public Expression expr2;

    public Expr(Token relop, Expression e, Expression t) {
        super(relop, e.type); // relop expressions sempre devem retornar
        // um BOOL...
        this.expr1 = e;
        this.expr2 = t;

        if (t != null) {
            check();
        }
    }

    public Expr(Expression e, Expression t) {
        super(t.op, Type.BOOL_TYPE); // relop expressions sempre devem retornar
        // um BOOL...
        this.expr1 = e;
        this.expr2 = t;
        check();
    }

    private void check() {

        if (!isRelop()) {
            if ((!Type.same(this.expr1.type, this.expr2.type, Type.INT_TYPE))
                    && (!Type.same(this.expr1.type, this.expr2.type,
                            Type.STRING_TYPE))
                    && (!Type.same(this.expr1.type, this.expr2.type,
                            Type.BOOL_TYPE))) {
                System.out.println(ANSI_RED + "ERRO NA LINHA: " + Parser.currentL + "  //////  Tipo: Tipos não conferem." + ANSI_RESET);
            }

        } else if ((!Type.same(this.expr1.type, this.expr2.type, Type.INT_TYPE))
                && (!Type.same(this.expr1.type, this.expr2.type,
                        Type.STRING_TYPE))) {
            System.out.println(ANSI_RED + "ERRO NA LINHA: " + Parser.currentL + "  //////  Tipo: Tipos não conferem." + ANSI_RESET);
            // pode existir:
            // erro semantico
        }
    }

    private boolean isRelop() {
        switch (this.op.tag) {
            case Tag.EQ:
            case Tag.GT:
            case Tag.GE:
            case Tag.MINUS:
            case Tag.LE:
            case Tag.NE:
                return true;
            default:
                return false;
        }
    }
}
