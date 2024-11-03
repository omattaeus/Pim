package br.com.localfarm.product.interfaces.controllers;

import br.com.localfarm.product.application.services.JwtService;
import br.com.localfarm.product.application.services.UserService;
import br.com.localfarm.product.domain.models.User;
import br.com.localfarm.product.domain.models.Role;
import br.com.localfarm.product.application.services.RoleService;
import br.com.localfarm.product.domain.models.dto.LoginDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final JwtService jwtService;

    public UserController(UserService userService, RoleService roleService, JwtService jwtService) {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        try {
            System.out.println("Received user: " + user.getUsername());

            // Buscar roles do banco de dados
            Set<Role> rolesToAdd = new HashSet<>();
            for (Role role : user.getRoles()) {
                Role existingRole = roleService.findById(role.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found"));
                rolesToAdd.add(existingRole);
            }
            user.setRoles(rolesToAdd);

            User createdUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            // Buscar o usuário pelo nome de usuário
            User user = userService.findByUsername(loginDTO.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Debug: Imprimir o usuário encontrado
            System.out.println("Found user: " + user.getUsername());

            // Verificar se a senha corresponde
            if (userService.passwordMatches(loginDTO.getPassword(), user.getPassword())) {
                // Gerar token JWT
                String token = jwtService.generateToken(user);
                return ResponseEntity.ok(token);
            } else {
                System.out.println("Invalid password attempt for user: " + loginDTO.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}