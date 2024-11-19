package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleApplicationContext extends AbstractApplicationContext {

    /**
     * Создает контекст, содержащий классы, переданные в параметре.
     * <br/>
     * Если на классе нет аннотации {@code @Bean}, имя бина получается из названия класса, скоуп бина по дефолту
     * считается {@code Singleton}.
     * <br/>
     * Подразумевается, что у всех классов, переданных в списке, есть конструктор без аргументов.
     *
     * @param beanClasses классы, из которых требуется создать бины
     */
    public SimpleApplicationContext(Class<?>... beanClasses) {
        beanDefinitions = Arrays.stream(beanClasses).collect(
                Collectors.toMap(
                        beanClass ->
                            beanClass.isAnnotationPresent(Bean.class) ?
                                    beanClass.getAnnotation(Bean.class).name() :
                                    decapitalize(beanClass.getSimpleName()),
                        Function.identity()
                )
        );
    }
}
