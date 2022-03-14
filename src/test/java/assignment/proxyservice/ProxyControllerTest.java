package assignment.proxyservice;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.domain.ProxyType;
import assignment.proxyservice.repository.ProxyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProxyControllerTest {

    @Autowired
    ProxyRepository proxyRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @Test
    @Sql(scripts = {"/insert_proxies.sql"})
    void getAllProxiesTest() throws Exception {
        final var lengthPath = "$._embedded.proxies.length()";

        mvc.perform(get("/api/proxies").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(lengthPath, equalTo(13)))
                .andDo(print());

        mvc.perform(get("/api/proxies?page=0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(lengthPath, equalTo(13)))
                .andDo(print());

        mvc.perform(get("/api/proxies?page=0&size=10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(lengthPath, equalTo(10)))
                .andDo(print());

        mvc.perform(get("/api/proxies?page=1&size=10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(lengthPath, equalTo(3)))
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
    @Sql(scripts = {"/insert_proxies.sql"})
    void postProxyFailTest() throws Exception {
        var proxy = new Proxy();
        proxy.setName("PROXY1");
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
    @Sql(scripts = {"/insert_proxies.sql"})
    void getProxyByIdTest() throws Exception {
        mvc.perform(get("/api/proxies/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("PROXY1")))
                .andDo(print());
    }

    @Test
    @Sql(scripts = {"/insert_proxies.sql"})
    void putProxyTest() throws Exception {
        final var proxy = new Proxy();
        proxy.setName("newProxy");
        proxy.setType(ProxyType.HTTP);
        proxy.setHostname("newhost");

        mvc.perform(put("/api/proxies/1")
                        .content(objectMapper.writeValueAsString(proxy))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Sql(scripts = {"/insert_proxies.sql"})
    void deleteProxyTest() throws Exception {
        mvc.perform(delete("/api/proxies/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Sql(scripts = {"/insert_proxies.sql"})
    void findByNameOrTypeTest() throws Exception {
        mvc.perform(get("/api/proxies/search")
                        .param("name", "PROXY1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$.[0].name", equalTo("PROXY1")))
                .andDo(print());

        mvc.perform(get("/api/proxies/search")
                        .param("type", "HTTPS")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(6)))
                .andExpect(jsonPath("$.[0].name", equalTo("PROXY2")))
                .andExpect(jsonPath("$.[1].name", equalTo("PROXY4")))
                .andExpect(jsonPath("$.[2].name", equalTo("PROXY6")))
                .andExpect(jsonPath("$.[3].name", equalTo("PROXY8")))
                .andExpect(jsonPath("$.[4].name", equalTo("PROXY10")))
                .andExpect(jsonPath("$.[5].name", equalTo("PROXY12")))
                .andDo(print());

        mvc.perform(get("/api/proxies/search")
                        .param("name", "PROXY1")
                        .param("type", "HTTPS")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(7)))
                .andDo(print());
    }
}
