package com.al;

import lombok.Data;

@Data
public class LogAction {
    private String username;
    private String action;
    private String objectAction;
    private Long id_objectAction;
}
