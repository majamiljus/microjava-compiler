// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class AddopTermListEmpty extends AddopTermList {

    public AddopTermListEmpty () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AddopTermListEmpty(\n");

        buffer.append(tab);
        buffer.append(") [AddopTermListEmpty]");
        return buffer.toString();
    }
}
