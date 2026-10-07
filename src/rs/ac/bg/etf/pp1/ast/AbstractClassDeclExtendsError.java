// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class AbstractClassDeclExtendsError extends AbstractClassDecl {

    private AbstractClassName AbstractClassName;
    private ClassExtendsRecovery ClassExtendsRecovery;
    private VarDeclList VarDeclList;
    private MethodDeclTypeOptional MethodDeclTypeOptional;

    public AbstractClassDeclExtendsError (AbstractClassName AbstractClassName, ClassExtendsRecovery ClassExtendsRecovery, VarDeclList VarDeclList, MethodDeclTypeOptional MethodDeclTypeOptional) {
        this.AbstractClassName=AbstractClassName;
        if(AbstractClassName!=null) AbstractClassName.setParent(this);
        this.ClassExtendsRecovery=ClassExtendsRecovery;
        if(ClassExtendsRecovery!=null) ClassExtendsRecovery.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.MethodDeclTypeOptional=MethodDeclTypeOptional;
        if(MethodDeclTypeOptional!=null) MethodDeclTypeOptional.setParent(this);
    }

    public AbstractClassName getAbstractClassName() {
        return AbstractClassName;
    }

    public void setAbstractClassName(AbstractClassName AbstractClassName) {
        this.AbstractClassName=AbstractClassName;
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

    public MethodDeclTypeOptional getMethodDeclTypeOptional() {
        return MethodDeclTypeOptional;
    }

    public void setMethodDeclTypeOptional(MethodDeclTypeOptional MethodDeclTypeOptional) {
        this.MethodDeclTypeOptional=MethodDeclTypeOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AbstractClassName!=null) AbstractClassName.accept(visitor);
        if(ClassExtendsRecovery!=null) ClassExtendsRecovery.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(MethodDeclTypeOptional!=null) MethodDeclTypeOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbstractClassName!=null) AbstractClassName.traverseTopDown(visitor);
        if(ClassExtendsRecovery!=null) ClassExtendsRecovery.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(MethodDeclTypeOptional!=null) MethodDeclTypeOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbstractClassName!=null) AbstractClassName.traverseBottomUp(visitor);
        if(ClassExtendsRecovery!=null) ClassExtendsRecovery.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(MethodDeclTypeOptional!=null) MethodDeclTypeOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AbstractClassDeclExtendsError(\n");

        if(AbstractClassName!=null)
            buffer.append(AbstractClassName.toString("  "+tab));
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

        if(MethodDeclTypeOptional!=null)
            buffer.append(MethodDeclTypeOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AbstractClassDeclExtendsError]");
        return buffer.toString();
    }
}
