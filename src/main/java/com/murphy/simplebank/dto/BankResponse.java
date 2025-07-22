package com.murphy.simplebank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BankResponse {
    private String responseCode;

    private String responseMessage;

    private AccountInfo accountInfo;

}
