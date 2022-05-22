package Modelo;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class InformacionConfig {
    private int puertoReceptor;
    private String ipServidor;
    private int puertoServidor;
    private static InformacionConfig instance = null;

    private InformacionConfig(){
        this.leeArchivoConfig();
    }

    //Se obtiene solo una vez toda la informacion del archivo de configuracion.
    public static InformacionConfig getInstance(){
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

            //NodeList nList = document.getElementsByTagName("config");

            NodeList nList = document.getElementsByTagName("Receptor");
            Element eElement = (Element) nList.item(0);

            String puertoReceptor = eElement.getElementsByTagName("puerto").item(0).getTextContent();

            nList = document.getElementsByTagName("Servidor");
            eElement = (Element) nList.item(0);

            String ipServidor = eElement.getElementsByTagName("ip").item(0).getTextContent();
            String puertoServidor = eElement.getElementsByTagName("puerto").item(0).getTextContent();

            this.puertoReceptor = Integer.parseInt(puertoReceptor);

            this.ipServidor = ipServidor;
            this.puertoServidor = Integer.parseInt(puertoServidor);

        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
        }
    }

    public int getPuertoReceptor(){
        return this.puertoReceptor;
    }
    public String getIpServidor(){
        return this.ipServidor;
    }
    public int getPuertoServidor(){
        return this.puertoServidor;
    }

}
