package com.dadapp.seniorproject.user;

import com.dadapp.seniorproject.config.security.SecurityService;
import com.dadapp.seniorproject.config.security.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        model.addAttribute("text", "DadApp - Login");

        if (securityService.isAuthenticated()) {
            return "redirect:/index";
        }

        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping("/userList")
    public String userList(Model model) {
        model.addAttribute("listUsers", userService.findAllUsers());
        model.addAttribute("text", "User List");
        return "userlist";
    }

    @GetMapping("/registration")
    public String viewRegistrationPage(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/index";
        }

        model.addAttribute("userForm", new User());
        model.addAttribute("text", "DadApp - Registration");

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/index";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseStatus> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<ResponseStatus>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/edit/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model) {
        User user = userService.getId(id);
        model.addAttribute("user", user);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("createdOn", user.getCreatedOn());
        model.addAttribute("text", "DadApp - Edit User");
        return "edituser";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUserData(@RequestParam(defaultValue = "") String username) {

        return userService.findByUsername(username);
    }

    @RequestMapping(value = "/saveUserDesc", method = RequestMethod.POST)
    @ResponseBody
    public String saveUserDesc(@RequestParam(defaultValue = "") String username, String description) {
        userService.updateUser(username, description);
        return "";
    }

    @RequestMapping(value = "/setFriend", method = RequestMethod.POST)
    @ResponseBody
    public String setFriend(@RequestParam(defaultValue = "") String adderUsername, String friendUsername,
            boolean adding) {
        User adderUser = userService.findByUsername(adderUsername);
        User friendUser = userService.findByUsername(friendUsername);
        userService.updateUser(adderUser, friendUser, adding);
        return "";
    }

    @RequestMapping(value = "/getFriends", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getFriends(@RequestParam(defaultValue = "") String username) {
        User user = userService.findByUsername(username);

        List<User> friendsOfUser = userService.findAllUsers().stream()
                .filter(tempUser -> (user.getFriendIds().contains(tempUser.getId().toString())))
                .collect(Collectors.toList());

        return friendsOfUser;
    }
}
