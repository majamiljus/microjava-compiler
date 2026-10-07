// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class DeclarationTypeGlobalVarErrorComma extends DeclarationType {

    private Type Type;
    private GlobalVarRecoveryComma GlobalVarRecoveryComma;
    private String I3;
    private Brackets Brackets;
    private IdentBracketsList IdentBracketsList;

    public DeclarationTypeGlobalVarErrorComma (Type Type, GlobalVarRecoveryComma GlobalVarRecoveryComma, String I3, Brackets Brackets, IdentBracketsList IdentBracketsList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.GlobalVarRecoveryComma=GlobalVarRecoveryComma;
        if(GlobalVarRecoveryComma!=null) GlobalVarRecoveryComma.setParent(this);
        this.I3=I3;
        this.Brackets=Brackets;
        if(Brackets!=null) Brackets.setParent(this);
        this.IdentBracketsList=IdentBracketsList;
        if(IdentBracketsList!=null) IdentBracketsList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public GlobalVarRecoveryComma getGlobalVarRecoveryComma() {
        return GlobalVarRecoveryComma;
    }

    public void setGlobalVarRecoveryComma(GlobalVarRecoveryComma GlobalVarRecoveryComma) {
        this.GlobalVarRecoveryComma=GlobalVarRecoveryComma;
    }

    public String getI3() {
        return I3;
    }

    public void setI3(String I3) {
        this.I3=I3;
    }

    public Brackets getBrackets() {
        return Brackets;
    }

    public void setBrackets(Brackets Brackets) {
        this.Brackets=Brackets;
    }

    public IdentBracketsList getIdentBracketsList() {
        return IdentBracketsList;
    }

    public void setIdentBracketsList(IdentBracketsList IdentBracketsList) {
        this.IdentBracketsList=IdentBracketsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(GlobalVarRecoveryComma!=null) GlobalVarRecoveryComma.accept(visitor);
        if(Brackets!=null) Brackets.accept(visitor);
        if(IdentBracketsList!=null) IdentBracketsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(GlobalVarRecoveryComma!=null) GlobalVarRecoveryComma.traverseTopDown(visitor);
        if(Brackets!=null) Brackets.traverseTopDown(visitor);
        if(IdentBracketsList!=null) IdentBracketsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(GlobalVarRecoveryComma!=null) GlobalVarRecoveryComma.traverseBottomUp(visitor);
        if(Brackets!=null) Brackets.traverseBottomUp(visitor);
        if(IdentBracketsList!=null) IdentBracketsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DeclarationTypeGlobalVarErrorComma(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalVarRecoveryComma!=null)
            buffer.append(GlobalVarRecoveryComma.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I3);
        buffer.append("\n");

        if(Brackets!=null)
            buffer.append(Brackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IdentBracketsList!=null)
            buffer.append(IdentBracketsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DeclarationTypeGlobalVarErrorComma]");
        return buffer.toString();
    }
}
