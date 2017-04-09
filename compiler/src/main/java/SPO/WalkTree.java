package SPO;

import java.util.Stack;

/**
 * Created by user on 28.03.2016.
 */
public class WalkTree {
    void walkTree(ExprNode node)
    {
        if (node.isNumber) {
//            System.out.println(node.value);
        }
        else {
            walkTree(node.left);
            walkTree(node.right);
//            System.out.println(node.binop);
        }
    }
    Stack<Integer> numbers = new Stack<>();
    //ArrayList<int> numbers(2);
    private void walkTree2(ExprNode node)
    {
        if (node.isNumber) {
            numbers.push(node.value);
        }
        else {
            walkTree2(node.left);
            walkTree2(node.right);
            //int x, y;
            int x, y;
            x = numbers.pop();
            y = numbers.pop();
            switch (node.binop) {
                case '+':
                    numbers.push(x + y);
                    break;
                case '-':
                    numbers.push(y - x);
                    break;
                case '*':
                    numbers.push(y * x);
                    break;
                case '/':
                    numbers.push(y / x);
                    break;
            }

        }
    }

    int calcTree(ExprNode expr) {
        numbers.clear();
        walkTree2(expr);
        return numbers.pop();
    }
}
