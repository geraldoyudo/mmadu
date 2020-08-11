package com.mmadu.otp.service.repositories;

import com.mmadu.otp.service.entities.OtpCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OtpCounterRepository extends MongoRepository<OtpCounter, String> {
}
