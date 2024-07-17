package com.srinjay.book_network.auth;

import com.srinjay.book_network.email.EmailService;
import com.srinjay.book_network.email.EmailTemplatename;
import com.srinjay.book_network.role.RoleRepository;
import com.srinjay.book_network.user.Token;
import com.srinjay.book_network.user.TokenRepository;
import com.srinjay.book_network.user.User;
import com.srinjay.book_network.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value ("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void regiter(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName ("USER")
//                TODO: Better exception handling
                .orElseThrow (() -> new IllegalStateException ("ROLE USER was not initialized"));

        var user = User.builder ()
                .firstName (request.getFirstName ())
                .lastName (request.getLastName ())
                .email (request.getEmail ())
                .password (passwordEncoder.encode (request.getPassword ()))
                .accountLocked (false)
                .enabled (false)
                .roles (List.of (userRole))
                .build ();
        userRepository.save (user);
        sendValidationEmail (user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken (user);
        emailService.sendEmail (
                user.getEmail (),
                user.getFullName (),
                EmailTemplatename.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);

        var token = Token.builder ()
                .token (generatedToken)
                .createdAt (LocalDateTime.now ())
                .expiresAt (LocalDateTime.now ().plusMinutes (15))
                .user (user)
                .build ();
        tokenRepository.save (token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {

        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom ();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt (characters.length ());
            codeBuilder.append (characters.charAt (randomIndex));
        }
        return codeBuilder.toString ();
    }
}
