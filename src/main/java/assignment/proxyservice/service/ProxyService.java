package assignment.proxyservice.service;

import assignment.proxyservice.domain.ProxyType;
import assignment.proxyservice.model.ProxyDto;

import java.util.List;

public interface ProxyService {
    ProxyDto getProxy(long id);

    List<ProxyDto> getProxies(int page, int size);

    List<ProxyDto> searchProxies(String name, ProxyType type);

    ProxyDto createProxy(ProxyDto dto);
}
