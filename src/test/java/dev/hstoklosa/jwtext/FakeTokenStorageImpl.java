package dev.hstoklosa.jwtext;

import dev.hstoklosa.jwtext.model.TokenParameters;
import dev.hstoklosa.jwtext.storage.TokenStorage;

import java.util.HashMap;
import java.util.Map;

public class FakeTokenStorageImpl implements TokenStorage {

    private final Map<String, String> tokens = new HashMap<>();

    private String subjectTokenKey(
            final String subject,
            final String type
    ) {
        return "tokens:" + subject + ":" + type;
    }

    @Override
    public String get(final TokenParameters params) {
        String tokenKey = subjectTokenKey(
                params.getSubject(),
                params.getType()
        );

        return tokens.get(tokenKey);
    }

    @Override
    public void save(
            final String token,
            final TokenParameters params
    ) {
        String tokenKey = subjectTokenKey(
                params.getSubject(),
                params.getType()
        );

        tokens.put(tokenKey, token);
    }

    @Override
    public boolean exists(
            final String token,
            final TokenParameters params
    ) {
        String tokenKey = subjectTokenKey(
                params.getSubject(),
                params.getType()
        );

        return token.equals(tokens.get(tokenKey));
    }

    @Override
    public boolean remove(final String token) {
        boolean deleted = false;

        for (Map.Entry<String, String> entry : tokens.entrySet()) {
            if (entry.getValue().equals(token)) {
                tokens.remove(entry.getKey());
                deleted = true;
            }
        }

        return deleted;
    }

    @Override
    public boolean remove(final TokenParameters params) {
        String tokenKey = subjectTokenKey(
                params.getSubject(),
                params.getType()
        );
        
        return tokens.remove(tokenKey) != null;
    }
}