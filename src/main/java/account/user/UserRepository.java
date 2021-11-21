package account.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmailIgnoreCase(String s);

    List<User> findAllByOrderById();

    @Query("UPDATE User u SET u.loginFailCount = ?1 WHERE u.email = ?2")
    @Modifying
    void setLoginFailCount(int val, String email);
}
