package assignment.proxyservice.service;

import assignment.proxyservice.domain.ProxyType;
import assignment.proxyservice.mappers.ProxyMapper;
import assignment.proxyservice.model.ProxyDto;
import assignment.proxyservice.repository.ProxyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProxyServiceImpl implements ProxyService {

    private final ProxyRepository repository;
    private final ProxyMapper mapper;

    public ProxyServiceImpl(
            ProxyRepository repository,
            ProxyMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProxyDto getProxy(long id) {
        var proxy = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "proxy not found"));
        return mapper.proxyToProxyDto(proxy);
    }

    @Override
    public List<ProxyDto> getProxies(int page, int size) {
        return mapper.map(repository.findAll(PageRequest.of(page, size)).getContent());
    }

    @Override
    public List<ProxyDto> searchProxies(String name, ProxyType type) {
        return mapper.map(repository.findByNameOrType(name, type));
    }

    @Override
    public ProxyDto createProxy(ProxyDto dto) {
        var proxy = repository.save(mapper.proxyDtoToProxy(dto));
        return mapper.proxyToProxyDto(proxy);
    }
}
