package com.ap.jvmheaplimitsdemo.rest;

import com.ap.jvmheaplimitsdemo.model.MemoryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@RestController
@RequestMapping(path = "v2")
public class HeapMemoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeapMemoryController.class);

    @GetMapping(path = "/memory-usage")
    public Mono<MemoryModel> getMemoryUsage() {

        int mb = 1024 * 1024;
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long xmx = memoryBean.getHeapMemoryUsage().getMax() / mb;
        long xms = memoryBean.getHeapMemoryUsage().getInit() / mb;

        return Mono.just(new MemoryModel(xms, xmx));
    }
}
