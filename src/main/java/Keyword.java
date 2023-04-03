public class Keyword extends Token{
    String type;
    String value;

    @Override
    public void getToken() {
        System.out.println(type + " : " + value);
    }
}
