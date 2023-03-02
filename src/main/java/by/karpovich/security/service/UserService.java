package by.karpovich.security.service;

import by.karpovich.security.dto.RegistrationForm;
import by.karpovich.security.exception.DuplicateException;
import by.karpovich.security.exception.NotFoundModelException;
import by.karpovich.security.jpa.model.Role;
import by.karpovich.security.jpa.model.Status;
import by.karpovich.security.jpa.model.User;
import by.karpovich.security.jpa.repository.RoleRepository;
import by.karpovich.security.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    private static final String ROLE_USER = "ROLE_USER";

    public void saveUser(RegistrationForm dto) {
        validateAlreadyExists(dto, null);
        User model = new User();

        model.setUsername(dto.getUsername());
        model.setPassword(passwordEncoder.encode(dto.getPassword()));
        model.setEmail(dto.getEmail());
        model.setRoles(findRoleByName(ROLE_USER));
        model.setStatus(Status.ACTIVE);

        userRepository.save(model);

    }

    private Set<Role> findRoleByName(String role) {
        Optional<Role> entity = roleRepository.findByName(role);

        Role roleEntity = entity.orElseThrow(
                () -> new NotFoundModelException(String.format("the role with name = %s not found", role)));

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleEntity);

        return userRoles;
    }

    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
        throw new NotFoundModelException(String.format(" the actor with id = %s not found", id));
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
            throw new DuplicateException(String.format("the actor with name = %s already exist", model.get().getUsername()));
        }
    }
}