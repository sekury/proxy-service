package assignment.proxyservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotEmpty
    @Size(max = 120)
    @Column(name = "name", nullable = false, unique = true, length = 120)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProxyType type;

    @NotEmpty
    @Size(max = 120)
    @Column(name = "hostname", nullable = false, unique = true, length = 120)
    private String hostname;

    @Min(0)
    @Max(65535)
    @Column(name = "port")
    private Integer port;

    @Size(max = 80)
    @Column(name = "username", length = 80)
    private String username;

    @JsonIgnore
    @Size(max = 80)
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
