import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	
	List<Token> tokens = new ArrayList<Token>();
	
	String accum = "";
	
	private Pattern sm = Pattern.compile("^;$");
	private Pattern var_kw = Pattern.compile("^var$");
        private Pattern do_kw = Pattern.compile("^do$");
        private Pattern while_kw = Pattern.compile("^while$");
        private Pattern loopOpen = Pattern.compile("^\\{$");
        private Pattern loopClose = Pattern.compile("^\\}$");
	private Pattern assign_op = Pattern.compile("^=$");
	private Pattern comp_op = Pattern.compile("^>|<|==|>=|<=|!=$");
	private Pattern incop = Pattern.compile("^\\+$");
	private Pattern multop = Pattern.compile("^\\*$");
	private Pattern divop = Pattern.compile("^\\/$");
	private Pattern decop = Pattern.compile("^\\-$");
	private Pattern digit = Pattern.compile("^0|[1-9]{1}[0-9]*$");
	private Pattern var = Pattern.compile("^[a-zA-Z]+$");
	private Pattern space = Pattern.compile("^\\s*$");
        private Pattern open = Pattern.compile("^\\($");
        private Pattern close = Pattern.compile("^\\)$");
	
	private Map<String, Pattern> keyWords = new HashMap<String, Pattern>();
	private Map<String, Pattern> otherTerminals = new HashMap<String, Pattern>();
	
	private int i;
	
	private String currentLucky = null;
	
	public Lexer() {
		// Keywords
		keyWords.put("VAR_KW", var_kw); 
                keyWords.put("DO_KW", do_kw);
                keyWords.put("WHILE_KW", while_kw);
		
		
		otherTerminals.put("VAR", var);
		otherTerminals.put("MULTOP", multop);
		otherTerminals.put("SM", sm);
		otherTerminals.put("ASSIGN_OP", assign_op);
		otherTerminals.put("DIVOP", divop);
		otherTerminals.put("INCOP", incop);
		otherTerminals.put("DECOP", decop);
                otherTerminals.put("COMPOP", comp_op);
		otherTerminals.put("DIGIT", digit);
		otherTerminals.put("WS", space);
                otherTerminals.put("OPEN", open);
                otherTerminals.put("CLOSE", close);
                otherTerminals.put("LOOP_OPEN", loopOpen);
                otherTerminals.put("LOOP_CLOSE", loopClose);
	}
	


	public void processFile(String filename) throws IOException {
		File file = new File(filename);
		Reader reader = new FileReader(file);
		BufferedReader breader = new BufferedReader(reader);
		String line;
		
                System.out.println("Lexer starting...\n");
		while((line = breader.readLine()) != null) {
			processString(line);
		}
		
		if (currentLucky == null) {
			throw new IOException("Expression: " + accum + " doesn't matches with any token\n");
		}
		
		System.out.println("TOKEN + "+currentLucky+" revognized with value "+accum);
		
		
		tokens.add(new Token(currentLucky, accum));
                System.out.println("\nLIST OF TOKENS\n____________");
		for (Token token: tokens) {
			System.out.println(token);
		}
	}

	public void processString(String line) {
		for (i=0; i<line.length(); i++) {
			accum = accum + line.charAt(i);
			processAccum();
		}
		
	}
	
	
	private void processAccum() {
		boolean found = false;
		
		for (String keyWordName : keyWords.keySet()) {
			Pattern newPattern = keyWords.get(keyWordName);
			Matcher match = newPattern.matcher(accum);
			if (match.matches()) {
				currentLucky = keyWordName;
				found = true;
			}
		}
			
			if (currentLucky != null && found) {
				System.out.println("TOKEN "+currentLucky+" recognized with value: "+accum.substring(0, accum.length()));
				tokens.add(new Token (currentLucky, accum.substring(0, accum.length())));
				accum = "";
				currentLucky = null;
			}
			
		for (String regExpName : otherTerminals.keySet()) {
			Pattern currentPattern = otherTerminals.get(regExpName);
			Matcher m = currentPattern.matcher(accum);
			if (m.matches()) {
				currentLucky = regExpName;
				found = true;
			}
			
		}
		if (currentLucky != null && !found) {
			System.out.println("TOKEN "+currentLucky+" recognized with value: "+accum.substring(0, accum.length()-1));
			tokens.add(new Token (currentLucky, accum.substring(0, accum.length()-1)));
			i--;
			accum = "";
			currentLucky = null;
		}
	}

	public List<Token> setTokens() {
		// TODO Auto-generated method stub
		return tokens;
	}
	

}
