package assignment.proxyservice.service;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.domain.ProxyType;
import assignment.proxyservice.exception.ProxyConflictException;
import assignment.proxyservice.exception.ProxyNotFoundException;
import assignment.proxyservice.mappers.ProxyMapper;
import assignment.proxyservice.model.ProxyDto;
import assignment.proxyservice.repository.ProxyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
        log.info("Get proxy(id={})", id);
        return repository.findById(id).map(mapper::proxyToProxyDto)
                .orElseThrow(() -> {
                    log.error("proxy(id={}) not found", id);
                    return new ProxyNotFoundException(id);
                });
    }

    @Override
    public List<ProxyDto> getProxies(int page, int size) {
        log.info("Get all proxies(page={}, size={})", page, size);
        return mapper.map(repository.findAll(PageRequest.of(page, size)).getContent());
    }

    @Override
    public List<ProxyDto> searchProxies(String name, ProxyType type) {
        log.info("Search proxies(name={}, type={})", name, type);
        return mapper.map(findProxyByNameAndType(name, type));
    }

    @Override
    public ProxyDto createProxy(ProxyDto dto) {
        log.info("Create proxy(name={}, type={})", dto.getName(), dto.getType());
        try {
            var proxy = repository.save(mapper.proxyDtoToProxy(dto));
            return mapper.proxyToProxyDto(proxy);
        } catch (DataIntegrityViolationException e) {
            log.error("Exception occurred while creating proxy", e);
            throw new ProxyConflictException(dto.getName(), e);
        }
    }

    @Override
    public ProxyDto updateProxy(long id, ProxyDto dto) {
        try {
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
                    .orElseThrow(() -> {
                        log.error("proxy{id={}} not found", id);
                        return new ProxyNotFoundException(id);
                    });
        } catch (DataIntegrityViolationException e) {
            log.error("Exception occurred while updating proxy", e);
            throw new ProxyConflictException(dto.getName(), e);
        }
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
