package SPO;

/**
 * Created by user on 28.03.2016.
 */
public class Token {
    TokenType type;
    //int value;
    int value;
    char c;

    public Token(int value) {
        this.type = TokenType.NUMBER;
        this.value = value;
    }

    public Token(char c) {
        this.type = TokenType.SYMBOL;
        this.c = c;
    }

    public Token() {
        this.type = TokenType.END;
    }

    @Override
    public String toString() {
        return type + "{" + value + "," + c + "}";
    }
}
