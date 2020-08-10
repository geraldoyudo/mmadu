package com.mmadu.notifications.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class SendgridRequest {
    @JsonProperty("from")
    private Address from;
    @JsonProperty("template_id")
    private String templateId;
    @JsonProperty("personalizations")
    @Singular
    private List<Personalization> personalizations;

    @Data
    @Builder
    @EqualsAndHashCode
    public static class Address {
        private String email;
    }

    @Data
    @Builder
    @EqualsAndHashCode
    public static class Personalization {
        @JsonProperty("to")
        @Singular("to")
        private List<Address> to;
        @JsonProperty("dynamic_template_data")
        private Object data;
    }

}
