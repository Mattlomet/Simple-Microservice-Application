package com.company.taskerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="Adserver-service")
public interface AdserverFeign {
    @GetMapping("/ad")
    public String getAd();
}
