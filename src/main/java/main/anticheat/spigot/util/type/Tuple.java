package main.anticheat.spigot.util.type;

public class Tuple<A, B> {
    private final A a;
    private final B b;

    public Tuple(final A var0, final B var1) {
        this.a = var0;
        this.b = var1;
    }

    public A a() {
        return this.a;
    }

    public B b() {
        return this.b;
    }
}
