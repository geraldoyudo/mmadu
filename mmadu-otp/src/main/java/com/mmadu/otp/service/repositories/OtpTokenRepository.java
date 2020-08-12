package com.mmadu.otp.service.repositories;

import com.mmadu.otp.service.entities.OtpToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OtpTokenRepository extends MongoRepository<OtpToken, String> {

}
