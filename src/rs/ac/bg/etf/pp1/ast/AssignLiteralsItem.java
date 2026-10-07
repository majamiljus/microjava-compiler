// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class AssignLiteralsItem extends AssignLiterals {

    private String constName;
    private LiteralConst LiteralConst;

    public AssignLiteralsItem (String constName, LiteralConst LiteralConst) {
        this.constName=constName;
        this.LiteralConst=LiteralConst;
        if(LiteralConst!=null) LiteralConst.setParent(this);
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LiteralConst!=null) LiteralConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LiteralConst!=null) LiteralConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LiteralConst!=null) LiteralConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignLiteralsItem(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        if(LiteralConst!=null)
            buffer.append(LiteralConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignLiteralsItem]");
        return buffer.toString();
    }
}
