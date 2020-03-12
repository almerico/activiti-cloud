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

import org.activiti.cloud.services.common.security.keycloak.KeycloakAccessTokenProvider;
import org.activiti.cloud.services.common.security.keycloak.KeycloakAccessTokenValidator;
import org.activiti.cloud.services.common.security.keycloak.KeycloakPrincipalIdentityProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeycloakPrincipalIdentityProviderTest {

    private KeycloakPrincipalIdentityProvider subject;

    @Mock
    private KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal;

    @Mock
    private RefreshableKeycloakSecurityContext keycloakSecurityContext;

    @Mock
    private AccessToken accessToken;

    @BeforeEach
    public void setUp() {
        KeycloakAccessTokenProvider keycloakAccessTokenProvider = new KeycloakAccessTokenProvider() { };
        KeycloakAccessTokenValidator keycloakAccessTokenValidator = new KeycloakAccessTokenValidator() { };

        subject = new KeycloakPrincipalIdentityProvider(keycloakAccessTokenProvider,
                                                        keycloakAccessTokenValidator);

        when(principal.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContext);
        when(keycloakSecurityContext.getToken()).thenReturn(accessToken);
    }

    @Test
    public void testGetUserIdValidToken() {
        // given
        when(accessToken.isActive()).thenReturn(true);
        when(accessToken.getPreferredUsername()).thenReturn("username");

        // when
        String userId = subject.getUserId(principal);

        // then
        assertThat(userId).isEqualTo("username");
    }

    @Test
    public void testGetUserIdInvalidToken() {
        // given
        when(accessToken.isActive()).thenReturn(false);

        // when
        Throwable thrown = catchThrowable(() -> { subject.getUserId(principal); });

        // then
        assertThat(thrown).isInstanceOf(SecurityException.class);
    }

}
