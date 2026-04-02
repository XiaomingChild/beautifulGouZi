package com.heavenfilms.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String account;
    private String nickname;
    private String phone;
    private String avatar;
    private String bio;
    private List<Integer> selected;
}
