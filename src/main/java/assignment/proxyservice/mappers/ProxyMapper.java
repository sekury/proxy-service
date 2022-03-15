package assignment.proxyservice.mappers;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.dto.ProxyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProxyMapper {
    @Mapping(target = "password", ignore = true)
    ProxyDto proxyToProxyDto(Proxy proxy);

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    Proxy proxyDtoToProxy(ProxyDto dto);

    List<ProxyDto> map(List<Proxy> proxies);

    @Named("encodePassword")
    static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
