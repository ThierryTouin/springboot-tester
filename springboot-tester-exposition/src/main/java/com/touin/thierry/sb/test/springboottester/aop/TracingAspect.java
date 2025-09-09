package com.touin.thierry.sb.test.springboottester.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TracingAspect {


    @Around("within(com.touin.thierry.sb..*)")
    public Object trace(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getSignature().getDeclaringTypeName();
        String method = pjp.getSignature().getName();
        String fqcn = pjp.getSignature().getDeclaringTypeName();
        String packageName = fqcn.substring(0, fqcn.lastIndexOf('.'));
        String moduleName = packageName.contains(".exposition") ? "exposition" :
                            packageName.contains(".application") ? "application" :
                            packageName.contains(".domain") ? "domain" :
                            packageName.contains(".infrastructure") ? "infrastructure" :
                            "unknown";

        // Log ou accumuler dans un contexte (ThreadLocal)
        System.out.println("Entering: " + moduleName + " -> " + className + "." + method);

        Object result = pjp.proceed();

        System.out.println("Exiting: " + moduleName + " -> " + className + "." + method);
        return result;
    }

}
