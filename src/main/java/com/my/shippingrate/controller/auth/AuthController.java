package com.my.shippingrate.controller.auth;

import com.my.shippingrate.config.security.JwtTokenProvider;
import com.my.shippingrate.dto.request.auth.JwtResponseDTO;
import com.my.shippingrate.dto.request.auth.LoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Operation( summary = "Authentication login", tags = { "authentication" })
    @PostMapping("v1/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO loginDTO) {

        log.info("Login request for username: {}", loginDTO.username());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.username());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.username(),
                loginDTO.password()
        ));

        String accessToken = jwtTokenProvider.generateToken(authentication);
        String tokenType = "bearer";
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO(accessToken, tokenType);
        return new ResponseEntity<>(jwtResponseDTO, HttpStatus.OK);

    }


}
