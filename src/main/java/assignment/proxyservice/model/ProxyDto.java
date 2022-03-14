package assignment.proxyservice.model;

import assignment.proxyservice.domain.ProxyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class ProxyDto {

    private Long id;

    @NotEmpty
    @Size(max = 120)
    private String name;

    @NotNull
    private ProxyType type;

    @NotEmpty
    @Size(max = 120)
    private String hostname;

    @Min(0)
    @Max(65535)
    private Integer port;

    @Size(max = 80)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 80)
    private String password;

    private Boolean active;
}
