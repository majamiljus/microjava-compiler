package rs.ac.bg.etf.pp1;

import java.util.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor{

	private int mainPc=-1,nextStaticAddress=0;
	private Obj programObj,currentMethod;
	private Struct currentClass;

	private final Set<Obj> classMethods=new HashSet<>();
	private final Set<Obj> abstractMethods=new HashSet<>();
	private final Map<Struct,Integer> vftAddresses=new IdentityHashMap<>();
	private final List<Struct> classOrder=new ArrayList<>();

	private final Map<DesignatorSelectorExpr,Integer> indexSlots=new IdentityHashMap<>();
	private int callTempBase=-1,mapTempBase=-1,findAnyTempBase=-1;

	private final Deque<Integer> andStack=new ArrayDeque<>();
	private final Deque<Integer> orStack=new ArrayDeque<>();
	private final Deque<TernaryContext> ternaryStack=new ArrayDeque<>();
	private final Deque<IfContext> ifStack=new ArrayDeque<>();
	private final Deque<ForContext> forStack=new ArrayDeque<>();
	private final Deque<MapContext> mapStack=new ArrayDeque<>();
	private final Deque<FindAnyContext> findAnyStack=new ArrayDeque<>();
	private final Deque<DoWhileContext> dowhilestack=new ArrayDeque<>();
	
	private static class DoWhileContext{
		int bodyAdr;
		int conditionAdr;
		List<Integer> breaks=new ArrayList<>();
		List<Integer> continues=new ArrayList<>();
		String label;
		DoWhileContext(int bodyAdr,String label){
			this.bodyAdr=bodyAdr;
			this.label = label;
		}
	}
	private static class TernaryContext{
		int falseAdr,endAdr;
	}

	private static class IfContext{
		int falseAdr,endAdr=-1;
	}

	private static class ForContext{
		int conditionAdr,updateAdr,bodyAdr=-1;
		int falseAdr=-1;
		List<Integer> breaks=new ArrayList<>();

		ForContext(int conditionAdr){
			this.conditionAdr=conditionAdr;
		}
	}

	private static class MapContext{
		Designator destination,source;
		Obj iterator;
		int sourceSlot,resultSlot,indexSlot,valueSlot,destBaseSlot,destIndexSlot;
		int loopAdr,endAdr;

		MapContext(Designator destination,Designator source,Obj iterator,int base){
			this.destination=destination;
			this.source=source;
			this.iterator=iterator;
			sourceSlot=base;
			resultSlot=base+1;
			indexSlot=base+2;
			valueSlot=base+3;
			destBaseSlot=base+4;
			destIndexSlot=base+5;
		}
	}

	private static class FindAnyContext{
		Designator destination,source;
		int valueSlot,sourceSlot,indexSlot,destBaseSlot,destIndexSlot;

		FindAnyContext(Designator destination,Designator source,int base){
			this.destination=destination;
			this.source=source;
			valueSlot=base;
			sourceSlot=base+1;
			indexSlot=base+2;
			destBaseSlot=base+3;
			destIndexSlot=base+4;
		}
	}

	private class MethodInfo extends VisitorAdaptor{

		List<DesignatorSelectorExpr> indexes=new ArrayList<>();
		boolean hasVirtualCall=false,hasMap=false,hasFindAny=false;
		int maxVirtualArgs=0;

		private void checkCall(Obj method){
			if(method==null||!isClassMethod(method))return;
			hasVirtualCall=true;
			maxVirtualArgs=Math.max(maxVirtualArgs,Math.max(0,method.getLevel()-1));
		}

		@Override
		public void visit(DesignatorSelectorExpr selector){
			indexes.add(selector);
		}

		@Override
		public void visit(FactorDesignator factor){
			if(factor.getActParsBracketsOpt() instanceof ActParsBracketsOptOne)
				checkCall(factor.getDesignator().obj);
		}

		@Override
		public void visit(DesignatorStatement statement){
			if(statement.getOperationOne() instanceof OperationOneActPars)
				checkCall(statement.getDesignator().obj);
		}

		@Override
		public void visit(StatementMap statement){
			hasMap=true;
		}

		@Override
		public void visit(StatementFindAny statement){
			hasFindAny=true;
		}
	}

	public int getMainPc(){
		return mainPc;
	}

	public int getDataSize(){
		return nextStaticAddress;
	}

	private <T extends SyntaxNode>T ancestor(SyntaxNode node,Class<T> type){
		SyntaxNode parent=node.getParent();

		while(parent!=null){
			if(type.isInstance(parent))return type.cast(parent);
			parent=parent.getParent();
		}

		return null;
	}

	@Override
	public void visit(ProgramName programName){

		programObj=programName.obj;
		nextStaticAddress=0;

		for(Obj obj:programObj.getLocalSymbols())
			if(obj.getKind()==Obj.Var)
				nextStaticAddress=Math.max(nextStaticAddress,obj.getAdr()+1);
	}

	private void reserveVft(Struct type){

		if(type==null||vftAddresses.containsKey(type))return;

		vftAddresses.put(type,nextStaticAddress);
		classOrder.add(type);

		for(Obj member:type.getMembers()){
			if(member.getKind()!=Obj.Meth)continue;
			classMethods.add(member);
			nextStaticAddress+=member.getName().length()+2;
		}

		nextStaticAddress++;
	}

	private void putStatic(int adr,int value){
		Code.loadConst(value);
		Code.put(Code.putstatic);
		Code.put2(adr);
	}

	private void initializeVfts(){

		for(Struct type:classOrder){

			int adr=vftAddresses.get(type);

			for(Obj method:type.getMembers()){

				if(method.getKind()!=Obj.Meth||abstractMethods.contains(method))continue;

				String name=method.getName();

				for(int i=0;i<name.length();i++)
					putStatic(adr++,name.charAt(i));

				putStatic(adr++,-1);
				putStatic(adr++,method.getAdr());
			}

			putStatic(adr,-2);
		}
	}

	@Override
	public void visit(ClassName className){
		currentClass=className.obj.getType();
		reserveVft(currentClass);
	}

	@Override
	public void visit(AbstractClassName className){
		currentClass=className.obj.getType();
		reserveVft(currentClass);
	}

	private void endClass(){
		currentClass=null;
	}

	@Override
	public void visit(ClassDeclValid node){
		endClass();
	}

	@Override
	public void visit(ClassDeclExtendsError node){
		endClass();
	}

	@Override
	public void visit(ClassDeclFieldErrorSemi node){
		endClass();
	}

	@Override
	public void visit(ClassDeclFieldErrorLBrace node){
		endClass();
	}

	@Override
	public void visit(AbstractClassDeclValid node){
		endClass();
	}

	@Override
	public void visit(AbstractClassDeclExtendsError node){
		endClass();
	}

	@Override
	public void visit(AbstractClassDeclFieldErrorSemi node){
		endClass();
	}

	@Override
	public void visit(AbstractClassDeclFieldErrorLBrace node){
		endClass();
	}

	@Override
	public void visit(AbstractMethodName methodName){

		if(methodName.obj!=null){
			classMethods.add(methodName.obj);
			abstractMethods.add(methodName.obj);
		}
	}

	private int localCount(Obj method){

		int count=0;

		for(Obj obj:method.getLocalSymbols())
			if(obj.getKind()==Obj.Var)
				count=Math.max(count,obj.getAdr()+1);

		return count;
	}

	private void startMethod(Obj method,SyntaxNode methodNode){

		currentMethod=method;
		method.setAdr(Code.pc);

		boolean main=currentClass==null&&"main".equals(method.getName());

		if(main)mainPc=Code.pc;

		MethodInfo info=new MethodInfo();
		methodNode.getParent().traverseBottomUp(info);

		int next=localCount(method);

		indexSlots.clear();

		for(DesignatorSelectorExpr selector:info.indexes)
			indexSlots.put(selector,next++);

		if(info.hasVirtualCall){
			callTempBase=next;
			next+=info.maxVirtualArgs+1;
		}
		else callTempBase=-1;

		if(info.hasMap){
			mapTempBase=next;
			next+=6;
		}
		else mapTempBase=-1;

		if(info.hasFindAny){
			findAnyTempBase=next;
			next+=5;
		}
		else findAnyTempBase=-1;

		Code.put(Code.enter);
		Code.put(method.getLevel());
		Code.put(next);

		if(main)initializeVfts();
	}

	@Override
	public void visit(MethodVoidName methodName){

		if(currentClass!=null)
			classMethods.add(methodName.obj);

		startMethod(methodName.obj,methodName);
	}

	@Override
	public void visit(MethodTypeName methodName){

		if(currentClass!=null)
			classMethods.add(methodName.obj);

		startMethod(methodName.obj,methodName);
	}

	@Override
	public void visit(MethodDecl methodDecl){

		if(currentMethod==null)return;

		if(currentMethod.getType()==Tab.noType){
			Code.put(Code.exit);
			Code.put(Code.return_);
		}
		else{
			Code.put(Code.trap);
			Code.put(1);
		}

		currentMethod=null;
		callTempBase=-1;
		mapTempBase=-1;
		findAnyTempBase=-1;
		indexSlots.clear();
	}

	private void loadLocal(int adr){

		if(adr>=0&&adr<=3)
			Code.put(Code.load_n+adr);
		else{
			Code.put(Code.load);
			Code.put(adr);
		}
	}

	private void storeLocal(int adr){

		if(adr>=0&&adr<=3)
			Code.put(Code.store_n+adr);
		else{
			Code.put(Code.store);
			Code.put(adr);
		}
	}

	private Obj findVisibleObject(String name){

		if(currentMethod!=null)
			for(Obj obj:currentMethod.getLocalSymbols())
				if(obj.getName().equals(name))
					return obj;

		if(currentClass!=null)
			for(Obj obj:currentClass.getMembers())
				if(obj.getName().equals(name))
					return obj;

		if(programObj!=null)
			for(Obj obj:programObj.getLocalSymbols())
				if(obj.getName().equals(name))
					return obj;

		return Tab.noObj;
	}

	private Obj findMember(Struct type,String name){

		if(type==null)return Tab.noObj;

		for(Obj member:type.getMembers())
			if(member.getName().equals(name))
				return member;

		return Tab.noObj;
	}

	private Obj getThis(){

		if(currentMethod==null)return Tab.noObj;

		for(Obj obj:currentMethod.getLocalSymbols())
			if(obj.getKind()==Obj.Var&&"this".equals(obj.getName()))
				return obj;

		return Tab.noObj;
	}

	private void loadObj(Obj obj){

		if(obj.getKind()==Obj.Fld){
			Code.put(Code.getfield);
			Code.put2(obj.getAdr()+1);
		}
		else Code.load(obj);
	}

	private void storeObj(Obj obj){

		if(obj.getKind()==Obj.Fld){
			Code.put(Code.putfield);
			Code.put2(obj.getAdr()+1);
		}
		else Code.store(obj);
	}

	private void loadThis(){

		Obj obj=getThis();

		if(obj!=Tab.noObj)
			Code.load(obj);
	}

	private List<DesignatorSelector> getSelectors(Designator designator){

		List<DesignatorSelector> selectors=new ArrayList<>();
		DesignatorSuffix suffix=designator.getDesignatorSuffix();

		while(suffix instanceof DesignatorSuffixMultiple){

			DesignatorSuffixMultiple multiple=(DesignatorSuffixMultiple)suffix;

			selectors.add(multiple.getDesignatorSelector());
			suffix=multiple.getDesignatorSuffix();
		}

		Collections.reverse(selectors);

		return selectors;
	}

	private void loadBase(Obj obj){

		if(obj.getKind()==Obj.Fld){
			loadThis();
			loadObj(obj);
		}
		else Code.load(obj);
	}

	private Struct loadSelector(Struct type,DesignatorSelector selector){

		if(selector instanceof DesignatorSelectorIdent){

			Obj member=findMember(
				type,
				((DesignatorSelectorIdent)selector).getMemberName()
			);

			loadObj(member);

			return member.getType();
		}

		if(selector instanceof DesignatorSelectorExpr){

			Integer slot=indexSlots.get((DesignatorSelectorExpr)selector);

			if(slot!=null)
				loadLocal(slot);

			Struct elemType=type.getElemType();
			Obj elem=new Obj(Obj.Elem,"$elem",elemType);

			loadObj(elem);

			return elemType;
		}

		Code.put(Code.arraylength);

		return Tab.intType;
	}

	private void loadDesignator(Designator designator){

		List<DesignatorSelector> selectors=getSelectors(designator);

		if(selectors.isEmpty()){
			loadBase(designator.obj);
			return;
		}

		Obj base=findVisibleObject(designator.getDesignatorName());

		loadBase(base);

		Struct type=base.getType();

		for(DesignatorSelector selector:selectors)
			type=loadSelector(type,selector);
	}

	private void loadReceiver(Designator designator){

		List<DesignatorSelector> selectors=getSelectors(designator);

		if(selectors.isEmpty()){
			loadThis();
			return;
		}

		Obj base=findVisibleObject(designator.getDesignatorName());

		loadBase(base);

		Struct type=base.getType();

		for(int i=0;i<selectors.size()-1;i++)
			type=loadSelector(type,selectors.get(i));
	}

	private void prepareLvalue(Designator designator){

		List<DesignatorSelector> selectors=getSelectors(designator);

		if(selectors.isEmpty()){

			if(designator.obj.getKind()==Obj.Fld)
				loadThis();

			return;
		}

		Obj base=findVisibleObject(designator.getDesignatorName());

		loadBase(base);

		Struct type=base.getType();

		for(int i=0;i<selectors.size()-1;i++)
			type=loadSelector(type,selectors.get(i));

		DesignatorSelector last=selectors.get(selectors.size()-1);

		if(last instanceof DesignatorSelectorExpr){

			Integer slot=indexSlots.get((DesignatorSelectorExpr)last);

			if(slot!=null)
				loadLocal(slot);
		}
	}

	private void storePrepared(Designator designator){
		storeObj(designator.obj);
	}

	private void saveAddress(Designator designator,int baseSlot,int indexSlot){

		prepareLvalue(designator);

		if(designator.obj.getKind()==Obj.Fld)
			storeLocal(baseSlot);
		else if(designator.obj.getKind()==Obj.Elem){
			storeLocal(indexSlot);
			storeLocal(baseSlot);
		}
	}

	private void restoreAddress(Designator designator,int baseSlot,int indexSlot){

		if(designator.obj.getKind()==Obj.Fld)
			loadLocal(baseSlot);
		else if(designator.obj.getKind()==Obj.Elem){
			loadLocal(baseSlot);
			loadLocal(indexSlot);
		}
	}

	@Override
	public void visit(DesignatorSelectorExpr selector){

		Integer slot=indexSlots.get(selector);

		if(slot!=null)
			storeLocal(slot);
	}

	@Override
	public void visit(FactorNumber factor){
		Code.loadConst(factor.getNumValue());
	}

	@Override
	public void visit(FactorChar factor){
		Code.loadConst(factor.getCharValue());
	}

	@Override
	public void visit(FactorBool factor){
		Code.loadConst(factor.getBoolValue());
	}

	private boolean isClassMethod(Obj method){
		return method!=null&&classMethods.contains(method);
	}

	@Override
	public void visit(CallStart marker){

		FactorDesignator factor=ancestor(marker,FactorDesignator.class);

		if(factor!=null){

			Designator designator=factor.getDesignator();

			if(isClassMethod(designator.obj))
				loadReceiver(designator);

			return;
		}

		DesignatorStatement statement=ancestor(marker,DesignatorStatement.class);

		if(statement!=null&&isClassMethod(statement.getDesignator().obj))
			loadReceiver(statement.getDesignator());
	}

	private void virtualCall(Obj method){

		int count=Math.max(0,method.getLevel()-1);

		for(int i=count-1;i>=0;i--)
			storeLocal(callTempBase+i);

		Code.put(Code.dup);
		Code.put(Code.getfield);
		Code.put2(0);

		int vftSlot=callTempBase+count;

		storeLocal(vftSlot);

		for(int i=0;i<count;i++)
			loadLocal(callTempBase+i);

		loadLocal(vftSlot);

		Code.put(Code.invokevirtual);

		String name=method.getName();

		for(int i=0;i<name.length();i++)
			Code.put4(name.charAt(i));

		Code.put4(-1);
	}

	private void callMethod(Obj method){

		if(method==Tab.find("ord")||method==Tab.find("chr"))
			return;

		if(method==Tab.find("len")){
			Code.put(Code.arraylength);
			return;
		}

		if(isClassMethod(method)){
			virtualCall(method);
			return;
		}

		int adr=method.getAdr()-Code.pc;

		Code.put(Code.call);
		Code.put2(adr);
	}

	@Override
	public void visit(FactorDesignator factor){

		Designator designator=factor.getDesignator();

		if(factor.getActParsBracketsOpt() instanceof ActParsBracketsOptOne){
			callMethod(designator.obj);
			return;
		}

		loadDesignator(designator);
	}

	@Override
	public void visit(FactorNew factor){

		if(factor.getExprBracketOptional() instanceof ExprBracketOptionalOne){

			Code.put(Code.newarray);
			Code.put(factor.getType().struct==Tab.charType?0:1);

			return;
		}

		Struct type=factor.getType().struct;

		if(!vftAddresses.containsKey(type))
			reserveVft(type);

		Code.put(Code.new_);
		Code.put2((type.getNumberOfFields()+1)*4);
		Code.put(Code.dup);
		Code.loadConst(vftAddresses.get(type));
		Code.put(Code.putfield);
		Code.put2(0);
	}

	@Override
	public void visit(MulopFactorListMultiple list){

		Mulop op=list.getMulop();

		if(op instanceof MulopMul)
			Code.put(Code.mul);
		else if(op instanceof MulopDiv)
			Code.put(Code.div);
		else
			Code.put(Code.rem);
	}

	@Override
	public void visit(AddopTermListMultiple list){

		if(list.getAddop() instanceof AddopPlus)
			Code.put(Code.add);
		else
			Code.put(Code.sub);
	}

	@Override
	public void visit(ExprMinusUnary expr){
		Code.put(Code.neg);
	}

	private int relop(Relop op){

		if(op instanceof RelopEq)return Code.eq;
		if(op instanceof RelopNeq)return Code.ne;
		if(op instanceof RelopGt)return Code.gt;
		if(op instanceof RelopGe)return Code.ge;
		if(op instanceof RelopLt)return Code.lt;

		return Code.le;
	}

	private void booleanResult(int op){

		Code.putFalseJump(op,0);

		int falseAdr=Code.pc-2;

		Code.loadConst(1);
		Code.putJump(0);

		int endAdr=Code.pc-2;

		Code.fixup(falseAdr);
		Code.loadConst(0);
		Code.fixup(endAdr);
	}

	@Override
	public void visit(RelopExprOne expr){
		booleanResult(relop(expr.getRelop()));
	}

	@Override
	public void visit(AndStart marker){

		Code.loadConst(0);
		Code.putFalseJump(Code.ne,0);

		andStack.push(Code.pc-2);
	}

	@Override
	public void visit(CondFactListMultiple list){

		int falseAdr=andStack.pop();

		Code.putJump(0);

		int endAdr=Code.pc-2;

		Code.fixup(falseAdr);
		Code.loadConst(0);
		Code.fixup(endAdr);
	}

	@Override
	public void visit(OrStart marker){

		Code.loadConst(0);
		Code.putFalseJump(Code.eq,0);

		orStack.push(Code.pc-2);
	}

	@Override
	public void visit(CondTermListMultiple list){

		int trueAdr=orStack.pop();

		Code.putJump(0);

		int endAdr=Code.pc-2;

		Code.fixup(trueAdr);
		Code.loadConst(1);
		Code.fixup(endAdr);
	}

	@Override
	public void visit(Condition condition){

		SyntaxNode parent=condition.getParent();

		if(parent instanceof ExprConditionExpr&&
		   ((ExprConditionExpr)parent).getCondition()==condition){

			Code.loadConst(0);
			Code.putFalseJump(Code.ne,0);

			TernaryContext context=new TernaryContext();

			context.falseAdr=Code.pc-2;

			ternaryStack.push(context);
		}
	}

	@Override
	public void visit(Expr expr){

		SyntaxNode parent=expr.getParent();

		if(parent instanceof ExprConditionExpr&&
		   ((ExprConditionExpr)parent).getExpr()==expr){

			TernaryContext context=ternaryStack.peek();

			Code.putJump(0);

			context.endAdr=Code.pc-2;

			Code.fixup(context.falseAdr);
		}
	}

	@Override
	public void visit(ExprConditionExpr expr){

		TernaryContext context=ternaryStack.pop();

		Code.fixup(context.endAdr);
	}

	@Override
	public void visit(AssignStart marker){

		DesignatorStatement statement=ancestor(marker,DesignatorStatement.class);

		if(statement!=null){
			prepareLvalue(statement.getDesignator());
			return;
		}

		StatementFindAny findAny=ancestor(marker,StatementFindAny.class);

		if(findAny!=null){

			FindAnyContext context=new FindAnyContext(
				findAny.getDesignator(),
				findAny.getDesignator1(),
				findAnyTempBase
			);

			findAnyStack.push(context);

			saveAddress(
				context.destination,
				context.destBaseSlot,
				context.destIndexSlot
			);

			return;
		}

		StatementMap map=ancestor(marker,StatementMap.class);

		if(map!=null){

			Obj iterator=findVisibleObject(map.getIteratorName());

			MapContext context=new MapContext(
				map.getDesignator(),
				map.getDesignator1(),
				iterator,
				mapTempBase
			);

			mapStack.push(context);

			saveAddress(
				context.destination,
				context.destBaseSlot,
				context.destIndexSlot
			);
		}
	}

	@Override
	public void visit(DesignatorStatement statement){

		Designator designator=statement.getDesignator();
		OperationOne operation=statement.getOperationOne();

		if(operation instanceof OperationOneExpr){
			storePrepared(designator);
			return;
		}

		if(operation instanceof OperationOneInc||
		   operation instanceof OperationOneDec){

			prepareLvalue(designator);

			if(designator.obj.getKind()==Obj.Fld){

				Code.put(Code.dup);

				loadObj(designator.obj);
			}
			else if(designator.obj.getKind()==Obj.Elem){

				Code.put(Code.dup2);

				loadObj(designator.obj);
			}
			else
				Code.load(designator.obj);

			Code.loadConst(1);

			Code.put(
				operation instanceof OperationOneInc?
				Code.add:
				Code.sub
			);

			storeObj(designator.obj);

			return;
		}

		callMethod(designator.obj);

		if(designator.obj.getType()!=Tab.noType)
			Code.put(Code.pop);
	}

	@Override
	public void visit(StatementRead statement){

		Designator designator=statement.getDesignator();

		prepareLvalue(designator);

		if(designator.obj.getType()==Tab.charType)
			Code.put(Code.bread);
		else
			Code.put(Code.read);

		storeObj(designator.obj);
	}

	@Override
	public void visit(StatementPrint statement){

		int width;

		if(statement.getCommaNumberOptional() instanceof CommaNumberOptionalOne)
			width=((CommaNumberOptionalOne)statement.getCommaNumberOptional()).getPrintWidth();
		else
			width=statement.getExpr().struct==Tab.charType?1:5;

		Code.loadConst(width);

		if(statement.getExpr().struct==Tab.charType)
			Code.put(Code.bprint);
		else
			Code.put(Code.print);
	}

	@Override
	public void visit(StatementReturn statement){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	@Override
	public void visit(DoStart marker) {
		StatementDoWhileLabel labeled = ancestor(marker,StatementDoWhileLabel.class);
		String label = null;
		if(labeled != null) {
			label = labeled.getLabel();
		}
		dowhilestack.push(new DoWhileContext(Code.pc,label));
	}
	@Override
	public void visit(ConditionStart marker) {
//		telo vec zavrseno
		DoWhileContext context = dowhilestack.peek();
		context.conditionAdr = Code.pc;
		for(int adr : context.continues) {
			Code.fixup(adr);
		}

	}
	@Override
	public void visit(StatementDoWhile statement) {
//		generisan condition
		DoWhileContext context = dowhilestack.pop();
		Code.loadConst(0);
		Code.putFalseJump(Code.eq, context.bodyAdr);
//		kraj
		for(int adr : context.breaks) {
			Code.fixup(adr);
		}
	}
	public void visit(StatementDoWhileLabel statement) {
//		generisan condition
		DoWhileContext context = dowhilestack.pop();
		Code.loadConst(0);
		Code.putFalseJump(Code.eq, context.bodyAdr);
//		kraj
		for(int adr : context.breaks) {
			Code.fixup(adr);
		}
	}
	@Override
	public void visit(IfStart marker){

		Code.loadConst(0);
		Code.putFalseJump(Code.ne,0);

		IfContext context=new IfContext();

		context.falseAdr=Code.pc-2;

		ifStack.push(context);
	}

	@Override
	public void visit(ElseStart marker){

		IfContext context=ifStack.peek();

		Code.putJump(0);

		context.endAdr=Code.pc-2;

		Code.fixup(context.falseAdr);
	}

	@Override
	public void visit(StatementIfElse statement){

		IfContext context=ifStack.pop();

		if(context.endAdr!=-1)
			Code.fixup(context.endAdr);
		else
			Code.fixup(context.falseAdr);
	}

	@Override
	public void visit(ForConditionStart marker){
		forStack.push(new ForContext(Code.pc));
	}

	@Override
	public void visit(ForUpdateStart marker){

		ForContext context=forStack.peek();
		StatementFor statement=ancestor(marker,StatementFor.class);

		if(statement.getConditionOptional() instanceof ConditionOptionalOne){

			Code.loadConst(0);
			Code.putFalseJump(Code.ne,0);

			context.falseAdr=Code.pc-2;
		}

		Code.putJump(0);

		context.bodyAdr=Code.pc-2;
		context.updateAdr=Code.pc;
	}

	@Override
	public void visit(ForBodyStart marker){

		ForContext context=forStack.peek();

		Code.putJump(context.conditionAdr);

		Code.fixup(context.bodyAdr);
	}

	@Override
	public void visit(StatementBreak statement){

		SyntaxNode parent=statement.getParent();

		while(parent!=null){
			if(parent instanceof StatementFor){
				Code.putJump(0);
				forStack.peek().breaks.add(Code.pc-2);
				return;
			}
			if(parent instanceof StatementDoWhile || parent instanceof StatementDoWhileLabel){
				Code.putJump(0);	
				dowhilestack.peek().breaks.add(Code.pc-2);
				return;
			}
			parent=parent.getParent();
		}

	}
	@Override
	public void visit(StatementBreakLabel statement){

		String labelBreak = statement.getLabelBreak();
		for(DoWhileContext context : dowhilestack) {
			if(labelBreak.equals(context.label)) {
				Code.putJump(0);
				context.breaks.add(Code.pc-2);
				return;
			}
		}

	}

	@Override
	public void visit(StatementContinue statement){
		SyntaxNode parent=statement.getParent();

		while(parent!=null){
			if(parent instanceof StatementFor){
				Code.putJump(forStack.peek().updateAdr);
				return;
			}
			if(parent instanceof StatementDoWhile){
				Code.putJump(0);
				dowhilestack.peek().continues.add(Code.pc-2);
				return;
			}
			parent=parent.getParent();
		}
	}

	@Override
	public void visit(StatementFor statement){

		ForContext context=forStack.pop();

		Code.putJump(context.updateAdr);

		if(context.falseAdr!=-1)
			Code.fixup(context.falseAdr);

		for(int adr:context.breaks)
			Code.fixup(adr);
	}

	@Override
	public void visit(FindAnyStart marker){

		FindAnyContext context=findAnyStack.peek();

		loadDesignator(context.source);

		storeLocal(context.sourceSlot);
	}

	@Override
	public void visit(StatementFindAny statement){

		FindAnyContext context=findAnyStack.pop();

		Struct elemType=context.source.obj.getType().getElemType();

		storeLocal(context.valueSlot);

		restoreAddress(
			context.destination,
			context.destBaseSlot,
			context.destIndexSlot
		);

		Code.loadConst(0);

		storeObj(context.destination.obj);

		Code.loadConst(0);

		storeLocal(context.indexSlot);

		int loopAdr=Code.pc;

		loadLocal(context.indexSlot);
		loadLocal(context.sourceSlot);

		Code.put(Code.arraylength);

		Code.putFalseJump(Code.lt,0);

		int endLoopAdr=Code.pc-2;

		loadLocal(context.sourceSlot);
		loadLocal(context.indexSlot);

		Obj elem=new Obj(
			Obj.Elem,
			"$findAnyElem",
			elemType
		);

		loadObj(elem);

		loadLocal(context.valueSlot);

		Code.putFalseJump(Code.eq,0);

		int nextAdr=Code.pc-2;

		restoreAddress(
			context.destination,
			context.destBaseSlot,
			context.destIndexSlot
		);

		Code.loadConst(1);

		storeObj(context.destination.obj);

		Code.putJump(0);

		int endAdr=Code.pc-2;

		Code.fixup(nextAdr);

		loadLocal(context.indexSlot);
		Code.loadConst(1);
		Code.put(Code.add);

		storeLocal(context.indexSlot);

		Code.putJump(loopAdr);

		Code.fixup(endLoopAdr);
		Code.fixup(endAdr);
	}

	@Override
	public void visit(MapStart marker){

		MapContext context=mapStack.peek();

		Struct sourceElem=context.source.obj.getType().getElemType();
		Struct destinationElem=context.destination.obj.getType().getElemType();

		loadDesignator(context.source);

		storeLocal(context.sourceSlot);

		loadLocal(context.sourceSlot);

		Code.put(Code.arraylength);
		Code.put(Code.newarray);
		Code.put(destinationElem==Tab.charType?0:1);

		storeLocal(context.resultSlot);

		Code.loadConst(0);

		storeLocal(context.indexSlot);

		context.loopAdr=Code.pc;

		loadLocal(context.indexSlot);
		loadLocal(context.sourceSlot);

		Code.put(Code.arraylength);

		Code.putFalseJump(Code.lt,0);

		context.endAdr=Code.pc-2;

		loadLocal(context.sourceSlot);
		loadLocal(context.indexSlot);

		Obj elem=new Obj(
			Obj.Elem,
			"$mapSourceElem",
			sourceElem
		);

		loadObj(elem);

		storeObj(context.iterator);
	}

	@Override
	public void visit(StatementMap statement){

		MapContext context=mapStack.pop();

		Struct destinationElem=context.destination.obj.getType().getElemType();

		storeLocal(context.valueSlot);

		loadLocal(context.resultSlot);
		loadLocal(context.indexSlot);
		loadLocal(context.valueSlot);

		Obj elem=new Obj(
			Obj.Elem,
			"$mapDestinationElem",
			destinationElem
		);

		storeObj(elem);

		loadLocal(context.indexSlot);
		Code.loadConst(1);
		Code.put(Code.add);

		storeLocal(context.indexSlot);

		Code.putJump(context.loopAdr);

		Code.fixup(context.endAdr);

		restoreAddress(
			context.destination,
			context.destBaseSlot,
			context.destIndexSlot
		);

		loadLocal(context.resultSlot);

		storeObj(context.destination.obj);
	}
}