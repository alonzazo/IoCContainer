package tests;

import Annotations.*;


@Component("heyBean")
@Scope("Singleton")
public class Hey {
    Hi injectThisPls;

    String testSingleton = "at first it says this";

    //@Autowired
    public Hey(){}

    @Autowired
    public Hey(Hi plsInject) {
        injectThisPls = plsInject;
    }

    //@Autowired("Hi")
    public void setIdsfgdsfg(Hi hi, String extra) {
        this.injectThisPls = hi;
    }

    //@Autowired("Hi")
    public void setIdasdfgdsfg(Hi hi, String extra) {
        this.injectThisPls = hi;
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
