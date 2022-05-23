package Modelo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class InformacionConfig {
    private static InformacionConfig instance = null;
    private Servidor servidor;
    private Ubicacion ubicacion;
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

            NodeList nList = document.getElementsByTagName("config");

            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;

            String ipserver = eElement.getElementsByTagName("ip").item(0).getTextContent();
            String puerto = eElement.getElementsByTagName("puerto").item(0).getTextContent();
            String ubicacion = eElement.getElementsByTagName("ubicacion").item(0).getTextContent();

            this.servidor = new Servidor(ipserver, Integer.parseInt(puerto));
            this.ubicacion = new Ubicacion(ubicacion);
        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
        }
    }

    public Servidor getServidor()
    {
        return servidor;
    }
    public Ubicacion getUbicacion()
    {
        return this.ubicacion;
    }
}
