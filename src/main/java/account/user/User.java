package account.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class User {
    @Id
    @GeneratedValue()
    private BigInteger id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;
}
