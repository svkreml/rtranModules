package SPO;

import java.util.ArrayList;

/**
 * Created by user on 28.03.2016.
 */
public class Parser {
    private ArrayList<Token> tokens;
    private int pos = 0;
    public int sign = 0;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    boolean end() {
        return (tokens.size() == pos);
    }

    Token current() {
        if (!end()) {
            return tokens.get(pos);
        }
        else {
            Token end = new Token();
            return end;
        }
    }

    void next() {
        if (!end()) {
            pos++;
        }
    }

    void error(String message) {
        System.out.println(message);
        //exit(1);
    }

    boolean isNumber() {
        Token t = current();
        return t.type == TokenType.NUMBER;
    }

    boolean isSymbol(char c) {
        Token t = current();
        return t.type == TokenType.SYMBOL && t.c == c;
    }
///////////////////////////////////

    ExprNode binOpNode(ExprNode pLeft, char pBinop, ExprNode pRight)
    {
        return new ExprNode(pLeft, pBinop, pRight);
    }

    ExprNode numberNode(int pValue)
    {
        return new ExprNode(pValue);
    }

    ExprNode mult() {
        if (isNumber()) {
            int n = current().value;
            next();
            return numberNode(n);
        }
        else if (isSymbol('(')) {
            next();
            ExprNode e;
            if (sign==0){
                e = expression();
            }else {
                ExprNode e1 = expression();
                ExprNode e2 = new ExprNode(-1);
                e = new ExprNode(e1, '*', e2);
                sign=0;
            }
            if (!isSymbol(')')) {
                error("Missing )");
            }
            next();
            return e;
        }
        else if (isSymbol('+')||isSymbol('-')){
            ExprNode e = null;
            if (isSymbol('-')){
                next();
                ExprNode e1 = mult();
                ExprNode e2 = new ExprNode(-1);
                e = new ExprNode(e1, '*',e2);
            } else if (isSymbol('+')) {
                next();
                ExprNode e1 = mult();
                ExprNode e2 = new ExprNode(1);
                e = new ExprNode(e1, '*', e2);

            }
             return  e;
        }
        else {
            error("Unexpected symbol");
            return null;
        }
    }

    ExprNode expression() {
        ExprNode left = sum();
        while (!end()) {
            if (isSymbol('+') || isSymbol('-')) {
                char c = current().c;
                next();
                ExprNode right = sum();
                left = binOpNode(left, c, right);

            }
            else {
                break;
            }
        }
        return left;
    }

    ExprNode sum() {
        ExprNode left = mult();
        while (!end()) {
            if (isSymbol('*') || isSymbol('/')) {
                char c = current().c;
                next();
                ExprNode right = mult();
                left = binOpNode(left, c, right);
            }
            else {
                break;
            }
        }
        return left;
    }
}
