package ru.t1.daev.bishopstarter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AuditAspect {

    @Value("${weyland.audit.mode:console}")
    private String auditMode;

    @Value("${weyland.audit.topic:android-audit}")
    private String auditTopic;

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Around("@annotation(WeylandWatchingYou)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Object result;
        long startTime = System.currentTimeMillis();

        try {
            result = joinPoint.proceed();
            logAudit(methodName, args, result, null, System.currentTimeMillis() - startTime);
            return result;
        } catch (Throwable e) {
            logAudit(methodName, args, null, e, System.currentTimeMillis() - startTime);
            throw e;
        }
    }

    private void logAudit(String method, Object[] args, Object result, Throwable error, long duration) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("method", method);
        auditData.put("args", args);
        auditData.put("result", result);
        auditData.put("error", error != null ? error.getMessage() : null);
        auditData.put("duration", duration);
        auditData.put("timestamp", System.currentTimeMillis());

        if ("kafka".equals(auditMode)) {
            if (kafkaTemplate != null) {
                kafkaTemplate.send(auditTopic, auditData);
            }
        } else {
            System.out.println("[AUDIT] " + auditData);
        }
    }
}