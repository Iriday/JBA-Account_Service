package account.admin;

import account.user.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
