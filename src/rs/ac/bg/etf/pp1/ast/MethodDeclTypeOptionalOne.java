// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclTypeOptionalOne extends MethodDeclTypeOptional {

    private MethodDeclTypeList MethodDeclTypeList;

    public MethodDeclTypeOptionalOne (MethodDeclTypeList MethodDeclTypeList) {
        this.MethodDeclTypeList=MethodDeclTypeList;
        if(MethodDeclTypeList!=null) MethodDeclTypeList.setParent(this);
    }

    public MethodDeclTypeList getMethodDeclTypeList() {
        return MethodDeclTypeList;
    }

    public void setMethodDeclTypeList(MethodDeclTypeList MethodDeclTypeList) {
        this.MethodDeclTypeList=MethodDeclTypeList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodDeclTypeList!=null) MethodDeclTypeList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodDeclTypeList!=null) MethodDeclTypeList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodDeclTypeList!=null) MethodDeclTypeList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclTypeOptionalOne(\n");

        if(MethodDeclTypeList!=null)
            buffer.append(MethodDeclTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclTypeOptionalOne]");
        return buffer.toString();
    }
}
