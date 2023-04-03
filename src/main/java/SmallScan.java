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
            for (int i = 0; i < oneLine.length(); i++){
                Character oneChar = oneLine.charAt(i);
                if (whiteSpace.contains(oneChar)){
                    if(!tempToken.isBlank()){
                        try {
                            Double t = Double.parseDouble(tempToken);
                            if (t % 1 == 0.0){
                                int tI = (int) Math.round(t);
//                                System.out.println("Number: " + tI);
                            }
                            else {
//                                System.out.println("Number: " + t);
                            }

                        } catch (Exception e){
                            if(keyword.contains(tempToken)){
//                                System.out.println("KEYWORD: " + tempToken);
                            }
                            else if (operator.contains(tempToken)){
//                                System.out.println("OPERATOR: " + tempToken);
                            }
                            else {
                                ArrayList<String> strings = tokenize(tempToken);
                                for (String tmp : strings){
                                    scan(tmp);
                                }
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

    private ArrayList<String> tokenize(String token){
        ArrayList<String> tokenList = new ArrayList<>();
        String tempToken = "";
        for (int i = 0; i < token.length(); i++){
            Character tempCharacter = token.charAt(i);
            if(operator.contains(tempCharacter)){
                if(!tempToken.isBlank()){
                    tokenList.add(tempToken);
                }
                tokenList.add(tempCharacter.toString());
            }
        }

        for (String t: tokenList){

        }

        return tokenList;
    }
}
