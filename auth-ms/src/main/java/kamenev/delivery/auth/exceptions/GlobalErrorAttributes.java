package kamenev.delivery.auth.exceptions;

import kamenev.delivery.auth.exceptions.exception.AuthException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Throwable t = getError(webRequest);
        if (t instanceof AuthException) {

        }
        Map<String, Object> map = super.getErrorAttributes(
                webRequest, options);
        map.put("status", HttpStatus.BAD_REQUEST);
        map.put("message", "username is required");
        return map;
    }
}
