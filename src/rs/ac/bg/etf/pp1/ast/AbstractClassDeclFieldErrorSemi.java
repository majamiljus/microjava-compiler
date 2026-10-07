// generated with ast extension for cup
// version 0.8
// 3/8/2026 22:10:39


package rs.ac.bg.etf.pp1.ast;

public class AbstractClassDeclFieldErrorSemi extends AbstractClassDecl {

    private AbstractClassName AbstractClassName;
    private ExtendsType ExtendsType;
    private VarDeclList VarDeclList;
    private ClassFieldRecoverySemi ClassFieldRecoverySemi;
    private VarDeclList VarDeclList1;
    private MethodDeclTypeOptional MethodDeclTypeOptional;

    public AbstractClassDeclFieldErrorSemi (AbstractClassName AbstractClassName, ExtendsType ExtendsType, VarDeclList VarDeclList, ClassFieldRecoverySemi ClassFieldRecoverySemi, VarDeclList VarDeclList1, MethodDeclTypeOptional MethodDeclTypeOptional) {
        this.AbstractClassName=AbstractClassName;
        if(AbstractClassName!=null) AbstractClassName.setParent(this);
        this.ExtendsType=ExtendsType;
        if(ExtendsType!=null) ExtendsType.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.ClassFieldRecoverySemi=ClassFieldRecoverySemi;
        if(ClassFieldRecoverySemi!=null) ClassFieldRecoverySemi.setParent(this);
        this.VarDeclList1=VarDeclList1;
        if(VarDeclList1!=null) VarDeclList1.setParent(this);
        this.MethodDeclTypeOptional=MethodDeclTypeOptional;
        if(MethodDeclTypeOptional!=null) MethodDeclTypeOptional.setParent(this);
    }

    public AbstractClassName getAbstractClassName() {
        return AbstractClassName;
    }

    public void setAbstractClassName(AbstractClassName AbstractClassName) {
        this.AbstractClassName=AbstractClassName;
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
        if(ExtendsType!=null) ExtendsType.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(ClassFieldRecoverySemi!=null) ClassFieldRecoverySemi.accept(visitor);
        if(VarDeclList1!=null) VarDeclList1.accept(visitor);
        if(MethodDeclTypeOptional!=null) MethodDeclTypeOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbstractClassName!=null) AbstractClassName.traverseTopDown(visitor);
        if(ExtendsType!=null) ExtendsType.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(ClassFieldRecoverySemi!=null) ClassFieldRecoverySemi.traverseTopDown(visitor);
        if(VarDeclList1!=null) VarDeclList1.traverseTopDown(visitor);
        if(MethodDeclTypeOptional!=null) MethodDeclTypeOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbstractClassName!=null) AbstractClassName.traverseBottomUp(visitor);
        if(ExtendsType!=null) ExtendsType.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(ClassFieldRecoverySemi!=null) ClassFieldRecoverySemi.traverseBottomUp(visitor);
        if(VarDeclList1!=null) VarDeclList1.traverseBottomUp(visitor);
        if(MethodDeclTypeOptional!=null) MethodDeclTypeOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AbstractClassDeclFieldErrorSemi(\n");

        if(AbstractClassName!=null)
            buffer.append(AbstractClassName.toString("  "+tab));
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

        if(MethodDeclTypeOptional!=null)
            buffer.append(MethodDeclTypeOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AbstractClassDeclFieldErrorSemi]");
        return buffer.toString();
    }
}
