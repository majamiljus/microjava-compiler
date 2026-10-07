// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class ExprBracketOptionalOne extends ExprBracketOptional {

    private ExprBracket ExprBracket;

    public ExprBracketOptionalOne (ExprBracket ExprBracket) {
        this.ExprBracket=ExprBracket;
        if(ExprBracket!=null) ExprBracket.setParent(this);
    }

    public ExprBracket getExprBracket() {
        return ExprBracket;
    }

    public void setExprBracket(ExprBracket ExprBracket) {
        this.ExprBracket=ExprBracket;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprBracket!=null) ExprBracket.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprBracket!=null) ExprBracket.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprBracket!=null) ExprBracket.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprBracketOptionalOne(\n");

        if(ExprBracket!=null)
            buffer.append(ExprBracket.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprBracketOptionalOne]");
        return buffer.toString();
    }
}
