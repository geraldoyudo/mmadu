package com.mmadu.registration.providers.otp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mmadu.registration.models.otp.Otp;
import com.mmadu.registration.models.otp.OtpGenerationRequest;
import com.mmadu.registration.models.otp.OtpValidationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OtpServiceImpl implements OtpService {
    private WebClient otpServiceClient;

    @Autowired
    @Qualifier("otpService")
    public void setOtpServiceClient(WebClient otpServiceClient) {
        this.otpServiceClient = otpServiceClient;
    }

    @Override
    public Mono<Otp> generateOtp(OtpGenerationRequest request) {
        return this.otpServiceClient.post()
                .uri(uriBuilder -> uriBuilder.path("/otp/token")
                        .build()
                ).body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(Otp.class);
    }

    @Override
    public Mono<Boolean> validateOtp(OtpValidationRequest request) {
        return this.otpServiceClient.post()
                .uri(uriBuilder -> uriBuilder.path("/otp/validate")
                        .build()
                ).body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(ValidationResult.class)
                .map(ValidationResult::isValid);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValidationResult {
        private boolean valid;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }
}
