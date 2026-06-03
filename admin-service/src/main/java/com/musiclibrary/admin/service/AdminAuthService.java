package com.musiclibrary.admin.service;

import com.musiclibrary.admin.dto.AdminDto;
import com.musiclibrary.admin.entity.Admin;
import com.musiclibrary.admin.exception.ResourceNotFoundException;
import com.musiclibrary.admin.repository.AdminRepository;
import com.musiclibrary.admin.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    // Seed a default admin on startup
    @EventListener(ApplicationReadyEvent.class)
    public void seedAdmin() {
        if (!adminRepository.existsByEmail("admin@musiclibrary.com")) {
            Admin admin = Admin.builder()
                    .name("Super Admin")
                    .email("admin@musiclibrary.com")
                    .password(passwordEncoder.encode("admin@123"))
                    .build();
            adminRepository.save(admin);
        }
    }

    public AdminDto.AuthResponse login(AdminDto.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return new AdminDto.AuthResponse(token, admin.getEmail(), admin.getName());
    }
}
