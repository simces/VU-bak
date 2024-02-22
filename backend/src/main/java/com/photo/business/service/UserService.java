package com.photo.business.service;

import com.photo.business.repository.model.UserDAO;
import com.photo.model.UserCreationDTO;
import com.photo.model.UserPasswordChangeDTO;
import com.photo.model.UserProfileDTO;

public interface UserService {

    /**
     * Retrieves a user profile based on the username.
     *
     * @param username The username of the user.
     * @return UserProfileDTO containing user details.
     */
    UserProfileDTO findByUsername(String username);

    /**
     * Registers a new user into the system.
     *
     * @param userCreationDTO The data transfer object containing user creation data.
     * @return UserDAO representing the newly created user.
     */
    UserDAO registerUser(UserCreationDTO userCreationDTO);

    /**
     * Updates the profile details of an existing user.
     *
     * @param userId The ID of the user to update.
     * @param userProfileDTO The data transfer object containing new user profile data.
     * @return UserProfileDTO representing the updated user profile.
     */
    UserProfileDTO changeProfileDetails(Long userId, UserProfileDTO userProfileDTO);

    /**
     * Changes the password of an existing user.
     *
     * @param userId The ID of the user whose password is to be changed.
     * @param passwordChangeDTO The data transfer object containing the old and new password.
     */
    void changePassword(Long userId, UserPasswordChangeDTO passwordChangeDTO);

    public UserProfileDTO getUserById(Long id);
}
