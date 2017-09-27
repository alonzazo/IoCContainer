package tests;

import Annotations.*;
import com.sun.org.glassfish.external.probe.provider.annotations.Probe;
import containers.Bean;

import javax.annotation.Resource;
import javax.xml.ws.Action;
import java.lang.annotation.Target;


@Component
@Scope("Prototype")
//@Resource
public class Hey {
    Hi injectThisPls;

    String testSingleton = "at first it says this";

    public Hey(Hi plsInject) {
        injectThisPls = plsInject;
    }

    @PostInicialization
    public void start() {
        System.out.println("SIRVE POST CONSTRUCTION");
    }

    @PreDestruction
    public void end(){
        System.out.println("SIRVE PRE DESCTRUCT");
    }

    public void funciono() {
        injectThisPls.funciono();
        System.out.println("HEY SIRVIÓ");
    }

    public String getTestSingleton() {
        return testSingleton;
    }

    public void changeTestSingleton() {
        testSingleton = "but now it says this instead";
    }
}
