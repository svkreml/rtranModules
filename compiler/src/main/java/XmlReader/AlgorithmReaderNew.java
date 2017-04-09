package XmlReader;

import Logic.Arm;
import Logic.ArmLine;
import Logic.Condition;
import Logic.Statement;
import Memories.*;
import Other.AllStorage;
import Other.R_machine;
import Other.Storage;
import Other.Tape;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by master on 21.11.2016.
 */
public class AlgorithmReaderNew {
    public HashMap<String,Memory> memoryHashMap;
    public HashMap<String,Arm> arms = new HashMap<>();
    public HashMap<String, Alphabet> alphabetHashMap = new HashMap<>();
    public String filename;

    public AlgorithmReaderNew(String filename) {
        this.filename=filename;
    }

    public void readAll(){

    }
    public HashMap<String, Memory> readMemories() throws ParserConfigurationException, IOException, SAXException {
        HashMap<String, Memory> memoryHashMap = new HashMap<>();
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document document = builder.parse(new File(filename));
        NodeList memoriesNodeList =document.getElementsByTagName("memory_block").item(0).getChildNodes();
        for (int i=0; i<memoriesNodeList.getLength();i++){
            if (memoriesNodeList.item(i).getNodeName()=="memory"){
                Node currentMemory = memoriesNodeList.item(i);
                String name="";
                switch (currentMemory.getAttributes().getNamedItem("type").getNodeValue()){
                    case "Register":
                        name = currentMemory.getAttributes().getNamedItem("name").getNodeValue();
                        memoryHashMap.put(name, new Register(name,null));
                        break;
                    case "Counter":
                        name = currentMemory.getAttributes().getNamedItem("name").getNodeValue();
                        memoryHashMap.put(name,new Counter(name,null));
                        break;
                    case "Wagon":
                        String lname = currentMemory.getAttributes().getNamedItem("leftName").getNodeValue();
                        String rname = currentMemory.getAttributes().getNamedItem("rightName").getNodeValue();
                        memoryHashMap.put(lname+"*"+rname, new Wagon(lname,rname,null));
                        break;
//                    case "Table":
//                        name = currentMemory.getAttributes().getNamedItem("name").getNodeValue().replace("\"","");
//                        ArrayList<String> colNames = new ArrayList<>();
//                        NodeList tableChildren = currentMemory.getChildNodes();
//                        for(int j=0; j<tableChildren.getLength();j++){
//                            if(tableChildren.item(j).getNextSibling()!=null){
//                                if(tableChildren.item(j).getNextSibling().getNodeName()=="columnsName"){
//                                    NodeList columnChildren=tableChildren.item(j).getNextSibling().getChildNodes();
//                                    for(int k=0; k<columnChildren.getLength()&&columnChildren.item(k)!=null;k++){
//                                        if(columnChildren.item(k).getNodeName().equals("column")){
//                                            colNames.add(columnChildren.item(k).getFirstChild().getNodeValue());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        break;
                }
            }
        }
        this.memoryHashMap=memoryHashMap;
        return memoryHashMap;
    }
    public void readAlgorithm() throws ParserConfigurationException, IOException, SAXException {
        alphabetHashMap = new HashMap<>();
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document document = builder.parse(new File(filename));
        NodeList alphabetsNodeList =document.getElementsByTagName("abc");
        for(int m=0; m<alphabetsNodeList.getLength();m++){
            alphabetHashMap.put(alphabetsNodeList.item(m).getAttributes().getNamedItem("name").getNodeValue(),
                    new Alphabet(alphabetsNodeList.item(m).getAttributes().getNamedItem("name").getNodeValue(),alphabetsNodeList.item(m).getAttributes().getNamedItem("short_name").getNodeValue(),
                            alphabetsNodeList.item(m).getFirstChild().getNodeValue().toCharArray()));
        }
        NodeList algorithmNodeList = document.getElementsByTagName("arm");
        for (int m=0;m<algorithmNodeList.getLength();m++){
            String currentNumber =algorithmNodeList.item(m).getAttributes().getNamedItem("begin").getNodeValue();
            ArrayList<ArmLine> armLines = new ArrayList<>();
            NodeList edgesList = algorithmNodeList.item(m).getChildNodes();
            for (int n=0;n<edgesList.getLength();n++){
                Condition condition = null;
                ArrayList<Statement> statements=new ArrayList<>();
                if(edgesList.item(n).hasChildNodes()){
                    String endNumber=(edgesList.item(n).getAttributes().getNamedItem("end").getNodeValue());
                    NodeList insideEdgeList = edgesList.item(n).getChildNodes();
                    for(int o =0; o<insideEdgeList.getLength();o++){
                        switch (insideEdgeList.item(o).getNodeName()) {
                            case ("predicate"):
                                switch (insideEdgeList.item(o).getAttributes().getNamedItem("type").getNodeValue()) {
                                    case ("alphabet"):
                                        String alphName=insideEdgeList.item(o).getFirstChild().getNodeValue();
                                        if(alphabetHashMap.containsKey(alphName)){
                                            condition= new Condition(alphabetHashMap.get(alphName));
                                        }else{
                                            for (String key:alphabetHashMap.keySet()){
                                                if(alphabetHashMap.get(key).getName().equals(alphName)){
                                                    condition=new Condition(alphabetHashMap.get(key));
                                                }
                                            }
                                            if (condition==null)
                                                System.err.println("Нет такого алфавита "+alphName);
                                        }
                                        break;
                                    case("expression"):
                                        Memory left=null,right = null;
                                        String sign=null;
                                        String name="";
                                        NodeList exprArgs = insideEdgeList.item(o).getChildNodes();
                                        for (int p=0; p<exprArgs.getLength();p++){
                                            if (exprArgs.item(p).getNodeName().equals("left")) {
                                                name=exprArgs.item(p).getAttributes().getNamedItem("value").getNodeValue();
                                                if(memoryHashMap.containsKey(name)) {
                                                    left = memoryHashMap.get(name);
                                                }else{
                                                    System.err.println("Нет памяти "+ name);
                                                }
                                                continue;
                                            }
                                            if (exprArgs.item(p).getNodeName().equals("right")){
                                                name=exprArgs.item(p).getAttributes().getNamedItem("value").getNodeValue();
                                                if(memoryHashMap.containsKey(name)){
                                                    right=memoryHashMap.get(name);
                                                }else{
                                                    System.err.println("Нет памяти "+ name);
                                                }
                                                continue;
                                            }
                                            if (exprArgs.item(p).hasChildNodes()){
                                                sign=exprArgs.item(p).getFirstChild().getNodeValue();
                                            }
                                        }
                                        condition= new Condition(left,sign,right);
                                        break;
                                    case ("string"):
                                        String text=insideEdgeList.item(o).getFirstChild().getNodeValue();
                                        condition=new Condition(text);
                                        break;
                                    case ("memory"):
                                        String memoryName=insideEdgeList.item(o).getFirstChild().getNodeValue();
                                        Memory memory=null;
                                        if(memoryHashMap.containsKey(memoryName)){
                                            memory=memoryHashMap.get(memoryName);
                                        }else{
                                            System.err.println("Нет памяти "+ memoryName);
                                        }
                                        condition= new Condition(memory);
                                        break;
                                }
                                break;
                            case ("operation"):
                                String left=null,right = null, operator = null;
                                String name="";
                                NodeList exprArgs = insideEdgeList.item(o).getChildNodes();
                                for (int p=0; p<exprArgs.getLength();p++){
                                    if (exprArgs.item(p).getNodeName().equals("left")) {
                                        left=exprArgs.item(p).getAttributes().getNamedItem("value").getNodeValue();
                                        continue;
                                    }
                                    if (exprArgs.item(p).getNodeName().equals("right")){
                                        right=exprArgs.item(p).getAttributes().getNamedItem("value").getNodeValue();
                                        continue;
                                    }
                                    if (exprArgs.item(p).hasChildNodes()){
                                        operator=exprArgs.item(p).getFirstChild().getNodeValue();
                                    }
                                }
                                statements.add(new Statement(left, Statement.getOperator(operator),right));
                                break;

//
//
                        }

                    }
                    armLines.add(new ArmLine(currentNumber,condition,statements,endNumber));

                }
            }
            arms.put(currentNumber,new Arm(currentNumber,armLines));
        }
    }
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        AlgorithmReaderNew algorithmReader = new AlgorithmReaderNew("templateStrorageTest.xml");
        algorithmReader.readMemories();
        algorithmReader.readAlgorithm();
        Tape tape = new Tape("perfectapple#");
        Storage storage = new Storage(algorithmReader.arms,algorithmReader.memoryHashMap,algorithmReader.alphabetHashMap);
        AllStorage allStorage = new AllStorage(storage,tape);
        R_machine r_machine = new R_machine(allStorage);
        r_machine.run();

    }

}
