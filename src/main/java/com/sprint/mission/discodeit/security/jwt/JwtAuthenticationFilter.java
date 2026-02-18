package com.sprint.mission.discodeit.security.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.type.Role;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            JWTClaimsSet jwtClaimsSet = jwtTokenProvider.validateToken(jwt); // 검증
            try {
                UUID userId = UUID.fromString(jwtClaimsSet.getStringClaim("user_id"));
                String name = jwtClaimsSet.getStringClaim("name");
                Role role = Role.valueOf(jwtClaimsSet.getStringClaim("role"));
                String email = jwtClaimsSet.getStringClaim("email");


                UserResponseDto userResponseDto = new UserResponseDto(
                        userId,
                        name,
                        email,
                        null,
                        true,
                        role,
                        null,
                        null
                );

                DiscodeitUserDetails userDetails = DiscodeitUserDetails.fromJwt(userResponseDto);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (ParseException e) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
