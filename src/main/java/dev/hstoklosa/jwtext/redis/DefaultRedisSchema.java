package dev.hstoklosa.jwtext.redis;

/**
 * Default implementation of the RedisSchema interface that provides
 * a standardised way to generate Redis key patterns for JWT token storage.
 *
 * @see RedisSchema
 */
public class DefaultRedisSchema implements RedisSchema {

    /**
     * Generates a Redis key for storing tokens associated with a specific subject and type.
     * The key follows the pattern "tokens:{subject}:{type}".
     *
     * @param subject the identifier of the subject (user/entity) associated with the token
     * @param type the type of token (e.g., "access", "refresh")
     * @return a formatted Redis key in a string format
     */
    @Override
    public String subjectTokenKey(final String subject, final String type) {
        return "tokens:" + subject + ":" + type;
    }
}