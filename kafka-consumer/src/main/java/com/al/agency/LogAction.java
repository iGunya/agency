package com.al.agency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogAction {
    private String username;
    private String action;
    private String objectAction;
    private Long id_objectAction;

}
