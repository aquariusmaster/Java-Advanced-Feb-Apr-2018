package com.flowergarden.annotations;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component
public class WorkMeterAnnotationBeanPostProcessor implements BeanPostProcessor {

    private static final Logger logger = Logger.getLogger(WorkMeterAnnotationBeanPostProcessor.class);

    private Map<String, Class> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        Class<?> beanClass = o.getClass();
        if (beanClass.isAnnotationPresent(WorkMeter.class)) {
            map.put(s, beanClass);
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class beanClass = map.get(s);
        if (beanClass != null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy, method, args) -> {
                long start = System.nanoTime();
                Object retVal = method.invoke(o, args);
                logger.debug("WorkMeter - " + beanClass.getName() +"."+method.getName() + " worked in: " +
                        (System.nanoTime() - start) + " ns");
                return retVal;
            });
        }
        return o;
    }
}
