package org.activiti.cloud.services.identity.basic;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BasicAuthenticationProviderTest {

    @InjectMocks
    private BasicAuthenticationProvider basicAuthenticationProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Test
    public void testAuthenticate() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("testrole"));
        User user = new User("test",
                             "pass",
                             authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test",
                                                                                                     "pass",
                                                                                                     authorities);

        when(userDetailsService.loadUserByUsername("test"))
                .thenReturn(user);

        assertThat(basicAuthenticationProvider.authenticate(authentication)).isNotNull();
    }

    @Test
    public void testAuthenticationFailure() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("testrole"));
        User user = new User("test",
                             "pass",
                             authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("differentuser",
                                                                                                     "differentpass",
                                                                                                     authorities);

        when(userDetailsService.loadUserByUsername("differentuser"))
                .thenReturn(user);

        assertThatExceptionOfType(BadCredentialsException.class).isThrownBy(() -> basicAuthenticationProvider.authenticate(authentication));
    }

    @Test
    public void testSupports() {
        assertThat(basicAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class)).isTrue();
        assertThat(basicAuthenticationProvider.supports(Integer.class)).isFalse();
    }
}
