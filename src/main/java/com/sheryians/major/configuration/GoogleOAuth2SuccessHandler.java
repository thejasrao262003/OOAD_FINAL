package com.sheryians.major.configuration;

import com.sheryians.major.model.Role;
import com.sheryians.major.model.User;
import com.sheryians.major.repository.RoleRepository;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email = token.getPrincipal().getAttributes().get("email").toString();

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if(userOptional.isPresent()) {
            // User already exists, do nothing
        } else {
            // Create a new user
            User user = new User.Builder(token.getPrincipal().getAttributes().get("given_name").toString(), email)
                    .lastName(token.getPrincipal().getAttributes().get("family_name").toString())
                    .roles(getDefaultRoles())
                    .build();
            userRepository.save(user);
        }

        redirectStrategy.sendRedirect(request, response, "/");
    }

    // Helper method to get default roles
    private List<Role> getDefaultRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(2).orElseThrow(() -> new RuntimeException("Role not found")));
        return roles;
    }
}
