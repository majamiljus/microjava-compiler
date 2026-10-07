// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class StatementFor extends Statement {

    private DesignatorStatementOptional DesignatorStatementOptional;
    private ForConditionStart ForConditionStart;
    private ConditionOptional ConditionOptional;
    private ForUpdateStart ForUpdateStart;
    private DesignatorStatementOptional DesignatorStatementOptional1;
    private ForBodyStart ForBodyStart;
    private Statement Statement;

    public StatementFor (DesignatorStatementOptional DesignatorStatementOptional, ForConditionStart ForConditionStart, ConditionOptional ConditionOptional, ForUpdateStart ForUpdateStart, DesignatorStatementOptional DesignatorStatementOptional1, ForBodyStart ForBodyStart, Statement Statement) {
        this.DesignatorStatementOptional=DesignatorStatementOptional;
        if(DesignatorStatementOptional!=null) DesignatorStatementOptional.setParent(this);
        this.ForConditionStart=ForConditionStart;
        if(ForConditionStart!=null) ForConditionStart.setParent(this);
        this.ConditionOptional=ConditionOptional;
        if(ConditionOptional!=null) ConditionOptional.setParent(this);
        this.ForUpdateStart=ForUpdateStart;
        if(ForUpdateStart!=null) ForUpdateStart.setParent(this);
        this.DesignatorStatementOptional1=DesignatorStatementOptional1;
        if(DesignatorStatementOptional1!=null) DesignatorStatementOptional1.setParent(this);
        this.ForBodyStart=ForBodyStart;
        if(ForBodyStart!=null) ForBodyStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public DesignatorStatementOptional getDesignatorStatementOptional() {
        return DesignatorStatementOptional;
    }

    public void setDesignatorStatementOptional(DesignatorStatementOptional DesignatorStatementOptional) {
        this.DesignatorStatementOptional=DesignatorStatementOptional;
    }

    public ForConditionStart getForConditionStart() {
        return ForConditionStart;
    }

    public void setForConditionStart(ForConditionStart ForConditionStart) {
        this.ForConditionStart=ForConditionStart;
    }

    public ConditionOptional getConditionOptional() {
        return ConditionOptional;
    }

    public void setConditionOptional(ConditionOptional ConditionOptional) {
        this.ConditionOptional=ConditionOptional;
    }

    public ForUpdateStart getForUpdateStart() {
        return ForUpdateStart;
    }

    public void setForUpdateStart(ForUpdateStart ForUpdateStart) {
        this.ForUpdateStart=ForUpdateStart;
    }

    public DesignatorStatementOptional getDesignatorStatementOptional1() {
        return DesignatorStatementOptional1;
    }

    public void setDesignatorStatementOptional1(DesignatorStatementOptional DesignatorStatementOptional1) {
        this.DesignatorStatementOptional1=DesignatorStatementOptional1;
    }

    public ForBodyStart getForBodyStart() {
        return ForBodyStart;
    }

    public void setForBodyStart(ForBodyStart ForBodyStart) {
        this.ForBodyStart=ForBodyStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorStatementOptional!=null) DesignatorStatementOptional.accept(visitor);
        if(ForConditionStart!=null) ForConditionStart.accept(visitor);
        if(ConditionOptional!=null) ConditionOptional.accept(visitor);
        if(ForUpdateStart!=null) ForUpdateStart.accept(visitor);
        if(DesignatorStatementOptional1!=null) DesignatorStatementOptional1.accept(visitor);
        if(ForBodyStart!=null) ForBodyStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorStatementOptional!=null) DesignatorStatementOptional.traverseTopDown(visitor);
        if(ForConditionStart!=null) ForConditionStart.traverseTopDown(visitor);
        if(ConditionOptional!=null) ConditionOptional.traverseTopDown(visitor);
        if(ForUpdateStart!=null) ForUpdateStart.traverseTopDown(visitor);
        if(DesignatorStatementOptional1!=null) DesignatorStatementOptional1.traverseTopDown(visitor);
        if(ForBodyStart!=null) ForBodyStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorStatementOptional!=null) DesignatorStatementOptional.traverseBottomUp(visitor);
        if(ForConditionStart!=null) ForConditionStart.traverseBottomUp(visitor);
        if(ConditionOptional!=null) ConditionOptional.traverseBottomUp(visitor);
        if(ForUpdateStart!=null) ForUpdateStart.traverseBottomUp(visitor);
        if(DesignatorStatementOptional1!=null) DesignatorStatementOptional1.traverseBottomUp(visitor);
        if(ForBodyStart!=null) ForBodyStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementFor(\n");

        if(DesignatorStatementOptional!=null)
            buffer.append(DesignatorStatementOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForConditionStart!=null)
            buffer.append(ForConditionStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionOptional!=null)
            buffer.append(ConditionOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForUpdateStart!=null)
            buffer.append(ForUpdateStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementOptional1!=null)
            buffer.append(DesignatorStatementOptional1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForBodyStart!=null)
            buffer.append(ForBodyStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementFor]");
        return buffer.toString();
    }
}
