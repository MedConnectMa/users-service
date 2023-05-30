package com.authentication.security.responseMessage;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMsg {
    private int status;
    private String Message;
    private int userId;

    public ResponseMsg(int status, String Message){
        this.status = status;
        this.Message = Message;
    }
}
