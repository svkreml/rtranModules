package SPO;

import java.util.ArrayList;

/**
 * Created by user on 28.03.2016.
 */
public class Lexer {

    private String str;
    int pos=0;

    public Lexer(String str) {
        this.str = str;
    }

    /////////////////////////////////////////////////////
    ArrayList<Token> strToTokens() {
        ArrayList<Token> tokens = new ArrayList<>();
        int x = 0;
        while (x < str.length()) {
            char ch = str.charAt(x);
            if (isDigit(ch)) {
                int x0 = x;
                while (x < str.length() && isDigit(str.charAt(x))) {
                    x++;
                }
                tokens.add(new Token(Integer.parseInt(str.substring(x0,x))));
            }
            else if ((ch == '(') || (ch == ')') || (ch == '+') ||
                    (ch == '-') || (ch == '*') || (ch == '/')) {
                tokens.add(new Token(ch));
                x++;
            }


            else {
                x++;
            }
        }
        return tokens;
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch<='9';
    }
}
