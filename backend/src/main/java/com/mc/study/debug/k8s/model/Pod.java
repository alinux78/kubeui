package com.mc.study.debug.k8s.model;


public record Pod(
    String name,
    String namespace,
    String status,
    String age
) {
}
