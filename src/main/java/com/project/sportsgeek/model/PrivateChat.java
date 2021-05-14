package com.project.sportsgeek.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChat {
    private int privateChatId;
    private int fromUserId;
    private int toUserId;
    private String message;
    private Timestamp chatTimestamp;
}
