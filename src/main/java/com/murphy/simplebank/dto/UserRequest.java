package com.murphy.simplebank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Schema(
            name = "User firstname"
    )
    private String firstName;
    @Schema(
            name = "User lastname"
    )
    private String lastName;
    @Schema(
            name = "User other name"
    )
    private String otherName;
    @Schema(
            name = "User gender"
    )
    private String gender;
    @Schema(
            name = "User address"
    )
    private String address;
    @Schema(
            name = "User state of origin"
    )
    private String stateOfOrigin;
    @Schema(
            name = "User email"
    )
    private String email;
    private String password;
    @Schema(
            name = "User phone number"
    )
    private String phoneNumber;
    @Schema(
            name = "User other phone number"
    )
    private String alternativePhoneNumber;
}
