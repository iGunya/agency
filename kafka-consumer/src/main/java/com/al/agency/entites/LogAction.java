package com.al.agency.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "logAction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_log;

    private String username;
    private String action;
    private String objectAction;
    private Long id_objectAction;
}
