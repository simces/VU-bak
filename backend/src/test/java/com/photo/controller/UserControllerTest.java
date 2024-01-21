package com.photo.controller;

import com.photo.business.service.UserService;
import com.photo.model.UserProfileDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;


    @Test
    @WithMockUser
    public void givenValidUsername_whenFindByUsername_thenReturns200() throws Exception {
        String validUsername = "simas";

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(validUsername);

        Mockito.when(userService.findByUsername(validUsername)).thenReturn(userProfileDTO);

        mockMvc.perform(get("/users/{username}", validUsername)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(validUsername));
    }
}

