package com.example.usersservice.service;

import com.example.usersservice.exception.ResourceNotFoundException;
import com.example.usersservice.model.User;
import com.example.usersservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUser(String username, User updatedUser) {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName()!= null) {
            user.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getLocation() != null) {
            user.setLocation(updatedUser.getLocation());
        }
        if (updatedUser.getProfileImageUrl() != null) {
            user.setProfileImageUrl(updatedUser.getProfileImageUrl());
        }
        if (updatedUser.getCoverImageUrl() != null) {
            user.setCoverImageUrl(updatedUser.getCoverImageUrl());
        }
        if (updatedUser.getUsername() != null) {
            user.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getTagline() != null) {
            user.setTagline(updatedUser.getTagline());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void followUser(String currentUsername, String userToFollowUsername) {
        User currentUser = userRepository.findByUserId(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));
        User userToFollow = userRepository.findByUserId(userToFollowUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userToFollowUsername));

        currentUser.getFollowing().add(userToFollow.getUserId());
        userToFollow.getFollowers().add(currentUser.getUserId());

        userRepository.save(currentUser);
        userRepository.save(userToFollow);
    }

    public void unfollowUser(String currentUsername, String userToUnfollowUsername) {
        User currentUser = userRepository.findByUserId(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));
        User userToUnfollow = userRepository.findByUserId(userToUnfollowUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userToUnfollowUsername));

        currentUser.getFollowing().remove(userToUnfollow.getUserId());
        userToUnfollow.getFollowers().remove(currentUser.getUserId());

        userRepository.save(currentUser);
        userRepository.save(userToUnfollow);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.searchByUsername(keyword);
    }

    public List<User> getFollowers(String username) {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        List<String> followerIds = new ArrayList<>(user.getFollowers());
        // findAllById returns an Iterable, we need to convert it to a List
        List<User> followers = new ArrayList<>();
        userRepository.findAllById(followerIds).forEach(followers::add);

        return followers;
    }

    public List<User> getFollowing(String username) {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        // Convert the Set of following IDs to a List to match findAllById signature
        List<String> followingIds = new ArrayList<>(user.getFollowing());
        // findAllById returns an Iterable, we need to convert it to a List
        List<User> following = new ArrayList<>();
        userRepository.findAllById(followingIds).forEach(following::add);

        return following;
    }
    public User getUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userId));
        return user;
    }
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }
}
