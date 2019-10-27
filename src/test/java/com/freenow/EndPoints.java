package com.freenow;

public final class EndPoints {
    //endpoint for Samantha
    public static final String user = String.format("/users/%d", Settings.samanthaID);
    //endpoint for Samantha's posts
    public static final String posts = String.format("/posts?userId=%d", Settings.samanthaID);
    //endpoint for comments under a post
    public static final String comments = "/posts/{id}/comments";
}
