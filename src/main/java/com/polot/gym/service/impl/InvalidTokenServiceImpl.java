package com.polot.gym.service.impl;

import com.polot.gym.entity.InvalidToken;
import com.polot.gym.repository.InvalidTokenRepository;
import com.polot.gym.service.InvalidTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class InvalidTokenServiceImpl implements InvalidTokenService {
    private final InvalidTokenRepository invalidTokenRepository;
    private final JwtService jwtService;

    @Override
    public void addToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (!authorization.startsWith("Bearer ") ||
                !jwtService.validateToken(authorization.split(" ")[1]) ||
                invalidTokenRepository.existsByToken(authorization.split(" ")[1]))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");

        invalidTokenRepository.save(InvalidToken.builder().token(authorization.split(" ")[1]).build());
    }
}
