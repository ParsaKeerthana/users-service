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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        if (updatedUser.getBio() != null) {
            user.setBio(updatedUser.getBio());
        }
        if (updatedUser.getFirstName() != null) {
            user.setBio(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName()!= null) {
            user.setBio(updatedUser.getLastName());
        }
        if (updatedUser.getLocation() != null) {
            user.setLocation(updatedUser.getLocation());
        }
        if (updatedUser.getWebsite() != null) {
            user.setWebsite(updatedUser.getWebsite());
        }
        if (updatedUser.getProfileImageUrl() != null) {
            user.setProfileImageUrl(updatedUser.getProfileImageUrl());
        }
        if (updatedUser.getCoverImageUrl() != null) {
            user.setCoverImageUrl(updatedUser.getCoverImageUrl());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void followUser(String currentUsername, String userToFollowUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));
        User userToFollow = userRepository.findByUsername(userToFollowUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userToFollowUsername));

        currentUser.getFollowing().add(userToFollow.getId());
        userToFollow.getFollowers().add(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(userToFollow);
    }

    public void unfollowUser(String currentUsername, String userToUnfollowUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));
        User userToUnfollow = userRepository.findByUsername(userToUnfollowUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userToUnfollowUsername));

        currentUser.getFollowing().remove(userToUnfollow.getId());
        userToUnfollow.getFollowers().remove(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(userToUnfollow);
    }

    public List<User> getFollowers(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        // Convert the Set of follower IDs to a List to match findAllById signature
        List<String> followerIds = new ArrayList<>(user.getFollowers());
        // findAllById returns an Iterable, we need to convert it to a List
        List<User> followers = new ArrayList<>();
        userRepository.findAllById(followerIds).forEach(followers::add);

        return followers;
    }

    public List<User> getFollowing(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        // Convert the Set of following IDs to a List to match findAllById signature
        List<String> followingIds = new ArrayList<>(user.getFollowing());
        // findAllById returns an Iterable, we need to convert it to a List
        List<User> following = new ArrayList<>();
        userRepository.findAllById(followingIds).forEach(following::add);

        return following;
    }
}
