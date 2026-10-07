// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class DeclarationTypeGlobalVarErrorSemi extends DeclarationType {

    private Type Type;
    private GlobalVarRecoverySemi GlobalVarRecoverySemi;

    public DeclarationTypeGlobalVarErrorSemi (Type Type, GlobalVarRecoverySemi GlobalVarRecoverySemi) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.GlobalVarRecoverySemi=GlobalVarRecoverySemi;
        if(GlobalVarRecoverySemi!=null) GlobalVarRecoverySemi.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public GlobalVarRecoverySemi getGlobalVarRecoverySemi() {
        return GlobalVarRecoverySemi;
    }

    public void setGlobalVarRecoverySemi(GlobalVarRecoverySemi GlobalVarRecoverySemi) {
        this.GlobalVarRecoverySemi=GlobalVarRecoverySemi;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(GlobalVarRecoverySemi!=null) GlobalVarRecoverySemi.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(GlobalVarRecoverySemi!=null) GlobalVarRecoverySemi.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(GlobalVarRecoverySemi!=null) GlobalVarRecoverySemi.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DeclarationTypeGlobalVarErrorSemi(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalVarRecoverySemi!=null)
            buffer.append(GlobalVarRecoverySemi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DeclarationTypeGlobalVarErrorSemi]");
        return buffer.toString();
    }
}
