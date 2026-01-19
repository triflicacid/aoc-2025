/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.shared;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class stores an item which may be fetched but also set.
 * The primary use is to pass this to an object, which can then fetch the stored item at a later date once the store has been populated.
 *
 * This is like an optional but supports setting a default return value if the data is not present.
 * All (unless specified) methods are mutating, meaning this may be passed to objects and updated elsewhere, making passing data between objects possible.
 * All methods return an instance to `this` for function chaining.
 */
public class Store<T> implements Supplier<T>
{
    private T mData;
    private T mDefault;
    private Supplier<T> mDefaultSupplier;

    private Store(T data)
    {
        mData = data;
    }

    private Store(T data, T defaultData, Supplier<T> defaultSupplier)
    {
        mData = data;
        mDefault = defaultData;
        mDefaultSupplier = defaultSupplier;
    }

    /**
     * Return the stored item if present, or return the default if set, or throws an NoSuchElementException if neither is set.
     */
    public T get()
    {
        if (mData != null)
        {
            return mData;
        }
        if (mDefault != null)
        {
            return mDefault;
        }
        if (mDefaultSupplier != null)
        {
            return mDefaultSupplier.get();
        }
        throw new NoSuchElementException();
    }

    /**
     * Same as `get()`, but returns `null` rather than throwing an exception.
     */
    public T orNull()
    {
        return isSet() ? get() : null;
    }

    /**
     * Return data if present (including data and default), or return the given item.
     */
    public T orElse(T other)
    {
        return isSet() ? get() : other;
    }

    /**
     * Return data if present (including data and default), or execute and return item from the supplier.
     */
    public T orElseGet(Supplier<T> other)
    {
        return isSet() ? get() : other.get();
    }

    /**
     * Return data if present or throw the given Exception.
     */
    public T orElseThrow(RuntimeException exception)
    {
        if (isSet())
        {
            return get();
        }
        else
        {
            throw exception;
        }
    }

    /**
     * Return data if present, or throw the given Exception supplied by the supplier.
     */
    public T orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier)
    {
        if (isSet())
        {
            return get();
        }
        else
        {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Sets the stored data.
     */
    public Store<T> set(T data)
    {
        mData = data;
        return this;
    }

    /**
     * If set, sets this' data to the other's data, otherwise clears our data.
     */
    public Store<T> set(Store<T> other)
    {
        if (other.isSet())
        {
            set(other.get());
        }
        else
        {
            clear();
        }
        return this;
    }

    /**
     * If set, sets this' data to other's data, otherwise does nothing.
     */
    public Store<T> merge(Store<T> other)
    {
        if (other.isSet())
        {
            set(other.get());
        }
        return this;
    }

    /**
     * Clears the stored data.
     */
    public Store<T> clear()
    {
        mData = null;
        return this;
    }

    /**
     * Check if there is data present.
     * Note this includes default, so this tells us if `.get` will throw or not.
     */
    public boolean isSet()
    {
        return mData != null || mDefault != null || mDefaultSupplier != null;
    }

    /**
     * Check if there is no data present.
     * Note this includes default, so this tells us if `.get` will throw or not.
     */
    public boolean isEmpty()
    {
        return mData == null && mDefault == null && mDefaultSupplier == null;
    }

    /**
     * Transform the input if it is set.
     */
    public Store<T> map(Function<? super T, ? extends T> mapper)
    {
        if (isSet())
        {
            mData = mapper.apply(get());
        }
        return this;
    }

    /**
     * Transforms this store's input, if set, but the given function, if set.
     */
    public Store<T> map(Store<Function<? super T, ? extends T>> mapperStore)
    {
        return mapperStore.isSet() ? map(mapperStore.get()) : this;
    }

    /**
     * Similar to `map`, but mapper returns a Store which is merged into this.
     * May be used to build a chain of transformations.
     */
    public Store<T> flatMap(Function<? super T, ? extends Store<? extends T>> mapper)
    {
        return isSet() ? merge((Store<T>) mapper.apply(get())) : this;
    }

    /**
     * Similar to `map`, but mapper returns a Store which is merged into this.
     * May be used to build a chain of transformations.
     */
    public Store<T> flatMap(Store<Function<? super T, ? extends Store<? extends T>>> mapperStore)
    {
        return mapperStore.isSet() ? flatMap(mapperStore.get()) : this;
    }

    /**
     * Filter this store's contents, if present - clear store if `predicate` return false.
     */
    public Store<T> filter(Predicate<? super T> predicate)
    {
        if (isSet() && !predicate.test(get()))
        {
            clear();
        }
        return this;
    }

    /**
     * Runs one of the given functions if the store is empty or if it is set,
     */
    public void test(Consumer<? super T> ifSet, Runnable ifEmpty)
    {
        if (isSet())
        {
            ifSet.accept(get());
        }
        else
        {
            ifEmpty.run();
        }
    }

    /**
     * Set a default return value.
     */
    public Store<T> setDefault(T defaultData)
    {
        mDefault = defaultData;
        mDefaultSupplier = null;
        return this;
    }

    /**
     * Set a function to create a default return value.
     */
    public Store<T> setDefault(Supplier<T> defaultSupplier)
    {
        mDefault = null;
        mDefaultSupplier = defaultSupplier;
        return this;
    }

    /**
     * Clear default return values.
     */
    public Store<T> clearDefault()
    {
        mDefault = null;
        mDefaultSupplier = null;
        return this;
    }

    /**
     * Convert to an Optional (contents is `get()` or `empty`).
     */
    public Optional<T> optional()
    {
        return isSet() ? Optional.of(get()) : Optional.empty();
    }

    /**
     * Return a copy of this store (data and defaults).
     * Note that the data referenced is the same.
     * This is different from `Store.of(original.orNull())` as defaults will be copied over,ta field.
     */
    public Store<T> copy()
    {
        return new Store<>(mData, mDefault, mDefaultSupplier);
    }

    /**
     * Return an empty store.
     */
    public static <T> Store<T> empty()
    {
        return new Store<>(null);
    }

    /**
     * Return a store containing the given item.
     */
    public static <T> Store<T> of(T data)
    {
        return new Store<>(data);
    }

    /**
     * Creates a new store from the given one (references the same data).
     */
    public static <T> Store<T> of(Store<T> other)
    {
        return new Store<>(other.orNull());
    }

    /**
     * Create a store from an Optional
     */
    public static <T> Store<T> of(Optional<T> data)
    {
        return new Store<>(data.orElse(null));
    }

    /**
     * Return an empty store, but with the given default return value.
     */
    public static <T> Store<T> defaultingTo(T defaultData)
    {
        return new Store<T>(null)
            .setDefault(defaultData);
    }

    /**
     * Return an empty store, but with the given default return value supplied.
     */
    public static <T> Store<T> defaultingTo(Supplier<T> defaultSupplier)
    {
        return new Store<T>(null)
            .setDefault(defaultSupplier);
    }
}
