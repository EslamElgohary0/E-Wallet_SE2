package com.ewallet.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "الاسم الأول مطلوب")
    private String firstName;

    @NotBlank(message = "اسم العائلة مطلوب")
    private String lastName;

    @Email(message = "البريد الإلكتروني غير صحيح")
    @NotBlank(message = "البريد الإلكتروني مطلوب")
    private String email;

    @NotBlank
    @Size(min = 8, message = "كلمة المرور 8 أحرف على الأقل")
    private String password;

    private String phoneNumber;
}