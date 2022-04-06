package nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken;

import java.util.Objects;
import java.util.UUID;

public class RefreshToken {
    private String refreshTokenKey;
    private RefreshTokenPayload refreshTokenPayload;

    public RefreshToken(String refreshTokenKey, RefreshTokenPayload refreshTokenPayload) {
        this.refreshTokenKey = refreshTokenKey;
        this.refreshTokenPayload = refreshTokenPayload;
    }

    public RefreshToken(RefreshTokenPayload refreshTokenPayload) {
        this.refreshTokenPayload = refreshTokenPayload;
        generateRefreshTokenKey();
    }

    public RefreshToken() {
    }

    public void generateRefreshTokenKey() {
        this.refreshTokenKey = UUID.randomUUID().toString();
    }

    public String getRefreshTokenKey() {
        return refreshTokenKey;
    }

    public void setRefreshTokenKey(String refreshTokenKey) {
        this.refreshTokenKey = refreshTokenKey;
    }

    public RefreshTokenPayload getPayload() {
        return refreshTokenPayload;
    }

    public void setRefreshTokenPayload(RefreshTokenPayload refreshTokenPayload) {
        this.refreshTokenPayload = refreshTokenPayload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(refreshTokenKey, that.refreshTokenKey) && Objects.equals(refreshTokenPayload, that.refreshTokenPayload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refreshTokenKey, refreshTokenPayload);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "refreshTokenKey='" + refreshTokenKey + '\'' +
                ", refreshTokenPayload=" + refreshTokenPayload +
                '}';
    }
}
