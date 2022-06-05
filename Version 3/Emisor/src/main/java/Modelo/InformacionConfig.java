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
    private Servidor servidor1;
    private Servidor servidor2;
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

            //NodeList nList = document.getElementsByTagName("config");

            NodeList nList = document.getElementsByTagName("Servidor1");
            Element eElement = (Element) nList.item(0);

            String ipServidor1 = eElement.getElementsByTagName("ip").item(0).getTextContent();
            String puertoServidor1 = eElement.getElementsByTagName("puerto").item(0).getTextContent();

            nList = document.getElementsByTagName("Servidor2");
            eElement = (Element) nList.item(0);

            String ipServidor2 = eElement.getElementsByTagName("ip").item(0).getTextContent();
            String puertoServidor2 = eElement.getElementsByTagName("puerto").item(0).getTextContent();

            nList = document.getElementsByTagName("direccion");
            eElement = (Element) nList.item(0);

            String ubicacion = eElement.getElementsByTagName("ubicacion").item(0).getTextContent();

            this.ubicacion = new Ubicacion(ubicacion);
            this.servidor1 = new Servidor(ipServidor1, Integer.parseInt(puertoServidor1));
            this.servidor2 = new Servidor(ipServidor2, Integer.parseInt(puertoServidor2));
        }

        catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
        }
    }
    public Servidor getServidor1()
    {
        return servidor1;
    }
    public Servidor getServidor2()
    {
        return servidor2;
    }
    public Ubicacion getUbicacion()
    {
        return this.ubicacion;
    }
}
