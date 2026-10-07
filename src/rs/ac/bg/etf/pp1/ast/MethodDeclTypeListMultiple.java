// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclTypeListMultiple extends MethodDeclTypeList {

    private MethodDeclTypeList MethodDeclTypeList;
    private MethodDeclType MethodDeclType;

    public MethodDeclTypeListMultiple (MethodDeclTypeList MethodDeclTypeList, MethodDeclType MethodDeclType) {
        this.MethodDeclTypeList=MethodDeclTypeList;
        if(MethodDeclTypeList!=null) MethodDeclTypeList.setParent(this);
        this.MethodDeclType=MethodDeclType;
        if(MethodDeclType!=null) MethodDeclType.setParent(this);
    }

    public MethodDeclTypeList getMethodDeclTypeList() {
        return MethodDeclTypeList;
    }

    public void setMethodDeclTypeList(MethodDeclTypeList MethodDeclTypeList) {
        this.MethodDeclTypeList=MethodDeclTypeList;
    }

    public MethodDeclType getMethodDeclType() {
        return MethodDeclType;
    }

    public void setMethodDeclType(MethodDeclType MethodDeclType) {
        this.MethodDeclType=MethodDeclType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodDeclTypeList!=null) MethodDeclTypeList.accept(visitor);
        if(MethodDeclType!=null) MethodDeclType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodDeclTypeList!=null) MethodDeclTypeList.traverseTopDown(visitor);
        if(MethodDeclType!=null) MethodDeclType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodDeclTypeList!=null) MethodDeclTypeList.traverseBottomUp(visitor);
        if(MethodDeclType!=null) MethodDeclType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclTypeListMultiple(\n");

        if(MethodDeclTypeList!=null)
            buffer.append(MethodDeclTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclType!=null)
            buffer.append(MethodDeclType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclTypeListMultiple]");
        return buffer.toString();
    }
}
