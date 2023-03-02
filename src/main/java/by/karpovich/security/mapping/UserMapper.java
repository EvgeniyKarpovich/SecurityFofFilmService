package by.karpovich.security.mapping;

import by.karpovich.security.api.dto.UserDtoForFindAll;
import by.karpovich.security.api.dto.UserFullDtoOut;
import by.karpovich.security.jpa.model.Role;
import by.karpovich.security.jpa.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDtoForFindAll mapUserDtoForFindAllFromModel(User model) {
        if (model == null) {
            return null;
        }

        UserDtoForFindAll dto = new UserDtoForFindAll();

        List<String> roles = model.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setEmail(model.getEmail());
        dto.setRoles(roles);
        dto.setStatus(model.getStatus());

        return dto;
    }

    public List<UserDtoForFindAll> mapListUserDtoForFindAllFromListModel(List<User> users) {
        if (users == null) {
            return null;
        }

        List<UserDtoForFindAll> usersDto = new ArrayList<>();

        for (User model : users) {
            usersDto.add(mapUserDtoForFindAllFromModel(model));
        }
        return usersDto;
    }

    public UserFullDtoOut mapUserFullDtoFromModel(User model) {
        if (model == null) {
            return null;
        }

        UserFullDtoOut dto = new UserFullDtoOut();

        dto.setUsername(model.getUsername());
        dto.setEmail(model.getEmail());

        return dto;
    }

}
