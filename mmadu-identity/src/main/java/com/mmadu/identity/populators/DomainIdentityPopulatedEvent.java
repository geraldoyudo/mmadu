package com.mmadu.identity.populators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainIdentityPopulatedEvent {
    private List<String> domainIds = Collections.emptyList();
}
