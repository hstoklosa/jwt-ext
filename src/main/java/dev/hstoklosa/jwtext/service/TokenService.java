package dev.hstoklosa.jwtext.service;

import dev.hstoklosa.jwtext.model.TokenParameters;

import java.util.Map;

public interface TokenService {

    /**
     * Creates a JWT token using the provided parameters in a map.
     *
     * @param params    the parameters for JWT token
     * @return          a JWT token as a String
     */
    String create(TokenParameters params);

    /**
     * Checks whether JWT token is expired by current time.
     *
     * @param token     the JWT token to be checked for expiration
     * @return          true - if JWT token expired, false - otherwise
     */
    boolean isExpired(String token);

    /**
     * Checks whether the token has a key-value pair within it's payload.
     *
     * @param token     the passed JWT token
     * @param key       the key of a payload
     * @param value     the value of a payload
     * @return          true  - if the key-value pair exists,
     *                  false - otherwise
     */
    boolean has(
        String token,
        String key,
        Object value
    );

    /**
     * Returns the "sub" claim of a received JWT token.
     *
     * @param token     the provided JWT token
     * @return          the subject of the JWT token
     */
    String getSubject(String token);

    /**
     * Returns the payload of a JWT token as a Map.
     *
     * @param token     the provided JWT token
     * @return          the key-value pairs from the payload
     */
    Map<String, Object> claims(String token);


    /**
     * Returns the type of a JWT token.
     *
     * @param token     a JWT token
     * @return          the type of JWT token
     */
    String getType(String token);
}