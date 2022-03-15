package assignment.proxyservice.mappers;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.dto.ProxyDto;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ProxyMapperTest {

    private final ProxyMapper mapper = new ProxyMapperImpl();;

    @Test
    void proxyToProxyDto() {
        var dto = mapper.proxyToProxyDto(new Proxy());
        assertNotNull(dto);

        dto = mapper.proxyToProxyDto(null);
        assertNull(dto);
    }

    @Test
    void proxyDtoToProxy() {
        var proxy = mapper.proxyDtoToProxy(new ProxyDto());
        assertNotNull(proxy);

        proxy = mapper.proxyDtoToProxy(null);
        assertNull(proxy);
    }

    @Test
    void map() {
        var dtoList = mapper.map(Collections.singletonList(new Proxy()));
        assertNotNull(dtoList);

        dtoList = mapper.map(null);
        assertNull(dtoList);
    }

    @Test
    void encodePassword() {
        var pswd = "hardToBrEaK_3000?";
        assertTrue(BCrypt.checkpw(pswd, ProxyMapper.encodePassword(pswd)));
    }
}