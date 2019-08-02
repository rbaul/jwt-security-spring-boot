package com.github.rbaul.spring.boot.security.domain.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "security_privilege")
public class Privilege implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    @Override
    public String getAuthority() {
        return name;
    }

    public void addRole(Role role) {
        if (CollectionUtils.isEmpty(roles)) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    public void removeRole(Role role) {
        if (CollectionUtils.isEmpty(roles)) {
            return;
        }
        roles.remove(role);
    }
}