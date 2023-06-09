import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SmallScan {
    ArrayList<Character> whiteSpace = new ArrayList<>(Arrays.asList(' ', '\n', '\t', '\r'));
    ArrayList<String> keyword = new ArrayList<>(Arrays.asList("program", "program_begin", "integer", "if", "begin", "display", "end", "elseif", "else", "while", "break", "program_end"));
    ArrayList<String> overlapOperator = new ArrayList<>(Arrays.asList("<",">", "=", "!"));
    ArrayList<String> operator = new ArrayList<>(Arrays.asList("+", "-", "*", "/", ".", "!"));
    ArrayList<String> overlappedOperator = new ArrayList<>(Arrays.asList("="));
    ArrayList<String> specialChar = new ArrayList<>(Arrays.asList("(", ")", ","));
    ArrayList<String> statementTerminator = new ArrayList<>(Arrays.asList(";"));
    ArrayList<Token> tokens = new ArrayList<>();

    public static void main(String[] args) {
        SmallScan smallScan = new SmallScan();
        smallScan.run(args);
    }

    private void run(String[] args){
        String testCaseString = "./" + args[0];
        String oneLine = null;

        try {
            byte[] content = Files.readAllBytes(Path.of(testCaseString));
            oneLine = new String(content);
            scan(oneLine);
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void scan(String oneLine){

        int i = 0;
        String previousString = "";
        String previousState = "0";
        String currentState = "0";
        char currentType = 'N';
        // L: Letter, O: Operator, Q: Quote, D: Digit, W: White space, N: Null
        for(i = 0; i < oneLine.length(); i++){
            Character oneChar = oneLine.charAt(i);
            currentType = detectCurrentType(oneChar);
            currentState = calculateState(previousState, currentType);

            if(isDelimiter(currentState)){
                if(currentState.equals("2")){
                    previousString += "\"";
                    i++;
                    StringLiteral str = new StringLiteral("STRING_LITERAL", previousString);
                    tokens.add(str);
                }
                else if (currentState.equals("4")) {
                    previousString += oneChar.toString();
                    i++;

                    Operator op = new Operator("OPERATOR", previousString);
                    tokens.add(op);
                }
                else if (currentState.equals("5")) {
                    if(previousString.equals("")){                                          // from none
                        Operator op2 = new Operator("OPERATOR", oneChar.toString());
                        tokens.add(op2);
                        i++;
                    }
                    else{                                                                   // from state 3
                        Operator op2 = new Operator("OPERATOR", previousString);
                        tokens.add(op2);
                    }
                }
                else if (currentState.equals("7")) {
                    if (keyword.contains(previousString)){
                        Keyword kw = new Keyword("KEYWORD", previousString);
                        tokens.add(kw);
                    }
                    else{
                        Identifier id = new Identifier("IDENTIFIER", previousString);
                        tokens.add(id);
                    }
                }
                else if (currentState.equals("9")) {
                    previousString += "";
                }
                else if (currentState.equals("12")) {
                    Number num = new Number("NUMBER", previousString);
                    tokens.add(num);
                }
                else if (currentState.equals("13")) {
                    Number num = new Number("NUMBER", previousString);
                    tokens.add(num);
                }
                else if (currentState.equals("14")) {
                    StatementTerminator statement_terminator = new StatementTerminator("STATEMENT_TERMINATOR", oneChar.toString());
                    tokens.add(statement_terminator);
                    i++;
                }
                else if (currentState.equals("15")) {
                    SpecialChar sc = new SpecialChar("SPECIAL_CHAR", oneChar.toString());
                    tokens.add(sc);
                    i++;
                }
                previousString = "";
                if(currentState.equals("14") || currentState.equals("15"))
                    previousState = "0";
                else{
                    do {
                        previousState = calculateState("0", detectCurrentType(oneLine.charAt(i)));

                        Character t = oneLine.charAt(i);
                        if (previousState.equals("5")) {

                            Operator op2 = new Operator("OPERATOR", t.toString());
                            tokens.add(op2);
                            i++;
                        }
                        else if (previousState.equals("14")) {
                            StatementTerminator statement_terminator = new StatementTerminator("STATEMENT_TERMINATOR", t.toString());
                            tokens.add(statement_terminator);
                            i++;
                        }
                        else if (previousState.equals("15")) {
                            SpecialChar sc = new SpecialChar("SPECIAL_CHAR", t.toString());
                            tokens.add(sc);
                            i++;
                        }
                        else if (previousState.equals("8"))
                            break;
                        else {
                            Character ch = oneLine.charAt(i);
                            previousString += ch.toString();
                            break;
                        }
                    } while (true);

                }

            }
            else{
                if(!currentState.equals("8"))
                    previousString += oneChar.toString();
                previousState = currentState;
            }

        }

        if (previousString.length() > 0) {
            String tempState = previousState;
            if (tempState.equals("0")){
                Character ttype = detectCurrentType(previousString.charAt(previousString.length() - 1));
                tempState = calculateState(previousState, ttype);
            }
            if(tempState.equals("1")){
                StringLiteral str = new StringLiteral("STRING_LITEAL", previousString);
                tokens.add(str);
            }
            else if(tempState.equals("3")){
                Operator op = new Operator("OPERATOR", previousString);
                tokens.add(op);
            }
            else if(tempState.equals("6")){
                if(keyword.contains(previousString)){
                    Keyword kwd = new Keyword("KEYWORD", previousString);
                    tokens.add(kwd);
                }
                else {
                    Identifier id = new Identifier("IDENTIFIER", previousString);
                    tokens.add(id);
                }
            }
            else if (tempState.equals("10") || previousState.equals("11")) {
                Number num = new Number("NUMBER", previousString);
                tokens.add(num);
            }
            else if (tempState.equals("16")){
                Token token = new Token("UNKNOWN", previousString);
                tokens.add(token);
            }
        }

        EOF eof = new EOF("EOF", "");
        tokens.add(eof);
        printTokens(tokens);

    }

    private void printTokens(ArrayList<Token> tokens){
        for(Token each : tokens){
            each.getToken();
        }
    }

    private boolean isLetter(Character ch){
        if (('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z')) return true;
        return false;
    }

    private boolean isDigit(Character ch){
        if('0' <= ch && ch <= '9') return true;
        return false;
    }

    private boolean isOverlapOperator(Character ch){
        if(overlapOperator.contains(ch.toString())) return true;
        return false;
    }

    private boolean isQuote(Character ch){
        if(ch.toString().equals("\"")) return true;
        return false;
    }

    private boolean isUnderbar(Character ch){
        if(ch.toString().equals("_")) return true;
        return false;
    }

    private boolean isDot(Character ch){
        if (ch.toString().equals(".")) return true;
        return false;
    }

    private boolean isOperator(Character ch){
        if(operator.contains(ch.toString())) return true;
        return false;
    }

    private boolean isStateTerminator(Character ch){
        if(statementTerminator.contains(ch.toString())) return true;
        return false;
    }

    private boolean isSpecialChar(Character ch){
        if(specialChar.contains(ch.toString())) return true;
        return false;
    }

    private boolean isOverlapped(Character ch){
        if(overlappedOperator.contains(ch.toString())) return true;
        return false;
    }
    private boolean isWhiteSpace(Character ch){
        if(whiteSpace.contains(ch)) return true;
        return false;
    }

    private Character detectCurrentType(Character ch){
        if(isDigit(ch)) return 'D';
        else if (isLetter(ch)) return 'L';
        else if (isOverlapped(ch)) return 'E';               // =
        else if (isOverlapOperator(ch)) return 'V';          // = < > !
        else if (isDot(ch)) return 'T';
        else if (isQuote(ch)) return 'Q';
        else if (isUnderbar(ch)) return 'U';
        else if (isOperator(ch)) return 'O';
        else if (isStateTerminator(ch)) return 'S';
        else if (isSpecialChar(ch)) return 'P';
        else if (isWhiteSpace(ch)) return  'W';
        else return 'N';
    }

    private boolean isDelimiter(String str){
        if(str.equals("2") || str.equals("4") || str.equals("5") || str.equals("7") || str.equals("9") || str.equals("12") || str.equals("13") || str.equals("14") || str.equals("15")) return true;
        return false;
    }

    private String calculateState(String state, Character type){     // T
        if(state.equals("0")){
            if (type == 'D') return "10";
            else if (type == 'L') return "6";
            else if (type == 'V') return "3";
            else if (type == 'T') return "3";
            else if (type == 'Q') return "1";
            else if (type == 'U') return "16";
            else if (type == 'O') return "5";
            else if (type == 'S') return "14";
            else if (type == 'P') return "15";
            else if (type == 'E') return "3";
            else if (type == 'W') return "8";
            else return "16";
        }
        else if (state.equals("1")) {
            if (type == 'D') return "1";
            else if (type == 'L') return "1";
            else if (type == 'V') return "1";
            else if (type == 'T') return "1";
            else if (type == 'Q') return "2";
            else if (type == 'U') return "1";
            else if (type == 'O') return "1";
            else if (type == 'S') return "1";
            else if (type == 'P') return "1";
            else if (type == 'E') return "1";
            else if (type == 'W') return "1";
            else return "1";
        }
        else if (state.equals("2")) {
            return "0";
        }
        else if (state.equals("3")) {
            if (type == 'D') return "5";
            else if (type == 'L') return "5";
            else if (type == 'V') return "5";
            else if (type == 'T') return "5";
            else if (type == 'Q') return "5";
            else if (type == 'U') return "5";
            else if (type == 'O') return "5";
            else if (type == 'S') return "5";
            else if (type == 'P') return "5";
            else if (type == 'E') return "4";
            else if (type == 'W') return "5";
            else return "5";
        }
        else if (state.equals("4")) {
            return "0";
        }
        else if (state.equals("5")) {
            return "0";
        }
        else if (state.equals("6")) {
            if (type == 'D') return "6";
            else if (type == 'L') return "6";
            else if (type == 'V') return "7";
            else if (type == 'T') return "7";
            else if (type == 'Q') return "7";
            else if (type == 'U') return "6";
            else if (type == 'O') return "7";
            else if (type == 'S') return "7";
            else if (type == 'P') return "7";
            else if (type == 'E') return "7";
            else if (type == 'W') return "7";
            else return "7";
        }
        else if (state.equals("7")) {
            return "0";
        }
        else if (state.equals("8")) {
            if (type == 'D') return "9";
            else if (type == 'L') return "9";
            else if (type == 'V') return "9";
            else if (type == 'T') return "9";
            else if (type == 'Q') return "9";
            else if (type == 'U') return "9";
            else if (type == 'O') return "9";
            else if (type == 'S') return "9";
            else if (type == 'P') return "9";
            else if (type == 'E') return "9";
            else if (type == 'W') return "8";
            else return "9";
        }
        else if (state.equals("9")) {
            return "0";
        }
        else if (state.equals("10")) {
            if (type == 'D') return "10";
            else if (type == 'L') return "12";
            else if (type == 'V') return "12";
            else if (type == 'T') return "11";
            else if (type == 'Q') return "12";
            else if (type == 'U') return "12";
            else if (type == 'O') return "12";
            else if (type == 'S') return "12";
            else if (type == 'P') return "12";
            else if (type == 'E') return "12";
            else if (type == 'W') return "12";
            else return "12";
        }
        else if (state.equals("11")) {
            if (type == 'D') return "11";
            else if (type == 'L') return "13";
            else if (type == 'V') return "13";
            else if (type == 'T') return "13";
            else if (type == 'Q') return "13";
            else if (type == 'U') return "13";
            else if (type == 'O') return "13";
            else if (type == 'S') return "13";
            else if (type == 'P') return "13";
            else if (type == 'E') return "13";
            else if (type == 'W') return "13";
            else return "13";
        }
        else if (state.equals("12")) {
            return "0";
        }
        else if (state.equals("13")) {
            return "0";
        }
        else if (state.equals("14")) {
            return "0";
        }
        else if (state.equals("15")) {
            return "0";
        }
        else if (state.equals("16")) {
            return "16";
        }
        return "0";
    }
//
//    private ArrayList<String> tokenize(String token){
//        ArrayList<String> tokenList = new ArrayList<String>();
//        String tempToken = "";
//        for (int i = 0; i < token.length(); i++){
//            Character t = token.charAt(i);
//            String tempStr = t.toString();
//
//            if(operator.contains(tempStr)){
//                if(!tempToken.isBlank()){
//                    tokenList.add(tempToken);
//                }
//                tokenList.add(tempStr);
//                tempToken = "";
//            }
//            else {
//                tempToken += tempStr;
//            }
//        }
//        tokenList.add(tempToken);
//
//
//        return tokenList;
//    }
}
