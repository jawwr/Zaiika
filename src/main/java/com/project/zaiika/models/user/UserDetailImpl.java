package com.project.zaiika.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private String userName;
    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailImpl of(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        Set<GrantedAuthority> rolePermissions = new HashSet<>();
        for (var role : user.getRoles()) {
            if (role.getPermissions() == null) {
                continue;
            }
            var permission = role.getPermissions()
                    .stream().map(perm -> new SimpleGrantedAuthority(perm.getName()))
                    .toList();
            rolePermissions.addAll(permission);
        }
        authorities.addAll(rolePermissions);

        return new UserDetailImpl(
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
