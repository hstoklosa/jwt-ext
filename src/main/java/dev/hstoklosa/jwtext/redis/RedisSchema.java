package dev.hstoklosa.jwtext.redis;

/**
 * Interface defining the schema for Redis token storage.
 */
public interface RedisSchema {

    /**
     * Generates the Redis key used for storing a JWT token.
     *
     * @param subject the JWT token subject (the "sub" claim)
     * @param type the token type identifier
     * @return the Redis key corresponding to the provided subject and token type
     */
    String subjectTokenKey(
            String subject,
            String type
    );
}
