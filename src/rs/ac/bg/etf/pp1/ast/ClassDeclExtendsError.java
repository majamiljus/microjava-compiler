// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclExtendsError extends ClassDecl {

    private ClassName ClassName;
    private ClassExtendsRecovery ClassExtendsRecovery;
    private VarDeclList VarDeclList;
    private MethodDeclListOptional MethodDeclListOptional;

    public ClassDeclExtendsError (ClassName ClassName, ClassExtendsRecovery ClassExtendsRecovery, VarDeclList VarDeclList, MethodDeclListOptional MethodDeclListOptional) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.ClassExtendsRecovery=ClassExtendsRecovery;
        if(ClassExtendsRecovery!=null) ClassExtendsRecovery.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.MethodDeclListOptional=MethodDeclListOptional;
        if(MethodDeclListOptional!=null) MethodDeclListOptional.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public ClassExtendsRecovery getClassExtendsRecovery() {
        return ClassExtendsRecovery;
    }

    public void setClassExtendsRecovery(ClassExtendsRecovery ClassExtendsRecovery) {
        this.ClassExtendsRecovery=ClassExtendsRecovery;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
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
        if(ClassExtendsRecovery!=null) ClassExtendsRecovery.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(MethodDeclListOptional!=null) MethodDeclListOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(ClassExtendsRecovery!=null) ClassExtendsRecovery.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(MethodDeclListOptional!=null) MethodDeclListOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(ClassExtendsRecovery!=null) ClassExtendsRecovery.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(MethodDeclListOptional!=null) MethodDeclListOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclExtendsError(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassExtendsRecovery!=null)
            buffer.append(ClassExtendsRecovery.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclListOptional!=null)
            buffer.append(MethodDeclListOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclExtendsError]");
        return buffer.toString();
    }
}
