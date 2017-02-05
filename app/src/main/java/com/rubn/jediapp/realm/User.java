package com.rubn.jediapp.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rubén on 04/02/2017.
 */

public class User extends RealmObject {
    @PrimaryKey
    String name;
    String password;
    byte[] image;
    boolean twitter;

    public User() {
    }

    public User(String name, String password, byte[] image, boolean twitter) {
        this.name = name;
        this.password = password;
        this.image = image;
        this.twitter = twitter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isTwitter() {
        return twitter;
    }

    public void setTwitter(boolean twitter) {
        this.twitter = twitter;
    }
}
