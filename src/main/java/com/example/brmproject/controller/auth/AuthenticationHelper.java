package com.example.brmproject.controller.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.brmproject.domain.entities.CustomerEntity;
import com.example.brmproject.domain.entities.StaffEntity;
import com.example.brmproject.repositories.CustomerEntityRepository;
import com.example.brmproject.repositories.StaffEntityRepository;
import com.example.brmproject.service.imp.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationHelper {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    StaffEntityRepository staffEntityRepository;

    @Autowired
    CustomerEntityRepository customerEntityRepository;


    public int getUserIdFromAuthentication() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return roles.stream()
                .anyMatch(role -> role.equalsIgnoreCase("STAFF") || role.equalsIgnoreCase("ADMIN")) ?
                staffEntityRepository.findByUserId(userDetails.getId()).map(StaffEntity::getId).orElse(-1) :
                customerEntityRepository.findByUserId(userDetails.getId()).map(CustomerEntity::getId).orElse(-1);
    }
}
