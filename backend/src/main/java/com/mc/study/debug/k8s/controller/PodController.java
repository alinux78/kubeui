package com.mc.study.debug.k8s.controller;

import com.mc.study.debug.k8s.model.Pod;
import com.mc.study.debug.k8s.service.PodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PodController {

    private final PodService podService;

    public PodController(PodService podService) {
        this.podService = podService;
    }

    @GetMapping("/pods")
    public List<Pod> getPods() {
        return podService.listPods();
    }

    @PostMapping("/pods/{namespace}/{podName}/restart")
    public ResponseEntity<Map<String, String>>  restartPod(@PathVariable("podName") String podName, @PathVariable("namespace") String namespace) {
        var podPath= namespace+"/"+podName;
        System.out.println("Restarting pod: " + podPath);
        podService.restartPod(podName, namespace);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pod '" + podPath + "' is being restarted (deleted).");
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }

}
