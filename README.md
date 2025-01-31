# jwt-ext

[![codecov](https://codecov.io/gh/hstoklosa/jwt-ext/graph/badge.svg?token=V3U1YHZXM7)](https://codecov.io/gh/hstoklosa/jwt-ext)

This library provides simple and convenient usage.

## Installation

First, you need to install this library.

### Manual

Until it is published onto Maven Central, you will need to:

- download the source code
- compile it with `mvn package`
- add the `jwt-***.jar` as a dependency to your project.

### Maven

...

## Usage

### Instantiate a service object

You need to create `TokenService` object and pass `secret` (base64 encoded
secret string for
JWT tokens) to the constructor.

```java
String secret = "c29tZWxvbmdzZWNyZXRzdHJpbmdmb3JleGFtcGxlYW5kaXRuZWVkc3RvYmVsb25nDQo=";
TokenService tokenService = new TokenServiceImpl(secret);
```

After, you can call available methods and use library.

### Create JWT token

To create token call method `create(TokenParameters params)` on `TokenService`
object.

```java
Durtation duration = Duration.of(1, ChronoUnit.HOURS);
String token = tokenService.create(
    TokenParameters.builder("user@example.com", duration).build()
);
```

You can specify in `TokenParameters`:

- claims to be put in JWT token
- JWT token issuing date
- JWT token expiration date
- "sub" of JWT token
  It all is configured via `TokenParameters` builder.

### Check if JWT token is expired

To check if JWT token is expired call method `isExpired(String token)` on `TokenService` object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
boolean expired = tokenService.isExpired(token);
```

### Check if JWT token has claim

To check if JWT token has claim in payload call method `has(String token, String key, Object value)` on `TokenService` object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
String key = "subject";
String value = "1234567890";

boolean hasSubject = tokenService.has(
        token,
        key,
        value
    );
```

### Get subject from JWT token

To get subject from JWT token payload call method `subject(String token)`
on `TokenService` object.

**Note:** Optional, you can call
method `claims(token).get("sub").toString()` on `TokenService` object.

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
String subject = tokenService.getSubject(token);
```

### Get claims from JWT token

To get all claims from JWT token payload call method `claims(String token)`
on `TokenService`
object.

```java
String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

Map<String, Object> claims=tokenService.claims(token);
claims.forEach((key,value) -> System.out.println(key + " " + value));
```

## License

H. Stoklosa - hubert.stoklosa23@gmail.com

Distributed under the MIT license. See `LICENSE` for more information.

[https://github.com/hstoklosa](https://github.com/hstoklosa)
