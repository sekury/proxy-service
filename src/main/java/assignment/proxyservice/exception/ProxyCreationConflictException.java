package assignment.proxyservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProxyCreationConflictException extends ResponseStatusException {
    public ProxyCreationConflictException(Throwable cause) {
        super(HttpStatus.CONFLICT, "Proxy creation conflict", cause);
    }
}
