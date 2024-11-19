package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AutoScanApplicationContext extends AbstractApplicationContext {

    /**
     * Создает контекст, содержащий классы из пакета {@code packageName}, помеченные аннотацией {@code @Bean}.
     * <br/>
     * Если имя бина в анноации не указано ({@code name} пустой), оно берется из названия класса.
     * <br/>
     * Подразумевается, что у всех классов, переданных в списке, есть конструктор без аргументов.
     *
     * @param packageName имя сканируемого пакета
     */
    public AutoScanApplicationContext(String packageName) {
        Reflections reflections = new Reflections(packageName, Scanners.TypesAnnotated);
        beanDefinitions = reflections.getTypesAnnotatedWith(Bean.class).stream()
                .collect(Collectors
                        .toMap(
                                clazz ->  {
                                    if (Objects.equals(clazz.getAnnotation(Bean.class).name(), "")) {
                                        return decapitalize(clazz.getSimpleName());
                                    }
                                    return clazz.getAnnotation(Bean.class).name();
                                },
                                Function.identity()
                        )
                );
    }
}
