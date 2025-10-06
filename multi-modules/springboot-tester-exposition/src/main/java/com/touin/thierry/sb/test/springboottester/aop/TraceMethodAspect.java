package com.touin.thierry.sb.test.springboottester.aop;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TraceMethodAspect {

    //private static final Logger LOGGER = LoggerFactory.getLogger(TraceMothodAspect.class);


    @Around("within(com.touin.thierry.sb..*)")
    public Object trace(ProceedingJoinPoint pjp) throws Throwable {

        // if (LOGGER.isInfoEnabled()) {
        //     LOGGER.info("trace()");
        // }

        String className = pjp.getSignature().getDeclaringTypeName();
        String method = pjp.getSignature().getName();

        // Création d’un logger dynamique pour la classe cible
        Logger dynamicLogger = LoggerFactory.getLogger(className);

        if (dynamicLogger.isInfoEnabled()) {
            dynamicLogger.info("++{}()", method);
        }

        Object result = pjp.proceed();

        if (dynamicLogger.isInfoEnabled()) {
            dynamicLogger.info("--{}()", method);
        }

        return result;
    }

}
