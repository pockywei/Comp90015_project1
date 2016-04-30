package test;

public class Main {
    public static void main(String[] args) {
        getClass(B.class);
    }
    
    public static void getClass(Class<? extends A> a) {
         try {
            a.newInstance();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}


class B extends A {
    public B() {
        System.out.println("i am B");
    }
}

abstract class A {
    public A() {
        System.out.println("hi i am A.");
    }
}