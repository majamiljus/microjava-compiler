// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class ExprMinusUnary extends ExprMinus {

    private ExprMinus ExprMinus;

    public ExprMinusUnary (ExprMinus ExprMinus) {
        this.ExprMinus=ExprMinus;
        if(ExprMinus!=null) ExprMinus.setParent(this);
    }

    public ExprMinus getExprMinus() {
        return ExprMinus;
    }

    public void setExprMinus(ExprMinus ExprMinus) {
        this.ExprMinus=ExprMinus;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprMinus!=null) ExprMinus.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprMinus!=null) ExprMinus.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprMinus!=null) ExprMinus.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprMinusUnary(\n");

        if(ExprMinus!=null)
            buffer.append(ExprMinus.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprMinusUnary]");
        return buffer.toString();
    }
}
