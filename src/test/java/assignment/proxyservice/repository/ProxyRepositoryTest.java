package assignment.proxyservice.repository;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.domain.ProxyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles({"test"})
class ProxyRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProxyRepository repository;

    @Test
    void findByNameAndType() {
        final var proxyName = "rightName";

        Proxy proxy = new Proxy();
        proxy.setName(proxyName);
        proxy.setType(ProxyType.SOCKS4);
        proxy.setHostname("proxy_host");
        proxy.setUsername("user");
        proxy.setPassword("pswd");

        entityManager.persist(proxy);
        entityManager.flush();

        assertEquals(1, repository.findByNameOrType(proxyName, ProxyType.SOCKS4).size());
        assertEquals(1, repository.findByNameOrType(proxyName, ProxyType.HTTPS).size());
        assertEquals(1, repository.findByNameOrType("wrongName", ProxyType.SOCKS4).size());
        assertEquals(0, repository.findByNameOrType("wrongName", ProxyType.HTTPS).size());
    }
}