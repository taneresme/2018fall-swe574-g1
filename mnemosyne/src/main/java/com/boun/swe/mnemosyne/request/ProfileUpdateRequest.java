package com.boun.swe.mnemosyne.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
    private String username;
    private String email;
}
