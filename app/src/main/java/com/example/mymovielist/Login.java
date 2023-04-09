package com.example.mymovielist;

import java.io.Serializable;

public class Login implements Serializable {
    String key;
    String id, image;
    // constructor
    public Login(String id, String image) {
        this.key = "";
        this.id = id;
        this.image = image;
    }
    public Login(String key, String id, String image) {
        this.key = key;
        this.id = id;
        this.image = image;
    }

    public String getKey() { return key; }
    public String getId() { return id; }
    public String getImage() { return image; }

    public void setKey(String key) { this.key = key; }
    public void setId(String id) { this.id = id; }
    public void setImage(String image) { this.image = image; }
}
