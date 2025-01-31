package dev.hstoklosa.jwtext.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Builder(
    builderMethodName = "hiddenBuilder",
    access = AccessLevel.PRIVATE
)
@Getter
public class TokenParameters {

    /** A map of claims to be inserted into a JWT token. */
    private Map<String, Object> claims;

    /** The "sub" claim of the JWT token. */
    private String subject;

    /** The date when the JWT token was issued. */
    private Date issuedAt;

    /** The date when the JWT token will be expired. */
    private Date expiredAt;

    /** Type of the JWT token. */
    private String type;

    /**
     * Creates a builder for the TokenParameters class.
     *
     * @param subject   the subject of a JWT token
     * @param duration  the duration between token issue and expiration date
     * @return          a TokenParametersBuilder
     */
    public static TokenParametersBuilder builder(
        final String subject,
        final String type,
        final Duration duration
    ) {
        Date issuedAt = new Date();
        long durationL = issuedAt.getTime() + 1000 * duration.get(ChronoUnit.SECONDS);

        return hiddenBuilder()
                .type(type)
                .claims(new HashMap<>())
                .issuedAt(issuedAt)
                .subject(subject)
                .expiredAt(new Date(durationL));
    }

    public static class TokenParametersBuilder {

        /**
         * Insert a single claim into the data structure storing the parameters.
         *
         * @param key       the key of claim
         * @param value     the value of claim
         * @return          a TokenParametersBuilder
         */
        public TokenParametersBuilder claim(
            final String key,
            final Object value
        ) {
            this.claims.put(key, value);

            if (this.claims != null) {
                this.claims.put(key, value);
            } else {
                this.claims = new HashMap<>();
            }

            return this;
        }

        /**
         * Inserts claims into a data structure storing the parameters.
         *
         * @param claims    a map of claims
         * @return          TokenParametersBuilder
         */
        public TokenParametersBuilder claims(final Map<String, Object> claims) {
            if (this.claims != null) {
                this.claims.putAll(claims);
            } else {
                this.claims = new HashMap<>();
            }
            
            return this;
        }

        /**
         * Sets issued date for the provided JWT token.
         *
         * @param issuedAt  date of issuing
         * @return          TokenParametersBuilder
         */
        public TokenParametersBuilder issuedAt(final Date issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        /**
         * Sets expiration date for the provided JWT token.
         *
         * @param expiredAt     the expiration date
         * @return              a TokenParametersBuilder
         */
        public TokenParametersBuilder expiredAt(final Date expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

        /**
         * Sets subject within the parameters.
         *
         * @param subject   the subject of a JWT token
         * @return          a TokenParametersBuilder
         */
        public TokenParametersBuilder subject(final String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Builds the final object storing the claims of a token.
         *
         * @return  a TokenParameters object
         */
        public TokenParameters build() {
            return new TokenParameters(
                    claims,
                    subject,
                    issuedAt,
                    expiredAt,
                    type
            );
        }
    }
}