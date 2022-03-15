package assignment.proxyservice.model;

import assignment.proxyservice.domain.ProxyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class ProxyDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @Size(max = 120)
    private String name;

    @NotNull
    private ProxyType type;

    @NotEmpty
    @Size(max = 120)
    @Pattern(regexp = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$")
    private String hostname;

    @Min(0)
    @Max(65535)
    private Integer port;

    @Size(min = 6, max = 50)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]+$")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min=8, max = 16)
    @Pattern(regexp = "^[a-zA-Z0-9#?!@$%^&*-]+$")
    private String password;

    private Boolean active;
}
