package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;


@RestController
@RequestMapping("/profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController {
    private final ProfileDao profileDao;
    private final UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping
    public Profile getProfile(Authentication authentication) {
        String username = authentication.getName();
        int userId = userDao.getIdByUsername(username);
        return profileDao.getByUserId(userId);
    }

    @PutMapping
    public void updateProfile(@RequestBody Profile updatedProfile, Authentication authentication) {
        String username = authentication.getName();
        int userId = userDao.getIdByUsername(username);
        updatedProfile.setUserId(userId); //
        profileDao.update(updatedProfile);
    }
}
