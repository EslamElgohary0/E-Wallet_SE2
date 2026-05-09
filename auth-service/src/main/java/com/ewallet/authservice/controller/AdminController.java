package com.ewallet.authservice.controller;

import com.ewallet.authservice.dto.ChangeRoleRequest;
import com.ewallet.authservice.dto.UserSummaryResponse;
import com.ewallet.authservice.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<UserSummaryResponse> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }


    @GetMapping("/users/email/{email}")
    public ResponseEntity<UserSummaryResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(adminService.getUserByEmail(email));
    }


    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserSummaryResponse> changeRole(
            @PathVariable String userId,
            @Valid @RequestBody ChangeRoleRequest request) {
        return ResponseEntity.ok(adminService.changeUserRole(userId, request));
    }


    @PutMapping("/users/{userId}/disable")
    public ResponseEntity<UserSummaryResponse> disableUser(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.disableUser(userId));
    }


    @PutMapping("/users/{userId}/enable")
    public ResponseEntity<UserSummaryResponse> enableUser(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.enableUser(userId));
    }


    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message", "تم حذف المستخدم بنجاح"));
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }
}