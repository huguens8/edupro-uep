package ht.uep.edupro_uep.user;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ht.uep.edupro_uep.audit.AuditAction;
import ht.uep.edupro_uep.audit.AuditLogService;
import ht.uep.edupro_uep.dto.LoginRequest;
import ht.uep.edupro_uep.dto.LoginResponse;
import ht.uep.edupro_uep.security.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String REFRESH_COOKIE_NAME = "refresh_token";

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AuditLogService auditLogService;
    private final boolean cookieSecure;
    private final long refreshExpirationMs;

    public AuthController(
            UserService userService,
            RefreshTokenService refreshTokenService,
            AuditLogService auditLogService,
            @Value("${app.auth.cookie-secure}") boolean cookieSecure,
            @Value("${app.jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.auditLogService = auditLogService;
        this.cookieSecure = cookieSecure;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        UserService.LoginResult result = userService.login(request.getEmail(), request.getPassword());
        setRefreshCookie(response, result.rawRefreshToken());
        return result.loginResponse();
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(
            @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshCookie,
            HttpServletResponse response) {
        if (refreshCookie == null) {
            throw new BadCredentialsException("Session invalide ou expirée.");
        }

        RefreshTokenService.Rotated rotated;
        try {
            rotated = refreshTokenService.rotate(refreshCookie);
        } catch (BadCredentialsException ex) {
            auditLogService.log(null, AuditAction.REFRESH_TOKEN_INVALID, "RefreshToken", null, ex.getMessage());
            throw ex;
        }

        setRefreshCookie(response, rotated.rawToken());
        User user = rotated.user();
        String accessToken = userService.generateAccessToken(user);
        return new LoginResponse(accessToken, user.getUsername(), user.getRole().name(), user.getRole().getLibelle());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshCookie,
            HttpServletResponse response) {
        if (refreshCookie != null) {
            refreshTokenService.revoke(refreshCookie);
        }
        clearRefreshCookie(response);
    }

    private void setRefreshCookie(HttpServletResponse response, String rawToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, rawToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(Duration.ofMillis(refreshExpirationMs))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
