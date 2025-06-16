package com.mc.study.debug.k8s.service;

import com.mc.study.debug.k8s.utils.PodUtils;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1DeleteOptions;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.mc.study.debug.k8s.model.Pod;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PodService {
    private static final Logger logger = LoggerFactory.getLogger(PodService.class);



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
            logger.error("Error when listing pods: ", e);
            throw new RuntimeException("Failed to list pods: " + e.getMessage(), e);
        }
        
    }

    public void restartPod(String podName, String namespace) {
        V1DeleteOptions deleteOptions = new V1DeleteOptions();
        deleteOptions.setGracePeriodSeconds(5L); // Wait up to 5 seconds before force delete
        CoreV1Api api = new CoreV1Api();

        var podFQN = namespace+"/"+podName;
        logger.info("Deleting pod: " + podFQN);
        try {
            api.deleteNamespacedPod(podName, namespace, null, null, null, null, "Foreground", deleteOptions);
            logger.info("Pod deletion triggered: " + podFQN);

        } catch (ApiException e) {
            logger.error("Error deleting pod:" + podFQN, e);
            throw new RuntimeException("Failed to delete pod: " + e.getMessage(), e);
        }

    }

    public V1Pod fetchPod(String namespace, String podName) {
        var podFQN = namespace+"/"+podName;
        try {
            CoreV1Api api = new CoreV1Api();
            V1Pod pod = api.readNamespacedPod(podName, namespace, null);
            return pod;
        } catch (ApiException e) {
            logger.error("Error when fetching pod:" + podFQN, e);
            throw new RuntimeException("Failed to fetch pod: " + e.getMessage(), e);
        }
    }

}
