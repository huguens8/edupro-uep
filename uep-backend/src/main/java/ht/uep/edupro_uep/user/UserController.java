package ht.uep.edupro_uep.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ht.uep.edupro_uep.dto.CreateUserRequest;
import ht.uep.edupro_uep.dto.ResetPasswordRequest;
import ht.uep.edupro_uep.dto.UpdateRoleRequest;
import ht.uep.edupro_uep.dto.UpdateStatusRequest;
import ht.uep.edupro_uep.dto.UpdateUserRequest;
import ht.uep.edupro_uep.dto.UserResponse;
import jakarta.validation.Valid;

/**
 * Gestion des comptes utilisateurs. Réservé au rôle ADM (voir SecurityConfig).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> listUsers() {
        return userService.listUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request, Authentication authentication) {
        return userService.createUser(request, authentication.getName());
    }

    @PatchMapping("/{id}/role")
    public UserResponse updateRole(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateRoleRequest request,
            Authentication authentication) {
        return userService.updateRole(id, request.getRole(), authentication.getName());
    }

    @PatchMapping("/{id}/active")
    public UserResponse updateStatus(
            @PathVariable Integer id,
            @RequestBody UpdateStatusRequest request,
            Authentication authentication) {
        return userService.updateStatus(id, request.isActive(), authentication.getName());
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication) {
        return userService.updateUser(id, request, authentication.getName());
    }

    @PatchMapping("/{id}/password")
    public UserResponse resetPassword(
            @PathVariable Integer id,
            @Valid @RequestBody ResetPasswordRequest request,
            Authentication authentication) {
        return userService.resetPassword(id, request.getPassword(), authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id, Authentication authentication) {
        userService.deleteUser(id, authentication.getName());
    }
}
