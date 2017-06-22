import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Stack;

/*
lang -> (expr SM)*
expr -> declare | assign
declare -> VAR_KW VAR
assign -> VAR ASSIGN_OP stmt
stmt -> (OPEN)* stmt_unit (op stmt_unit)* (CLOSE)*
op -> INCOP|DECOP|MULTOP
stmt_unit -> VAR|DIGIT
do_while_loop -> DO_KW LOOP_OPEN expr SM LOOP_CLOSE WHILE_KW loop_cond
loop_cond -> OPEN VAR COMP_OP stmt_unit CLOSE
while_loop -> WHILE_KW loop_cond LOOP_OPEN lang LOOP_CLOSE
*/


public class Parser {
	    private List<Token> tokens;
        
        private Token currentToken;

        private Iterator<Token> tokenIterator;
        
        int bktCounter;


	public void lang() throws Exception {
                System.out.println("\nParser starting...");
		boolean exist = false;
                while (match() ) {
                    if (expr() ) {
                        if (!sm() ) {
                            throw new Exception("SM expexted, but " +
                            currentToken + " found.");
                        }
                    }
                    else if (!doWhileLoop() && !whileLoop() ) {
                        throw new Exception("Expression expexted, but " +
                        currentToken + " found.");
                    }
                    exist = true;
                }
                if (!exist) {
                    throw new Exception();
                }
                System.out.println("\nPARSER CHECK.....OK");
		
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
                tokenIterator = this.tokens.iterator();
		
	}
        
        public boolean match(){
        if ( tokenIterator.hasNext() ) {
            currentToken = tokenIterator.next();
            while (currentToken.getName().equals("WS") ) {
                currentToken = tokenIterator.next();
            }
            return true;
        }
        return false;
    }
        public boolean expr() throws Exception {
        if (declare() || assign() ) {
        } else {
            return false;
        }
        return true;
    }
        
        public boolean declare() throws Exception {
            if (varKw() ) {
                nextToken();
                if (!ws() ) {
                    throw new Exception("Ws expexted, but " + 
                            currentToken + " found");
                } else {
                nextToken();    
                if (!var() ) {
                    throw new Exception("Var expexted, but " + 
                            currentToken + " found");
                } else {
                    nextToken();
                }
              }
            }
            else {
                return false;
            }
            return true;
        }
        
        public boolean assign() throws Exception {
            bktCounter = 0;
            if (var() ) {
                nextToken();
                if (!assignOp() ) {
                    throw new Exception("Assign_operator expexted, but " + 
                            currentToken + " found");
                }
                else {
                    nextToken();
                    if (!stmt() ) {
                        throw new Exception("Statement expexted, but " + 
                            currentToken + " found");
                    }
                    else if (bktCounter < 0) {
                        bktCounter = bktCounter * (-1);
                        throw new Exception("ERROR: " + bktCounter + 
                                " opening bracket(s) missing.");
                    }
                    else if (bktCounter > 0) {
                        throw new Exception("ERROR: " + bktCounter + 
                                " closing bracket(s) missing.");
                    }
                }
            }
            else {
                return false;
            }
            return true;
                
        }
        
        public boolean stmt() throws Exception {
            if (stmt_unit() ) {
                do {
                    if (op() ) {
                        nextToken();
                        if (!stmt_unit() ) {
                            throw new Exception("Statement unit expexted, but " + 
                            currentToken + " found");
                        } 
                    }
                }
                while (op());
            } else { return false; }
            return true;
        }
        
        public boolean stmt_unit() throws Exception {
            while (open() ) {
                bktCounter++;
                nextToken();
            }
            if (var() || digit() ) {
                nextToken();
            } else {
                throw new Exception("Var or digit expexted, but " + 
                currentToken + " found");
            }
            while (close() ) {
                bktCounter--;
                nextToken();
            }
            return true;
        }
        
        public boolean op() throws Exception {
            if (incOp() || decOp() || multOp() || divOp() ) {
            } else {
                return false;
            }
            return true;
        }
        
        public boolean doWhileLoop() throws Exception {
            if (doKw() ) {
                nextToken();
                if (openLoop() ) {
                    nextToken();
                    while (expr() ) {
                        if (!sm() ) {
                           throw new Exception("SM expexted, but " +
                           currentToken + " found."); 
                        }
                        else { nextToken(); }
                    }
                    if (!closeLoop() ) {
                        throw new Exception("Loop closing bracket expexted, but " +
                        currentToken + " found.");
                    }
                    else { nextToken(); }
                    if (whileKw() ) {
                        nextToken();
                        if (!loopCond() ) {
                            throw new Exception("Loop condition expexted, but " +
                            currentToken + " found.");
                        }
                    }
                    else {
                        throw new Exception("'While' keyword expexted, but " +
                        currentToken + " found.");
                    }
                }
                else {
                    throw new Exception("Loop opening bracket expexted, but " +
                    currentToken + " found.");
                }
            }
            else {
                return false;
            }
            return true;
        }
      
        public boolean whileLoop() throws Exception {
            if (whileKw() ) {
            nextToken();  
            if (loopCond() ) {
                nextToken();
                if (openLoop() ) {
                    nextToken();
                    while (expr() ) {
                        if (!sm() ) {
                           throw new Exception("SM expexted, but " +
                           currentToken + " found."); 
                        }
                        else { nextToken(); }
                    }
                    if (!closeLoop() ) {
                        throw new Exception("Loop closing bracket expexted, but " +
                        currentToken + " found.");
                    }
                }
                else {
                    throw new Exception("Loop opening bracket expexted, but " +
                    currentToken + " found.");
                }
            }
            else {
               throw new Exception("Loop condition expexted, but " +
               currentToken + " found."); 
            }
        }
        else { return false; }
        return true;
        }
        
