package assignment.proxyservice.repository;

import assignment.proxyservice.domain.Proxy;
import assignment.proxyservice.domain.ProxyType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProxyRepository extends PagingAndSortingRepository<Proxy, Long> {

    List<Proxy> findByNameAndType(@Param("name") String name, @Param("type") ProxyType type);
}
