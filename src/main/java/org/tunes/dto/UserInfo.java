package org.tunes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo {
    private Long userId;
    private String username;
    private String email;

}
