package com.freenow;

public final class EndPoints {
    //endpoint for Samantha
    public static final String USER = String.format("/users/%d", Settings.SAMANTHA_ID);
    //endpoint for Samantha's posts
    public static final String POSTS = String.format("/posts?userId=%d", Settings.SAMANTHA_ID);
    //endpoint for comments under a post
    public static final String COMMENTS = "/posts/{id}/comments";
}
