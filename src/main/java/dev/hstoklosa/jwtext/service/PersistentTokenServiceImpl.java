package dev.hstoklosa.jwtext.service;

import dev.hstoklosa.jwtext.model.TokenParameters;
import dev.hstoklosa.jwtext.storage.TokenStorage;
import dev.hstoklosa.jwtext.storage.TokenStorageImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of TokenService that manages JWT token creation, 
 * verification, and persistence using JWT token storage.
 */
public class PersistentTokenServiceImpl implements TokenService {

    /**
     * The secret key used for signing and verifying JWT tokens.
     */
    private final SecretKey key;

    /**
     * The token storage mechanism for persisting and retrieving JWT tokens.
     */
    private final TokenStorage tokenStorage;

    /**
     * Constant representing the field name for the token type in a JWT token.
     */
    public static final String TOKEN_TYPE_KEY = "tokenType";

    /**
     * Constructs a PersistentTokenServiceImpl instance using a secret key. Internally, it initializes
     * TokenStorage using the default TokenStorageImpl.
     *
     * @param secret secret used for JWT token signing
     */
    public PersistentTokenServiceImpl(final String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.tokenStorage = new TokenStorageImpl();
    }

    /**
     * Constructs a PersistentTokenServiceImpl instance using the provided 
     * secret key and an explicit TokenStorage implementation.
     *
     * @param secret secret used for JWT token signing
     * @param tokenStorage an implementation of the TokenStorage interface for managing JWT tokens
     */
    public PersistentTokenServiceImpl(
            final String secret,
            final TokenStorage tokenStorage
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.tokenStorage = tokenStorage;
    }

    /**
     * Creates a JWT token based on the provided TokenParameters.
     * 
     * If a token already exists for the given parameters, it returns the existing token;
     * otherwise, a new token is generated, persisted in storage, and then returned.
     *
     * @param params the parameters including claims, subject, timestamps, and token type for generating the JWT
     * @return a JWT token string
     */
    @Override
    public String create(final TokenParameters params) {
        String token = tokenStorage.get(
                params
        );
        if (token != null) {
            return token;
        }
        Claims claims = Jwts.claims()
                .subject(params.getSubject())
                .add(params.getClaims())
                .add(TOKEN_TYPE_KEY, params.getType())
                .build();
        token = Jwts.builder()
                .claims(claims)
                .issuedAt(params.getIssuedAt())
                .expiration(params.getExpiredAt())
                .signWith(key)
                .compact();

        tokenStorage.save(token, params);
        return token;
    }

    /**
     * Determines whether the provided JWT token is expired.
     *
     * @param token the JWT token string to check
     * @return true if the token is expired or an ExpiredJwtException is thrown; false otherwise
     */
    @Override
    public boolean isExpired(final String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return claims.getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Checks if the specified JWT token contains a given claim with the expected value.
     *
     * @param token the JWT token string to inspect
     * @param key the claim key to check
     * @param value the expected value associated with the claim key
     * @return true if the claim exists and matches the given value; false otherwise
     */
    @Override
    public boolean has(
            final String token,
            final String key,
            final Object value
    ) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token);

        return claims.getPayload()
                .get(key)
                .equals(value);
    }

    /**
     * Retrieves the subject from the given JWT token.
     *
     * @param token the JWT token string from which to extract the subject
     * @return the subject embedded within the token
     */
    @Override
    public String getSubject(
            final String token
    ) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Retrieves the token type from the provided JWT token.
     *
     * @param token the JWT token string from which the type is extracted
     * @return the token type as a string
     */
    @Override
    public String getType(final String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(TOKEN_TYPE_KEY, String.class);
    }

    /**
     * Extracts all claims from the provided JWT token.
     *
     * @param token the JWT token string to parse
     * @return a map of all claims contained within the token
     */
    @Override
    public Map<String, Object> claims(final String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        return new HashMap<>(claims.getPayload());
    }
}
