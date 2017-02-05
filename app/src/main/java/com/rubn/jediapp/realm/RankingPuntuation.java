package com.rubn.jediapp.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rubén on 04/02/2017.
 */

public class RankingPuntuation extends RealmObject{

    private String name;
    @PrimaryKey
    private int date;
    private int intents;

    public RankingPuntuation() {
    }

    public RankingPuntuation(String name, int date, int intents) {
        this.name = name;
        this.date = date;
        this.intents = intents;
    }

    public int getIntents() {
        return intents;
    }

    public void setIntents(int intents) {
        this.intents = intents;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
