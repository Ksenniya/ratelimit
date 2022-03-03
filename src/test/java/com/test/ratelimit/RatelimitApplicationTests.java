package com.test.ratelimit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RatelimitApplicationTests {
    @Value("${api.capacity}")
    private Integer capacity;

    @Autowired
    private MockMvc mockMvc;

    private static final String url = "/api/v1/rate-limit";

    @Test
    public void ipRateLimited() throws Exception {
        List<String> ipList = new ArrayList<>(Arrays.asList("192.168.0.2", "192.168.0.1", "192.168.0.0"));
        ipList.parallelStream().forEach(ip -> {
                    IntStream.rangeClosed(1, capacity)
                            .boxed()
                            .sorted(Collections.reverseOrder())
                            .parallel()
                            .forEach(counter -> {
                                successfulRequest(url, ip);
                            });
                    //uncomment to check if N+1 request fails - assume that duration is enough to complete requests in one interval
                    //  blockedRequest(url, ip);
                }
        );
    }

    private void successfulRequest(String url, String ipAddress) {
        try {
            this.mockMvc
                    .perform(get(url)
                            .with(request -> {
                                request.setRemoteAddr(ipAddress);
                                return request;
                            })
                    )
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void blockedRequest(String url, String ipAddress) {
        try {
            this.mockMvc
                    .perform(get(url).with(request -> {
                        request.setRemoteAddr(ipAddress);
                        return request;
                    }))
                    .andExpect(status().is(HttpStatus.BAD_GATEWAY.value()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
