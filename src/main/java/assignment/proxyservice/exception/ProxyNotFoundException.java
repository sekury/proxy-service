package assignment.proxyservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProxyNotFoundException extends ResponseStatusException {
    public ProxyNotFoundException(long id) {
        super(HttpStatus.NOT_FOUND, String.format("Proxy(id=%d) not found", id));
    }
}
