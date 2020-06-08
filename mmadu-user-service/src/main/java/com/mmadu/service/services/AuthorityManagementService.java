package com.mmadu.service.services;

import com.mmadu.service.models.AuthorityData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface AuthorityManagementService {

    void saveAuthorities(String domainId, List<AuthorityData> authorities);

    Page<AuthorityData> getAuthorities(String domainId, Pageable p);

    void deleteAuthority(String domainId, String identifier);

    void grantUserAuthorities(String domainId, String userId, List<String> authorityIdentifiers);

    void revokeUserAuthorities(String domainId, String userId, List<String> authorityIdentifiers);

    Set<String> getUserAuthorities(String domainId, String userId);
}
