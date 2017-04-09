package XmlReader;
import Logic.Arm;
import Memories.*;
import Other.Storage;
import Other.Tape;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
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
 * Created by master on 31.10.2016.
 */
public class AlgorithmReader {
    public static HashMap<String, Memory> readMemories() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document document = builder.parse(new File("templateStrorage.xml"));
        HashMap<String, Memory> memoriesMap = new HashMap<>();
        NodeList armsMemory = document.getElementsByTagName("memory");

        for (int j = 0; j < armsMemory.getLength(); j++) {
            NamedNodeMap atrs = armsMemory.item(j).getAttributes();
            switch (atrs.getNamedItem("type").getNodeValue()) {
                case ("Register"): {
                    String name = atrs.getNamedItem("name").getNodeValue();
                    memoriesMap.put(name, new Register(name, null));
                    break;
                }
                case ("Counter"): {
                    String name = atrs.getNamedItem("name").getNodeValue();
                    memoriesMap.put(name, new Counter(name, null));
                    break;
                }
                case ("Wagon"): {
                    String name = atrs.getNamedItem("leftName").getNodeValue() + "*" + atrs.getNamedItem("rightName").getNodeValue();
                    memoriesMap.put(name, new Wagon(atrs.getNamedItem("leftName").getNodeValue(), atrs.getNamedItem("rightName").getNodeValue(), null));
                    break;
                }
                case ("Table"): {
                    String name = atrs.getNamedItem("name").getNodeValue();
                    ArrayList<String> colnames = new ArrayList<>();
                    NodeList tableChildren = armsMemory.item(j).getChildNodes();
                    for (int k = 0; k < tableChildren.getLength(); k++) {
                        if (tableChildren.item(k).hasChildNodes()) {
                            NodeList columnNames = tableChildren.item(k).getChildNodes();
                            for (int m = 0; m < columnNames.getLength(); m++) {
                                if (columnNames.item(m).hasChildNodes()) {
                                    colnames.add(columnNames.item(m).getFirstChild().getNodeValue());
                                }
                            }
                        }
                    }
                    memoriesMap.put(name, new Table(name/*, null, colnames*/));
                    break;
                }
            }
        }

        //ПРОВЕРКА!
//        Set<String> keys = memoriesMap.keySet();
//        for(String key:keys){
//            System.out.println(memoriesMap.get(key));
//        }
        return memoriesMap;
    }
