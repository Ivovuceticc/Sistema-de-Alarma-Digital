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
    private Ubicacion ubicacion;
    private List<Receptor> receptores;
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

            //NodeList nList = document.getElementsByTagName("ubicacion");
            NodeList nList = document.getElementsByTagName("ubicacion");
            //System.out.println(document.getElementsByTagName("ubicacion").item(0));
            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;

            ubicacion = new Ubicacion(eElement.getElementsByTagName("direccion").item(0).getTextContent());
            ///obtengo lista de receptores
            NodeList listaReceptores = document.getElementsByTagName("Receptor");

            receptores = new ArrayList<>();

            for (int i = 0 ; i < listaReceptores.getLength() ; i++) {
                Node nodo = listaReceptores.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element receptor = (Element) nodo;
                    receptores.add(new Receptor(receptor.getElementsByTagName("ip").item(0).getTextContent(), Integer.parseInt(receptor.getElementsByTagName("puerto").item(0).getTextContent())));

                }
            }

        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
        }


    }

    public List<Receptor> getReceptores() {
        return receptores;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }
}
