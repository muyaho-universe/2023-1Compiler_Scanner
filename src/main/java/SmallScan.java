import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class SmallScan {
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
            System.out.println(e.getMessage());
        }

    }

    private void scan(String oneLine){
        try {
            for (int i = 0; i < oneLine.length(); i++){

            }
        } catch (Exception e) {

        }

    }
}
