/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.services.common.security.keycloak.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.activiti.cloud.services.common.security.keycloak.KeycloakSecurityContextTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;


public class KeycloakSecurityContextTokenProviderTest {
    
    private KeycloakSecurityContextTokenProvider subject = new KeycloakSecurityContextTokenProvider();

    @Mock
    private KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal;

    @Mock
    private RefreshableKeycloakSecurityContext keycloakSecurityContext;
    
    @Mock
    private AccessToken accessToken;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        when(principal.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContext);
        when(keycloakSecurityContext.getTokenString()).thenReturn("bearer");
    }    
    
    @Test
    public void testGetCurrentToken() {
        // given
        String subjectId = UUID.randomUUID().toString();
        KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal = new KeycloakPrincipal<>(subjectId,
                                                                                                  keycloakSecurityContext);
        KeycloakAccount account = new SimpleKeycloakAccount(principal,
                                                            Collections.emptySet(),
                                                            principal.getKeycloakSecurityContext());

        SecurityContextHolder.getContext()
                             .setAuthentication(new KeycloakAuthenticationToken(account,
                                                                                false));
        
        // when
        Optional<String> result = subject.getCurrentToken();
        
        // then
        assertThat(result).isPresent()
                          .contains("bearer");
    }
    
    
    @Test
    public void testGetCurrentTokenEmpty() {
        // given
        SecurityContextHolder.clearContext();
        
        // when
        Optional<String> result = subject.getCurrentToken();
        
        // then
        assertThat(result).isEmpty();
    }

}
