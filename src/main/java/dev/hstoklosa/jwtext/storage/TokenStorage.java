package dev.hstoklosa.jwtext.storage;

import dev.hstoklosa.jwtext.model.TokenParameters;

public interface TokenStorage {

    /**
     * Save the provided token to storage.
     *
     * @param token     the JWT token
     * @param params    the params of JWT token
     */
    void save(
            String token,
            TokenParameters params
    );

    /**
     * Checks whether a token is located within the storage.
     *
     * @param token     the JWT token
     * @param params    the params of JWT token
     * @return          true  - if JWT token is stored, 
     *                  false - otherwise
     */
    boolean exists(
            String token,
            TokenParameters params
    );

    /**
     * Return a token from storage based on the provided parameters.
     *
     * @param params    the params of JWT token
     * @return          the stored JWT token
     */
    String get(TokenParameters params);

    /**
     * Removes JWT token from storage.
     *
     * @param token JWT token to be removed
     * @return true - if JWT token was removed, false - otherwise
     */
    boolean remove(String token);

    /**
     * Removes JWT token from storage.
     *
     * @param params params of JWT token
     * @return true - if JWT token was removed, false - otherwise
     */
    boolean remove(TokenParameters params);

}