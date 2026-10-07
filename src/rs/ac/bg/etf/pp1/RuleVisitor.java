package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.StatementPrint;
import rs.ac.bg.etf.pp1.ast.VarDecl;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public  class RuleVisitor extends VisitorAdaptor{

	int printCallCount = 0;
	int varDeclCount = 0;

	Logger log = Logger.getLogger(getClass());

	@Override
	public void visit(VarDecl vardecl){
		varDeclCount++;
	}

    @Override
	public void visit(StatementPrint print) {
		printCallCount++;
	}

}
