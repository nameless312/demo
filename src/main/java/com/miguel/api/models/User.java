package com.miguel.api.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.Data;

@Data
public class User {
    private String username;
    private String password;
    @ServerTimestamp
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
