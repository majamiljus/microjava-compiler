// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclFieldErrorSemi extends ClassDecl {

    private ClassName ClassName;
    private ExtendsType ExtendsType;
    private VarDeclList VarDeclList;
    private ClassFieldRecoverySemi ClassFieldRecoverySemi;
    private VarDeclList VarDeclList1;
    private MethodDeclListOptional MethodDeclListOptional;

    public ClassDeclFieldErrorSemi (ClassName ClassName, ExtendsType ExtendsType, VarDeclList VarDeclList, ClassFieldRecoverySemi ClassFieldRecoverySemi, VarDeclList VarDeclList1, MethodDeclListOptional MethodDeclListOptional) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.ExtendsType=ExtendsType;
        if(ExtendsType!=null) ExtendsType.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.ClassFieldRecoverySemi=ClassFieldRecoverySemi;
        if(ClassFieldRecoverySemi!=null) ClassFieldRecoverySemi.setParent(this);
        this.VarDeclList1=VarDeclList1;
        if(VarDeclList1!=null) VarDeclList1.setParent(this);
        this.MethodDeclListOptional=MethodDeclListOptional;
        if(MethodDeclListOptional!=null) MethodDeclListOptional.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public ExtendsType getExtendsType() {
        return ExtendsType;
    }

    public void setExtendsType(ExtendsType ExtendsType) {
        this.ExtendsType=ExtendsType;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public ClassFieldRecoverySemi getClassFieldRecoverySemi() {
        return ClassFieldRecoverySemi;
    }

    public void setClassFieldRecoverySemi(ClassFieldRecoverySemi ClassFieldRecoverySemi) {
        this.ClassFieldRecoverySemi=ClassFieldRecoverySemi;
    }

    public VarDeclList getVarDeclList1() {
        return VarDeclList1;
    }

    public void setVarDeclList1(VarDeclList VarDeclList1) {
        this.VarDeclList1=VarDeclList1;
    }

    public MethodDeclListOptional getMethodDeclListOptional() {
        return MethodDeclListOptional;
    }

    public void setMethodDeclListOptional(MethodDeclListOptional MethodDeclListOptional) {
        this.MethodDeclListOptional=MethodDeclListOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassName!=null) ClassName.accept(visitor);
        if(ExtendsType!=null) ExtendsType.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(ClassFieldRecoverySemi!=null) ClassFieldRecoverySemi.accept(visitor);
        if(VarDeclList1!=null) VarDeclList1.accept(visitor);
        if(MethodDeclListOptional!=null) MethodDeclListOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(ExtendsType!=null) ExtendsType.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(ClassFieldRecoverySemi!=null) ClassFieldRecoverySemi.traverseTopDown(visitor);
        if(VarDeclList1!=null) VarDeclList1.traverseTopDown(visitor);
        if(MethodDeclListOptional!=null) MethodDeclListOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(ExtendsType!=null) ExtendsType.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(ClassFieldRecoverySemi!=null) ClassFieldRecoverySemi.traverseBottomUp(visitor);
        if(VarDeclList1!=null) VarDeclList1.traverseBottomUp(visitor);
        if(MethodDeclListOptional!=null) MethodDeclListOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclFieldErrorSemi(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExtendsType!=null)
            buffer.append(ExtendsType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassFieldRecoverySemi!=null)
            buffer.append(ClassFieldRecoverySemi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList1!=null)
            buffer.append(VarDeclList1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclListOptional!=null)
            buffer.append(MethodDeclListOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclFieldErrorSemi]");
        return buffer.toString();
    }
}
