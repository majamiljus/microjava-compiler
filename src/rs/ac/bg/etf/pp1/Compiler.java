package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.mj.runtime.Code;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}


	public static void main(String[] args) throws Exception {

		Logger log = Logger.getLogger(Compiler.class);
		String testFile;

		if (args.length > 0) {
			testFile = args[0];
		}
		else {
			testFile = "test/apstraktna_klasa.mj";
		}

		String objFileName;

		if (args.length > 1) {
			objFileName = args[1];
		}
		else {
			objFileName = "test/apstraktna_klasa.obj";
		}

		log.info("-------------------- Test " + testFile + " --------------------");
		File sourceCode = new File(testFile);

		if (!sourceCode.exists()) {
			log.error("Source file [" + sourceCode.getAbsolutePath() + "] not found!");
			return;
		}

		if (!sourceCode.isFile()) {
			log.error("Source path does not represent a regular file: " + sourceCode.getAbsolutePath());
			return;
		}
		log.info("Compiling source file: " + sourceCode.getAbsolutePath());
		try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {

			Yylex lexer = new Yylex(br);
			MJParser p = new MJParser(lexer);
			Symbol s = p.parse();
			if (p.errorDetected) {
				log.error("Parsiranje nije uspesno zavrseno.");
				return;
			}
			if (s == null || !(s.value instanceof Program)) {
				log.error("Parser nije vratio ispravan koren sintaksnog stabla.");
				return;
			}
			Program prog = (Program) s.value;
			log.info("Parsiranje uspesno zavrseno.");
			
			log.info("------- SINTAKSNO STABLO");
			log.info(prog.toString(""));
			log.info("-------------------------");

			RuleVisitor v = new RuleVisitor();
			prog.traverseBottomUp(v);
			log.info("Print count calls = " + v.printCallCount);
			log.info("Deklarisanih promenljivih ima = " + v.varDeclCount);

			Tab.init();
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
			prog.traverseBottomUp(semanticAnalyzer);
			log.info("------- TABELA SIMBOLA");
			tsdump();
			log.info("-----------------------");
			
			if (!semanticAnalyzer.passed()) {
				log.error("Semanticka analiza nije uspesno zavrsena.");
				return;
			}
			log.info("Semanticka analiza uspesno zavrsena.");

			Code.pc=0;
			CodeGenerator codeGenerator=new CodeGenerator();
			prog.traverseBottomUp(codeGenerator);
			Code.dataSize=codeGenerator.getDataSize();
			Code.mainPc=codeGenerator.getMainPc();
			File objFile = new File(objFileName);

			if (objFile.exists()) {
				objFile.delete();
			}

			try (FileOutputStream fos = new FileOutputStream(objFile)) {
				Code.write(fos);
			}
			log.info("Generisanje koda uspesno zavrseno.");
			log.info("Obj fajl: " + objFile.getAbsolutePath());
			log.info("-------------------- Finished " + testFile + " --------------------");
		}

		catch (Exception e) {
			log.error("Greska prilikom obrade fajla " + testFile + ": " + e.getMessage(),e);
		}
	}



	public static void tsdump() {
		Tab.dump();
	}

}