package assignment.proxyservice.mappers;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.model.ProxyDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProxyMapper {
    ProxyDto proxyToProxyDto(Proxy proxy);

    Proxy proxyDtoToProxy(ProxyDto dto);

    List<ProxyDto> map(List<Proxy> proxies);
}
