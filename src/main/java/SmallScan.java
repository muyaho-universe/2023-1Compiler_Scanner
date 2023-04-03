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
    ArrayList<String> operator = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "<", ">", "=", "(", ")", ",", ".", ";"));
    

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
        try {
            String tempToken = "";
            ArrayList<Token> tokens = new ArrayList<>();
            for (int i = 0; i < oneLine.length(); i++){
                Character oneChar = oneLine.charAt(i);
                if (whiteSpace.contains(oneChar)){
                    if(!tempToken.isBlank()){
                        try {
                            Double t = Double.parseDouble(tempToken);
                            if (t % 1 == 0.0){
                                int tI = (int) Math.round(t);
                                Number number = new Number("NUMBER", Integer.toString(tI));
                                tokens.add(number);
//                                System.out.println("Number: " + tI);
                            }
                            else {
                                Number number = new Number("NUMBER", Double.toString(t));
                                tokens.add(number);
//                                System.out.println("Number: " + t);
                            }

                        } catch (Exception e){
                            if (tempToken.charAt(0) == '\"'){
                                while(oneLine.charAt(i) != '\"'){
                                    tempToken += oneLine.charAt(i);
                                    i++;
                                }
                                tempToken += "\"";
                                StringLiteral stringLiteral = new StringLiteral("String_Literal", tempToken);
                                tokens.add(stringLiteral);
                            }
                            else if (operator.contains(tempToken)){
                                Operator op = new Operator("OPERATOR", tempToken);
                                tokens.add(op);
                            }
                            else if(keyword.contains(tempToken)){
                                Keyword keyword1 = new Keyword("KEYWORD", tempToken);
                                tokens.add(keyword1);
                            }
                        }


                    }
                    tempToken = "";
                }

                else {
                    tempToken += oneChar;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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
