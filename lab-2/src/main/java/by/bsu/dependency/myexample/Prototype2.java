package by.bsu.dependency.myexample;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "prototype2", scope = BeanScope.PROTOTYPE)
public class Prototype2 {
    @Inject
    Prototype1 prototype1;
}