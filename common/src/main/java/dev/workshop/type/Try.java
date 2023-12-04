package dev.workshop.type;

import java.util.Optional;
import java.util.function.Function;

public final class Try<T> extends Either<T, Exception> {

    private Try(T success, Exception failure) {
        super(success, failure);
    }

    public static <T> Try<T> success(T value) {
        return new Try<>(value, null);
    }

    public static <T> Try<T> failure(Exception exception) {
        return new Try<>(null, exception);
    }

    @FunctionalInterface
    public interface CheckedFunction<T, U, V extends Throwable> {
        U apply(T t) throws V;
    }

    public static <R, S> Function<R, Try<S>> lift(CheckedFunction<R, S, Exception> function) {
        return t -> {
            try {
                return Try.success(function.apply(t));
            } catch (Exception exception) {
                return Try.failure(exception);
            }
        };
    }

    public Optional<T> getSuccess() {
        return this.getLeft();
    }

    public Optional<Exception> getFailure() {
        return this.getRight();
    }

    public boolean isSuccess() {
        return this.isLeft();
    }

    public boolean isFailure() {
        return this.isRight();
    }
}
