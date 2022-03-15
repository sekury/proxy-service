package assignment.proxyservice.service;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.domain.ProxyType;
import assignment.proxyservice.exception.ProxyCreationConflictException;
import assignment.proxyservice.exception.ProxyNotFoundException;
import assignment.proxyservice.mappers.ProxyMapper;
import assignment.proxyservice.model.ProxyDto;
import assignment.proxyservice.repository.ProxyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
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
        return repository.findById(id).map(mapper::proxyToProxyDto)
                .orElseThrow(() -> new ProxyNotFoundException(id));
    }

    @Override
    public List<ProxyDto> getProxies(int page, int size) {
        return mapper.map(repository.findAll(PageRequest.of(page, size)).getContent());
    }

    @Override
    public List<ProxyDto> searchProxies(String name, ProxyType type) {
        return mapper.map(findProxyByNameAndType(name, type));
    }

    @Override
    public ProxyDto createProxy(ProxyDto dto) {
        try {
            var proxy = repository.save(mapper.proxyDtoToProxy(dto));
            return mapper.proxyToProxyDto(proxy);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ProxyCreationConflictException(e);
        }
    }

    @Override
    public ProxyDto updateProxy(long id, ProxyDto dto) {
        return repository.findById(id).map(proxy -> {
                    var newProxy = mapper.proxyDtoToProxy(dto);
                    proxy.setName(newProxy.getName());
                    proxy.setType(newProxy.getType());
                    proxy.setHostname(newProxy.getHostname());
                    proxy.setPort(newProxy.getPort());
                    proxy.setUsername(newProxy.getUsername());
                    proxy.setPassword(newProxy.getPassword());
                    proxy.setActive(newProxy.getActive());
                    return repository.save(proxy);
                })
                .map(mapper::proxyToProxyDto)
                .orElseThrow(() -> new ProxyNotFoundException(id));
    }

    @Override
    public void deleteProxy(long id) {
        repository.deleteById(id);
    }

    private List<Proxy> findProxyByNameAndType(String name, ProxyType type) {
        if (name != null && type != null) {
            return repository.findByNameAndType(name, type);
        }
        if (name != null) {
            return repository.findByName(name);
        }
        if (type != null) {
            return repository.findByType(type);
        }
        return Collections.emptyList();
    }
}
