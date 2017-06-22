import java.io.IOException;
import java.util.List;


public class lab1 {
	static FileHelper fh = new FileHelper();
	private static List<postfixToken> getPostfixTokens;
	
	public static void main (String[] args) throws Exception {
		//wrongInput();
		validInput();
	}
	
	static void process (String file) throws Exception {
		Lexer lexer =  new Lexer();
		lexer.processFile(file);
		List<Token> tokens = lexer.setTokens();
		Parser parser = new Parser();
		parser.setTokens(tokens);
        try {
		   parser.lang();
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
        List<postfixToken> postfixTokens = parser.getPostfixTokens();
        PolizProcessor processor = new PolizProcessor(postfixTokens);
        processor.calculate();
	}
	
	static void validInput() throws Exception {
		fh.fileInput("valid-test.input");
		process("valid-test.input");
	}
	
	static void wrongInput() throws Exception {
		fh.fileInput("wrong-test.input");
		process("wrong-test.input");
	}

}
