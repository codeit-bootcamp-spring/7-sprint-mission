package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.user.AuthService;
import com.sprint.mission.discodeit.user.UserService;
import com.sprint.mission.discodeit.user.dto.AuthUserDTO;
import com.sprint.mission.discodeit.user.dto.LoginRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController   {
    private final AuthService authService;
    private final UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthUserDTO> login(@RequestBody @Valid LoginRequestDTO request, HttpServletRequest httpServletRequest){
        AuthUserDTO authUserDTO = authService.login(request);

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("PrincipalUser", userService.findByUsername(authUserDTO.username()));

        return ResponseEntity.ok(authUserDTO);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession();
        if(session != null){
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }
}
