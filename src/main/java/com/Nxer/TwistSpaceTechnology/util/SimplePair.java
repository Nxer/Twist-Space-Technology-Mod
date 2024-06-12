package com.Nxer.TwistSpaceTechnology.util;

import java.io.Serializable;
import java.util.Objects;

public class SimplePair<L, R> implements Serializable {

    private static final long serialVersionUID = 202405181655L;

    public static <L, R> SimplePair<L, R> of(L left, R right) {
        return new SimplePair<>(left, right);
    }

    public SimplePair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    public L left;
    public R right;

    public SimplePair<L, R> setLeft(L left) {
        this.left = left;
        return this;
    }

    public SimplePair<L, R> setRight(R right) {
        this.right = right;
        return this;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimplePair<?, ?>that)) {
            return false;
        }
        return Objects.equals(getLeft(), that.getLeft()) && Objects.equals(getRight(), that.getRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getRight());
    }

}
