package by.bsu.dependency.myexample;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "prototype1", scope = BeanScope.PROTOTYPE)
public class Prototype1 {
    @Inject
    Prototype2 prototype2;
}