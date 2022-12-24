package com.example.authserver.utils.payload;

public class ServerResponse {
    private String statusCode;
    private String message;

    public ServerResponse(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }


}
