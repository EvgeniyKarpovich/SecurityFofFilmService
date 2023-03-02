package by.karpovich.security.service;

import by.karpovich.security.api.dto.role.RoleDto;
import by.karpovich.security.exception.DuplicateException;
import by.karpovich.security.exception.NotFoundModelException;
import by.karpovich.security.jpa.model.Role;
import by.karpovich.security.jpa.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(RoleDto dto) {
        validateAlreadyExists(dto, null);

        Role entity = new Role();

        entity.setName(dto.getName());
        roleRepository.save(entity);

        return entity;
    }

    public Set<Role> findRoleByName(String role) {
        Optional<Role> entity = roleRepository.findByName(role);

        Role roleEntity = entity.orElseThrow(
                () -> new NotFoundModelException(String.format("Role with name = %s not found", role)));

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleEntity);

        return userRoles;
    }

    public RoleDto findById(Long id) {
        Optional<Role> model = roleRepository.findById(id);
        Role role = model.orElseThrow(
                () -> new NotFoundModelException(String.format("Role with id = %s not found", model.get().getId())));

        log.info("method findById - Role found with id = {} ", role.getId());

        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }

    public List<RoleDto> findAll() {
        List<Role> rolesModel = roleRepository.findAll();

        log.info("method findAll - the number of roles found  = {} ", rolesModel.size());

        List<RoleDto> rolesDto = new ArrayList<>();

        for (Role model : rolesModel) {
            RoleDto dto = new RoleDto();
            dto.setId(model.getId());
            dto.setName(model.getName());

            rolesDto.add(dto);
        }

        return rolesDto;
    }

    public RoleDto update(Long id, RoleDto dto) {
        validateAlreadyExists(dto, id);

        Role role = new Role();
        role.setName(dto.getName());
        role.setId(id);
        Role updated = roleRepository.save(role);

        log.info("method update - Role {} updated", updated.getName());

        RoleDto roleDto = new RoleDto();
        roleDto.setId(updated.getId());
        roleDto.setName(updated.getName());
        return roleDto;
    }

    public void deleteById(Long id) {
        if (roleRepository.findById(id).isPresent()) {
            roleRepository.deleteById(id);
        } else {
            throw new NotFoundModelException(String.format("Role with id = %s not found", id));
        }
        log.info("method deleteById - Role with id = {} deleted", id);
    }


    private void validateAlreadyExists(RoleDto dto, Long id) {
        Optional<Role> model = roleRepository.findByName(dto.getName());

        if (model.isPresent() && !model.get().getId().equals(id)) {
            throw new DuplicateException(String.format("Role with name = %s already exist", model.get().getName()));
        }
    }
}
