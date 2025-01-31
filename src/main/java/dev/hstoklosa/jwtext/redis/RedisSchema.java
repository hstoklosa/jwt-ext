package dev.hstoklosa.jwtext.redis;

/**
 * Schema interface for the RedisTokenStorage implementation.
 */
public interface RedisSchema {
    /**
     * Redis key to be stored with the JWT token.
     *
     * @param subject   a "sub" of JWT token
     * @param type      the token type
     * @return          the key to store JWT token with
     */
    String subjectTokenKey(
            String subject,
            String type
    );
}