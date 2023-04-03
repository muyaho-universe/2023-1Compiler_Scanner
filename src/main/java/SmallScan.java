import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class SmallScan {
    ArrayList<Character> whiteSpace = new ArrayList<>(Arrays.asList(' ', '\n', '\t'));
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
            String temp = "";
            for (int i = 0; i < oneLine.length(); i++){
                Character oneChar = oneLine.charAt(i);
                if (whiteSpace.contains(oneChar)){
                    if(!temp.isBlank()){
                        System.out.println(temp);
                    }
                    temp = "";
                }
                else {
                    temp += oneChar;
                }
            }

        } catch (Exception e) {

        }

    }
}
