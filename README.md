# jwt-ext

[![codecov](https://codecov.io/gh/hstoklosa/jwt-ext/graph/badge.svg?token=V3U1YHZXM7)](https://codecov.io/gh/hstoklosa/jwt-ext)

An extension of the JJWT library with Redis integration to streamline API authentications for convenient usage.

## Key Features

- Seamless JWT token management
- Redis integration for token storage
- Flexible token parameter configuration
- Supports token persistence and retrieval
- Easy-to-use API for JWT operations

## Installation

### Manual

Until the library is published onto Maven Central, you will need to:

- download the source code
- compile it with `mvn package`
- add the `jwt-***.jar` as a dependency to your project.

### Maven (pending)

Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>dev.hstoklosa.jwtext</groupId>
    <artifactId>jwt-ext</artifactId>
    <version>1.0</version>
</dependency>
```

## Usage

### Instantiate a service object

You need to create a `TokenService` object and pass your generated `secret` (base64 encoded string for JWT tokens) to the constructor.

```java
String secret = "e94cf7017da408f96589e9d4b33d2f018c4bf56b3467d901c632d3fb91f0dafd=";
TokenService tokenService = new TokenServiceImpl(secret);
```

After, you can call the available methods and use library.

### Persist tokens

Library supports `PersistentTokenServiceImpl` implementation with saving
tokens to `TokenStorage`.
With such approach you can store tokens in Redis or in-memory Map and create new
one if no specified tokens exist, otherwise, stored JWT token would be returned.
This approach allows you to invalidate created and stored JWT token.
For this look at `TokenStorage` class. Use `TokenStorageImpl` for in-memory
storage (default) and `RedisTokenStorageImpl` for Redis storage.

```java
String secret = "e94cf7017da408f96589e9d4b33d2f018c4bf56b3467d901c632d3fb91f0dafd=";
TokenService tokenService = new PersistentTokenServiceImpl(secret);
```

To use Redis you need to pass the `RedisTokenStorageImpl` object to constructor, and to create the `RedisTokenStorageImpl` you need to pass a JedisPool.

```java
String secret = "e94cf7017da408f96589e9d4b33d2f018c4bf56b3467d901c632d3fb91f0dafd=";

JedisPoolConfig config = new JedisPoolConfig();
config.setJmxEnabled(false);

JedisPool jedisPool = new JedisPool(config, "localhost", 6379);
TokenStorage tokenStorage = new RedisTokenStorageImpl(jedisPool);
TokenService tokenService = new PersistentTokenServiceImpl(secret, tokenStorage);
```

You can choose your own `RedisSchema` which is used to generate a Redis key for the JWT token. Just pass it as an argument in `RedisTokenStorageImpl` constructor. By default, the library uses a key format of `"tokens:" + subject + ":" + type`.

### Create JWT token

To create a token, call the method `create(TokenParameters params)` on the `TokenService` object.

```java
Duration duration = Duration.of(1, ChronoUnit.HOURS);
String token = tokenService.create(
    TokenParameters.builder("user@example.com", duration).build()
);
```

In `TokenParameters`, you can specify the:

- claims to be put in JWT token
- issue date
- expiration date
- subject
- type

This all is configured via `TokenParameters` builder.

### Check if JWT token is expired

To check if JWT token is expired, call the method `isExpired(String token)` on `TokenService` object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
boolean expired = tokenService.isExpired(token);
```

### Check if JWT token has claim

To check if JWT token has claim in payload, call the method `has(String token, String key, Object value)` on `TokenService` object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
String key = "subject";
String value = "1234567890";

boolean hasSubject = tokenService.has(token, key, value);
```

### Retrieve the subject from a token

To get the subject from a JWT token payload, call the method `getSubject(String token)` on a `TokenService` object.

**Note:** Optionally, you can call the method `claims(token).get("sub").toString()` on a `TokenService` object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
String subject = tokenService.getSubject(token);

System.out.println(subject);
```

### Retrieve all claims from a token

To get all claims from JWT token payload call method `claims(String token)`
on `TokenService` object.

```java
String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
Map<String, Object> claims = tokenService.claims(token);

claims.forEach((key, value) -> System.out.println(key + " " + value));
```

### Retrieve the type of a token

To get type from JWT token payload call method `getType(String token)`
on `TokenService` object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
String subject = tokenService.getType(token);

System.out.println(subject);
```

## License

H. Stoklosa - hubert.stoklosa23@gmail.com

Distributed under the MIT license. See `LICENSE` for more information.

[https://github.com/hstoklosa](https://github.com/hstoklosa)