//    public static HashMap<String,Arm> readArms() throws ParserConfigurationException, IOException, SAXException {
//        HashMap<String,Arm> arms = new HashMap<>();
//
//        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
//        f.setValidating(false);
//        DocumentBuilder builder = f.newDocumentBuilder();
//        Document document = builder.parse(new File("templateStrorage.xml"));
//        NodeList nodeList = document.getElementsByTagName("arm");
//        for(int i=0;i<nodeList.getLength();i++) {
//            ArrayList<ArmLine> armLines = new ArrayList<>();
//
//
//            Node armNode = nodeList.item(i);
//            String nodeNumber = armNode.getAttributes().getNamedItem("begin").getNodeValue();
//            NodeList edges = armNode.getChildNodes();
//            for (int j = 0; j < edges.getLength(); j++) {
//                Logic.Condition condition = null;
//                ArrayList<Statement> statements = new ArrayList<>();
//                Node edge = edges.item(j);
//                if (edge.hasChildNodes()) {
//                    String currentNumber = edge.getAttributes().getNamedItem("end").getNodeValue();
//                    NodeList allLines = edge.getChildNodes();
//                    for (int k = 0; k < allLines.getLength(); k++) {
//                        Node line = allLines.item(k);
//                        switch (line.getNodeName()) {
//                            case ("predicate"):
//                                NodeList lineNodes = line.getChildNodes();
//                                for (int l = 0; l < lineNodes.getLength(); l++) {
//                                    if (lineNodes.item(l).hasChildNodes()) {
//                                        switch (lineNodes.item(l).getNodeName()) {
//                                            case ("alphabet"): {
//                                                condition = new Logic.Condition(null, new Alphabet(lineNodes.item(l).getFirstChild().getNodeValue()), currentNumber);
//                                                break;
//                                            }
//                                            case ("predicateText"): {
//                                                condition = new Logic.Condition(lineNodes.item(l).getFirstChild().getNodeValue(), null, currentNumber);
//                                                break;
//                                            }
////                                            case ("const"): {
////                                                //TODO:Реализуй меня полностью, но сначала в Condition
////                                                break;
////                                            }
//                                        }
//                                    }
//                                }
//                                break;
//                            case ("operation"): {
//                                NamedNodeMap atributes = line.getAttributes();
//                                statements.add(new Statement(atributes.getNamedItem("left").getNodeValue(),
//                                        Statement.getOperator(atributes.getNamedItem("operator").getNodeValue().toCharArray()), atributes.getNamedItem("right").getNodeValue()));
//                            }
//                        }
//                    }
//                    armLines.add(new ArmLine(nodeNumber, condition, statements));
//                }
//            }
//            arms.put(nodeNumber, new Arm(nodeNumber, armLines));
//        }
//        return arms;
//    }
//    public static Tape readTape() throws ParserConfigurationException, IOException, SAXException {
//        HashMap<String,Arm> arms = new HashMap<>();
//
//        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
//        f.setValidating(false);
//        DocumentBuilder builder = f.newDocumentBuilder();
//        Document document = builder.parse(new File("templateStrorage.xml"));
//        Tape tape = null;
//        NodeList description = document.getElementsByTagName("descriptive_part");
//        for(int i=0;i<description.getLength();i++){
//            if(description.item(i).hasChildNodes()){
//                Node descriptive=description.item(i);
//                NodeList descrParts=descriptive.getChildNodes();
//                for(int j=0;j<descrParts.getLength();j++){
//                    if(descrParts.item(j).hasChildNodes()){
//                        switch (descrParts.item(j).getNodeName()){
//                            case("tape"):{
//                                String tapeString = descrParts.item(j).getFirstChild().getNodeValue();
//                                char[] tapeChar = tapeString.toCharArray();
//                                Character[] tapeCharacter = new Character[tapeChar.length];
//                                for(int g=0;g<tapeCharacter.length;g++){
//                                    tapeCharacter[g]=new Character(tapeChar[g]);
//                                }
//
//                                tape=new Tape(new LinkedList<Character>(Arrays.asList(tapeCharacter)));
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return tape;
//
//    }
    public static HashMap<String,Alphabet> readAlphabets() throws IOException, SAXException, ParserConfigurationException {
        HashMap<String,Arm> arms = new HashMap<>();

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document document = builder.parse(new File("templateStrorage.xml"));
        HashMap<String,Alphabet> alphabetMap=new HashMap<>();
        NodeList description = document.getElementsByTagName("descriptive_part");
        for(int i=0;i<description.getLength();i++){
            if(description.item(i).hasChildNodes()){
                Node descriptive=description.item(i);
                NodeList descrParts=descriptive.getChildNodes();
                for(int j=0;j<descrParts.getLength();j++){
                    if(descrParts.item(j).hasChildNodes()){
                        switch (descrParts.item(j).getNodeName()){
                            case("alphabet"):{
                                NodeList alphNodes = descrParts.item(j).getChildNodes();
                                for(int b=0;b<alphNodes.getLength();b++){
                                    if(alphNodes.item(b).hasChildNodes()){
                                        NamedNodeMap alphAtr= alphNodes.item(b).getAttributes();
                                        alphabetMap.put(alphAtr.getNamedItem("name").getNodeValue(),
                                                new Alphabet(alphAtr.getNamedItem("name").getNodeValue(),alphAtr.getNamedItem("short_name").getNodeValue(),null));
                                        //alphNodes.item(b).getFirstChild().getNodeValue().toCharArray())
                                    }
                                }
                                break;
                            }
                            }
                        }
                    }
                }
            }
        return alphabetMap;
    }
    public static Storage readAlgorithm() throws IOException, SAXException, ParserConfigurationException {
        HashMap<String,Alphabet> alphabetMap=readAlphabets();
        //HashMap<String,Arm> arms = readArms();
       // Set<String> armsName =arms.keySet();
        Tape tape=new Tape("Cat");
        HashMap<String,Memory> memoriesMap=readMemories();
       // return new Storage(arms,memoriesMap,alphabetMap,tape);
        return null;
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        Storage storage=readAlgorithm();
        storage.printStorage();
    }

}
