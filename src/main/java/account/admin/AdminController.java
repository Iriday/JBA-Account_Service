package account.admin;

import account.user.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;

    @GetMapping("api/admin/user")
    public List<UserDto> getAllUsersOrderById() {
        return adminService.getAllUsersOrderById();
    }

    @DeleteMapping("api/admin/user/{email}")
    public StatusDto deleteUserByEmail(@PathVariable String email) {
        return adminService.deleteUserByEmail(email);
    }
}
