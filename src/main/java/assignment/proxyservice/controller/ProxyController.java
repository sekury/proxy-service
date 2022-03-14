package assignment.proxyservice.controller;

import assignment.proxyservice.domain.ProxyType;
import assignment.proxyservice.model.ProxyDto;
import assignment.proxyservice.service.ProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/proxies")
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ProxyDto getProxy(@PathVariable("id") long id) {
        return proxyService.getProxy(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProxyDto> getAllProxies(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return proxyService.getProxies(page, size);
    }

    @GetMapping(
            path = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProxyDto> searchProxies(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "type", required = false) ProxyType type
    ) {
        return proxyService.searchProxies(name, type);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProxyDto createProxy(@Valid @RequestBody ProxyDto dto) {
        return proxyService.createProxy(dto);
    }
}
