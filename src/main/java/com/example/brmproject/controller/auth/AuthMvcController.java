package com.example.brmproject.controller.auth;


import com.example.brmproject.domain.dto.CustomerDTO;
import com.example.brmproject.domain.dto.LoginRequestDTO;
import com.example.brmproject.domain.dto.SignupRequestDTO;
import com.example.brmproject.domain.entities.*;
import com.example.brmproject.repositories.CustomerEntityRepository;
import com.example.brmproject.repositories.RoleEntityRepository;
import com.example.brmproject.repositories.StaffEntityRepository;
import com.example.brmproject.repositories.UserEntityRepository;
import com.example.brmproject.security.jwt.JwtUtils;
import com.example.brmproject.service.imp.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;




@Controller
public class AuthMvcController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserEntityRepository userEntityRepository;

  @Autowired
  RoleEntityRepository roleEntityRepository;

  @Autowired
  CustomerEntityRepository customerEntityRepository;

  @Autowired
  StaffEntityRepository staffEntityRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  private HttpServletResponse response;

  @GetMapping("/register")
  public String register (Model model){
    SignupRequestDTO signupRequest = new SignupRequestDTO();
    model.addAttribute("signupRequest", signupRequest);

    return "customerTemplate/register";
  }

  @GetMapping("/login")
  public String login (Model model){
    LoginRequestDTO loginRequest = new LoginRequestDTO();
    model.addAttribute("loginRequest",loginRequest);
    return "customerTemplate/login";
  }

  @GetMapping("/forbidden")
  public String forbidden(){
    return "/forbidden";
  }

  @PostMapping("/login")
    public String login( @Valid @ModelAttribute("loginRequest") LoginRequestDTO loginRequest, BindingResult bindingResult  , Model model ){
        try {

          if (bindingResult.hasErrors()) { 
            model.addAttribute("loginRequest", loginRequest);
           
            return "customerTemplate/login";
          }

          Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


          SecurityContextHolder.getContext().setAuthentication(authentication);
          UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
          String jwt = jwtUtils.generateJwtToken(authentication);
  
          List<String> roles = userDetails.getAuthorities().stream()
          .map(item -> item.getAuthority())
          .collect(Collectors.toList());
          int userId;
          String url;
          if (roles.stream().anyMatch(role -> role.equalsIgnoreCase("STAFF") || role.equalsIgnoreCase("ADMIN"))) {
            StaffEntity staff = staffEntityRepository.findByUserId(userDetails.getId()).get();
            userId = staff.getId();
            url = "redirect:/staff/dashboard";
          } else {
              CustomerEntity customer = customerEntityRepository.findByUserId(userDetails.getId()).get();
              userId = customer.getId();

            url = "redirect:/";
          }

          Cookie cookieJwt = createCookie("jwtToken", jwt);
          Cookie cookieInfo = createCookie("userId", Integer.toString(userId));

          response.addCookie(cookieJwt);
          response.addCookie(cookieInfo);


  
            return url;
      } catch (AuthenticationException e) {       
          model.addAttribute("loginRequest", loginRequest);        
          model.addAttribute("errorAuthen", "Wrong username or password");
          return "customerTemplate/login";
      }
  }


  @PostMapping("/register")
  public String registerCustomer(@Valid @ModelAttribute("signupRequest") SignupRequestDTO signupRequest, BindingResult bindingResult, Model model) {
     if (bindingResult.hasErrors()) { 
            return "customerTemplate/register";
      }

      if (userEntityRepository.existsByUsername(signupRequest.getUsername())) {
            model.addAttribute("signupRequest", signupRequest);
            model.addAttribute("errors", "Email is already exist!");
            return "customerTemplate/register";
      }

    // Create new user's account
    UserEntity user = new UserEntity(signupRequest.getUsername(),
        encoder.encode(signupRequest.getPassword()));

    Set<RoleEntity> roles = new HashSet<>();
    
    RoleEntity userRole = roleEntityRepository.findByName(ERole.CUSTOMER)
        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    roles.add(userRole);

    user.setRoles(roles);
    userEntityRepository.save(user);
   
    
    int userId = userEntityRepository.save(user).getId();

    CustomerEntity newCustomer = new CustomerEntity();
    newCustomer.setAddress(signupRequest.getAddress());
    newCustomer.setEmail(signupRequest.getUsername());
    newCustomer.setName(signupRequest.getName());
    newCustomer.setPhone(signupRequest.getPhone());
    newCustomer.setUserId(userId);
    newCustomer.setUserByUserId(user);
    newCustomer.setDebit(0.0);
    customerEntityRepository.save(newCustomer);
   
    return "redirect:/login";
  }






  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/staff/createStaff")
  public String registerStaff(@Valid @ModelAttribute("signupRequestStaff") SignupRequestDTO signupRequestStaff, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) { 
      return "adminTemplate/staffDashboard/CreateStaffDashboard";
    }

    if (userEntityRepository.existsByUsername(signupRequestStaff.getUsername())) {
          model.addAttribute("signupRequestStaff", signupRequestStaff);
          model.addAttribute("errors", "Email is already exist!");
          return "adminTemplate/staffDashboard/CreateStaffDashboard";
    }


    // Create new user's account
    UserEntity user = new UserEntity(signupRequestStaff.getUsername(),
        encoder.encode(signupRequestStaff.getPassword()));

    Set<RoleEntity> roles = new HashSet<>();
    
    RoleEntity userRole = roleEntityRepository.findByName(ERole.STAFF)
        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    roles.add(userRole);

    user.setRoles(roles);
    userEntityRepository.save(user);
    
    int userId = userEntityRepository.save(user).getId();

    StaffEntity newStaff = new StaffEntity();
   
    newStaff.setEmail(signupRequestStaff.getUsername());
    newStaff.setName(signupRequestStaff.getName());
    newStaff.setEmployeeCode(RandomStringUtils.randomAlphanumeric(6));
    newStaff.setUserId(userId);
    newStaff.setUserByUserId(user);
    staffEntityRepository.save(newStaff);
   
    return "redirect:/staff/employees/getAll";
  }



  private Cookie createCookie(String name, String value) {
    Cookie cookie = new Cookie(name, value);
    cookie.setMaxAge(86400000);
    cookie.setPath("/");
    return cookie;
}

@GetMapping("/mylogout")
  public String logout(HttpServletRequest request, HttpServletResponse response){
  Cookie[] cookies = request.getCookies();

  // Xóa tất cả các cookie trong danh sách
  if (cookies != null) {
    for (Cookie cookie : cookies) {
      cookie.setValue("");
      cookie.setPath("/");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }
  }
  return "redirect:/login";

}

  
 
    
}
