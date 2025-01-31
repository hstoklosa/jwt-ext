package dev.hstoklosa.jwtext.redis;

public class DefaultRedisSchema implements RedisSchema {
    @Override
    public String subjectTokenKey(
            final String subject,
            final String type
    ) {
        return "tokens:" + subject + ":" + type;
    }
}