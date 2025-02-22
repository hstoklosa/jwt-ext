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

    /** A map containing the JWT claims to be included in the token. */
    private Map<String, Object> claims;

    /** The 'sub' claim representing the subject of the JWT token. */
    private String subject;

    /** The date the JWT token was issued. */
    private Date issuedAt;

    /** The expiration date of the JWT token. */
    private Date expiredAt;

    /** The type identifier of the JWT token. */
    private String type;

    /**
     * Creates a builder for a TokenParameters instance.
     *
     * This method initializes the 'issuedAt' field with the current system time and computes the 'expiredAt' field by adding 
     * the specified duration (in seconds) to the issuance time.
     *
     * @param subject the subject of the JWT token
     * @param type the token type identifier
     * @param duration the duration between token issuance and expiration, in seconds
     * @return a TokenParametersBuilder pre-populated with the provided values
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
         * Adds a single claim to the parameters.
         *
         * @param key the claim key
         * @param value the claim value
         * @return the current TokenParametersBuilder instance with the claim added
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
         * Adds multiple claims to the parameters.
         *
         * @param claims a map containing the claims to add
         * @return the current TokenParametersBuilder instance with all claims added.
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
         * Sets the issue date of the JWT token.
         *
         * @param issuedAt the date when the token was issued
         * @return the current TokenParametersBuilder instance with the issuance date set.
         */
        public TokenParametersBuilder issuedAt(final Date issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        /**
         * Sets the expiration date of the JWT token.
         *
         * @param expiredAt the date when the token will expire
         * @return the current TokenParametersBuilder instance with the expiration date set.
         */
        public TokenParametersBuilder expiredAt(final Date expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

        /**
         * Sets the subject of the JWT token.
         *
         * @param subject the subject value
         * @return the current TokenParametersBuilder instance with the subject set.
         */
        public TokenParametersBuilder subject(final String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Constructs and returns the final TokenParameters instance with all configured settings.
         *
         * @return a TokenParameters object containing the claims, subject, issuance, expiration, and type information.
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
