package XmlReader;

import Memories.*;
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
import java.util.Map;
import java.util.Set;

/**
 * Created by master on 31.10.2016.
 */
public class XmlReader {
    public Map<String, Memory> readXML() throws ParserConfigurationException, IOException, SAXException {
        Map<String, Memory> memoryMap = new HashMap<>();
        Map<String, Register> registerMap = new HashMap<>();
        Map<String, Counter> counterMap = new HashMap<>();
        Map<String, Wagon> wagonMap = new HashMap<>();
        Map<String, Table> tableMap = new HashMap<>();
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document document = builder.parse(new File("templateData.xml"));
        NodeList nodeList = document.getElementsByTagName("memory");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String memoryType = node.getAttributes().getNamedItem("type").getNodeValue();
            switch (memoryType) {
                case ("Register"): {
                    memoryMap.put(node.getAttributes().getNamedItem("name").getNodeValue(),
                            new Register(node.getAttributes().getNamedItem("name").getNodeValue(),node.getChildNodes().item(0).getNodeValue()));
                    break;
                }
                case ("Counter"): {
                    memoryMap.put(node.getAttributes().getNamedItem("name").getNodeValue(),
                            new Counter(node.getAttributes().getNamedItem("name").getNodeValue(),
                                    Integer.parseInt(node.getChildNodes().item(0).getNodeValue())));

                    break;
                }
                case ("Wagon"): {
                    NodeList nodeList1 = node.getChildNodes();
                    ArrayList<String> wagons = new ArrayList<>();
                    for (int j = 0; j < nodeList1.getLength(); j++) {
                        if (nodeList1.item(j).hasChildNodes()) {
                            wagons.add(nodeList1.item(j).getChildNodes().item(0).getNodeValue());
                        }
                    }
                    memoryMap.put((node.getAttributes().getNamedItem("leftName").getNodeValue()+'*'+
                            node.getAttributes().getNamedItem("rightName").getNodeValue()),
                            new Wagon(node.getAttributes().getNamedItem("leftName").getNodeValue(),
                                    node.getAttributes().getNamedItem("rightName").getNodeValue(),
                                    wagons));
                    break;
                }
                case("Table"):{
                    NodeList nodeList2= node.getChildNodes();
                    ArrayList<String> columnNames=new ArrayList<>();
                    ArrayList<ArrayList<String>> rows = new ArrayList<>();
                    for(int k=0;k<nodeList2.getLength();k++){
                        Node currentNode=nodeList2.item(k);
                        if (currentNode.hasChildNodes()){
                            switch(currentNode.getNodeName()){
                                case("columnsName"):
                                    NodeList columnsName = currentNode.getChildNodes();
                                    for(int x=0;x<columnsName.getLength();x++){
                                        if (columnsName.item(x).hasChildNodes()){
                                            columnNames.add(columnsName.item(x).getFirstChild().getNodeValue());
                                        }
                                    }
                                    break;
                                case("rows"):
                                    NodeList rowsList=currentNode.getChildNodes();
                                    for(int l=0;l<rowsList.getLength();l++){
                                        Node node1=rowsList.item(l);
                                        if(node1.hasChildNodes()){
                                            NodeList rowList=node1.getChildNodes();
                                            ArrayList<String> oneRow= new ArrayList<>();
                                            for(int z=0;z<rowList.getLength();z++){
                                                if (rowList.item(z).hasChildNodes()){
                                                    oneRow.add(rowList.item(z).getFirstChild().getNodeValue());
                                                }
                                            }
                                            rows.add(oneRow);
                                        }

                                    }
                                    memoryMap.put(node.getAttributes().getNamedItem("name").getNodeValue(),new Table(node.getAttributes().getNamedItem("name").getNodeValue()/*,rows,columnNames*/));
                                    break;

                            }
                        }
                    }


                }
            }
        }
        return memoryMap;
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        XmlReader xmlReader= new XmlReader();
        Map<String,Memory> memoryMap=xmlReader.readXML();
        System.out.println(memoryMap.keySet().size());
        Set<String> keys = memoryMap.keySet();
        for (String key:keys){
            System.out.println(memoryMap.get(key));
        }
    }
}
