package edu.ucsd.cse110.walkwalkrevolution;

public interface Callback {
    interface NoArg {
        void call();
    }

    interface SingleArg<T1> {
        void call(T1 arg);
    }
}
