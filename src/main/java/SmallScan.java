import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SmallScan {
    ArrayList<Character> whiteSpace = new ArrayList<>(Arrays.asList(' ', '\n', '\t'));
    ArrayList<String> keyword = new ArrayList<>(Arrays.asList("program", "program_begin", "integer", "if", "begin", "display", "end", "elesif", "else", "while", "break", "program_end"));
    ArrayList<String> operator = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "<", ">", "=", "(", ")", ",", ".", ";", "==", "<=", ">=", "!=", "!"));


    public static void main(String[] args) {
        SmallScan smallScan = new SmallScan();
        smallScan.run(args);
    }

    private void run(String[] args){
        String testCaseString = "../test/" + args[0];
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
        ArrayList<Token> tokens = new ArrayList<>();
        int i = 0;
        String previousString = "";
        char previousType = 'N';
        char currentType = 'N';
        // L: Letter, O: Operator, Q: Quote, D: Digit, W: White space, N: Null
        for(i = 0; i < oneLine.length(); i++){
            Character oneChar = oneLine.charAt(i);
            if(previousType == 'N'){    // from q0
                currentType = detectCurrentType(oneChar);
                previousType = currentType;
                previousString += oneChar;
            }
            else {
                currentType = detectCurrentType(oneChar);
                if(previousType == currentType){

                }
            }
        }
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

    private boolean isOperator(Character ch){
        if(operator.contains(ch.toString())) return true;
        return false;
    }

    private boolean isWhiteSpace(Character ch){
        if(whiteSpace.contains(ch)) return true;
        return false;
    }

    private Character detectCurrentType(Character ch){
        if(isDigit(ch)) return 'D';
        else if (isLetter(ch)) return 'L';
        else if (isOperator(ch)) return 'O';
        else if (isWhiteSpace(ch)) return  'W';
        else return 'N';
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
