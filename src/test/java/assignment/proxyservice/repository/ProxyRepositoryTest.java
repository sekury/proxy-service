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
        proxy.setHostname("proxy-host");

        entityManager.persist(proxy);
        entityManager.flush();

        assertEquals(1, repository.findByNameAndType(proxyName, ProxyType.SOCKS4).size());
        assertEquals(0, repository.findByNameAndType(proxyName, ProxyType.HTTPS).size());
        assertEquals(0, repository.findByNameAndType("wrongName", ProxyType.SOCKS4).size());
        assertEquals(0, repository.findByNameAndType("wrongName", ProxyType.HTTPS).size());

        assertEquals(1, repository.findByName(proxyName).size());
        assertEquals(0, repository.findByName("wrongName").size());

        assertEquals(1, repository.findByType(ProxyType.SOCKS4).size());
        assertEquals(0, repository.findByType(ProxyType.HTTPS).size());
    }
}