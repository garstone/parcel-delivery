package kamenev.delivery.orderservice.errors;

import org.w3c.dom.Document;

public class WrongXmlCoordinatesException extends RuntimeException {

    private String text;

    public WrongXmlCoordinatesException(Document document) {
        this.text = "Wrong XML - does not contain lattitude or logitude: " + document.toString();
    }

}
