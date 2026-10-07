// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class FactorNew extends Factor {

    private Type Type;
    private ExprBracketOptional ExprBracketOptional;

    public FactorNew (Type Type, ExprBracketOptional ExprBracketOptional) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ExprBracketOptional=ExprBracketOptional;
        if(ExprBracketOptional!=null) ExprBracketOptional.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ExprBracketOptional getExprBracketOptional() {
        return ExprBracketOptional;
    }

    public void setExprBracketOptional(ExprBracketOptional ExprBracketOptional) {
        this.ExprBracketOptional=ExprBracketOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ExprBracketOptional!=null) ExprBracketOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ExprBracketOptional!=null) ExprBracketOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ExprBracketOptional!=null) ExprBracketOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorNew(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprBracketOptional!=null)
            buffer.append(ExprBracketOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNew]");
        return buffer.toString();
    }
}
