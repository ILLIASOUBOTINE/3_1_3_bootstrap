package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class AppController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, @RequestParam(name = "roles1[]", required = false) List<String> roles, Model model) {

        // Проверка существования пользователя
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "User with this email already exists.");
            return "register";
        }

        // Преобразование строковых значений ролей в объекты Role
        Set<Role> userRoles = roles.stream()
                .map(roleName -> {
                    Role role = roleService.findByName(roleName);
                    if (role == null) {
                        throw new IllegalArgumentException("Role not found: " + roleName);
                    }
                    return role;
                })
                .collect(Collectors.toSet());

        // Устанавливаем роли пользователю
        user.setRoles(userRoles);

        // Шифрование пароля перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        System.out.println("Received user: " + user);

        // Сохраняем нового пользователя
        userService.add(user);

        return "redirect:/login";
    }


}
