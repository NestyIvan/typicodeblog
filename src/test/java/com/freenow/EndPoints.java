package com.freenow;

public final class EndPoints {
    //endpoint for user by id
    public static final String USER = "/users/{id}";
    //endpoint for user by Name
    public static final String USER_BY_NAME = "/users?username={username}";
    //endpoint for user's posts
    public static final String POSTS = "/posts?userId={userId}";
    //endpoint for comments under a post
    public static final String COMMENTS = "/posts/{id}/comments";
}
