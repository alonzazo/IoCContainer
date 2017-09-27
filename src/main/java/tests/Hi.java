package tests;

import Annotations.Component;

@Component
public class Hi {
    byte by;
    short sh;
    int in;
    long lo;
    float fl;
    double dou;
    boolean tru;
    boolean fal;
    char ch;
    String str;

    public Hi(byte b, short s, int i, long l, float f, double d, boolean t, boolean fa, char c, String st){
        by = b;
        sh = s;
        in = i;
        lo = l;
        fl = f;
        dou = d;
        tru = t;
        fal = fa;
        ch = c;
        str = st;
    }

    public void funciono() {
        System.out.println("by: "+by+"\n"+"sh: "+sh+"\n"+"in: "+in+"\n"+"lo: "+lo+"\n"+"fl: "+fl+"\n"+"dou: "+dou+"\n"+"tru: "+tru+"\n"+"fal: "+fal+"\n"+"ch: "+ch+"\n"+"st: "+str+"\nHI SIRVIÓ");
    }
}
