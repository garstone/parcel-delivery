package kamenev.delivery.orderservice.service;

import kamenev.delivery.orderservice.domain.Coordinates;
import kamenev.delivery.orderservice.errors.WrongXmlCoordinatesException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
@Slf4j
@NoArgsConstructor
public class TrackingService {

    private static Document convert(String xmlString) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlString)));
    }

    public Coordinates trackCourier(UUID id) {
        Document document;
        try {
            URL url = new URL("https://api.3geonames.org/?randomland=yes");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();

            if (status > 299) {
                log.warn("Tracking service does not respond");
                return new Coordinates("-", "-");
            }
            StringBuilder content = getContent(con);
            con.disconnect();
            document = convert(content.toString());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
        return parseXmlCoordinates(document);
    }

    private Coordinates parseXmlCoordinates(Document document) {
        NodeList nodeList = document.getElementsByTagName("major");
        int i = 0;
        String latitude = "";
        String longitude = "";
        do {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("latt")) {
                latitude = node.getNodeValue();
            }
            if (node.getNodeName().equals("longt")) {
                longitude = node.getNodeValue();
            }
        } while (i < nodeList.getLength());
        if (latitude.equals("") || longitude.equals("")) {
            throw new WrongXmlCoordinatesException(document);
        }
        return new Coordinates(latitude, longitude);
    }

    private StringBuilder getContent(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content;
    }
}
