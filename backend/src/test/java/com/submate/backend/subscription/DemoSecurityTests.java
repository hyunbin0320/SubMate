package com.submate.backend.subscription;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "SUBMATE_DEMO_PASSWORD=test-only-password",
        "spring.datasource.url=jdbc:h2:mem:demo_security;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "submate.scheduler.enabled=false"
})
@ActiveProfiles("demo")
@AutoConfigureMockMvc
class DemoSecurityTests {
    @Autowired MockMvc mvc;
    @Test void demoBasicAuthenticationAndRolesWork() throws Exception {
        mvc.perform(get("/api/subscriptions")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/subscriptions").with(httpBasic("user@submate.test", "wrong")))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/subscriptions").with(httpBasic("user@submate.test", "test-only-password")))
                .andExpect(status().isOk());
        mvc.perform(get("/api/admin/refunds").with(httpBasic("user@submate.test", "test-only-password")))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/admin/refunds").with(httpBasic("admin@submate.test", "test-only-password")))
                .andExpect(status().isOk());
    }
    @Test void loopbackFrontendCanMakeAuthenticatedPayment() throws Exception {
        mvc.perform(post("/api/subscriptions").with(httpBasic("user@submate.test", "test-only-password"))
                .header("Origin", "http://127.0.0.1:5173").contentType("application/json")
                .content("{\"productId\":1,\"paymentMethod\":\"CARD\",\"requestKey\":\"cors-test\",\"simulateFailure\":false}"))
                .andExpect(status().isCreated());
    }
}
