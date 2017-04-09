package SPO;

/**
 * Created by user on 28.03.2016.
 */
public class ExprNode {
    boolean  isNumber;
    ExprNode left;
    char binop;
    ExprNode right;
    int value;

    public ExprNode(ExprNode left, char binop, ExprNode right) {
        this.isNumber = false;
        this.left = left;
        this.binop = binop;
        this.right = right;
    }

    public ExprNode(int value) {
        this.isNumber = true;
        this.value = value;
    }
}
