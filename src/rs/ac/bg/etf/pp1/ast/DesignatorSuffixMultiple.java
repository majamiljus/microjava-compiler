// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class DesignatorSuffixMultiple extends DesignatorSuffix {

    private DesignatorSuffix DesignatorSuffix;
    private DesignatorSelector DesignatorSelector;

    public DesignatorSuffixMultiple (DesignatorSuffix DesignatorSuffix, DesignatorSelector DesignatorSelector) {
        this.DesignatorSuffix=DesignatorSuffix;
        if(DesignatorSuffix!=null) DesignatorSuffix.setParent(this);
        this.DesignatorSelector=DesignatorSelector;
        if(DesignatorSelector!=null) DesignatorSelector.setParent(this);
    }

    public DesignatorSuffix getDesignatorSuffix() {
        return DesignatorSuffix;
    }

    public void setDesignatorSuffix(DesignatorSuffix DesignatorSuffix) {
        this.DesignatorSuffix=DesignatorSuffix;
    }

    public DesignatorSelector getDesignatorSelector() {
        return DesignatorSelector;
    }

    public void setDesignatorSelector(DesignatorSelector DesignatorSelector) {
        this.DesignatorSelector=DesignatorSelector;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorSuffix!=null) DesignatorSuffix.accept(visitor);
        if(DesignatorSelector!=null) DesignatorSelector.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorSuffix!=null) DesignatorSuffix.traverseTopDown(visitor);
        if(DesignatorSelector!=null) DesignatorSelector.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorSuffix!=null) DesignatorSuffix.traverseBottomUp(visitor);
        if(DesignatorSelector!=null) DesignatorSelector.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorSuffixMultiple(\n");

        if(DesignatorSuffix!=null)
            buffer.append(DesignatorSuffix.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorSelector!=null)
            buffer.append(DesignatorSelector.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorSuffixMultiple]");
        return buffer.toString();
    }
}
