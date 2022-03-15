package assignment.proxyservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProxyConflictException extends ResponseStatusException {
    public ProxyConflictException(String name, Throwable e) {
        super(HttpStatus.CONFLICT, String.format("Proxy(name=%s) conflict", name), e);
    }
}
