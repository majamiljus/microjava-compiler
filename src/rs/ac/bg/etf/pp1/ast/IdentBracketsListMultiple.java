// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class IdentBracketsListMultiple extends IdentBracketsList {

    private IdentBracketsList IdentBracketsList;
    private IdentBrackets IdentBrackets;

    public IdentBracketsListMultiple (IdentBracketsList IdentBracketsList, IdentBrackets IdentBrackets) {
        this.IdentBracketsList=IdentBracketsList;
        if(IdentBracketsList!=null) IdentBracketsList.setParent(this);
        this.IdentBrackets=IdentBrackets;
        if(IdentBrackets!=null) IdentBrackets.setParent(this);
    }

    public IdentBracketsList getIdentBracketsList() {
        return IdentBracketsList;
    }

    public void setIdentBracketsList(IdentBracketsList IdentBracketsList) {
        this.IdentBracketsList=IdentBracketsList;
    }

    public IdentBrackets getIdentBrackets() {
        return IdentBrackets;
    }

    public void setIdentBrackets(IdentBrackets IdentBrackets) {
        this.IdentBrackets=IdentBrackets;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IdentBracketsList!=null) IdentBracketsList.accept(visitor);
        if(IdentBrackets!=null) IdentBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IdentBracketsList!=null) IdentBracketsList.traverseTopDown(visitor);
        if(IdentBrackets!=null) IdentBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IdentBracketsList!=null) IdentBracketsList.traverseBottomUp(visitor);
        if(IdentBrackets!=null) IdentBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IdentBracketsListMultiple(\n");

        if(IdentBracketsList!=null)
            buffer.append(IdentBracketsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IdentBrackets!=null)
            buffer.append(IdentBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IdentBracketsListMultiple]");
        return buffer.toString();
    }
}
