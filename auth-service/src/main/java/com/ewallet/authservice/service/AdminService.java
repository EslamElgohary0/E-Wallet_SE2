package com.ewallet.authservice.service;

import com.ewallet.authservice.dto.ChangeRoleRequest;
import com.ewallet.authservice.dto.UserSummaryResponse;
import com.ewallet.authservice.model.Role;
import com.ewallet.authservice.model.User;
import com.ewallet.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;


    public List<UserSummaryResponse> getAllUsers() {
        log.info("[ADMIN] Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }


    public UserSummaryResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود بالـ ID: " + userId));
        return toSummary(user);
    }


    public UserSummaryResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود بالـ Email: " + email));
        return toSummary(user);
    }


    public UserSummaryResponse changeUserRole(String userId, ChangeRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));

        try {
            Role newRole = Role.valueOf(request.getRole().toUpperCase());
            String oldRole = user.getRole().name();
            user.setRole(newRole);
            User updated = userRepository.save(user);
            log.info("[ADMIN] Changed role for user {} from {} to {}",
                    user.getEmail(), oldRole, newRole);
            return toSummary(updated);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("دور غير صالح. الأدوار المتاحة: ROLE_USER, ROLE_ADMIN");
        }
    }


    public UserSummaryResponse disableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));
        user.setEnabled(false);
        User updated = userRepository.save(user);
        log.info("[ADMIN] Disabled user: {}", user.getEmail());
        return toSummary(updated);
    }


    public UserSummaryResponse enableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));
        user.setEnabled(true);
        User updated = userRepository.save(user);
        log.info("[ADMIN] Enabled user: {}", user.getEmail());
        return toSummary(updated);
    }


    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));
        userRepository.delete(user);
        log.info("[ADMIN] Deleted user: {}", user.getEmail());
    }


    public Map<String, Object> getStats() {
        List<User> allUsers = userRepository.findAll();
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalUsers", allUsers.size());
        stats.put("totalAdmins", allUsers.stream()
                .filter(u -> u.getRole() == Role.ROLE_ADMIN).count());
        stats.put("totalRegularUsers", allUsers.stream()
                .filter(u -> u.getRole() == Role.ROLE_USER).count());
        stats.put("activeUsers", allUsers.stream()
                .filter(User::isEnabled).count());
        return stats;
    }

    private UserSummaryResponse toSummary(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
