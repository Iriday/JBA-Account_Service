package com.example.account_service.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigInteger id;
    @NotBlank
    @Size(max = 30)
    private String name;
    @NotBlank
    @Size(max = 30)
    private String lastname;
    @NotBlank
    @Email(regexp = "\\w{1,30}@acme\\.com$")
    private String email;
    @NotBlank
    @Size(min=12, max = 50)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
