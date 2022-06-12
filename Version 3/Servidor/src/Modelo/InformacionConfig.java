package Modelo;

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

public class InformacionConfig {
    private String puertoAlarma;
    private String puertoServidor;
    private String puertoMonitor;
    private static InformacionConfig instance = null;

    private InformacionConfig(){
        this.leeArchivoConfig();
    }

    //Se obtiene solo una vez toda la informacion del archivo de configuracion.
    public static synchronized InformacionConfig getInstance(){
        if(InformacionConfig.instance == null){
            InformacionConfig.instance = new InformacionConfig();
        }
        return instance;
    }

    private void leeArchivoConfig() {
        try {
            File file = new File("config.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("informacion");

            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;
            this.puertoAlarma = eElement.getElementsByTagName("puertoAlarma").item(0).getTextContent();
            this.puertoMonitor = eElement.getElementsByTagName("puertoMonitor").item(0).getTextContent();
            this.puertoServidor = eElement.getElementsByTagName("puertoServidor").item(0).getTextContent();
        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
        }
    }

    public int getPuertoAlarma(){
        return Integer.parseInt(this.puertoAlarma);
    }
    public int getPuertoServidor(){
        return Integer.parseInt(this.puertoServidor);
    }
    public int getPuertoMonitor(){
        return Integer.parseInt(this.puertoMonitor);
    }

}
