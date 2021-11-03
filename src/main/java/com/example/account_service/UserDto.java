package com.example.account_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
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
    @Size(max = 30)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
