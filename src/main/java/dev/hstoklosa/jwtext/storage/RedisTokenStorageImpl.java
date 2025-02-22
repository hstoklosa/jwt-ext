package dev.hstoklosa.jwtext.storage;

import dev.hstoklosa.jwtext.model.TokenParameters;
import dev.hstoklosa.jwtext.redis.DefaultRedisSchema;
import dev.hstoklosa.jwtext.redis.RedisSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Implementation of TokenStorage with Redis integration.
 */
public class RedisTokenStorageImpl implements TokenStorage {
    
    /**
     * Pool of Redis connections.
     */
    private final JedisPool jedisPool;

    /**
     * Schema of keys for storing tokens.
     */
    private final RedisSchema redisSchema;

    /**
     * Creates an object with the provided JedisPool and DefaultRedisSchema.
     *
     * @param jedisPool     JedisPool object
     */
    public RedisTokenStorageImpl(final JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.redisSchema = new DefaultRedisSchema();
    }

    /**
     * Creates an object with the provided JedisPool and RedisSchema.
     *
     * @param jedisPool     JedisPool object
     * @param redisSchema   RedisSchema object
     */
    public RedisTokenStorageImpl(
            final JedisPool jedisPool,
            final RedisSchema redisSchema
    ) {
        this.jedisPool = jedisPool;
        this.redisSchema = redisSchema;
    }

    /**
     * Creates an object with the provided host and port.
     *
     * @param host the host of the Redis server
     * @param port the port of the Redis server
     */
    public RedisTokenStorageImpl(
            final String host,
            final int port
    ) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);
        this.jedisPool = new JedisPool(
                config,
                host,
                port
        );
        this.redisSchema = new DefaultRedisSchema();
    }

    /**
     * Creates an object with the provided host, port, user, and password.
     *
     * @param host the host of the Redis server
     * @param port the port of the Redis server
     * @param user the user of the Redis server
     * @param password the password of the Redis server
     */
    public RedisTokenStorageImpl(
            final String host,
            final int port,
            final String user,
            final String password
    ) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);
        this.jedisPool = new JedisPool(
                config,
                host,
                port,
                user,
                password
        );
        this.redisSchema = new DefaultRedisSchema();
    }

    @Override
    public void save(
            final String token,
            final TokenParameters params
    ) {
        try (Jedis jedis = jedisPool.getResource()) {
            String tokenKey = redisSchema.subjectTokenKey(
                    params.getSubject(),
                    params.getType()
            );

            jedis.set(tokenKey, token);
            jedis.pexpireAt(tokenKey, params.getExpiredAt().getTime());
        }
    }

    @Override
    public boolean exists(
            final String token,
            final TokenParameters params
    ) {
        try (Jedis jedis = jedisPool.getResource()) {
            String tokenKey = redisSchema.subjectTokenKey(
                    params.getSubject(),
                    params.getType()
            );
            
            return token.equals(jedis.get(tokenKey));
        }
    }

    @Override
    public String get(final TokenParameters params) {
        try (Jedis jedis = jedisPool.getResource()) {
            String tokenKey = redisSchema.subjectTokenKey(
                    params.getSubject(),
                    params.getType()
            );

            return jedis.get(tokenKey);
        }
    }

    @Override
    public boolean remove(final String token) {
        try (Jedis jedis = jedisPool.getResource()) {
            String script = """
                    local keys = redis.call('keys', ARGV[1])
                    for _, key in ipairs(keys) do
                      redis.call('del', key)
                    end
                    return #keys > 0
                    """;
            Long result = (Long) jedis.eval(script, 0, "*", token);
            
            return result != null && result > 0;
        }
    }

    @Override
    public boolean remove(final TokenParameters params) {
        try (Jedis jedis = jedisPool.getResource()) {
            String tokenKey = redisSchema.subjectTokenKey(
                    params.getSubject(), params.getType()
            );
            return jedis.del(tokenKey) > 0;
        }
    }
}
