public class SpecialChar extends Token{
    String type;
    String value;

    public SpecialChar(String type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public void getToken() {
        System.out.println(type + " : " + value);
    }
}
