import static java.lang.Integer.parseInt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class PolizProcessor {
	private Stack<Token> stack;
	private Map<String, Integer> varTable;
	private List<postfixToken> srcPoliz;
	postfixToken currToken;
	int size;
	int index;
	Token tmpValue1;
	Token tmpValue2;
        Token resToken;
	int val1, val2, res, doFlag = 0, doStart = -1, whileFin = -1, 
            compFlag = -1, whileStart = -1;
	String name, tokenName;

	public PolizProcessor(List<postfixToken> postfixTokens) {
		varTable = new HashMap<String, Integer>();
		stack = new Stack<Token>();
		this.srcPoliz = postfixTokens;
		
	}

	public void calculate() throws Exception {
	    index=0;
	    size = srcPoliz.size();
		while (index < size) {
                        
                        compFlag = -1;
			currToken = srcPoliz.get(index);
			if ((currToken.getName().equals("VAR")) ) {
				stack.push(currToken);
			}
			if ((currToken.getName().equals("DIGIT")) ) {
				stack.push(currToken);
			}
			if (currToken.getName().equals("ASSIGN_OP") ) {
                            tmpValue1 = stack.pop();
                            if (tmpValue1.getName().equals("DIGIT") ) {
                                tmpValue2 = stack.pop();
                                name = tmpValue2.getValue();
                                val1 = parseInt(tmpValue1.getValue() );
                                varTable.put(name, val1);
                            }
                            
                            if (tmpValue1.getName().equals("VAR") ) {
                                if (varTable.containsKey(tmpValue1.getName()) ) {
                                    val1 = varTable.get(tmpValue1.getName());
                                    tmpValue2 = stack.pop();
                                    name = tmpValue2.getValue();
                                    varTable.put(name, val1);
                                }
                                else {
                                    throw new Exception ("Variable " + 
                                              tmpValue1.getValue() + " missing in"
                                            + " the variables table.");
                                }
                            }
			}
			tokenName = currToken.getName();
			if ((tokenName == "MULTOP") || (tokenName == "INCOP") || (tokenName == "DECOP") ||
                                (tokenName == "DIVOP") || (tokenName == "COMPOP")) {
                            tmpValue1 = stack.pop();
                            if (tmpValue1.getName().equals("DIGIT") ) {
                                val1 = parseInt(tmpValue1.getValue() );
                            }
                            else if (tmpValue1.getName().equals("VAR")) {
                            	if (varTable.containsKey(tmpValue1.getValue()) ) {
                            		val1 = varTable.get(tmpValue1.getValue());
                            	}
                            	else {
                            		throw new Exception ("Variable " + 
                                            tmpValue2.getValue() + " missing in"
                                          + " the variables table.");
                            	}
                            }
                            
                            tmpValue2 = stack.pop();  
                            if (tmpValue2.getName().equals("DIGIT") ) {
                                    val2 = parseInt(tmpValue2.getValue() );
                                }
                                
                            else if (tmpValue2.getName().equals("VAR")) {
                                	if (varTable.containsKey(tmpValue2.getValue()) ) {
                                		val2 = varTable.get(tmpValue2.getValue());
                                	}
                                	else {
                                		throw new Exception ("Variable " + 
                                                tmpValue2.getValue() + " missing in"
                                              + " the variables table.");
                                	}
                            }
                            switch (currToken.getName()) {
                                case ("INCOP"): {
                                    res = val2 + val1;
                                    resToken = new Token("DIGIT", Integer.toString(res));
                                    stack.push(resToken);
                                    break;
                                }
                                case ("DECOP"): {
                                    res = val2 - val1;
                                    resToken = new Token("DIGIT", Integer.toString(res));
                                    stack.push(resToken);
                                    break;
                                }
                                case ("MULTOP"): {
                                    res = val2 * val1;
                                    resToken = new Token("DIGIT", Integer.toString(res));
                                    stack.push(resToken);
                                    break;
                                }
                                case ("DIVOP"):  {
                                    res = val2 / val1;
                                    resToken = new Token("DIGIT", Integer.toString(res));
                                    stack.push(resToken);
                                    break;
                                }
                                case ("COMPOP"): {
                                    switch (currToken.getValue() ) {
                                        case (">"): {
                                            if (val2 > val1) { compFlag = 1; }
                                            else { compFlag = 0; }
                                            break;
                                        }
                                        case ("<"): {
                                            if (val2 < val1) { compFlag = 1; }
                                            else { compFlag = 0; }
                                            break;
                                        }
                                        case ("=="): {
                                            if (val2 == val1) { compFlag = 1; }
                                            else { compFlag = 0; }
                                            break;
                                        }
                                        case (">="): {
                                            if (val2 >= val1) { compFlag = 1; }
                                            else { compFlag = 0; }
                                            break;
                                        }
                                        case ("<="): {
                                            if (val2 <= val1) { compFlag = 1; }
                                            else { compFlag = 0; }
                                            break;
                                        }
                                        case ("!="): {
                                            if (val2 != val1) { compFlag = 1; }
                                            else { compFlag = 0; }
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
			}
                        if (currToken.getName().equals("DO_KW")) {
                            doFlag = 1;
                            doStart = index + 2;
                        }
                        if (currToken.getName().equals("WHILE_KW") ) {
                            whileStart = index + 1;
                        }
                        if (currToken.getName().equals("LOOP_CLOSE")) {
                            if (doFlag == 0) {
                                whileFin = index + 1;
                                index = whileStart - 1;
                            }
                        }
                        if (doFlag == 1 && compFlag == 1) {
                            index = doStart;
                        }
                        else if (doFlag == 1 && compFlag == 0) {
                            doFlag = 0;
                            index++;
                        }
                        else if (doFlag == 0 && compFlag == 0) {
                            index = whileFin;
                        }
                        else {
                            index++;
                        }
				
		}
		System.out.print("\n\nSucessfully counted!\nVARMAP: " + varTable);
	}

}
