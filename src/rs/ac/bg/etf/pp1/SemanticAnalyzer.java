package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected=false;
	Logger log=Logger.getLogger(getClass());

	private Obj currentProgram=null;
	private Obj currentMethod=null;
	private Struct currentType=Tab.noType;
	private Struct boolType=Tab.noType;

	private Obj standardOrd=Tab.noObj;
	private Obj standardChr=Tab.noObj;
	private Obj standardLen=Tab.noObj;

	private int currentFormParamCount=0;
	private List<Struct> currentFormParamTypes=new ArrayList<>();

	private Struct currentClass=null;
	private Obj overriddenMethod=null;

	private Set<Obj> inheritedMembers=Collections.newSetFromMap(new IdentityHashMap<Obj,Boolean>());
	private Set<Struct> abstractClasses=Collections.newSetFromMap(new IdentityHashMap<Struct,Boolean>());
	private Set<Obj> abstractMethods=Collections.newSetFromMap(new IdentityHashMap<Obj,Boolean>());
	private Map<Struct,String> classNames=new IdentityHashMap<>();

	private int nVars;

	public int getnVars(){
		return nVars;
	}

	public void report_error(String message,SyntaxNode info){
		errorDetected=true;
		StringBuilder msg=new StringBuilder(message);
		int line=(info==null)?0:info.getLine();

		if(line!=0){
			msg.append(" na liniji ").append(line);
		}

		log.error(msg.toString());
	}

	public boolean passed(){
		return !errorDetected;
	}

	private String getObjKindName(int kind){
		switch(kind){
			case Obj.Con:return "Con";
			case Obj.Var:return "Var";
			case Obj.Type:return "Type";
			case Obj.Meth:return "Meth";
			case Obj.Fld:return "Fld";
			case Obj.Elem:return "Elem";
			case Obj.Prog:return "Prog";
			default:return "Unknown";
		}
	}

	private String getStructName(Struct type){
		if(type==null)return "null";
		if(type==Tab.noType)return "noType";
		if(type==Tab.intType)return "int";
		if(type==Tab.charType)return "char";
		if(type==boolType)return "bool";
		if(type==Tab.nullType)return "null";

		switch(type.getKind()){
			case Struct.Int:return "int";
			case Struct.Char:return "char";
			case Struct.Bool:return "bool";
			case Struct.Array:return getStructName(type.getElemType())+"[]";
			case Struct.Class:
				String name=classNames.get(type);
				return name!=null?name:"class";
			default:return "type("+type.getKind()+")";
		}
	}

	private String formatObj(Obj obj){
		if(obj==null)return "null";
		if(obj==Tab.noObj)return "noObj";

		return getObjKindName(obj.getKind())+
				" "+obj.getName()+
				", type="+getStructName(obj.getType())+
				", adr="+obj.getAdr()+
				", level="+obj.getLevel();
	}

	private String typeName(Struct type){
		return getStructName(type);
	}

	private boolean sameType(Struct first,Struct second){
		if(first==second)return true;
		if(first==null||second==null)return false;
		if(first==Tab.noType||second==Tab.noType)return false;
		if(first.getKind()!=second.getKind())return false;

		if(first.getKind()==Struct.Array){
			return sameType(first.getElemType(),second.getElemType());
		}

		return false;
	}

	private boolean isReferenceType(Struct type){
		return type!=null&&
				(type.getKind()==Struct.Array||type.getKind()==Struct.Class);
	}

	private boolean compatibleTypes(Struct first,Struct second){
		if(first==Tab.noType||second==Tab.noType)return true;
		if(sameType(first,second))return true;
		if(first==Tab.nullType&&isReferenceType(second))return true;
		if(second==Tab.nullType&&isReferenceType(first))return true;
		return false;
	}

	private boolean isSubclassOf(Struct derivedType,Struct baseType){
		if(derivedType==null||baseType==null)return false;

		Struct type=derivedType;

		while(type!=null&&type.getKind()==Struct.Class){
			if(type==baseType)return true;

			Struct parentType=type.getElemType();

			if(parentType==null||parentType==type||parentType.getKind()!=Struct.Class){
				break;
			}

			type=parentType;
		}

		return false;
	}

	private boolean assignableTypes(Struct sourceType,Struct destinationType){
		if(sourceType==Tab.noType||destinationType==Tab.noType)return true;
		if(sameType(sourceType,destinationType))return true;

		if(sourceType==Tab.nullType&&isReferenceType(destinationType)){
			return true;
		}

		if(sourceType.getKind()==Struct.Class&&destinationType.getKind()==Struct.Class){
			return isSubclassOf(sourceType,destinationType);
		}

		if(sourceType.getKind()==Struct.Array&&destinationType.getKind()==Struct.Array){
			if(destinationType.getElemType()==Tab.noType)return true;
			return sameType(sourceType.getElemType(),destinationType.getElemType());
		}

		return false;
	}

	private boolean isLValue(Obj object){
		return object!=null&&object!=Tab.noObj&&
				(object.getKind()==Obj.Var||object.getKind()==Obj.Elem||object.getKind()==Obj.Fld);
	}

	private Obj findClassMember(Struct classType,String memberName){
		if(classType==null||classType.getKind()!=Struct.Class)return Tab.noObj;

		if(classType==currentClass){
			Scope classScope=Tab.currentScope();

			if(currentMethod!=null){
				classScope=classScope.getOuter();
			}

			if(classScope!=null){
				Obj member=classScope.findSymbol(memberName);

				if(member!=null&&(member.getKind()==Obj.Fld||member.getKind()==Obj.Meth)){
					return member;
				}
			}
		}

		for(Obj member:classType.getMembers()){
			if(member.getName().equals(memberName))return member;
		}

		return Tab.noObj;
	}

	private List<Struct> getFormalTypes(Obj method){
		if(method==currentMethod){
			return new ArrayList<>(currentFormParamTypes);
		}

		List<Obj> formalObjects=new ArrayList<>();

		for(Obj local:method.getLocalSymbols()){
			if(local.getKind()==Obj.Var&&local.getAdr()<method.getLevel()){
				formalObjects.add(local);
			}
		}

		Collections.sort(formalObjects,(a,b)->Integer.compare(a.getAdr(),b.getAdr()));

		List<Struct> formalTypes=new ArrayList<>();

		for(Obj formal:formalObjects){
			if("this".equals(formal.getName()))continue;
			formalTypes.add(formal.getType());
		}

		return formalTypes;
	}

	private void checkOverride(SyntaxNode node){
		if(overriddenMethod==null)return;

		if(!sameType(currentMethod.getType(),overriddenMethod.getType())){
			report_error("Povratni tip redefinisane metode "+currentMethod.getName()
					+" mora biti isti kao povratni tip nasledjene metode",node);
		}

		List<Struct> currentParams=getFormalTypes(currentMethod);
		List<Struct> inheritedParams=getFormalTypes(overriddenMethod);

		if(currentParams.size()!=inheritedParams.size()){
			report_error("Redefinisana metoda "+currentMethod.getName()
					+" mora imati isti broj parametara kao nasledjena metoda",node);
			return;
		}

		for(int i=0;i<currentParams.size();i++){
			if(!sameType(currentParams.get(i),inheritedParams.get(i))){
				report_error("Parametar broj "+(i+1)+" redefinisane metode "
						+currentMethod.getName()+" mora imati isti tip kao u nasledjenoj metodi",node);
			}
		}
	}

	private void finishClass(SyntaxNode node,boolean concreteClass){
		Tab.chainLocalSymbols(currentClass);

		int fieldCount=0;

		for(Obj member:currentClass.getMembers()){
			if(member.getKind()==Obj.Fld)fieldCount++;
		}

		if(fieldCount>65536){
			report_error("Klasa ima vise od 65536 polja",node);
		}

		if(concreteClass){
			for(Obj member:currentClass.getMembers()){
				if(member.getKind()==Obj.Meth&&abstractMethods.contains(member)){
					report_error("Konkretna klasa nije implementirala apstraktnu metodu "+member.getName(),node);
				}
			}
		}

		Tab.closeScope();
		currentClass=null;
		inheritedMembers.clear();
		log.info("Zatvoren opseg klase");
	}

	// PROGRAM
	@Override
	public void visit(ProgramName programName){
		standardOrd=Tab.find("ord");
		standardChr=Tab.find("chr");
		standardLen=Tab.find("len");

		Obj boolObj=Tab.find("bool");
		if(boolObj==Tab.noObj){
			boolObj=Tab.insert(Obj.Type,"bool",new Struct(Struct.Bool));
		}
		boolType=boolObj.getType();

		if(Tab.find("eol")==Tab.noObj){
			Obj eol=Tab.insert(Obj.Con,"eol",Tab.charType);
			eol.setAdr('\n');
		}

		currentProgram=Tab.insert(Obj.Prog,programName.getProgName(),Tab.noType);
		programName.obj=currentProgram;
		Tab.openScope();
		log.info("Otvoren opseg programa "+programName.getProgName());
	}

	@Override
	public void visit(Program program){
		Obj main=Tab.currentScope().findSymbol("main");

		if(main==null){
			report_error("Program nema main metodu",program);
		}
		else if(main.getKind()!=Obj.Meth){
			report_error("Ime main mora oznacavati metodu",program);
		}
		else{
			if(main.getType()!=Tab.noType){
				report_error("Main metoda mora biti tipa void",program);
			}
			if(main.getLevel()!=0){
				report_error("Main metoda ne sme imati formalne parametre",program);
			}
		}

		if(Tab.currentScope().getnVars()>65536){
			report_error("Program ima vise od 65536 globalnih promenljivih",program);
		}

		nVars=Tab.currentScope().getnVars();
		Tab.chainLocalSymbols(currentProgram);
		Tab.closeScope();
		log.info("Zatvoren opseg programa");
	}

	@Override
	public void visit(Type type){
		Obj typeObj=Tab.find(type.getTypeName());

		if(typeObj==Tab.noObj){
			report_error("Tip "+type.getTypeName()+" nije deklarisan",type);
			type.struct=Tab.noType;
			currentType=Tab.noType;
			return;
		}

		if(typeObj.getKind()!=Obj.Type){
			report_error("Ime "+type.getTypeName()+" ne predstavlja tip",type);
			type.struct=Tab.noType;
			currentType=Tab.noType;
			return;
		}

		type.struct=typeObj.getType();
		currentType=type.struct;
	}

	// KONSTANTE
	@Override
	public void visit(LiteralConstNum literal){
		literal.struct=Tab.intType;
	}

	@Override
	public void visit(LiteralConstChar literal){
		literal.struct=Tab.charType;
	}

	@Override
	public void visit(LiteralConstBool literal){
		literal.struct=boolType;
	}

	@Override
	public void visit(ConstDecl constDecl){
		Struct declaredType=constDecl.getType().struct;
		String name=constDecl.getConstName();
		LiteralConst literal=constDecl.getLiteralConst();

		if(declaredType!=Tab.noType){
			Obj existing=Tab.currentScope().findSymbol(name);

			if(existing!=null){
				report_error("Ime "+name+" je vec deklarisano u ovom opsegu",constDecl);
			}
			else{
				if(literal.struct!=Tab.noType&&!sameType(literal.struct,declaredType)){
					report_error("Vrednost konstante "+name+" nije tipa "+typeName(declaredType),constDecl);
				}

				Obj constant=Tab.insert(Obj.Con,name,declaredType);

				if(literal instanceof LiteralConstNum){
					constant.setAdr(((LiteralConstNum)literal).getNumValue());
				}
				else if(literal instanceof LiteralConstChar){
					constant.setAdr(((LiteralConstChar)literal).getCharValue());
				}
				else{
					constant.setAdr(((LiteralConstBool)literal).getBoolValue());
				}
			}
		}

		List<AssignLiteralsItem> additional=new ArrayList<>();
		AssignLiteralsList list=constDecl.getAssignLiteralsList();

		while(list instanceof AssignLiteralsListMultiple){
			AssignLiteralsListMultiple multiple=(AssignLiteralsListMultiple)list;
			additional.add((AssignLiteralsItem)multiple.getAssignLiterals());
			list=multiple.getAssignLiteralsList();
		}

		Collections.reverse(additional);

		for(AssignLiteralsItem item:additional){
			name=item.getConstName();
			literal=item.getLiteralConst();

			if(declaredType==Tab.noType)continue;

			Obj existing=Tab.currentScope().findSymbol(name);

			if(existing!=null){
				report_error("Ime "+name+" je vec deklarisano u ovom opsegu",item);
				continue;
			}

			if(literal.struct!=Tab.noType&&!sameType(literal.struct,declaredType)){
				report_error("Vrednost konstante "+name+" nije tipa "+typeName(declaredType),item);
			}

			Obj constant=Tab.insert(Obj.Con,name,declaredType);

			if(literal instanceof LiteralConstNum){
				constant.setAdr(((LiteralConstNum)literal).getNumValue());
			}
			else if(literal instanceof LiteralConstChar){
				constant.setAdr(((LiteralConstChar)literal).getCharValue());
			}
			else{
				constant.setAdr(((LiteralConstBool)literal).getBoolValue());
			}
		}
	}

	// PROMENLJIVE
	@Override
	public void visit(VarDecl varDecl){
		boolean isArray=varDecl.getBrackets() instanceof BracketsOne;
		declareVariable(varDecl.getVarName(),isArray,varDecl);

		List<IdentBrackets> additional=new ArrayList<>();
		IdentBracketsList list=varDecl.getIdentBracketsList();

		while(list instanceof IdentBracketsListMultiple){
			IdentBracketsListMultiple multiple=(IdentBracketsListMultiple)list;
			additional.add(multiple.getIdentBrackets());
			list=multiple.getIdentBracketsList();
		}

		Collections.reverse(additional);

		for(IdentBrackets variable:additional){
			isArray=variable.getBrackets() instanceof BracketsOne;
			declareVariable(variable.getVarName(),isArray,variable);
		}
	}

	private void declareVariable(String name,boolean isArray,SyntaxNode node){
		if(currentType==Tab.noType){
			report_error("Promenljiva "+name+" nema ispravan tip",node);
			return;
		}

		Obj existing=Tab.currentScope().findSymbol(name);

		if(existing!=null){
			report_error("Ime "+name+" je vec deklarisano u ovom opsegu",node);
			return;
		}

		Struct variableType=isArray?new Struct(Struct.Array,currentType):currentType;
		int kind=(currentClass!=null&&currentMethod==null)?Obj.Fld:Obj.Var;

		Tab.insert(kind,name,variableType);
	}

	// KLASE
	@Override
	public void visit(ClassName className){
		inheritedMembers.clear();

		Struct classType=new Struct(Struct.Class);
		Obj existing=Tab.currentScope().findSymbol(className.getClassName());

		if(existing!=null){
			report_error("Ime "+className.getClassName()+" je vec deklarisano u ovom opsegu",className);
			className.obj=Tab.noObj;
		}
		else{
			className.obj=Tab.insert(Obj.Type,className.getClassName(),classType);
		}

		classNames.put(classType,className.getClassName());
		currentClass=classType;
		Tab.openScope();
		log.info("Otvoren opseg klase "+className.getClassName());
	}

	@Override
	public void visit(AbstractClassName className){
		inheritedMembers.clear();

		Struct classType=new Struct(Struct.Class);
		Obj existing=Tab.currentScope().findSymbol(className.getClassName());

		if(existing!=null){
			report_error("Ime "+className.getClassName()+" je vec deklarisano u ovom opsegu",className);
			className.obj=Tab.noObj;
		}
		else{
			className.obj=Tab.insert(Obj.Type,className.getClassName(),classType);
		}

		classNames.put(classType,className.getClassName());
		abstractClasses.add(classType);
		currentClass=classType;
		Tab.openScope();
		log.info("Otvoren opseg apstraktne klase "+className.getClassName());
	}

	@Override
	public void visit(ExtendsTypeOne extendsType){
		Struct parentType=extendsType.getType().struct;

		if(parentType==Tab.noType)return;

		if(parentType.getKind()!=Struct.Class){
			report_error("Tip nakon extends mora biti klasa",extendsType);
			return;
		}

		if(parentType==currentClass){
			report_error("Klasa ne moze naslediti samu sebe",extendsType);
			return;
		}

		currentClass.setElementType(parentType);

		inheritedMembers.clear();
		inheritedMembers.addAll(parentType.getMembers());

		for(Obj member:parentType.getMembers()){
			Tab.currentScope().addToLocals(member);
		}
	}

	@Override
	public void visit(ClassDeclValid classDecl){
		finishClass(classDecl,true);
	}

	@Override
	public void visit(ClassDeclExtendsError classDecl){
		finishClass(classDecl,true);
	}

	@Override
	public void visit(ClassDeclFieldErrorSemi classDecl){
		finishClass(classDecl,true);
	}

	@Override
	public void visit(ClassDeclFieldErrorLBrace classDecl){
		finishClass(classDecl,true);
	}

	@Override
	public void visit(AbstractClassDeclValid classDecl){
		finishClass(classDecl,false);
	}

	@Override
	public void visit(AbstractClassDeclExtendsError classDecl){
		finishClass(classDecl,false);
	}

	@Override
	public void visit(AbstractClassDeclFieldErrorSemi classDecl){
		finishClass(classDecl,false);
	}

	@Override
	public void visit(AbstractClassDeclFieldErrorLBrace classDecl){
		finishClass(classDecl,false);
	}

	// METODE
	private Obj startMethod(String name,Struct returnType,SyntaxNode node){
		overriddenMethod=null;

		Obj existing=Tab.currentScope().findSymbol(name);
		Obj astObject;

		if(existing!=null&&currentClass!=null&&existing.getKind()==Obj.Meth&&inheritedMembers.contains(existing)){
			overriddenMethod=existing;
			Tab.currentScope().getLocals().deleteKey(name);
			currentMethod=Tab.insert(Obj.Meth,name,returnType);
			astObject=currentMethod;
		}
		else if(existing!=null){
			report_error("Ime "+name+" je vec deklarisano u ovom opsegu",node);
			currentMethod=new Obj(Obj.Meth,name,returnType);
			astObject=Tab.noObj;
		}
		else{
			currentMethod=Tab.insert(Obj.Meth,name,returnType);
			astObject=currentMethod;
		}

		Tab.openScope();
		currentFormParamCount=0;
		currentFormParamTypes.clear();

		if(currentClass!=null){
			Obj thisObj=Tab.insert(Obj.Var,"this",currentClass);
			thisObj.setFpPos(0);
			currentFormParamCount=1;
		}

		log.info("Otvoren opseg metode "+name);
		return astObject;
	}

	@Override
	public void visit(MethodVoidName methodName){
		methodName.obj=startMethod(methodName.getMethodName(),Tab.noType,methodName);
	}

	@Override
	public void visit(MethodTypeName methodName){
		methodName.obj=startMethod(methodName.getMethodName(),methodName.getType().struct,methodName);
	}

	@Override
	public void visit(TypeOrVoidType typeOrVoid){
		typeOrVoid.struct=typeOrVoid.getType().struct;
	}

	@Override
	public void visit(TypeOrVoidVoid typeOrVoid){
		typeOrVoid.struct=Tab.noType;
	}

	@Override
	public void visit(AbstractMethodName methodName){
		methodName.obj=startMethod(methodName.getMethodName(),methodName.getTypeOrVoid().struct,methodName);
	}

	@Override
	public void visit(FormParsValid formPars){
		Struct paramType=formPars.getType().struct;

		if(formPars.getBrackets() instanceof BracketsOne&&paramType!=Tab.noType){
			paramType=new Struct(Struct.Array,paramType);
		}

		int fpPos=currentFormParamCount;
		currentFormParamCount++;
		currentFormParamTypes.add(paramType);

		if(paramType==Tab.noType){
			report_error("Formalni parametar "+formPars.getParamName()+" nema ispravan tip",formPars);
		}
		else if(Tab.currentScope().findSymbol(formPars.getParamName())!=null){
			report_error("Ime "+formPars.getParamName()+" je vec deklarisano u ovom opsegu",formPars);
		}
		else{
			Obj parameter=Tab.insert(Obj.Var,formPars.getParamName(),paramType);
			parameter.setFpPos(fpPos);
		}

		List<FormParsListMultiple> additional=new ArrayList<>();
		FormParsList list=formPars.getFormParsList();

		while(list instanceof FormParsListMultiple){
			FormParsListMultiple multiple=(FormParsListMultiple)list;
			additional.add(multiple);
			list=multiple.getFormParsList();
		}

		Collections.reverse(additional);

		for(FormParsListMultiple parameterNode:additional){
			paramType=parameterNode.getType().struct;

			if(parameterNode.getBrackets() instanceof BracketsOne&&paramType!=Tab.noType){
				paramType=new Struct(Struct.Array,paramType);
			}

			fpPos=currentFormParamCount;
			currentFormParamCount++;
			currentFormParamTypes.add(paramType);

			if(paramType==Tab.noType){
				report_error("Formalni parametar "+parameterNode.getParamName()+" nema ispravan tip",parameterNode);
			}
			else if(Tab.currentScope().findSymbol(parameterNode.getParamName())!=null){
				report_error("Ime "+parameterNode.getParamName()+" je vec deklarisano u ovom opsegu",parameterNode);
			}
			else{
				Obj parameter=Tab.insert(Obj.Var,parameterNode.getParamName(),paramType);
				parameter.setFpPos(fpPos);
			}
		}
	}

	private void finishMethod(SyntaxNode node){
		if(Tab.currentScope().getnVars()>256){
			report_error("Metoda "+currentMethod.getName()+" ima vise od 256 lokalnih promenljivih i parametara",node);
		}

		currentMethod.setLevel(currentFormParamCount);
		Tab.chainLocalSymbols(currentMethod);
		checkOverride(node);
		Tab.closeScope();
		log.info("Zatvoren opseg metode "+currentMethod.getName());

		currentMethod=null;
		overriddenMethod=null;
		currentFormParamCount=0;
		currentFormParamTypes.clear();
	}

	@Override
	public void visit(MethodDecl methodDecl){
		finishMethod(methodDecl);
	}

	@Override
	public void visit(AbstractMethodDecl methodDecl){
		if(currentMethod!=null){
			abstractMethods.add(currentMethod);
		}
		finishMethod(methodDecl);
	}

	// DESIGNATOR
	@Override
	public void visit(Designator designator){
		Obj object=Tab.find(designator.getDesignatorName());

		if(object==Tab.noObj){
			report_error("Ime "+designator.getDesignatorName()+" nije deklarisano",designator);
			designator.obj=Tab.noObj;
			return;
		}

		log.info("Pretraga na "+designator.getLine()+"("+object.getName()+"), nadjeno "+formatObj(object));

		List<DesignatorSelector> selectors=new ArrayList<>();
		DesignatorSuffix suffix=designator.getDesignatorSuffix();

		while(suffix instanceof DesignatorSuffixMultiple){
			DesignatorSuffixMultiple multiple=(DesignatorSuffixMultiple)suffix;
			selectors.add(multiple.getDesignatorSelector());
			suffix=multiple.getDesignatorSuffix();
		}

		Collections.reverse(selectors);

		for(DesignatorSelector selector:selectors){
			if(selector instanceof DesignatorSelectorExpr){
				DesignatorSelectorExpr arrayAccess=(DesignatorSelectorExpr)selector;

				if(object==Tab.noObj||object.getType().getKind()!=Struct.Array){
					report_error("Indeksiranje je dozvoljeno samo nad nizom",selector);
					object=Tab.noObj;
					break;
				}

				if(arrayAccess.getExpr().struct!=Tab.noType&&arrayAccess.getExpr().struct!=Tab.intType){
					report_error("Indeks niza mora biti tipa int",selector);
				}

				object=new Obj(Obj.Elem,object.getName(),object.getType().getElemType());
			}
			else if(selector instanceof DesignatorSelectorLength){
				if(object==Tab.noObj||object.getType().getKind()!=Struct.Array){
					report_error("length se moze koristiti samo nad nizom",selector);
					object=Tab.noObj;
					break;
				}

				object=new Obj(Obj.Con,"length",Tab.intType);
			}
			else{
				DesignatorSelectorIdent memberSelector=(DesignatorSelectorIdent)selector;
				int kind=object.getKind();

				if(kind!=Obj.Var&&kind!=Obj.Fld&&kind!=Obj.Elem){
					report_error("Clanu klase se moze pristupiti samo preko objekta",selector);
					object=Tab.noObj;
					break;
				}

				if(object.getType().getKind()!=Struct.Class){
					report_error("Operator . sa identifikatorom zahteva klasni tip",selector);
					object=Tab.noObj;
					break;
				}

				Obj member=findClassMember(object.getType(),memberSelector.getMemberName());

				if(member==Tab.noObj){
					report_error("Klasa nema clan "+memberSelector.getMemberName(),selector);
					object=Tab.noObj;
					break;
				}

				log.info("Pretraga na "+selector.getLine()+"("+member.getName()+"), nadjeno "+formatObj(member));
				object=member;
			}
		}

		designator.obj=object;
	}

	// POZIV METODE
	private boolean checkMethodCall(Obj method,ActParsOptional optionalActPars,SyntaxNode node){
		if(method==null||method==Tab.noObj||method.getKind()!=Obj.Meth){
			report_error("Designator u pozivu mora oznacavati metodu",node);
			return false;
		}

		List<Struct> actualTypes=new ArrayList<>();

		if(optionalActPars instanceof ActParsOptionalOne){
			ActPars actPars=((ActParsOptionalOne)optionalActPars).getActPars();
			actualTypes.add(actPars.getExpr().struct);

			List<Struct> rest=new ArrayList<>();
			ActParsList list=actPars.getActParsList();

			while(list instanceof ActParsListMultiple){
				ActParsListMultiple multiple=(ActParsListMultiple)list;
				rest.add(multiple.getExpr().struct);
				list=multiple.getActParsList();
			}

			Collections.reverse(rest);
			actualTypes.addAll(rest);
		}

		if(method==standardOrd){
			if(actualTypes.size()!=1){
				report_error("Metoda ord ocekuje 1 argument, a prosledjeno je "+actualTypes.size(),node);
				return false;
			}
			if(actualTypes.get(0)!=Tab.noType&&actualTypes.get(0)!=Tab.charType){
				report_error("Argument metode ord mora biti tipa char",node);
				return false;
			}
			return true;
		}

		if(method==standardChr){
			if(actualTypes.size()!=1){
				report_error("Metoda chr ocekuje 1 argument, a prosledjeno je "+actualTypes.size(),node);
				return false;
			}
			if(actualTypes.get(0)!=Tab.noType&&actualTypes.get(0)!=Tab.intType){
				report_error("Argument metode chr mora biti tipa int",node);
				return false;
			}
			return true;
		}

		if(method==standardLen){
			if(actualTypes.size()!=1){
				report_error("Metoda len ocekuje 1 argument, a prosledjeno je "+actualTypes.size(),node);
				return false;
			}
			Struct actual=actualTypes.get(0);
			if(actual!=Tab.noType&&actual.getKind()!=Struct.Array){
				report_error("Argument metode len mora biti niz",node);
				return false;
			}
			return true;
		}

		List<Struct> formalTypes=getFormalTypes(method);

		if(actualTypes.size()!=formalTypes.size()){
			report_error("Metoda "+method.getName()+" ocekuje "+formalTypes.size()
					+" argumenata, a prosledjeno je "+actualTypes.size(),node);
			return false;
		}

		boolean ok=true;

		for(int i=0;i<actualTypes.size();i++){
			Struct actual=actualTypes.get(i);
			Struct formal=formalTypes.get(i);

			if(actual==Tab.noType||formal==Tab.noType)continue;

			if(!assignableTypes(actual,formal)){
				report_error("Argument broj "+(i+1)+" metode "+method.getName()+" nije odgovarajuceg tipa",node);
				ok=false;
			}
		}

		return ok;
	}

	// FAKTORI
	@Override
	public void visit(FactorNumber factor){
		factor.struct=Tab.intType;
	}

	@Override
	public void visit(FactorChar factor){
		factor.struct=Tab.charType;
	}

	@Override
	public void visit(FactorBool factor){
		factor.struct=boolType;
	}

	@Override
	public void visit(FactorExpr factor){
		factor.struct=factor.getExpr().struct;
	}

	@Override
	public void visit(FactorDesignator factor){
		Obj object=factor.getDesignator().obj;

		if(object==null||object==Tab.noObj){
			factor.struct=Tab.noType;
			return;
		}

		if(factor.getActParsBracketsOpt() instanceof ActParsBracketsOptOne){
			ActParsBracketsOptOne call=(ActParsBracketsOptOne)factor.getActParsBracketsOpt();
			checkMethodCall(object,call.getActParsOptional(),factor);

			if(object.getKind()==Obj.Meth){
				if(object.getType()==Tab.noType){
					report_error("Void metoda "+object.getName()+" ne moze se koristiti kao izraz",factor);
				}
				factor.struct=object.getType();
			}
			else{
				factor.struct=Tab.noType;
			}
		}
		else{
			int kind=object.getKind();

			if(kind!=Obj.Con&&kind!=Obj.Var&&kind!=Obj.Elem&&kind!=Obj.Fld){
				report_error("Designator ne predstavlja vrednost koja se moze koristiti u izrazu",factor);
				factor.struct=Tab.noType;
			}
			else{
				factor.struct=object.getType();
			}
		}
	}

	@Override
	public void visit(FactorNew factor){
		Struct type=factor.getType().struct;

		if(type==Tab.noType){
			factor.struct=Tab.noType;
			return;
		}

		if(factor.getExprBracketOptional() instanceof ExprBracketOptionalOne){
			ExprBracket bracket=((ExprBracketOptionalOne)factor.getExprBracketOptional()).getExprBracket();

			if(bracket.getExpr().struct!=Tab.noType&&bracket.getExpr().struct!=Tab.intType){
				report_error("Velicina novog niza mora biti tipa int",factor);
			}

			factor.struct=new Struct(Struct.Array,type);

			if(type.getKind()==Struct.Class){
				Obj classObj=Tab.find(factor.getType().getTypeName());
				if(classObj!=Tab.noObj){
					log.info("Pretraga na "+factor.getLine()+"("+classObj.getName()+"), nadjeno "+formatObj(classObj));
				}
			}

			return;
		}

		if(type.getKind()!=Struct.Class){
			report_error("Operator new bez uglastih zagrada zahteva klasni tip",factor);
			factor.struct=Tab.noType;
			return;
		}

		Obj classObj=Tab.find(factor.getType().getTypeName());

		if(classObj!=Tab.noObj){
			log.info("Pretraga na "+factor.getLine()+"("+classObj.getName()+"), nadjeno "+formatObj(classObj));
		}

		if(abstractClasses.contains(type)){
			report_error("Apstraktna klasa se ne moze instancirati",factor);
			factor.struct=Tab.noType;
			return;
		}

		factor.struct=type;
	}

	// TERM I ARITMETIKA
	@Override
	public void visit(MulopFactorListEmpty list){
		list.struct=Tab.noType;
	}

	@Override
	public void visit(MulopFactorListMultiple list){
		Struct previous=list.getMulopFactorList().struct;
		Struct right=list.getFactor().struct;

		if(right!=Tab.noType&&right!=Tab.intType){
			report_error("Operand operatora *, / ili % mora biti tipa int",list);
		}

		if(!(list.getMulopFactorList() instanceof MulopFactorListEmpty)&&previous!=Tab.noType&&previous!=Tab.intType){
			report_error("Operand operatora *, / ili % mora biti tipa int",list);
		}

		list.struct=(right==Tab.noType)?Tab.noType:Tab.intType;
	}

	@Override
	public void visit(Term term){
		if(term.getMulopFactorList() instanceof MulopFactorListEmpty){
			term.struct=term.getFactor().struct;
			return;
		}

		if(term.getFactor().struct!=Tab.noType&&term.getFactor().struct!=Tab.intType){
			report_error("Operand operatora *, / ili % mora biti tipa int",term);
			term.struct=Tab.noType;
		}
		else{
			term.struct=term.getMulopFactorList().struct==Tab.noType?Tab.noType:Tab.intType;
		}
	}

	@Override
	public void visit(AddopTermListEmpty list){
		list.struct=Tab.noType;
	}

	@Override
	public void visit(AddopTermListMultiple list){
		Struct previous=list.getAddopTermList().struct;
		Struct right=list.getTerm().struct;

		if(right!=Tab.noType&&right!=Tab.intType){
			report_error("Operand operatora + ili - mora biti tipa int",list);
		}

		if(!(list.getAddopTermList() instanceof AddopTermListEmpty)&&previous!=Tab.noType&&previous!=Tab.intType){
			report_error("Operand operatora + ili - mora biti tipa int",list);
		}

		list.struct=(right==Tab.noType)?Tab.noType:Tab.intType;
	}

	@Override
	public void visit(ExprMinusUnary expr){
		if(expr.getExprMinus().struct!=Tab.noType&&expr.getExprMinus().struct!=Tab.intType){
			report_error("Operand unarnog minusa mora biti tipa int",expr);
			expr.struct=Tab.noType;
		}
		else{
			expr.struct=expr.getExprMinus().struct==Tab.noType?Tab.noType:Tab.intType;
		}
	}

	@Override
	public void visit(ExprMinusTerm expr){
		if(expr.getAddopTermList() instanceof AddopTermListEmpty){
			expr.struct=expr.getTerm().struct;
			return;
		}

		if(expr.getTerm().struct!=Tab.noType&&expr.getTerm().struct!=Tab.intType){
			report_error("Operand operatora + ili - mora biti tipa int",expr);
			expr.struct=Tab.noType;
		}
		else{
			expr.struct=expr.getAddopTermList().struct==Tab.noType?Tab.noType:Tab.intType;
		}
	}

	// USLOVI
	@Override
	public void visit(RelopExprEmpty relopExpr){
		relopExpr.struct=Tab.noType;
	}

	@Override
	public void visit(RelopExprOne relopExpr){
		relopExpr.struct=relopExpr.getExprMinus().struct;
	}

	@Override
	public void visit(CondFact condFact){
		Struct left=condFact.getExprMinus().struct;

		if(condFact.getRelopExpr() instanceof RelopExprEmpty){
			condFact.struct=left;
			return;
		}

		RelopExprOne relation=(RelopExprOne)condFact.getRelopExpr();
		Struct right=relation.struct;

		if(left!=Tab.noType&&right!=Tab.noType&&!compatibleTypes(left,right)){
			report_error("Izrazi u relacionom izrazu nisu kompatibilnog tipa",condFact);
		}

		boolean reference=isReferenceType(left)||isReferenceType(right)||left==Tab.nullType||right==Tab.nullType;

		if(reference&&!(relation.getRelop() instanceof RelopEq)&&!(relation.getRelop() instanceof RelopNeq)){
			report_error("Za referentne tipove dozvoljeni su samo == i !=",condFact);
		}

		condFact.struct=boolType;
	}

	@Override
	public void visit(CondFactListEmpty list){
		list.struct=Tab.noType;
	}

	@Override
	public void visit(CondFactListMultiple list){
		Struct previous=list.getCondFactList().struct;
		Struct right=list.getCondFact().struct;

		if(right!=Tab.noType&&right!=boolType){
			report_error("Operand operatora && mora biti tipa bool",list);
		}

		if(!(list.getCondFactList() instanceof CondFactListEmpty)&&previous!=Tab.noType&&previous!=boolType){
			report_error("Operand operatora && mora biti tipa bool",list);
		}

		list.struct=(right==Tab.noType)?Tab.noType:boolType;
	}

	@Override
	public void visit(CondTerm condTerm){
		if(condTerm.getCondFactList() instanceof CondFactListEmpty){
			condTerm.struct=condTerm.getCondFact().struct;
			return;
		}

		if(condTerm.getCondFact().struct!=Tab.noType&&condTerm.getCondFact().struct!=boolType){
			report_error("Operand operatora && mora biti tipa bool",condTerm);
			condTerm.struct=Tab.noType;
		}
		else{
			condTerm.struct=condTerm.getCondFactList().struct==Tab.noType?Tab.noType:boolType;
		}
	}

	@Override
	public void visit(CondTermListEmpty list){
		list.struct=Tab.noType;
	}

	@Override
	public void visit(CondTermListMultiple list){
		Struct previous=list.getCondTermList().struct;
		Struct right=list.getCondTerm().struct;

		if(right!=Tab.noType&&right!=boolType){
			report_error("Operand operatora || mora biti tipa bool",list);
		}

		if(!(list.getCondTermList() instanceof CondTermListEmpty)&&previous!=Tab.noType&&previous!=boolType){
			report_error("Operand operatora || mora biti tipa bool",list);
		}

		list.struct=(right==Tab.noType)?Tab.noType:boolType;
	}

	@Override
	public void visit(Condition condition){
		if(condition.getCondTermList() instanceof CondTermListEmpty){
			condition.struct=condition.getCondTerm().struct;
			return;
		}

		if(condition.getCondTerm().struct!=Tab.noType&&condition.getCondTerm().struct!=boolType){
			report_error("Operand operatora || mora biti tipa bool",condition);
			condition.struct=Tab.noType;
		}
		else{
			condition.struct=condition.getCondTermList().struct==Tab.noType?Tab.noType:boolType;
		}
	}

	// TERNARNI I EXPR
	@Override
	public void visit(ExprConditionCond exprCondition){
		exprCondition.struct=exprCondition.getCondition().struct;
	}

	@Override
	public void visit(ExprConditionExpr exprCondition){
		Struct conditionType=exprCondition.getCondition().struct;
		Struct trueType=exprCondition.getExpr().struct;
		Struct falseType=exprCondition.getExprCondition().struct;

		if(conditionType!=Tab.noType&&conditionType!=boolType){
			report_error("Uslov ternarnog operatora mora biti tipa bool",exprCondition);
		}

		if(trueType!=Tab.noType&&falseType!=Tab.noType&&!sameType(trueType,falseType)){
			report_error("Drugi i treci operand ternarnog operatora moraju biti istog tipa",exprCondition);
			exprCondition.struct=Tab.noType;
		}
		else{
			exprCondition.struct=trueType!=Tab.noType?trueType:falseType;
		}
	}

	@Override
	public void visit(Expr expr){
		expr.struct=expr.getExprCondition().struct;
	}

	// DESIGNATOR STATEMENT
	@Override
	public void visit(DesignatorStatement statement){
		Obj object=statement.getDesignator().obj;
		OperationOne operation=statement.getOperationOne();

		if(object==null||object==Tab.noObj)return;

		if(operation instanceof OperationOneExpr){
			Struct source=((OperationOneExpr)operation).getExpr().struct;

			if(!isLValue(object)){
				report_error("Leva strana dodele mora biti promenljiva, element niza ili polje",statement);
			}
			else if(source!=Tab.noType&&!assignableTypes(source,object.getType())){
				report_error("Tip izraza nije kompatibilan pri dodeli sa tipom designatora",statement);
			}
		}
		else if(operation instanceof OperationOneInc||operation instanceof OperationOneDec){
			if(!isLValue(object)){
				report_error("Operator ++/-- zahteva promenljivu, element niza ili polje",statement);
			}

			if(object.getType()!=Tab.intType){
				report_error("Operator ++/-- se moze primeniti samo na int",statement);
			}
		}
		else{
			OperationOneActPars call=(OperationOneActPars)operation;
			checkMethodCall(object,call.getActParsOptional(),statement);
		}
	}

	// STATEMENTS
	@Override
	public void visit(StatementRead statement){
		Obj object=statement.getDesignator().obj;
		if(object==null||object==Tab.noObj)return;

		if(!isLValue(object)){
			report_error("read zahteva promenljivu, element niza ili polje",statement);
		}

		Struct type=object.getType();

		if(type!=Tab.intType&&type!=Tab.charType&&type!=boolType){
			report_error("read podrzava samo int, char i bool",statement);
		}
	}

	@Override
	public void visit(StatementPrint statement){
		Struct type=statement.getExpr().struct;

		if(type!=Tab.noType&&type!=Tab.intType&&type!=Tab.charType&&type!=boolType){
			report_error("print podrzava samo int, char i bool",statement);
		}
	}

	@Override
	public void visit(StatementReturn statement){
		if(currentMethod==null){
			report_error("return se moze koristiti samo unutar metode",statement);
			return;
		}

		if(statement.getExprOptional() instanceof ExprOptionalEmpty){
			if(currentMethod.getType()!=Tab.noType){
				report_error("Metoda "+currentMethod.getName()+" mora vratiti vrednost",statement);
			}
		}
		else{
			Struct returned=((ExprOptionalOne)statement.getExprOptional()).getExpr().struct;

			if(currentMethod.getType()==Tab.noType){
				report_error("Void metoda ne sme vracati vrednost",statement);
			}
			else if(returned!=Tab.noType&&!sameType(returned,currentMethod.getType())){
				report_error("Tip izraza u return naredbi ne odgovara povratnom tipu metode",statement);
			}
		}
	}

	@Override
	public void visit(StatementIfElse statement){
		Struct type=statement.getCondition().struct;

		if(type!=Tab.noType&&type!=boolType){
			report_error("Uslov if naredbe mora biti tipa bool",statement);
		}
	}

	@Override
	public void visit(StatementFor statement){
		if(statement.getConditionOptional() instanceof ConditionOptionalOne){
			Struct type=((ConditionOptionalOne)statement.getConditionOptional()).getCondition().struct;

			if(type!=Tab.noType&&type!=boolType){
				report_error("Uslov for petlje mora biti tipa bool",statement);
			}
		}
	}
	@Override
	public void visit(StatementDoWhile statement) {
		Struct type=statement.getCondition().struct;
		if(type!=Tab.noType&&type!=boolType){
			report_error("Uslov while naredbe mora biti tipa bool",statement);
		}
	}
	@Override
	public void visit(StatementDoWhileLabel statement) {
		Struct type=statement.getCondition().struct;
		if(type!=Tab.noType&&type!=boolType){
			report_error("Uslov while naredbe mora biti tipa bool",statement);
		}
//		String label = statement.getLabel();
	}	
	@Override
	public void visit(StatementBreakLabel statement){
		boolean insideFor=false;
		SyntaxNode parent=statement.getParent();
		String labelbreak = statement.getLabelBreak();
		while(parent!=null){
			if(parent instanceof StatementDoWhileLabel && labelbreak.equals(((StatementDoWhileLabel) parent).getLabel())){
				insideFor=true;
				break;
			}
			parent=parent.getParent();
		}

		if(!insideFor){
			report_error("break label se moze koristiti samo unutar while label petlje",statement);
		}
	}
	@Override
	public void visit(StatementBreak statement){
		boolean insideFor=false;
		SyntaxNode parent=statement.getParent();

		while(parent!=null){
			if(parent instanceof StatementFor){
				insideFor=true;
				break;
			}
			if(parent instanceof StatementDoWhile){
				insideFor=true;
				break;
			}
			parent=parent.getParent();
		}

		if(!insideFor){
			report_error("break se moze koristiti samo unutar for ili while petlje",statement);
		}
	}

	@Override
	public void visit(StatementContinue statement){
		boolean insideFor=false;
		SyntaxNode parent=statement.getParent();

		while(parent!=null){
			if(parent instanceof StatementFor){
				insideFor=true;
				break;
			}
			if(parent instanceof StatementDoWhile){
				insideFor=true;
				break;
			}
			parent=parent.getParent();
		}

		if(!insideFor){
			report_error("continue se moze koristiti samo unutar for ili while petlje",statement);
		}
	}
	@Override
	public void visit(StatementForRange statement) {
		Struct expr1 = statement.getExpr().struct;
		Struct expr2 = statement.getExpr1().struct;
		if((expr1!=Tab.noType && expr1 != Tab.intType) || (expr2!=Tab.noType && expr2 != Tab.intType)){
			report_error("In range indeksi moraju biti int",statement);
		}

	}
	@Override
	public void visit(ForRangeStart marker) {
		StatementForRange st = (StatementForRange)marker.getParent();
		String index = st.getIndex();
		Tab.insert(Obj.Var, index, Tab.intType);

	}
	// FINDANY
	@Override
	public void visit(StatementFindAny statement){
		Obj destination=statement.getDesignator().obj;
		Obj source=statement.getDesignator1().obj;
		Struct exprType=statement.getExpr().struct;

		if(destination!=null&&destination!=Tab.noObj){
			if(destination.getKind()!=Obj.Var||destination.getType()!=boolType){
				report_error("Levi designator funkcije findAny mora biti promenljiva tipa bool",statement);
			}
		}

		if(source==null||source==Tab.noObj)return;

		if(source.getType().getKind()!=Struct.Array){
			report_error("Desni designator funkcije findAny mora biti niz",statement);
			return;
		}

		Struct elementType=source.getType().getElemType();

		if(elementType!=Tab.intType&&elementType!=Tab.charType&&elementType!=boolType){
			report_error("findAny radi samo nad nizom ugradjenog tipa",statement);
		}

		if(exprType!=Tab.noType&&!compatibleTypes(exprType,elementType)){
			report_error("Tip izraza u findAny mora odgovarati tipu elemenata niza",statement);
		}
	}

	// MAP
	@Override
	public void visit(StatementMap statement){
		Obj destination=statement.getDesignator().obj;
		Obj source=statement.getDesignator1().obj;
		Obj iterator=Tab.find(statement.getIteratorName());
		Struct exprType=statement.getExpr().struct;

		if(destination==null||destination==Tab.noObj||source==null||source==Tab.noObj){
			return;
		}

		if(!isLValue(destination)||destination.getType().getKind()!=Struct.Array){
			report_error("Levi designator funkcije map mora biti prethodno deklarisan niz",statement);
		}

		if(source.getType().getKind()!=Struct.Array){
			report_error("Desni designator funkcije map mora biti niz",statement);
			return;
		}

		Struct sourceElementType=source.getType().getElemType();

		if(iterator==Tab.noObj){
			report_error("Iterator "+statement.getIteratorName()+" nije deklarisan",statement);
		}
		else{
			log.info("Pretraga na "+statement.getLine()+"("+iterator.getName()+"), nadjeno "+formatObj(iterator));

			if(iterator.getKind()!=Obj.Var){
				report_error("Iterator funkcije map mora biti lokalna ili globalna promenljiva",statement);
			}
			else if(!sameType(iterator.getType(),sourceElementType)){
				report_error("Iterator funkcije map mora biti istog tipa kao elementi izvornog niza",statement);
			}
		}

		if(destination.getType().getKind()==Struct.Array){
			Struct destinationElementType=destination.getType().getElemType();

			if(exprType!=Tab.noType&&!assignableTypes(exprType,destinationElementType)){
				report_error("Rezultat izraza u map nije kompatibilan sa tipom elemenata odredisnog niza",statement);
			}
		}
	}
}
