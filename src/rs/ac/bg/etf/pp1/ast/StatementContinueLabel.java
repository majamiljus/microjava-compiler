// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class StatementContinueLabel extends Statement {

    private String labelContinue;

    public StatementContinueLabel (String labelContinue) {
        this.labelContinue=labelContinue;
    }

    public String getLabelContinue() {
        return labelContinue;
    }

    public void setLabelContinue(String labelContinue) {
        this.labelContinue=labelContinue;
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
        buffer.append("StatementContinueLabel(\n");

        buffer.append(" "+tab+labelContinue);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementContinueLabel]");
        return buffer.toString();
    }
}
