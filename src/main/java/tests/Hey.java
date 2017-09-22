package tests;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.ws.Action;
import java.lang.annotation.Target;

@Resource
public class Hey {
    String hola;
    String lalo;
    public Hey()
    {

    }

    @Action
    public void SetHola(String hi)
    {
        this.hola = hi;
    }
    @PostConstruct
    public void setCasa(String tu) {
        this.lalo = tu;
    }
}
