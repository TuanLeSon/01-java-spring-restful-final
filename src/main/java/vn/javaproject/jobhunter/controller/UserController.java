package vn.javaproject.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.javaproject.jobhunter.domain.User;
import vn.javaproject.jobhunter.domain.response.ResCreateUserDTO;
import vn.javaproject.jobhunter.domain.response.ResUpdateUserDTO;
import vn.javaproject.jobhunter.domain.response.ResUserDTO;
import vn.javaproject.jobhunter.domain.response.ResultPaginationDTO;
import vn.javaproject.jobhunter.service.UserService;
import vn.javaproject.jobhunter.util.annotation.ApiMessage;
import vn.javaproject.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

    }

    // @GetMapping("/users/create")
    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User reqUser) throws IdInvalidException {
        if (userService.isEmailExist(reqUser.getEmail())) {
            throw new IdInvalidException("Email " + reqUser.getEmail() + "đã tồn tại, vui lòng sử dụng email khác");
        }
        String hashPassword = this.passwordEncoder.encode(reqUser.getPassword());
        reqUser.setPassword(hashPassword);
        // Company company =
        // this.companyService.fetchCompanyById(reqUser.getCompany().getId());
        // reqUser.setCompany(company);
        User user = this.userService.handleCreateUser(reqUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToResCreateUserDTO(user));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (userService.fetchUserById(id) == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
        // return ResponseEntity.ok("ericUser");
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        if (userService.fetchUserById(id) == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }
        User user = this.userService.fetchUserById(id);
        return ResponseEntity.ok(userService.convertToResUserDTO(user));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable
    // ,
    // @RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional
    ) {

        ResultPaginationDTO users = this.userService.fetchAllUser(spec, pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User postManUser) throws IdInvalidException {

        User user = this.userService.handleUpdateUser(postManUser);
        if (user == null) {
            throw new IdInvalidException("User với id = " + postManUser.getId() + " không tồn tại");
        }

        return ResponseEntity.ok(userService.convertToResUpdateUserDTO(user));
    }
}
