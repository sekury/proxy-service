package assignment.proxyservice;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.domain.ProxyType;
import assignment.proxyservice.repository.ProxyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"it"})
public class ProxyControllerTest {

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.2-alpine")
            .withDatabaseName("proxy_db")
            .withUsername("sa")
            .withPassword("sa")
            .waitingFor(Wait.forListeningPort());

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.flyway.url", container::getJdbcUrl);
        registry.add("spring.flyway.user", container::getUsername);
        registry.add("spring.flyway.password", container::getPassword);
    }

    @Autowired
    ProxyRepository proxyRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    public void cleanDB() {
        proxyRepository.deleteAll();
    }

    @Test
    void getAllProxiesTest() throws Exception {
        final var lengthPath = "$.length()";

        var proxy1 = createProxy("proxy1", "host1", ProxyType.HTTP);
        var proxy2 = createProxy("proxy2", "host2", ProxyType.HTTP);
        var proxy3 = createProxy("proxy3", "host3", ProxyType.HTTP);
        proxyRepository.saveAll(Arrays.asList(proxy1, proxy2, proxy3));

        mvc.perform(get("/api/proxies").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(lengthPath, equalTo(3)))
                .andDo(print());

        mvc.perform(get("/api/proxies?page=0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(lengthPath, equalTo(3)))
                .andDo(print());

        mvc.perform(get("/api/proxies?page=0&size=2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(lengthPath, equalTo(2)))
                .andDo(print());

        mvc.perform(get("/api/proxies?page=1&size=2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(lengthPath, equalTo(1)))
                .andDo(print());
    }

    @Test
    void postProxySuccessTest() throws Exception {
        final var proxy = new Proxy();
        proxy.setName("newProxy");
        proxy.setType(ProxyType.HTTP);
        proxy.setHostname("newhost");

        mvc.perform(post("/api/proxies")
                        .content(objectMapper.writeValueAsString(proxy))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("newProxy")))
                .andExpect(jsonPath("$.type", equalTo("HTTP")))
                .andExpect(jsonPath("$.hostname", equalTo("newhost")))
                .andDo(print());
    }

    @Test
    void postProxyFailTest() throws Exception {
        proxyRepository.save(createProxy("proxy1", "host1", ProxyType.HTTP));

        var proxy = new Proxy();
        proxy.setName("proxy1");
        proxy.setType(ProxyType.HTTP);
        proxy.setHostname("newhost");

        mvc.perform(post("/api/proxies")
                        .content(objectMapper.writeValueAsString(proxy))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void getProxyByIdSuccessTest() throws Exception {
        var proxy1 = proxyRepository.save(createProxy("proxy1", "host1", ProxyType.HTTP));

        mvc.perform(get("/api/proxies/" + proxy1.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("proxy1")))
                .andDo(print());
    }

    @Test
    void getProxyByIdFailTest() throws Exception {
        mvc.perform(get("/api/proxies/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void putProxySuccessTest() throws Exception {
        var proxy1 = proxyRepository.save(createProxy("proxy1", "host1", ProxyType.HTTP));

        final var proxy = new Proxy();
        proxy.setName("newProxy");
        proxy.setType(ProxyType.HTTP);
        proxy.setHostname("newhost");

        mvc.perform(put("/api/proxies/" + proxy1.getId())
                        .content(objectMapper.writeValueAsString(proxy))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void putProxyConflictFailTest() throws Exception {
        var proxy1 = proxyRepository.save(createProxy("proxy1", "host1", ProxyType.HTTP));
        var proxy2 = proxyRepository.save(createProxy("proxy2", "host2", ProxyType.HTTP));

        final var proxy = new Proxy();
        proxy.setName("proxy2");
        proxy.setType(ProxyType.HTTP);
        proxy.setHostname("newhost");

        mvc.perform(put("/api/proxies/" + proxy1.getId())
                        .content(objectMapper.writeValueAsString(proxy))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void putProxyNotFoundFailTest() throws Exception {
        final var proxy = new Proxy();
        proxy.setName("newProxy");
        proxy.setType(ProxyType.HTTP);
        proxy.setHostname("newhost");

        mvc.perform(put("/api/proxies/1")
                        .content(objectMapper.writeValueAsString(proxy))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void findByNameOrTypeTest() throws Exception {
        var proxy1 = createProxy("proxy1", "host1", ProxyType.HTTP);
        var proxy2 = createProxy("proxy2", "host2", ProxyType.HTTP);
        var proxy3 = createProxy("proxy3", "host3", ProxyType.HTTPS);
        proxyRepository.saveAll(Arrays.asList(proxy1, proxy2, proxy3));

        // empty search
        mvc.perform(get("/api/proxies/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(0)))
                .andDo(print());

        // search by name
        mvc.perform(get("/api/proxies/search")
                        .param("name", "proxy1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$.[0].name", equalTo("proxy1")))
                .andDo(print());

        // search by type
        mvc.perform(get("/api/proxies/search")
                        .param("type", "HTTP")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(2)))
                .andExpect(jsonPath("$.[0].name", equalTo("proxy1")))
                .andExpect(jsonPath("$.[1].name", equalTo("proxy2")))
                .andDo(print());

        // search both name and type
        mvc.perform(get("/api/proxies/search")
                        .param("name", "proxy1")
                        .param("type", "HTTP")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andDo(print());
    }

    @Test
    void deleteProxyTest() throws Exception {
        var proxy1 = proxyRepository.save(createProxy("proxy1", "host1", ProxyType.HTTP));

        mvc.perform(delete("/api/proxies/" + proxy1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    private static Proxy createProxy(String name, String host, ProxyType type) {
        var proxy = new Proxy();
        proxy.setName(name);
        proxy.setHostname(host);
        proxy.setType(type);
        return proxy;
    }
}
