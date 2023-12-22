package com.pluton.yelody.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacebookUserDTO {
    private String id;
    private String name;
    private String email;
}
