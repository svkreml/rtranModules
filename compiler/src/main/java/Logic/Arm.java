package  Logic;

import java.util.ArrayList;

/**
 * Created by master on 31.10.2016.
 */

/**
 * Общее хранилище для одной вершины алгоритма.
 */
public class Arm {
    String currentNodeNumber;
    ArrayList<ArmLine> lines;

    /**
     *
     * @param currentNodeNumber номер текущей вершины
     * @param lines упорядоченный(!) список всех ребер, выходящих из этого узла
     */
    public Arm(String currentNodeNumber, ArrayList<ArmLine> lines) {
        this.currentNodeNumber = currentNodeNumber;
        this.lines = lines;
    }

    /**
     * Вывод в консоль номера вершины и всех ребер.
     */
    public void printArm(){
        System.out.println("current arm: "+currentNodeNumber);
        System.out.println("armLines:");
        for(ArmLine armLine:lines){
            armLine.printLine();
        }
    }
    public ArrayList<ArmLine> getLines(){
        return  lines;
    }


}
