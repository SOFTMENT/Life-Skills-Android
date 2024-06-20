package in.softment.lifeskillsapp.Model;

import java.util.Date;

public class UserModel {

    public String fullName = "";
    public String email = "";
    public String uid = "";
    public Date registredAt = new Date();
    public String regiType = "";
    public int lastQuotesId = 0;
    public Date lastQuotesDate = new Date();
    public String notiToken = "";
    public static UserModel data = new UserModel();
    public boolean membership = false;
    public UserModel() {

        data = this;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getRegistredAt() {
        return registredAt;
    }

    public void setRegistredAt(Date registredAt) {
        this.registredAt = registredAt;
    }

    public String getRegiType() {
        return regiType;
    }

    public void setRegiType(String regiType) {
        this.regiType = regiType;
    }

    public int getLastQuotesId() {
        return lastQuotesId;
    }

    public void setLastQuotesId(int lastQuotesId) {
        this.lastQuotesId = lastQuotesId;
    }

    public Date getLastQuotesDate() {
        return lastQuotesDate;
    }

    public void setLastQuotesDate(Date lastQuotesDate) {
        this.lastQuotesDate = lastQuotesDate;
    }

    public String getNotiToken() {
        return notiToken;
    }

    public void setNotiToken(String notiToken) {
        this.notiToken = notiToken;
    }

    public boolean isMembership() {
        return membership;
    }

    public void setMembership(boolean membership) {
        this.membership = membership;
    }
}
