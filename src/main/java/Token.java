public class Token {
    String type;
    String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public Token() {
    }

    public void getToken(){
        System.out.println(type + " " + value);
    }
}
