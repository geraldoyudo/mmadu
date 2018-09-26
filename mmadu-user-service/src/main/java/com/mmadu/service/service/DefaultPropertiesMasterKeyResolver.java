package com.mmadu.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DefaultPropertiesMasterKeyResolver implements MasterKeyResolver {
    private String defaultMasterKey;
    private MasterKeyGenerator masterKeyGenerator;

    @Value("${mmadu.security.master-key}")
    public void setDefaultMasterKey(String defaultMasterKey) {
        this.defaultMasterKey = defaultMasterKey;
    }

    @Autowired
    public void setMasterKeyGenerator(MasterKeyGenerator masterKeyGenerator) {
        this.masterKeyGenerator = masterKeyGenerator;
    }

    @Override
    public String getMasterKey() {
        if(StringUtils.isEmpty(defaultMasterKey)){
            defaultMasterKey = generateMasterKey();
        }
        return defaultMasterKey;
    }

    private String generateMasterKey(){
        String masterKey = masterKeyGenerator.generateMasterKey();
        System.out.println("Generated master key: " + masterKey);
        return masterKey;
    }
}
