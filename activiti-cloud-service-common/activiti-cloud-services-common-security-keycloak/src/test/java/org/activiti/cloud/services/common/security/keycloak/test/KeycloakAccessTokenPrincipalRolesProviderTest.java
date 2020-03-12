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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import org.activiti.cloud.services.common.security.keycloak.KeycloakAccessTokenPrincipalRolesProvider;
import org.activiti.cloud.services.common.security.keycloak.KeycloakAccessTokenProvider;
import org.activiti.cloud.services.common.security.keycloak.KeycloakAccessTokenValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeycloakAccessTokenPrincipalRolesProviderTest {

    @InjectMocks
    private KeycloakAccessTokenPrincipalRolesProvider subject;

    @Mock
    private KeycloakAccessTokenProvider keycloakSecurityContextProvider;

    @Mock
    private KeycloakAccessTokenValidator keycloakAccessTokenValidator;

    @Mock
    private KeycloakPrincipal<RefreshableKeycloakSecurityContext> keycloakPrincipal;

    @Mock
    private AccessToken accessToken;

    @Test
    public void testGetRoles() {
        // given
        when(keycloakSecurityContextProvider.accessToken(ArgumentMatchers.any())).thenReturn(Optional.of(accessToken));
        when(keycloakAccessTokenValidator.isValid(ArgumentMatchers.any())).thenReturn(true);
        when(accessToken.getRealmAccess()).thenReturn(new AccessToken.Access().roles(new LinkedHashSet<>(Arrays.asList("role1",
                                                                                                                       "role2"))));
        // when
        List<String> result = subject.getRoles(keycloakPrincipal);

        // then
        assertThat(result).isNotEmpty()
                          .containsExactly("role1",
                                           "role2");
    }

    @Test
    public void testGetRolesInvalidToken() {
        // given
        when(keycloakSecurityContextProvider.accessToken(ArgumentMatchers.any())).thenReturn(Optional.of(accessToken));
        when(keycloakAccessTokenValidator.isValid(ArgumentMatchers.any())).thenReturn(false);

        // when
        List<String> result = subject.getRoles(keycloakPrincipal);

        // then
        assertThat(result).isNull();
    }
}
