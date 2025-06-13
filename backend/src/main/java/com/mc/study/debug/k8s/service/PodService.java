package com.mc.study.debug.k8s.service;

import com.mc.study.debug.k8s.utils.PodUtils;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1DeleteOptions;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import org.springframework.stereotype.Service;
import com.mc.study.debug.k8s.model.Pod;


import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PodService {

    public List<Pod> listPods() {
        CoreV1Api api = new CoreV1Api();
        V1PodList list;
        List<Pod> pods = new ArrayList<>();;
        try {
            list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            // Print pod name and status for each pod
            for (V1Pod pod : list.getItems()) {
                var name = "unknown";
                var namespace = "unknown";
                var status =  "unknown";
                String age = "unknown";

                if (pod.getMetadata() != null) {
                    name = pod.getMetadata().getName();
                    namespace = pod.getMetadata().getNamespace();
                }
                if (pod.getStatus() != null) {
                    status = pod.getStatus().getPhase();
                    OffsetDateTime started = pod.getStatus().getStartTime();
                    age = PodUtils.getAgeFromStartTime(started);
                }
                Pod aPod = new Pod(name, namespace, status, age);
                pods.add(aPod);
            }
            return pods;
        } catch (ApiException e) {
            throw new RuntimeException("Failed to list pods: " + e.getMessage(), e);
        }
        
    }

    public void restartPod(String podName, String namespace) {
        V1DeleteOptions deleteOptions = new V1DeleteOptions();
        deleteOptions.setGracePeriodSeconds(5L); // Wait up to 5 seconds before force delete
        CoreV1Api api = new CoreV1Api();

        try {
            var list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            // Delete the pod
            api.deleteNamespacedPod(podName, namespace, null, null, null, null, "Foreground", deleteOptions);
            System.out.println("Pod deletion triggered: " + namespace+ "/" + podName);

        } catch (ApiException e) {
            System.out.println(e);
            throw new RuntimeException("Failed to list pods: " + e.getMessage(), e);
        }

    }

}
