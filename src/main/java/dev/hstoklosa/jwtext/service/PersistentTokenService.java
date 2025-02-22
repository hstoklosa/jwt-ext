package dev.hstoklosa.jwtext.service;

import dev.hstoklosa.jwtext.model.TokenParameters;

/**
 * Extension of TokenService that provides methods to 
 * invalidate tokens from the underlying storage mechanism.
 */
public interface PersistentTokenService extends TokenService {

    /**
     * Invalidates a JWT token by removing it from the persistent storage.
     *
     * @param token the JWT token string to invalidate
     * @return true if successfully removed, false otherwise
     */
    boolean invalidate(String token);

    /**
     * Invalidates a JWT token using its associated parameters.
     *
     * @param params the parameters of the JWT token to invalidate
     * @return true if successfully removed, false otherwise
     */
    boolean invalidate(TokenParameters params);
}