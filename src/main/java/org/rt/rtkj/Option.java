package org.rt.rtkj;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A container object which may or may not contain a null value. If a value is stored in the container, isPresent() returns true.
 * Otherwise the container is considered empty and isPresent() will return false.
 *
 * @param <T> type of value
 * @apiNote Option is intended to be used as a data member in classes but can also be used as a method return type
 * where it needs to be clear that no result is returned. The goal in general is to avoid errors being cause by null.
 * A variable who's type is Option should never itself be null.
 * The container implements Serializable which is generally implemented for data structure properties.
 */
public final class Option<T> implements Serializable {
    /**
     * If the value is null, it indicates no value is present. If the value not a null, a value is present.
     */
    private final T value;

    /**
     * A common instance for empty().
     */
    private static final Option<?> EMTPY = new Option<>(null);

    /**
     * Construct an instance of a value. The constructor should be called indirectly by the of() function if a non-null value needs to be stored or by empty() function to store a null value.
     *
     * @param value the value to be stored in the container.
     */
    private Option(T value) {
        this.value = value;
    }

    /**
     * Creates an Option describing a non-null value.
     *
     * @param value non-null value
     * @param <T>   type of the data
     * @return an Option with a value present
     * @throws NullPointerException if the value is null
     */
    public static <T> Option<T> of(T value) {
        return new Option<>(Objects.requireNonNull(value));
    }

    /**
     * Creates an Option describing a non-null value.
     *
     * @param value non-null Optional value
     * @param <T>   type of the data
     * @return an Option with a value present
     * @throws NullPointerException if the value is null
     */
    public static <T> Option<T> of(Optional<T> value) {
        Objects.requireNonNull(value);
        return new Option<>(Objects.requireNonNull(value.get()));
    }

    /**
     * Creates an Option describing a non-null value.
     *
     * @param value non-null Option value
     * @param <T>   type of the data
     * @return an Option with a value present
     * @throws NullPointerException if the value is null
     */
    public static <T> Option<T> of(Option<T> value) {
        Objects.requireNonNull(value);
        return new Option<>(Objects.requireNonNull(value.get()));
    }

    /**
     * Creates an Option describing a value which can be null or non-null. If a null value is provided, an empty Option is returned.
     *
     * @param value a value which can be null
     * @param <T>   type of the data
     * @return an Option with a present value if the specified value is non-null, otherwise an empty Option is returned.
     */
    public static <T> Option<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    /**
     * Create an Option describing a value from an Optional value.
     * If the value of the Optional is an Option or Optional, it will flatten out these types and only retain the internal value of those types.
     *
     * @param value an optional value
     * @param <T>   type of the data
     * @return an Option with a present value if the specified value is non-empty, otherwise an empty Option is returned.
     */
    public static <T> Option<T> ofNullable(Optional<T> value) {
        if (value == null) return empty();
        return value.isEmpty() ? empty() : ofNullable(value.get());
    }

    /**
     * Create an Option describing a value from an Option value.
     * If the value of the Option is an Option or Optional, it will flatten out these types and only retain the internal value of those types.
     *
     * @param value an optional value
     * @param <T>   type of the data
     * @return an Option with a present value if the specified value is non-empty, otherwise an empty Option is returned.
     */
    public static <T> Option<T> ofNullable(Option<T> value) {
        if (value == null) return empty();
        return value.isEmpty() ? empty() : ofNullable(value.get());
    }

    /**
     * Creates an empty Option with no value being stored.
     *
     * @param <T> type of the non-existing data
     * @return an empty Option
     * @apiNote Avoid testing if an Option is empty by comparing with == against instances returned by Option.empty(). There is no garantee that is a singleton. Instead use isPresent or isEmpty.
     */
    public static <T> Option<T> empty() {
        @SuppressWarnings("unchecked")
        var t = (Option<T>) EMTPY;
        return t;
    }

    /**
     * If the value is present, the value is returned, otherwise a NoSuchElementException is thrown.
     *
     * @return A non null value stored in the Option
     * @apiNote An alternative is to use the method orElseThrow().
     */
    public T get() {
        if (value == null) throw new NoSuchElementException("No value present");
        return value;
    }

    /**
     * If the value is present, returns true, otherwise false.
     *
     * @return True if a value is present, otherwise false.
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * If the value is not present, returns true, otherwise false.
     *
     * @return True if no value is present, otherwise false.
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * If a value is present in the Option, perform a provided action with the value. If the Option is empty, no action is performed.
     *
     * @param action the action to be executed if a value is present.
     * @throws NullPointerException if the value is present and the action is null.
     */
    public void ifPresent(Consumer<? super T> action) {
        if (value != null) action.accept(value);
    }

