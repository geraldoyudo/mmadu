package com.mmadu.otp.service.repositories;

import com.mmadu.otp.service.entities.OtpToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface OtpTokenRepository extends MongoRepository<OtpToken, String> {

    void deleteByDomainIdAndProfileAndKey(@Param("domainId") String domainId,
                                          @Param("profile") String profile,
                                          @Param("key") String key);
}
