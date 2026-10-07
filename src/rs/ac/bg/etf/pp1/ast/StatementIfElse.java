// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class StatementIfElse extends Statement {

    private Condition Condition;
    private IfStart IfStart;
    private Statement Statement;
    private ElseOptional ElseOptional;

    public StatementIfElse (Condition Condition, IfStart IfStart, Statement Statement, ElseOptional ElseOptional) {
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.IfStart=IfStart;
        if(IfStart!=null) IfStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ElseOptional=ElseOptional;
        if(ElseOptional!=null) ElseOptional.setParent(this);
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public IfStart getIfStart() {
        return IfStart;
    }

    public void setIfStart(IfStart IfStart) {
        this.IfStart=IfStart;
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
        if(Condition!=null) Condition.accept(visitor);
        if(IfStart!=null) IfStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(ElseOptional!=null) ElseOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(IfStart!=null) IfStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ElseOptional!=null) ElseOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(IfStart!=null) IfStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ElseOptional!=null) ElseOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementIfElse(\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfStart!=null)
            buffer.append(IfStart.toString("  "+tab));
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
        buffer.append(") [StatementIfElse]");
        return buffer.toString();
    }
}