        public boolean loopCond() throws Exception {
            if (open() ) {
                nextToken();
                if (var() ) {
                    nextToken();
                    if (compOp() ) {
                        nextToken();
                        if (var() || digit() ) {
                            nextToken();
                            if (!close() ) {
                                throw new Exception("Closing bracket expexted, but " +
                                currentToken + " found.");
                            }
                        }
                        else {
                            throw new Exception("Var or digit expexted, but " +
                            currentToken + " found.");
                        }
                    }
                    else {
                        throw new Exception("Compare operator expexted, but " +
                        currentToken + " found.");
                    }
                }
                else {
                    throw new Exception("Var expexted, but " +
                    currentToken + " found.");
                }
            }
            else {
                throw new Exception("Opening bracket expexted, but " +
                currentToken + " found.");
            }
            
            return true;
        }
        
        public boolean digit() {
        return currentToken.getName().equals("DIGIT");
        }
        
        public boolean var() {
        return currentToken.getName().equals("VAR");
        }
        
        public boolean sm() {
        return currentToken.getName().equals("SM");
        }
        
        public boolean varKw() {
        return currentToken.getName().equals("VAR_KW");
        }
        
        public boolean assignOp() {
        return currentToken.getName().equals("ASSIGN_OP");
        }
        
        public boolean incOp() {
        return currentToken.getName().equals("INCOP");
        }
        
        public boolean decOp() {
        return currentToken.getName().equals("DECOP");
        }
        
        public boolean multOp() {
        return currentToken.getName().equals("MULTOP");
        }
        
        public boolean divOp() {
        return currentToken.getName().equals("DIVOP");
        }
        
        public boolean ws() {
        return currentToken.getName().equals("WS");
        }
        
        public boolean open() {
        return currentToken.getName().equals("OPEN");
        }
        
        public boolean close() {
        return currentToken.getName().equals("CLOSE");
        }
        
        public boolean openLoop() {
        return currentToken.getName().equals("LOOP_OPEN");
        }
        
        public boolean closeLoop() {
        return currentToken.getName().equals("LOOP_CLOSE");
        }
        
        public boolean doKw() {
        return currentToken.getName().equals("DO_KW");
        }
        
        public boolean whileKw() {
        return currentToken.getName().equals("WHILE_KW");
        }
        
        public boolean compOp() {
        return currentToken.getName().equals("COMPOP");
        }
        
        public Token nextToken() {
            if ( tokenIterator.hasNext() ) {
                currentToken = tokenIterator.next();
                while (currentToken.getName().equals("WS") ) {
                currentToken = tokenIterator.next();
                }
            }
            return currentToken;
        }

	public List<postfixToken> getPostfixTokens() throws Exception {
            List<postfixToken> postFix = new ArrayList<postfixToken>();
            Stack<postfixToken> stack = new Stack<postfixToken>();
            postfixToken tmp;
			
            tokenIterator = this.tokens.iterator();
			while (match() ) {
                            int pr = 4;
                            if (doKw() || openLoop() || closeLoop() ) {
                                postFix.add(new postfixToken(currentToken.getName(), currentToken.getValue()));
                            }
                            else if (whileKw() ) {
                                postFix.add(new postfixToken(currentToken.getName(), currentToken.getValue()));
                            }
                            else  {

                                    if (var() || digit() ) {
                                        postFix.add(new postfixToken(currentToken.getName(), currentToken.getValue()));
					}
                                    else if (assignOp() || compOp() ) {
                                        stack.push(new postfixToken(currentToken.getName(), currentToken.getValue()));
					}
                                    else if (open() ) {
                                        stack.push(new postfixToken(currentToken.getName(), currentToken.getValue()));
                                        pr = 3;
                                        }
                                    else if (close() ) {
                                        tmp = stack.pop();
                                        while (!tmp.getName().equals("OPEN") ) {
                                            postFix.add(tmp);
                                            tmp = stack.pop();
                                            }
                                        }
                                    else if (op() ) {
					if (incOp() || decOp() ) {
                                            if (pr <= 2) {
                                                tmp = stack.pop();
						while (pr <= 2) {
                                                    postFix.add(tmp);
                                                    tmp = stack.pop();
                                                    pr = checkPriority(tmp);
                                                    }
                                                stack.push(new postfixToken(tmp.getName(), tmp.getValue()));
						}
                                            stack.push(new postfixToken(currentToken.getName(), currentToken.getValue()));
                                            pr = 2;
                                            }
                                            else if (multOp() || divOp() ) {
						if (pr == 1) {
                                                    tmp = stack.pop();
                                                    while (pr == 1 ) {
							postFix.add(tmp);
                                                        tmp = stack.pop();
                                                        pr = checkPriority(tmp);
							}
                                                    stack.push(new postfixToken(tmp.getName(), tmp.getValue()));
                                                    }
						stack.push(new postfixToken(currentToken.getName(), currentToken.getValue()));
						pr = 1;
						}
							
                                    }
                                    else if (sm() ) {
                                        while (!stack.empty() ) {
                                        tmp = stack.pop();
                                        postFix.add(tmp);
                                        }
                                    }
                            }
			}
                        System.out.println("\nPostfix form:");
			for (int k = 0; k < postFix.size(); k++) {
				System.out.print(postFix.get(k).getValue());
			}
			return postFix;
		}
        
        public int checkPriority (Token tmpToken) {
            String name;
            int priority = 4;
            name = tmpToken.getName();
            if (name.equals("DECOP") || name.equals("INCOP") ) {
                priority = 2;
            }
            else if (name.equals("DIVOP") || name.equals("MULTOP") ) {
                priority = 1;
            }
            else if (name.equals("OPEN") ) {
                priority = 3;
            }
            return priority;
        }

}
