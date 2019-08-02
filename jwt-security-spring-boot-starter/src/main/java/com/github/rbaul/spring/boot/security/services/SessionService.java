package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.domain.model.Session;
import com.github.rbaul.spring.boot.security.domain.repository.SessionRepository;
import com.github.rbaul.spring.boot.security.services.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public boolean validateLoginTime(String token) {
        String username = jwtProvider.getUsername(token);
        Optional<Session> sessionOptional = sessionRepository.findByUsername(username);
        if (sessionOptional.isPresent()) {
            Date tokenIat = jwtProvider.getIssuedAtTime(token);
            Date userLastLogin = sessionOptional.get().getLastLogin();
            return userLastLogin == null || userLastLogin.getTime() == tokenIat.getTime();
        }
        return true;
    }

    @Transactional
    public void updateSession(String token) {
        String username = jwtProvider.getUsername(token);
        Date loginAt = jwtProvider.getIssuedAtTime(token);
        Optional<Session> sessionOptional = sessionRepository.findByUsername(username);
//        Optional<String> cookieValueOfSessionId = SessionUtils.getCookieValueOfSessionId();
        if (sessionOptional.isPresent()) {
            sessionOptional.get().setLastLogin(loginAt);
//            sessionOptional.get().setId(cookieValueOfSessionId.get());
        } else {
            createNewSessionByUser(username, loginAt);
        }
    }

    private void createNewSessionByUser(String username, Date loginAt) {
        Session newSession = Session.builder()
                .username(username)
                .lastLogin(loginAt).build();
        sessionRepository.save(newSession);
    }

    @Transactional
    public void createSession(String username) {
        Optional<Session> sessionOptional = sessionRepository.findByUsername(username);
        if (!sessionOptional.isPresent()) {
            createNewSessionByUser(username, new Date());
        }
    }

    @Transactional
    public void logoutSession(String username) {
//        Optional<String> cookieValueOfSessionId = SessionUtils.getCookieValueOfSessionId();
//            Optional<Session> sessionOptional = cookieValueOfSessionId.isPresent() ?
//                    sessionRepository.findByIdAndUsername(userDetails.getUsername(), cookieValueOfSessionId.get()) :
//                    sessionRepository.findByUsername(userDetails.getUsername());
        Optional<Session> sessionOptional = sessionRepository.findByUsername(username);
        Session session = sessionOptional
                .orElseThrow(() -> new EmptyResultDataAccessException("No found session with user: " + username, 1));
        session.setLastLogin(new Date());
    }
}
