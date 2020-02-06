package util;

import android.app.Application;

public class ItemApi extends Application {
    private String username;
    private String userId;
    private static ItemApi instance;

    public static ItemApi getInstance(){
        if(instance == null)
            instance = new ItemApi();
        return instance;

    }

    public ItemApi(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
