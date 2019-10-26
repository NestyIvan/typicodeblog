package com.freenow;

public final class EndPoints {
    //TODO: use environment\gradle variable instead
    public static final int samanthaID = 3;
    //endpoint for Samantha
    public static final String user = String.format("/users/%d", samanthaID);
    //endpoint for Samantha's posts
    public static final String posts = String.format("/posts?userId=%d", samanthaID);
    //endpoint for comments under a post
    public static final String comments = "/posts/{id}/comments";
}
