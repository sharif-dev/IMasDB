package com.example.imasdb.model;

public class User {
    private Token RequestToken;
    private static String apiKey = "d8bc38ce00cdce9cbe04ac602f29ffbc";
    private Session sessionToken;
    private static User user = new User();
    private String username;
    private String password;
    private Account account;
    public static enum LoginSuccess {
        FAILED, IN_PROGRESS, SUCCEED
    }

    private LoginSuccess loginSuccess;

    public LoginSuccess isLoginSucceed() {
        return loginSuccess;
    }

    public void setLoginSuccess(LoginSuccess loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    private User() {
    }
    public void setAccount(Account account){
        this.account = account;
    }
    public Account getAccount(){
        return this.account;
    }
    public Token getRequestToken() {
        return RequestToken;
    }

    public void setRequestToken(Token requestToken) {
        RequestToken = requestToken;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String apiKey) {
        User.apiKey = apiKey;
    }

    public Session getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(Session sessionToken) {
        this.sessionToken = sessionToken;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public static User getInstance() {
        return user;
    }

}
