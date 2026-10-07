// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private String constName;
    private LiteralConst LiteralConst;
    private AssignLiteralsList AssignLiteralsList;

    public ConstDecl (Type Type, String constName, LiteralConst LiteralConst, AssignLiteralsList AssignLiteralsList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.constName=constName;
        this.LiteralConst=LiteralConst;
        if(LiteralConst!=null) LiteralConst.setParent(this);
        this.AssignLiteralsList=AssignLiteralsList;
        if(AssignLiteralsList!=null) AssignLiteralsList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public LiteralConst getLiteralConst() {
        return LiteralConst;
    }

    public void setLiteralConst(LiteralConst LiteralConst) {
        this.LiteralConst=LiteralConst;
    }

    public AssignLiteralsList getAssignLiteralsList() {
        return AssignLiteralsList;
    }

    public void setAssignLiteralsList(AssignLiteralsList AssignLiteralsList) {
        this.AssignLiteralsList=AssignLiteralsList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(LiteralConst!=null) LiteralConst.accept(visitor);
        if(AssignLiteralsList!=null) AssignLiteralsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(LiteralConst!=null) LiteralConst.traverseTopDown(visitor);
        if(AssignLiteralsList!=null) AssignLiteralsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(LiteralConst!=null) LiteralConst.traverseBottomUp(visitor);
        if(AssignLiteralsList!=null) AssignLiteralsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        if(LiteralConst!=null)
            buffer.append(LiteralConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignLiteralsList!=null)
            buffer.append(AssignLiteralsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl]");
        return buffer.toString();
    }
}
