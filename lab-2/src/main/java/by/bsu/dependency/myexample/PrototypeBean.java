package by.bsu.dependency.myexample;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "prototypeBean", scope = BeanScope.PROTOTYPE)
public class PrototypeBean {
    int counter = 0;

    @Inject
    private SingletonBean singletonBean;

    void doSomething() {
        counter++;
        System.out.println("counter in prototype : " + counter);
    }

    @PostConstruct
    void print() {
        System.out.println("constructor was called, counter is " + counter);
    }
}
