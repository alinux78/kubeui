package com.mc.study.debug.k8s.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc.study.debug.k8s.model.Pod;
import com.mc.study.debug.k8s.service.PodService;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PodController {

    private final PodService podService;

    private final ObjectMapper objectMapper;


    public PodController(PodService podService, ObjectMapper objectMapper) {
        this.podService = podService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/pods")
    public List<Pod> getPods() {
        return podService.listPods();
    }

    @PostMapping("/pods/{namespace}/{podName}/restart")
    public ResponseEntity<Map<String, String>>  restartPod(@PathVariable("podName") String podName, @PathVariable("namespace") String namespace) {
        var podPath= namespace+"/"+podName;
        podService.restartPod(podName, namespace);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pod '" + podPath + "' is being restarted (deleted).");
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pods/{namespace}/{podName}/describe")
    public ResponseEntity<String> describePod(@PathVariable String namespace, @PathVariable String podName) {
        try {
            V1Pod pod = podService.fetchPod(namespace,podName);
            if (pod == null) {
                return ResponseEntity.status(404).body("Pod not found");
            }

            String json = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pod);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body("Error fetching pod details: " + e.getMessage());
        }
    }

}
