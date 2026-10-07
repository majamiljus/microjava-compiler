// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class AssignLiteralsListMultiple extends AssignLiteralsList {

    private AssignLiteralsList AssignLiteralsList;
    private AssignLiterals AssignLiterals;

    public AssignLiteralsListMultiple (AssignLiteralsList AssignLiteralsList, AssignLiterals AssignLiterals) {
        this.AssignLiteralsList=AssignLiteralsList;
        if(AssignLiteralsList!=null) AssignLiteralsList.setParent(this);
        this.AssignLiterals=AssignLiterals;
        if(AssignLiterals!=null) AssignLiterals.setParent(this);
    }

    public AssignLiteralsList getAssignLiteralsList() {
        return AssignLiteralsList;
    }

    public void setAssignLiteralsList(AssignLiteralsList AssignLiteralsList) {
        this.AssignLiteralsList=AssignLiteralsList;
    }

    public AssignLiterals getAssignLiterals() {
        return AssignLiterals;
    }

    public void setAssignLiterals(AssignLiterals AssignLiterals) {
        this.AssignLiterals=AssignLiterals;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AssignLiteralsList!=null) AssignLiteralsList.accept(visitor);
        if(AssignLiterals!=null) AssignLiterals.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AssignLiteralsList!=null) AssignLiteralsList.traverseTopDown(visitor);
        if(AssignLiterals!=null) AssignLiterals.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AssignLiteralsList!=null) AssignLiteralsList.traverseBottomUp(visitor);
        if(AssignLiterals!=null) AssignLiterals.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignLiteralsListMultiple(\n");

        if(AssignLiteralsList!=null)
            buffer.append(AssignLiteralsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignLiterals!=null)
            buffer.append(AssignLiterals.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignLiteralsListMultiple]");
        return buffer.toString();
    }
}
