package com.mmadu.security;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WiremockExtension implements BeforeAllCallback, AfterAllCallback {
    public static final int PORT = 19998;
    private WireMockServer wireMockServer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        wireMockServer = new WireMockServer(options().port(PORT));
        configureFor("localhost", PORT);
        wireMockServer.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
