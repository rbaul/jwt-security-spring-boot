package com.github.rbaul.spring.boot.security.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "security_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public void setRoles(Collection<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            roles = new ArrayList<>();
        }
        this.roles.forEach(role -> role.removeUser(this));
        roles.forEach(role -> role.addUser(this));
        this.roles = roles;
    }

    public Set<Privilege> getPrivileges() {
        if (!StringUtils.isEmpty(roles)) {
            return roles.stream()
                    .map(Role::getPrivileges)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }

    public Set<String> getPrivilegeNames() {
        return getPrivileges().stream()
                .map(Privilege::getAuthority)
                .collect(Collectors.toSet());
    }

    public Set<String> getRoleNames() {
        return getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

}
