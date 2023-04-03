import java.io.BufferedReader;
import java.io.FileReader;

public class SmallScan {
    public static void main(String[] args) {
        SmallScan smallScan = new SmallScan();
        smallScan.run(args);
    }

    private void run(String[] args){
        String testCaseString = "../test/" + args[0];
        String str;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(testCaseString));
            while ((str = bufferedReader.readLine()) != null) {
                String[] oneLine = str.split(" |\n|\t");
                System.out.println(oneLine[1]);
            }
//            System.out.println(args[0]);
        } catch (Exception e){
            System.out.println("Failed to open the file");
        }

    }
}
