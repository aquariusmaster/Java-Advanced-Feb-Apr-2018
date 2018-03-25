package com.flowergarden.annotations;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class WorkMeterAnnotationBeanPostProcessor implements BeanPostProcessor {

    private static final Logger logger = Logger.getLogger(WorkMeterAnnotationBeanPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        Method[] methods = o.getClass().getDeclaredMethods();
        for (Method method : methods) {
            WorkMeter annotation = method.getAnnotation(WorkMeter.class);
            if (annotation != null) {
                logger.debug("Field was checked: " + method.getName());
            }
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }
}
