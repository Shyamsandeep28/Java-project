package ru.sfedu.dateme.domain.authentication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ru.sfedu.dateme.domain.exceptions.PasswordTooLongException;
import ru.sfedu.dateme.domain.exceptions.PasswordTooShortException;
import ru.sfedu.dateme.domain.exceptions.UnknownEncodingAlgorithmException;

public class Password {
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 128;

    private EncodingAlgorithm encodingAlgorithm;
    private String encodedPassword;

    public Password(EncodingAlgorithm encodingAlgorithm, String encodedPassword) {
        this.encodingAlgorithm = encodingAlgorithm;
        this.encodedPassword = encodedPassword;
    }

    public EncodingAlgorithm getEncodingAlgorithm() {
        return encodingAlgorithm;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public static Password create(EncodingAlgorithm encodingAlgorithm, String rawPassword) {
        if (rawPassword.length() < MIN_PASSWORD_LENGTH)
            throw new PasswordTooShortException();

        if (rawPassword.length() > MAX_PASSWORD_LENGTH)
            throw new PasswordTooLongException();

        switch (encodingAlgorithm) {
            case BCRYPT:
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(rawPassword);
                return new Password(
                        encodingAlgorithm, encodedPassword
                );
            default:
                throw new UnknownEncodingAlgorithmException();
        }
    }

    public boolean matches(String rawPassword) {
        switch (encodingAlgorithm) {
            case BCRYPT:
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                return passwordEncoder.matches(rawPassword,
                        encodedPassword);
            default:
                throw new UnknownEncodingAlgorithmException();
        }
    }
}