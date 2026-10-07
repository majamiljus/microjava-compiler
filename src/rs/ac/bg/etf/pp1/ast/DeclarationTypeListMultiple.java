// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class DeclarationTypeListMultiple extends DeclarationTypeList {

    private DeclarationTypeList DeclarationTypeList;
    private DeclarationType DeclarationType;

    public DeclarationTypeListMultiple (DeclarationTypeList DeclarationTypeList, DeclarationType DeclarationType) {
        this.DeclarationTypeList=DeclarationTypeList;
        if(DeclarationTypeList!=null) DeclarationTypeList.setParent(this);
        this.DeclarationType=DeclarationType;
        if(DeclarationType!=null) DeclarationType.setParent(this);
    }

    public DeclarationTypeList getDeclarationTypeList() {
        return DeclarationTypeList;
    }

    public void setDeclarationTypeList(DeclarationTypeList DeclarationTypeList) {
        this.DeclarationTypeList=DeclarationTypeList;
    }

    public DeclarationType getDeclarationType() {
        return DeclarationType;
    }

    public void setDeclarationType(DeclarationType DeclarationType) {
        this.DeclarationType=DeclarationType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DeclarationTypeList!=null) DeclarationTypeList.accept(visitor);
        if(DeclarationType!=null) DeclarationType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DeclarationTypeList!=null) DeclarationTypeList.traverseTopDown(visitor);
        if(DeclarationType!=null) DeclarationType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DeclarationTypeList!=null) DeclarationTypeList.traverseBottomUp(visitor);
        if(DeclarationType!=null) DeclarationType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DeclarationTypeListMultiple(\n");

        if(DeclarationTypeList!=null)
            buffer.append(DeclarationTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DeclarationType!=null)
            buffer.append(DeclarationType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DeclarationTypeListMultiple]");
        return buffer.toString();
    }
}
