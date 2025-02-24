# jwt-ext

[![codecov](https://codecov.io/gh/hstoklosa/jwt-ext/graph/badge.svg?token=V3U1YHZXM7)](https://codecov.io/gh/hstoklosa/jwt-ext)

An extension of the JJWT library that integrates Redis to enhance JWT management and API authentication. It offers robust token storage, flexible persistence, and advanced claim handling, making JWT operations simple and efficient for your project.

## Key Features

- Effortless JWT token creation and management
- Redis integration for secure token storage
- Configurable token parameters to meet diverse security policies
- Persistent token storage with flexible retrieval options
- Developer-friendly API for streamlined JWT operations

## Technologies

- Java 21
- JJWT (0.12.6) for JWT handling
- Redis integration using Jedis (5.2.0)
- Lombok (1.18.34) for reducing boilerplate code
- JUnit Jupiter (5.11.4) & Testcontainers (1.20.4) for testing
- JaCoCo (0.8.12) for code coverage
- Maven for build and dependency management

## Installation

### Manual

Until the library is published onto Maven Central, you will need to:

- download the source code
- compile it with `mvn package`
- add the `jwt-***.jar` as a dependency to your project.

### Maven (temporarily unavailable)

Add the following to your `pom.xml` file and run `mvn install`:

```xml
<dependency>
    <groupId>dev.hstoklosa.jwtext</groupId>
    <artifactId>jwt-ext</artifactId>
    <version>1.0</version>
</dependency>
```

## Usage

### Initializing the JWT Service

Start by creating a TokenService instance with your base64-encoded secret. For example:

```java
String secret = "e94cf7017da408f96589e9d4b33d2f018c4bf56b3467d901c632d3fb91f0dafd=";
TokenService tokenService = new TokenServiceImpl(secret);
```

### Token Persistence

The library supports PersistentTokenService implementation for saving tokens to TokenStorage. This allows you to store tokens in Redis or an in-memory map. If no specified tokens exist, a new one will be created; otherwise, the stored JWT token will be returned. This approach also allows you to invalidate created and stored JWT tokens.

For in-memory storage, use TokenStorageImpl (default). For Redis storage, use RedisTokenStorageImpl.

```java
String secret = "e94cf7017da408f96589e9d4b33d2f018c4bf56b3467d901c632d3fb91f0dafd=";
TokenService tokenService = new PersistentTokenServiceImpl(secret);
```

With Redis, pass a RedisTokenStorageImpl object to the constructor. To create RedisTokenStorageImpl, pass JedisPool / host and port / host, port, username, and password.

```java
String secret = "e94cf7017da408f96589e9d4b33d2f018c4bf56b3467d901c632d3fb91f0dafd=";
String host   = "localhost";
int port      = 6379;

TokenStorage tokenStorage = new RedisTokenStorageImpl(host, port);
PersistentTokenService tokenService = new PersistentTokenServiceImpl(secret, tokenStorage);
```

You can choose your own RedisSchema, which is used to generate a Redis key for the JWT token. Just pass it as an argument in the RedisTokenStorageImpl constructor. By default, the library uses the key `"tokens:" + subject + ":" + type`.

### Token Invalidation

With PersistentTokenService, you can invalidate a token by the token itself or by subject and token type. If the first option is chosen, all keys with such token values will be deleted. If the token is deleted from storage, you will receive `true`.

```java
String token    = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
boolean deleted = persistentTokenService.invalidate(token);
```

```java
TokenParameters params = TokenParameters
        .builder("user@example.com", "access", Duration.of(1, ChronoUnit.HOURS))
        .build();

boolean deleted = persistentTokenService.invalidate(params);
```

### Token Creation

To create a token, call the `create(TokenParameters params)` method on the TokenService object.

```java
TokenParameters params = TokenParameters
    .builder("user@example.com", "access", Duration.of(1, ChronoUnit.HOURS))
    .build();

String token = tokenService.create(params);
```

In TokenParameters, you can specify the claims of the JWT token, such as:

- issuing date
- expiration date
- subject of the token
- type of the token

All of this is configured via the TokenParameters builder.

### Token Expiration Check

To check if a JWT token is expired, call the `isExpired(String token)` method on the TokenService object.

```java
String token    = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
boolean expired = tokenService.isExpired(token);
```

You can also check expiration with any other date.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
Date date    = new Date(1705911211182);

boolean expired = tokenService.isExpired(token, date);
```

### Claim Existence Check

To check if a JWT token has a claim in the payload, call the `has(String token, String key, Object value)` method on the TokenService object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
String key   = "subject";
String value = "1234567890";

boolean hasSubject = tokenService.has(token, key, value);
```

### Retrieving the Token Subject

To get the subject from the JWT token payload, call the `getSubject(String token)` method on the TokenService object.

**Note:** Optionally, you can call the `claims(token).get("sub").toString()` method on the TokenService object.

```java
String token   = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
String subject = tokenService.getSubject(token);
```

### Retrieving the Token Type

To get the type from the JWT token payload, call the `getType(String token)` method on the TokenService object.

```java
String token   = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
String subject = tokenService.getType(token);
```

### Retrieving All Token Claims

To get all claims from the JWT token payload, call the `claims(String token)` method on the TokenService object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

Map<String, Object> claims = tokenService.claims(token);
claims.forEach((key, value) -> System.out.println(key + " " + value));
```

### Retrieving a Specific Claim

To get a claim by its name from the JWT token payload, call the `claim(String token, String key)` method on the TokenService object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
String claim = (String) tokenService.claim(token, "subject");
```

## License

H. Stoklosa - hubert.stoklosa23@gmail.com

Distributed under the MIT license. See `LICENSE` for more information.

[https://github.com/hstoklosa](https://github.com/hstoklosa)
