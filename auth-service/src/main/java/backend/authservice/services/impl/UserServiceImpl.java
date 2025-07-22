package backend.authservice.services.impl;

import backend.authservice.models.entities.UserCustom;
import backend.authservice.repositories.UserRepository;
import backend.authservice.services.UserService;
import backend.authservice.utils.JwtUtils;
import backend.dtos.auth.requests.AuthLoginRequest;
import backend.dtos.auth.responses.AuthLoginResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCustom> usuarioEncontrado = userRepository.findByUsername(username);
        if (usuarioEncontrado.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Añadir roles y permisos al usuario encontrado en la base de datos

        usuarioEncontrado.get().getRoles().forEach(rol -> {
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + rol.getName().name()));
        });

        // Añadir permisos al usuario encontrado en la base de datos (opcional)
        usuarioEncontrado.get().getRoles().stream()
                .flatMap(rol -> rol.getPermissions().stream())
                .forEach(permiso -> authorityList.add(new SimpleGrantedAuthority(permiso.getName())));

        // Crear un objeto User con los datos del usuario encontrado en la base de datos

        return new User(usuarioEncontrado.get().getUsername(),
                usuarioEncontrado.get().getPassword(),
                usuarioEncontrado.get().isEnabled(),
                usuarioEncontrado.get().isAccountNoExpired(),
                usuarioEncontrado.get().isCredentialsNoExpired(),
                usuarioEncontrado.get().isAccountNoLocked(),
                authorityList);

    }

    public AuthLoginResponse loginUser(AuthLoginRequest userRequest) {
        String username = userRequest.username();
        String password = userRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        AuthLoginResponse authResponse = new AuthLoginResponse(username, "Connected user", accessToken, true);
        return authResponse;
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("User not found");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
