package javabasic;

public abstract class PTest<T extends Parent> {
    void execute(Parent p){

    }
    protected abstract void print1(T s);
}
