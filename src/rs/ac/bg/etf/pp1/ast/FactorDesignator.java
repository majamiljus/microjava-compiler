// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class FactorDesignator extends Factor {

    private Designator Designator;
    private ActParsBracketsOpt ActParsBracketsOpt;

    public FactorDesignator (Designator Designator, ActParsBracketsOpt ActParsBracketsOpt) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ActParsBracketsOpt=ActParsBracketsOpt;
        if(ActParsBracketsOpt!=null) ActParsBracketsOpt.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ActParsBracketsOpt getActParsBracketsOpt() {
        return ActParsBracketsOpt;
    }

    public void setActParsBracketsOpt(ActParsBracketsOpt ActParsBracketsOpt) {
        this.ActParsBracketsOpt=ActParsBracketsOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ActParsBracketsOpt!=null) ActParsBracketsOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ActParsBracketsOpt!=null) ActParsBracketsOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ActParsBracketsOpt!=null) ActParsBracketsOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDesignator(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsBracketsOpt!=null)
            buffer.append(ActParsBracketsOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDesignator]");
        return buffer.toString();
    }
}
