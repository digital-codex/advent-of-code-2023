package dev.workshop.type;

import java.util.Optional;

public class Either<L, R> {
    private final L left;
    private final R right;

    protected Either(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public Optional<L> getLeft() {
        return Optional.ofNullable(this.left);
    }

    public Optional<R> getRight() {
        return Optional.ofNullable(this.right);
    }

    public boolean isLeft() {
        return this.left == null;
    }

    public boolean isRight() {
        return this.right == null;
    }
}
