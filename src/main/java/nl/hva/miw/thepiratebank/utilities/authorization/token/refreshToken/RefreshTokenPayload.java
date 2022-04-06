package nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken;

import java.util.Date;
import java.util.Objects;

import static nl.hva.miw.thepiratebank.utilities.authorization.token.RefreshTokenService.REFRESHTOKEN_STANDARD_EXPIRY_DATE_MS;

public class RefreshTokenPayload {
    private int userId;
    private Date expiryDate;
    private boolean active;

    public RefreshTokenPayload(int userId) {
        this.userId = userId;
        this.expiryDate = new Date(System.currentTimeMillis()+ REFRESHTOKEN_STANDARD_EXPIRY_DATE_MS);
        this.active = true;
    }

    public RefreshTokenPayload() {
    }

    public int getUserId() {
        return userId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RefreshTokenInfo{" +
                "userId=" + userId +
                ", expiry_date=" + expiryDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshTokenPayload that = (RefreshTokenPayload) o;
        return userId == that.userId && active == that.active && Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, expiryDate, active);
    }
}
