// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class StatementIfConditionError extends Statement {

    private IfConditionRecovery IfConditionRecovery;
    private Statement Statement;
    private ElseOptional ElseOptional;

    public StatementIfConditionError (IfConditionRecovery IfConditionRecovery, Statement Statement, ElseOptional ElseOptional) {
        this.IfConditionRecovery=IfConditionRecovery;
        if(IfConditionRecovery!=null) IfConditionRecovery.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ElseOptional=ElseOptional;
        if(ElseOptional!=null) ElseOptional.setParent(this);
    }

    public IfConditionRecovery getIfConditionRecovery() {
        return IfConditionRecovery;
    }

    public void setIfConditionRecovery(IfConditionRecovery IfConditionRecovery) {
        this.IfConditionRecovery=IfConditionRecovery;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public ElseOptional getElseOptional() {
        return ElseOptional;
    }

    public void setElseOptional(ElseOptional ElseOptional) {
        this.ElseOptional=ElseOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfConditionRecovery!=null) IfConditionRecovery.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(ElseOptional!=null) ElseOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfConditionRecovery!=null) IfConditionRecovery.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ElseOptional!=null) ElseOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfConditionRecovery!=null) IfConditionRecovery.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ElseOptional!=null) ElseOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementIfConditionError(\n");

        if(IfConditionRecovery!=null)
            buffer.append(IfConditionRecovery.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseOptional!=null)
            buffer.append(ElseOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementIfConditionError]");
        return buffer.toString();
    }
}
