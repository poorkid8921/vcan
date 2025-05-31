package main.anticheat.spigot.util.type;

public final class Pair<X, Y> {
    private X x;
    private Y y;

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public void setX(X x) {
        this.x = x;
    }

    public Y getY() {
        return y;
    }

    public void setY(Y y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return (x == null ? pair.x == null : x.equals(pair.x)) && (y == null ? pair.y == null : y.equals(pair.y));
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (x == null ? 0 : x.hashCode());
        result = 31 * result + (y == null ? 0 : y.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Pair(x=" + x + ", y=" + y + ")";
    }
}