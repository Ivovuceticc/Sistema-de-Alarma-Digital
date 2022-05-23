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
    private String puerto;
    private String tipoSolicitud;
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

            NodeList nList = document.getElementsByTagName("informacion");

            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;
            /* Muestra ok
            System.out.println("Tipo de solicitud : " + eElement.getElementsByTagName("tiposolicitud").item(0).getTextContent());
            System.out.println("Puerto : " + eElement.getElementsByTagName("puerto").item(0).getTextContent());
            */
            this.tipoSolicitud = eElement.getElementsByTagName("tiposolicitud").item(0).getTextContent();
            this.puerto = eElement.getElementsByTagName("puerto").item(0).getTextContent();
        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
        }
    }

    public String getPuerto(){
        return this.puerto;
    }

    public String getTipoSolicitud(){
        return this.tipoSolicitud;
    }
}
