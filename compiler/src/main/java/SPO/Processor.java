package SPO;

import Memories.Memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Created by user on 28.03.2016.
 */
public class Processor {
    public static String count(String str, HashMap<String,Memory> memories) {

//        Scanner s=new Scanner(System.in);
//        String str = s.nextLine();
        if(memories.containsKey(str)) {
            return memories.get(str).read();
        } else {
            String tablename = null;
            String index = null;
            for(int i = 0; i < str.length(); i++) {
                if(str.charAt(i) == '.') {
                    tablename = str.substring(0,i);
                    index = str.substring(i+1, str.length());
                    break;
                }
            }
            String key = findTable(tablename, memories);
            if(key!=null) {
                return memories.get(key).read(index);
            }
            key= findWagon(str,memories);
            if(key!=null){
                return memories.get(key).read(str);
            }
        }
        if(str.contains("+")||str.contains("-")||str.contains("*")||str.contains("/")) {
            Lexer lex = new Lexer(str);
            ArrayList<Token> tokens = lex.strToTokens();
            Parser parser = new Parser(tokens);
            ExprNode e = parser.expression();
            WalkTree walkTree = new WalkTree();
            walkTree.walkTree(e);

            return Integer.toString(walkTree.calcTree(e));
        }
        return str;
    }

    public String read(String varName, HashMap<String, Memory> memories){
        if(memories.containsKey(varName)){
            return memories.get(varName).read();
        }else{
            String key=findWagon(varName,memories);
            if(key!=null){
                return memories.get(key).read(varName);
            }
        }
        return varName;
    }
//    String findWagon(String varName, HashMap<String, Memory> memories){
//        Set<String> keys = memories.keySet();
//        for(String key:keys) {
//            String[] parts = key.split("/*");
//            if (parts.length > 1) {
//                for (String part : parts) {
//                    if (part.equals(varName)) {
//                        return key;
//                    }
//                }
//            }
//        }
//        return null;
//    }

    static String findWagon(String varName, HashMap<String, Memory> memories){
        Set<String> keys = memories.keySet();
        for(String key:keys) {
            String[] parts = key.split("\\*");
            if (parts.length > 1) {
                for (String part : parts) {
                    if (part.equals(varName)) {
                        return key;
                    }
                }
            } else if (parts.length == 1)
                if(parts[0].equals(varName))
                    return key;
        }
        return null;
    }

    static String findTable(String varName, HashMap<String, Memory> memories) {
        Set<String> keys = memories.keySet();
        for(String key: keys) {
            if(Objects.equals(key, varName))
                return key;
        }
        return null;
    }

}
