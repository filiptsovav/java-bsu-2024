package by.bsu.dependency.context;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import by.bsu.dependency.exceptions.NoSuchBeanException;


public class HardCodedSingletonApplicationContext extends AbstractApplicationContext {


    /**
     * ! Класс существует только для базового примера !
     * <br/>
     * Создает контекст, содержащий классы, переданные в параметре. Полагается на отсутсвие зависимостей в бинах,
     * а также на наличие аннотации {@code @Bean} на переданных классах.
     * <br/>
     * ! Контекст данного типа не занимается внедрением зависимостей !
     * <br/>
     * ! Создает только бины со скоупом {@code SINGLETON} !
     *
     * @param beanClasses классы, из которых требуется создать бины
     */
    public HardCodedSingletonApplicationContext(Class<?>... beanClasses) {
        this.beanDefinitions = Arrays.stream(beanClasses).collect(
                Collectors.toMap(
                        beanClass -> beanClass.getAnnotation(Bean.class).name(),
                        Function.identity()
                )
        );
    }

    @Override
    public void start() {
        beanDefinitions.forEach((beanName, beanClass) -> beansSingletons.put(beanName, instantiateBean(beanClass)));
        contextStatus = ContextStatus.STARTED;
        beansSingletons.forEach((beanName, beanObject) ->  {
            inject(beansSingletons.get(beanName));
        });
        beansSingletons.forEach((beanName, beanObject) ->  {
            invokePostConstruct(beansSingletons.get(beanName));
        });
    }

    /**
     * В этой реализации отсутствуют проверки статуса контекста (запущен ли он).
     */
    @Override
    public boolean containsBean(String name) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        return beansSingletons.containsKey(name);
    }

    /**
     * В этой реализации отсутствуют проверки статуса контекста (запущен ли он) и исключения в случае отсутствия бина
     */
    @Override
    public Object getBean(String name) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        if (!containsBean(name)) {
            throw new NoSuchBeanException();
        }
        return beansSingletons.get(name);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        String beanName = clazz.isAnnotationPresent(Bean.class) ? clazz.getAnnotation(Bean.class).name() : decapitalize(clazz.getName());
        if (!containsBean(beanName)) {
            throw new NoSuchBeanException();
        }
        return clazz.cast(beansSingletons.get(beanName));
    }

    @Override
    public boolean isPrototype(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanException();
        }
        return false;
    }

    @Override
    public boolean isSingleton(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanException();
        }
        return true;
    }
}
