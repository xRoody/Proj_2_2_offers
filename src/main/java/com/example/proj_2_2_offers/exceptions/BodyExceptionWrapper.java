package com.example.proj_2_2_offers.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BodyExceptionWrapper {
    private String status;
    private String reason;

    public BodyExceptionWrapper(String status, String reason) {
        this.status = status;
        this.reason = reason;
    }
}
