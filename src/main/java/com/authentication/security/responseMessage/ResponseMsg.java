package com.authentication.security.responseMessage;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMsg {
    private int status;
    private String errorMessage;

}
