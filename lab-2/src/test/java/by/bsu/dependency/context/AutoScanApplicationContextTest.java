package by.bsu.dependency.context;


import by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import by.bsu.dependency.exceptions.NoSuchBeanException;
import by.bsu.dependency.myexample.Prototype1;
import by.bsu.dependency.myexample.Prototype2;
import by.bsu.dependency.myexample.*;
import by.bsu.dependency.myexample.PrototypeBean;
import by.bsu.dependency.myexample.SingletonBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AutoScanApplicationContextTest {

    private AutoScanApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new AutoScanApplicationContext("by.bsu.dependency.myexample");
    }

    @Test
    void testIsRunning() {
        assertThat(applicationContext.isRunning()).isFalse();
        applicationContext.start();
        assertThat(applicationContext.isRunning()).isTrue();
    }

    @Test
    void testContextContainsNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.containsBean("singletonBean")
        );
    }

    @Test
    void testContextContainsBeans() {
        applicationContext.start();

        assertThat(applicationContext.containsBean("prototypeBean")).isTrue();
        assertThat(applicationContext.containsBean("singletonBean")).isTrue();
        assertThat(applicationContext.containsBean("prototype1")).isTrue();
        assertThat(applicationContext.containsBean("prototype2")).isTrue();
        assertThat(applicationContext.containsBean("anotherName")).isFalse();
    }

    @Test
    void testContextGetBeanNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.getBean("singletonBean")
        );
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.getBean("prototypeBean")
        );
    }

    @Test
    void testGetBeanReturns() {
        applicationContext.start();

        assertThat(applicationContext.getBean("prototypeBean")).isNotNull().isInstanceOf(PrototypeBean.class);
        assertThat(applicationContext.getBean("singletonBean")).isNotNull().isInstanceOf(SingletonBean.class);
    }


    @Test
    void testGetBeanThrows() {
        applicationContext.start();

        assertThrows(
                NoSuchBeanException.class,
                () -> applicationContext.getBean("randomName")
        );
    }

    @Test
    void testIsSingletonReturns() {
        assertThat(applicationContext.isSingleton("singletonBean")).isTrue();
        assertThat(applicationContext.isSingleton("prototypeBean")).isFalse();
    }

    @Test
    void testIsSingletonThrows() {
        assertThrows(
                NoSuchBeanException.class,
                () -> applicationContext.isSingleton("randomName")
        );
    }

    @Test
    void testIsPrototypeReturns() {
        assertThat(applicationContext.isPrototype("singletonBean")).isFalse();
        assertThat(applicationContext.isPrototype("prototypeBean")).isTrue();
        assertThat(applicationContext.isPrototype("prototype2")).isTrue();
        assertThat(applicationContext.isPrototype("prototype1")).isTrue();
    }

    @Test
    void testIsPrototypeThrows() {
        assertThrows(
                NoSuchBeanException.class,
                () -> applicationContext.isPrototype("randomName")
        );
    }

    @Test
    void testSameElementsReturns() {
        applicationContext.start();

        assertEquals(applicationContext.getBean("singletonBean"), applicationContext.getBean("singletonBean"));
    }

    @Test
    void testDifferentElementsReturns() {
        applicationContext.start();

        assertNotEquals(applicationContext.getBean("prototypeBean"),applicationContext.getBean("prototypeBean"));
    }

    @Test
    void testInitiate() {
        assertNotNull(applicationContext.instantiateBean(SingletonBean.class));
        assertNotNull(applicationContext.instantiateBean(PrototypeBean.class));
        assertNotNull(applicationContext.instantiateBean(Prototype1.class));
        assertNotNull(applicationContext.instantiateBean(Prototype2.class));
    }

    @Test
    void testInject() throws NoSuchFieldException, IllegalAccessException {
        applicationContext.start();
        PrototypeBean prototypeBean = (PrototypeBean) applicationContext.getBean("prototypeBean");
        Field singletonBeanField = prototypeBean.getClass().getDeclaredField("singletonBean");
        singletonBeanField.setAccessible(true);
        Object injectedSingletonBean = singletonBeanField.get(prototypeBean);
        assertThat(injectedSingletonBean).isNotNull();
        assertThat(injectedSingletonBean).isEqualTo(applicationContext.getBean(SingletonBean.class));
        assertThrows(
                StackOverflowError.class,
                () -> applicationContext.getBean("prototype2")
        );
    }

}
