package assignment.proxyservice.service;

import assignment.proxyservice.domain.ProxyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"test"})
public class ProxyServiceTest {

    @Autowired
    ProxyService proxyService;

    @Test
    @Sql(scripts = {"/insert_proxies.sql"})
    public void findProxyByNameAndTypeTest() {
        assertEquals(1, proxyService.searchProxies("PROXY1", ProxyType.HTTP).size());
        assertEquals(1, proxyService.searchProxies("PROXY1", null).size());
        assertEquals(7, proxyService.searchProxies(null, ProxyType.HTTP).size());
        assertEquals(0, proxyService.searchProxies(null, null).size());
    }

}
