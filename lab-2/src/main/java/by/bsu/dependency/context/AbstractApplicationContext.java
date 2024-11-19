package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import by.bsu.dependency.exceptions.NoSuchBeanException;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApplicationContext implements ApplicationContext {

    Map<String, Class<?>> beanDefinitions;
    Map<String, Object> beansSingletons = new HashMap<>();

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    ContextStatus contextStatus = ContextStatus.NOT_STARTED;

    public void start() {
        beanDefinitions.forEach((beanName, beanClass) -> {
            if (isSingleton(beanName)) {
                    beansSingletons.put(beanName, instantiateBean(beanClass));
            }
        });
        contextStatus = ContextStatus.STARTED;
        beansSingletons.forEach((beanName, beanObject) ->  {
            inject(beansSingletons.get(beanName));
        });
        beansSingletons.forEach((beanName, beanObject) ->  {
            invokePostConstruct(beansSingletons.get(beanName));
        });
    }

    public boolean isRunning() {
        return contextStatus == ContextStatus.STARTED;
    }

    public boolean containsBean(String name) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        return beanDefinitions.containsKey(name);
    }

    public Object getBean(String name) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanException();
        }
        if (beansSingletons.containsKey(name)) {
            return beansSingletons.get(name);
        }
        return prototypeCreate(name);
    }

    public <T> T getBean(Class<T> clazz) {
        String beanName;
        if (!clazz.isAnnotationPresent(Bean.class)) {
            beanName = decapitalize(clazz.getSimpleName());
            return clazz.cast(beansSingletons.get(beanName));
        }
        if (clazz.getAnnotation(Bean.class).scope() == BeanScope.SINGLETON) {
            beanName = clazz.getAnnotation(Bean.class).name();
            return clazz.cast(beansSingletons.get(beanName));
        }
        return clazz.cast(prototypeCreate(decapitalize(clazz.getSimpleName())));
    }

    public boolean isPrototype(String name) {

        return !isSingleton(name);
    }

    public boolean isSingleton(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanException();
        }
        return !beanDefinitions.get(name).isAnnotationPresent(Bean.class) ||
                beanDefinitions.get(name).getAnnotation(Bean.class).scope() == BeanScope.SINGLETON;
    }

    String decapitalize(String word) {
        return word.substring(0, 1).toLowerCase() + word.substring(1);
    }

    public void inject (Object object) {
        Arrays.stream(object.getClass().getDeclaredFields()).forEach(
                field -> {
                    if (field.isAnnotationPresent(Inject.class)) {
                        field.setAccessible(true);
                        Object value = getBean(field.getType());
                        try {
                            field.set(object, value);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    void invokePostConstruct(Object object) {
        Arrays.stream(object.getClass().getDeclaredMethods()).forEach(
                method -> {
                    if (method.isAnnotationPresent(PostConstruct.class)) {
                        method.setAccessible(true);
                        try {
                            method.invoke(object);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }


    public <T> T instantiateBean(Class<T> beanClass) {
        try {
            return beanClass.getConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    Object prototypeCreate(String name) {
        if (!containsBean(name)) {
            throw new NoSuchBeanException();
        }
        Object bean = instantiateBean(beanDefinitions.get(name));
        inject(bean);
        invokePostConstruct(bean);
        return bean;
    }
}
