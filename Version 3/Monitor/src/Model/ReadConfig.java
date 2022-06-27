package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadConfig {
        private static ReadConfig instance = null;
        private List<Server> servers = new ArrayList();

    public static ReadConfig getInstance(){
        if(ReadConfig.instance == null){
            ReadConfig.instance = new ReadConfig();

        }
        return instance;
    }

    public ReadConfig() {
        readConfigFile();
    }

    private void readConfigFile() {
        try {
            Server sv;
            File file = new File("config.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();

            NodeList listaServers = document.getElementsByTagName("server");

            for (int i = 0 ; i < listaServers.getLength() ; i++) {
                Node nodo = listaServers.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element server = (Element) nodo;
                    servers.add(new Server(Integer.parseInt(server.getElementsByTagName("port").item(0).getTextContent()), server.getElementsByTagName("address").item(0).getTextContent()));
                }
            }
        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
        }
    }

    public List<Server> getServers() {
        return servers;
    }
}
