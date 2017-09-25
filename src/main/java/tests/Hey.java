package tests;

import Annotations.*;
import com.sun.org.glassfish.external.probe.provider.annotations.Probe;

import javax.annotation.Resource;
import javax.xml.ws.Action;
import java.lang.annotation.Target;


@Component
@Scope("Singleton")
//@Resource
public class Hey {
    String hola;
    String lalo;

    public Hey()
    {

    }

    @Autowired/*("HOLAAA")*/
    public void setHolaetHola(String hi)
    {
        this.hola = hi;
    }

    @PostInicialization
    public void setCasa(String tu) {
        this.lalo = tu;
    }

    @PreDestruction
    public void destroy(){}
}
