package by.karpovich.security.service;

import by.karpovich.security.api.dto.authentification.JwtResponse;
import by.karpovich.security.api.dto.authentification.LoginForm;
import by.karpovich.security.api.dto.authentification.RegistrationForm;
import by.karpovich.security.api.dto.user.UserDtoForFindAll;
import by.karpovich.security.api.dto.user.UserForUpdate;
import by.karpovich.security.api.dto.user.UserFullDtoOut;
import by.karpovich.security.exception.DuplicateException;
import by.karpovich.security.exception.NotFoundModelException;
import by.karpovich.security.jpa.model.Status;
import by.karpovich.security.jpa.model.User;
import by.karpovich.security.jpa.repository.RoleRepository;
import by.karpovich.security.jpa.repository.UserRepository;
import by.karpovich.security.mapping.UserMapper;
import by.karpovich.security.security.JwtUtils;
import by.karpovich.security.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserMapper userMapper;

    private static final String ROLE_USER = "ROLE_USER";

    public void signUp(RegistrationForm dto) {
        validateAlreadyExists(dto, null);
        User model = new User();

        model.setUsername(dto.getUsername());
        model.setPassword(passwordEncoder.encode(dto.getPassword()));
        model.setEmail(dto.getEmail());
        model.setRoles(roleService.findRoleByName(ROLE_USER));
        model.setStatus(Status.ACTIVE);

        userRepository.save(model);
    }

    public JwtResponse signIn(LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public UserFullDtoOut findById(Long id) {
        Optional<User> model = userRepository.findById(id);
        User user = model.orElseThrow(
                () -> new NotFoundModelException(String.format("User with id = %s not found", model.get().getId())));

        log.info("method findById - the user found with id = {} ", user.getId());

        return userMapper.mapUserFullDtoFromModel(user);
    }


    public void deleteById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
        throw new NotFoundModelException(String.format("User with id = %s not found", id));
    }

    public List<UserDtoForFindAll> findAll() {
        List<User> usersModel = userRepository.findAll();

        log.info("method findAll - number of users found  = {} ", usersModel.size());

        return userMapper.mapListUserDtoForFindAllFromListModel(usersModel);
    }

    public UserFullDtoOut update(Long id, UserForUpdate dto) {

        Optional<User> userById = userRepository.findById(id);
        User user = userById.orElseThrow(
                () -> new NotFoundModelException(String.format("the country with id = %s not found", userById.get().getId())));

        user.setId(id);
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        User updatedUser = userRepository.save(user);

        log.info("method update - the user {} updated", updatedUser.getUsername());

        return userMapper.mapUserFullDtoFromModel(updatedUser);
    }

    public User findByName(String name) {
        Optional<User> userByName = userRepository.findByUsername(name);

        User userModel = userByName.orElseThrow(
                () -> new NotFoundModelException(String.format("User with username = %s not found", userByName.get().getUsername()))
        );

        log.info("method findByName -  User with username = {} found", userModel.getUsername());

        return userModel;
    }

    private void validateAlreadyExists(RegistrationForm dto, Long id) {
        Optional<User> model = userRepository.findByUsername(dto.getUsername());

        if (model.isPresent() && !model.get().getId().equals(id)) {
            throw new DuplicateException(String.format("User with name = %s already exist", model.get().getUsername()));
        }
    }
}