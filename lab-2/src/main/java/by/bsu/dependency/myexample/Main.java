package by.bsu.dependency.myexample;

import by.bsu.dependency.context.ApplicationContext;
import by.bsu.dependency.context.HardCodedSingletonApplicationContext;
import by.bsu.dependency.context.SimpleApplicationContext;
import by.bsu.dependency.example.FirstBean;
import by.bsu.dependency.example.OtherBean;
import by.bsu.dependency.myexample.PrototypeBean;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SimpleApplicationContext(
                SingletonBean.class, PrototypeBean.class
        );

        applicationContext.start();
        SingletonBean singletonBean = (SingletonBean) applicationContext.getBean("prototype2Bean");
        PrototypeBean prototypeBean = (PrototypeBean) applicationContext.getBean("prototype1Bean");

        singletonBean.doSomething();
        prototypeBean.doSomething();
        singletonBean = (SingletonBean) applicationContext.getBean("singletonBean");
        prototypeBean = (PrototypeBean) applicationContext.getBean("prototype2Bean");
        singletonBean.doSomething();
        prototypeBean.doSomething();
    }
}
