package com.parcelhub.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.parcelhub.entity.Admin;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SecurityUser implements UserDetails{
    private Admin admin;

    private List<String> permission;

    public SecurityUser(Admin admin, List<String> permission){
        this.admin = admin;
        this.permission = permission;
    }

    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends  GrantedAuthority> getAuthorities(){
        if(authorities != null){
            return authorities;
        }
        authorities = permission.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword(){
        return admin.getPassword();
    }

    @Override
    public String getUsername(){
        return admin.getContact();
    }

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
