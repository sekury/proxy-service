package assignment.proxyservice.repository;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.domain.ProxyType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProxyRepository extends PagingAndSortingRepository<Proxy, Long> {

    List<Proxy> findByNameOrType(@Param("name") String name, @Param("type") ProxyType type);
}
