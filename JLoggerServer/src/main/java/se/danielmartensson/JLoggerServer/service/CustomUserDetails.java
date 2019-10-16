package se.danielmartensson.JLoggerServer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import se.danielmartensson.JLoggerServer.model.Role;
import se.danielmartensson.JLoggerServer.model.User;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1256711395932122675L;
	private User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<Role> roles = user.getRoles();
		HashSet<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(roles.size());
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().toUpperCase()));
		}
		return authorities;

	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}