package assignment.proxyservice.repository;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.domain.ProxyType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProxyRepository extends PagingAndSortingRepository<Proxy, Long>, JpaSpecificationExecutor<Proxy> {

    List<Proxy> findByNameAndType(String name, ProxyType type);

    List<Proxy> findByName(String name);

    List<Proxy> findByType(ProxyType type);
}
