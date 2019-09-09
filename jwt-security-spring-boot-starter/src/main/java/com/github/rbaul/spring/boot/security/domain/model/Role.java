package com.github.rbaul.spring.boot.security.domain.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
@Entity
@Table(name = "security_role")
public class Role extends AuditableEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;

    public void addUser(User user) {
        if (CollectionUtils.isEmpty(users)) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void removeUser(User user) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        users.remove(user);
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        if (CollectionUtils.isEmpty(this.privileges)) {
            this.privileges = new ArrayList<>();
        }
        this.privileges.forEach(privilege -> privilege.removeRole(this));
        privileges.forEach(privilege -> privilege.addRole(this));
        this.privileges = privileges;
    }

}