    /**
     * If a value is present in the Option, run a given action with the value. If the Option is empty, run the empty action.
     *
     * @param action      the action to be executed, if the value is present
     * @param emptyAction the action to be executed, if no value is present.
     * @throws NullPointerException if the value is present and the action is null, or if no value is present and the empty based action is null.
     */
    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (value != null) action.accept(value);
        else emptyAction.run();
    }

    /**
     * If a value is present and the value matches the predicate, the Option is returned, otherwise an empty Opion is returned.
     *
     * @param predicate the predicate to apply if a value is present
     * @return An Option with the value if the value matches the predicate, otherwise an empty Option is returned.
     * @throws NullPointerException if the predicate is null
     */
    public Option<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        }
        return predicate.test(value) ? this : empty();
    }

    /**
     * If a value is present, the function returns an Option that is the result of applying a given
     * mapping function to the value, otherwise an empty Option is returned.
     * If the mapping function returns a null, an empty Option is returned.
     *
     * @param mapper the mapping function to apply to the value of the Option, if present
     * @param <U>    type of the returned value from the mapping function
     * @return If a value is present and if the mapping function returns a non-null value,
     * an Option with value type U is returned. Otherwise an empty Option is returned.
     * @throws NullPointerException if the mapping function is null.
     */
    public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) return empty();
        return Option.ofNullable(mapper.apply(value));
    }

    /**
     * If a value is present, the function returns an Option that is the result of applying a given
     * mapping function to the value. The mapping function itself returns an Option which is flattened
     * to an Option of the inner type of the returned Option.
     *
     * @param mapper the mapping function to apply to the value of the Option, if present
     * @param <U>    type of the inner value of the returned Option from the mapping function
     * @return If a value is present and if the mapping function returns an Option containing a non-null value,
     * an Option with that value is returned. Otherwise an empty Option is returned.
     * @throws NullPointerException if the mapping function is null.
     */
    public <U> Option<U> flatMap(Function<? super T, ? extends Option<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) return empty();
        @SuppressWarnings("unchecked")
        Option<U> r = (Option<U>) ofNullable(mapper.apply(value));
        return Objects.requireNonNull(r);
    }

    /**
     * If a value is present, return this Option describing the value, otherwise generate an Option provided by the supplying function.
     *
     * @param supplier the supplying function which creates an Option, if no value is present
     * @return If a value is present this Option is returned, otherwise the Option generated from the supplying function is returned.
     * @throws NullPointerException if the supplying function is null.
     */
    public Option<T> or(Supplier<? extends Option<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        if (isPresent()) return this;
        @SuppressWarnings("unchecked")
        Option<T> r = (Option<T>) supplier.get();
        return Objects.requireNonNull(r);
    }

    /**
     * If a value is present, create a sequential Stream containing only that value, otherwise an empty Stream is returned.
     *
     * @return the Option value as a Stream
     */
    public Stream<T> stream() {
        if (!isPresent()) return Stream.empty();
        return Stream.of(value);
    }

    /**
     * If a value is present, that is returned. Otherwise the supplied other value is returned.
     *
     * @param other the value to be returned, if no value is present.
     * @return the value is present, otherwise other
     */
    public T orElse(T other) {
        return value == null ? other : value;
    }

    /**
     * If a value is present, that value is returned. Otherswise the supplier generated Option is returned.
     *
     * @param supplier the supplying function which creates an Option, if no value is present.
     * @return If a value is present this Option is returned, otherwise the Option generated from the supplying function is returned.
     */
    public T orElseGet(Supplier<? extends T> supplier) {
        return value != null ? value : supplier.get();
    }

    /**
     * If a value is present, return the value, otherwise throw {@link NoSuchElementException}
     *
     * @return the non-null value described by the Option
     * @throws NoSuchElementException if no value is present
     */
    public T orElseThrow() {
        if (value == null) throw new NoSuchElementException("No value present");
        return value;
    }

    /**
     * If a value is present, return the value, otherwise throw an exception produced by the exception supplying function.
     *
     * @param exceptionHandler the supplying function that produces an exception
     * @param <X>              Type of the exception to be thrown
     * @return value if present
     * @throws X                    if no value is present
     * @throws NullPointerException if no value is present and the exception supplying function is {@code null}.
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionHandler) throws X {
        if (value != null) return value;
        throw exceptionHandler.get();
    }

    /**
     * Check if an object is equal to this Option. The other object is considered equal if:
     * - it is also an Option and
     * - both instances have no value present or
     * - the present values are "equal to" each other via equals().
     *
     * @param o an object tested for equality
     * @return True if the other object is "equal to" this object otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) {
            return false;
        }
        Option<?> other = (Option<?>) o;
        return Objects.equals(value, other.value);
    }

    /**
     * Returns the hash code of the value, if present, otherwise 0 (zero) if no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Returns a non-empty string representation of this Option suitable for debugging. The exact
     * presentation format is unspecified and may vary between implementations and version.
     *
     * @return string representation of this instance
     */
    @Override
    public String toString() {
        return value != null ? String.format("Option[%s]", value) : "Option.empty";
    }
}
