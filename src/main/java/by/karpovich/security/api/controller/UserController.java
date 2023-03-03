package by.karpovich.security.api.controller;

import by.karpovich.security.api.dto.user.UserDtoForFindAll;
import by.karpovich.security.api.dto.user.UserForUpdate;
import by.karpovich.security.api.dto.user.UserFullDtoOut;
import by.karpovich.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserFullDtoOut findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<UserDtoForFindAll> findAll() {
        return userService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserForUpdate dto,
                                    @PathVariable("id") Long id) {
        userService.update(id, dto);
        return new ResponseEntity<>("User successfully updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }
}
