package assignment.proxyservice.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="proxy", indexes = @Index(name = "idx_proxy_name_type", columnList = "name"))
public class Proxy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProxyType type;

    @Column(name = "hostname", nullable = false, unique = true, length = 120)
    private String hostname;

    @Column(name = "port")
    private Integer port;

    @Column(name = "username", length = 80)
    private String username;

    @Column(name = "password", length = 80)
    private String password;

    @Column(name = "active")
    private Boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Proxy proxy = (Proxy) o;
        return id != null && Objects.equals(id, proxy.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
