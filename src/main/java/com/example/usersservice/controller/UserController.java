package com.example.usersservice.controller;

import com.example.usersservice.model.User;
import com.example.usersservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Received request to create a new user with username: {}", user.getUsername());
        User createdUser = userService.createUser(user);
        log.info("Created new user with id: {}", createdUser.getId());
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User updatedUser) {
        log.info("Received request to update user: {}", username);
        User user = userService.updateUser(username, updatedUser);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{username}/follow/{userToFollowUsername}")
    public ResponseEntity<Void> followUser(@PathVariable String username, @PathVariable String userToFollowUsername) {
        log.info("Received request for user with username: {} to follow user with name: {}", username, userToFollowUsername);
        userService.followUser(username, userToFollowUsername);
        log.info("User with username: {} followed user with id: {}", username, userToFollowUsername);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{username}/unfollow/{userToUnfollowUsername}")
    public ResponseEntity<Void> unfollowUser(@PathVariable String username, @PathVariable String userToUnfollowUsername) {
        log.info("Received request for user with username: {} to unfollow user with id: {}", username, userToUnfollowUsername);
        userService.unfollowUser(username, userToUnfollowUsername);
        log.info("User with username: {} unfollowed user with id: {}", username, userToUnfollowUsername);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable String username) {
        log.info("Received request to get followers for user with username: {}", username);
        List<User> followers = userService.getFollowers(username);
        log.info("Retrieved {} followers for user with username: {}", followers.size(), username);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable String username) {
        log.info("Received request to get following for user with username: {}", username);
        List<User> following = userService.getFollowing(username);
        log.info("Retrieved {} following for user with username: {}", following.size(), username);
        return ResponseEntity.ok(following);
    }

}
