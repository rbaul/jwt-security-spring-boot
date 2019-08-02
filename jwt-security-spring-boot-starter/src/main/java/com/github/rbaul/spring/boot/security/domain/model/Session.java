package com.github.rbaul.spring.boot.security.domain.model;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "security_session")
public class Session {

    @Id
    private String username;

    private String id;

    private Date lastLogin;

}