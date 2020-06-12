package com.mmadu.security.api;

import com.mmadu.security.MmaduJwtConfiguration;
import com.mmadu.security.MmaduWebSecurityConfigurer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {
        MmaduJwtConfiguration.class,
        WebApiSecurityConfigurationTest.ApiSecurity.class
})
@AutoConfigureMockMvc
public class WebApiSecurityConfigurationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void accessingOpenApiShouldBeSuccessful() throws Exception {
        mockMvc.perform(get("/open/api"))
                .andExpect(status().isNotFound());
    }

    @Test
    void accessingOpenApiShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/something"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessingOpenApiWithExpiredTokenShouldFail() throws Exception {
        mockMvc.perform(
                get("/api/something")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1ZWUyNDUxMTgxOTIwZDdlNjU0MThkODEiLCJyb2xlcyI6W10sImlzcyI6Im1tYWR1LmNvbSIsImdyb3VwcyI6WyJ0ZXN0Iiwic2FtcGxlIl0sImF1dGhvcml0aWVzIjpbXSwiY2xpZW50X2lkIjoiMjJlNjViNzItOTIzNC00MjgxLTlkNzMtMzIzMDA4OWQ0OWE3IiwiZG9tYWluX2lkIjoiMCIsImF1ZCI6InRlc3QiLCJuYmYiOjE1OTE4ODcxNjMsInVzZXJfaWQiOiIxMTExMTExMTEiLCJzY29wZSI6InZpZXcgZWRpdCIsImV4cCI6MTU5MTg4NzQ2MywiaWF0IjoxNTkxODg3MTYzLCJqdGkiOiJkMDZmZGEyZC04NTYxLTRlNjgtODg5Zi1lMmYyMDYyMGFkYzcifQ.W3zR1qS1JoLmG765SYrJL9i9Ly3t8_3DyxFUayDPmVtH3R1g9nN9ojFUSyJJYVq8rOL8t83Bx7P_SJDjI9gfvJXTDuTwGCpCYkYlRNh4fgNNIQ3QZIbpvMEcvb1GcsHETO672N83ccer5nPDFNyrrMShUyWltATEcpCTT9jSkUglyFjwH7YwP8OXZnw6pKo796rWN5rc07kJcL6oiVrboD1gmfT0CR6Na5OlzAKb64KZxSFb43-PC5IL8zHhfQEk0Q0meX_GV1rECExcb6OiJK38Ebnd9Cq-uSKqn_BOAgiDlHk_a1uvrhjzwp0qebJKj_zxd5_PssRIOhS1zTXyWA")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessingApiWithCorrectTokenShouldPass() throws Exception {
        mockMvc.perform(
                get("/api/something")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1ZWUzNzhhZDQ3NDg5MTI5Y2M0OWIzYjAiLCJyb2xlcyI6W10sImlzcyI6Im1tYWR1LmNvbSIsImdyb3VwcyI6WyJ0ZXN0Iiwic2FtcGxlIl0sImF1dGhvcml0aWVzIjpbXSwiY2xpZW50X2lkIjoiMjJlNjViNzItOTIzNC00MjgxLTlkNzMtMzIzMDA4OWQ0OWE3IiwiZG9tYWluX2lkIjoiMCIsImF1ZCI6InRlc3QiLCJuYmYiOjE1OTE5NjU4OTIsInVzZXJfaWQiOiIxMTExMTExMTEiLCJzY29wZSI6InZpZXcgZWRpdCIsImV4cCI6Mjk5MTk2NjE5MiwiaWF0IjoxNTkxOTY1ODkyLCJqdGkiOiJmNWJmNzVhNi0wNGEwLTQyZjctYTFlMC01ODNlMjljZGU4NmMifQ.EgjaDmX03BYWbBdnFx4rYLTlT3wnRqnILd-pLWZLrUPZ48llyuzPoB2dJ3QcNSuzxb9koOS55513nzpKekOAkcDuA3XP7OTxw_4X5rar7xQiA3gEnQ1RAgUcUCOXGmlzl5f9XQsdHtY-WxMuh-qgdELqH8fkb4p0HcAHOOdhKOivSoIGu1uGBrbmT8RFUcAti1mmUzDJM0RFn0JZc7IULizoaibEh-mGNuBn0AN2ZhK1xRM-tbKIOZBp5_wVY1YcGc7M1bO-VeCmg2dWilZC9_9GT2X2t4E1vXoz1a4OkiBZx27GhZwJCSWnrve5OwRPf4ONTV7B0FZqJxFP3yQTIg")
        )
                .andExpect(status().isNotFound());
    }

    @TestConfiguration
    static class ApiSecurity extends MmaduWebSecurityConfigurer {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.authorizeRequests()
                    .antMatchers("/api/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll();
        }
    }
}
