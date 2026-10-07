package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;

public class MJTest {

    static {
        DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws IOException {
        Logger log = Logger.getLogger(MJTest.class);


        String[] testFiles = { "test/test301.mj", "test/test302.mj", "test/test303.mj" };

        for (String testFilePath : testFiles) {
            Reader br = null;
            try {
                File sourceCode = new File(testFilePath);
                log.info("\nCompiling source file: " + sourceCode.getAbsolutePath());

                if (!sourceCode.exists()) {
                    log.error("File not found: " + sourceCode.getAbsolutePath());
                    continue;
                }

                br = new BufferedReader(new FileReader(sourceCode));

                Yylex lexer = new Yylex(br);
                Symbol currToken = null;
                while ((currToken = lexer.next_token()).sym != sym.EOF) {
                    if (currToken != null && currToken.value != null) {
						log.info(currToken.toString() + " " + currToken.value.toString());
					}
                }

                log.info("Finished compiling: " + sourceCode.getName());

            } catch (Exception e) {
                log.error("Error while compiling " + testFilePath, e);
            } finally {
                if (br != null) {
					try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
				}
            }
        }
    }
}
