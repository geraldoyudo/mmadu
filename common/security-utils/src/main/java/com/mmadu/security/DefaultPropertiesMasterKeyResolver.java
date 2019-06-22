package com.mmadu.security;

import org.springframework.util.StringUtils;

public class DefaultPropertiesMasterKeyResolver implements MasterKeyResolver {
    private String defaultMasterKey;
    private MasterKeyGenerator masterKeyGenerator;

    public void setDefaultMasterKey(String defaultMasterKey) {
        this.defaultMasterKey = defaultMasterKey;
    }

    public void setMasterKeyGenerator(MasterKeyGenerator masterKeyGenerator) {
        this.masterKeyGenerator = masterKeyGenerator;
    }

    @Override
    public String getMasterKey() {
        if (StringUtils.isEmpty(defaultMasterKey)) {
            defaultMasterKey = generateMasterKey();
        }
        return defaultMasterKey;
    }

    private String generateMasterKey() {
        String masterKey = masterKeyGenerator.generateMasterKey();
        return masterKey;
    }
}
