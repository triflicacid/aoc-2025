/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.shared;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ListUtils
{
    /**
     * Filter the given list to only the items for which the predicate returns true
     *
     * @param items Original items to filter.
     * @param predicate Predicate to test each item.
     * @return Filtered list.
     */
    public static <T> List<T> filter(List<T> items, Predicate<? super T> predicate)
    {
        List<T> result = new ArrayList<>();
        for (T item : items)
        {
            if (predicate.test(item)) result.add(item);
        }
        return result;
    }

    /**
     * Reverse the given list (doesn't mutate it)
     *
     * @param items List to reverse
     * @return Reversed list
     */
    public static <T> List<T> reverse(List<T> items)
    {
        List<T> result = new ArrayList<>();
        for (int i = items.size() - 1; i >= 0; i--)
        {
            result.add(items.get(i));
        }
        return result;
    }

    /**
     * Apply the given function to every item in the given list.
     *
     * @param items Original items.
     * @param mapper Function to apply to each item.
     * @return The resultant list after the mapper has been applied to each original item.
     */
    public static <T, R> List<R> map(List<T> items, Function<? super T, ? extends R> mapper)
    {
        List<R> result = new ArrayList<>();
        for (T item : items)
        {
            result.add(mapper.apply(item));
        }
        return result;
    }

    /**
     * Apply the given function to every item in the given list, adding all elements returned to the resultant list.
     *
     * @param items Original items.
     * @param mapper Function to apply to each item.
     * @return The resultant list after the mapper has been applied to each original item.
     */
    public static <T, R> List<R> flatMap(List<T> items, Function<? super T, List<? extends R>> mapper)
    {
        List<R> result = new ArrayList<>();
        for (T item : items)
        {
            result.addAll(mapper.apply(item));
        }
        return result;
    }

    /**
     * Combine two lists by applying the function on each item element-wise.
     * This is done as much as possible - the resultant list is the min size of the two input lists.
     *
     * @param as First list to zip.
     * @param bs Second list to zip.
     * @param zipper Function to combine.zip each element-wise pair.
     * @return Zipped list.
     */
    public static <A, B, R> List<R> zip(List<A> as, List<B> bs, BiFunction<A, B, R> zipper)
    {
        List<R> result = new ArrayList<>();
        int size = Math.min(as.size(), bs.size());
        for (int i = 0; i < size; i++)
        {
            result.add(zipper.apply(as.get(i), bs.get(i)));
        }

        return result;
    }

    /**
     * For each element in `as`, combine it with each element in `bs` using the given combiner function.
     *
     * @param as First (outer) list to permute.
     * @param bs Second (inner) list to permute.
     * @param combiner Function to combine elements from each list.
     * @return Permuted list.
     */
    public static <A, B, R> List<R> permute(List<A> as, List<B> bs, BiFunction<A, B, R> combiner)
    {
        List<R> result = new ArrayList<>();

        for (A a : as)
        {
            for (B b : bs)
            {
                result.add(combiner.apply(a, b));
            }
        }

        return result;
    }

    /**
     * Interleave two lists together: [a1, b1, a2, b2, ...]. Excess items from either list will be added to the end.
     *
     * @param as First list to interleave.
     * @param bs Second list to interleave.
     * @return Interleaved list.
     */
    public static <T> List<T> interleave(List<T> as, List<T> bs)
    {
        int idx = 0;
        List<T> results = new ArrayList<>();

        int minSize = Math.min(as.size(), bs.size());
        for (; idx < minSize; idx++)
        {
            results.add(as.get(idx));
            results.add(bs.get(idx));
        }

        for (int i = idx; i < as.size(); i++)
        {
            results.add(as.get(i));
        }
        for (int i = idx; i < bs.size(); i++)
        {
            results.add(bs.get(i));
        }

        return results;
    }

    /**
     * For each item in the list, apply the scanner on the result of the previous items and the current item.
     * E.g., `scan([1, 2, 3], Integer::sum, 0)` yields `[1, 3, 6]`
     *
     * @param items Items to scan.
     * @param scanner (acc, item) -> result
     * @param initial Initial 'seed' value for acc
     * @return Scanned list
     */
    public static <T, R> List<R> scan(List<T> items, BiFunction<R, T, R> scanner, R initial)
    {
        List<R> results = new ArrayList<>();
        R acc = initial;
        for (T item : items)
        {
            acc = scanner.apply(acc, item);
            results.add(acc);
        }

        return results;
    }

    /**
     * Iterate through the list, accumulating results by passing them and the previous accumulated value to the function.
     * E.g., `reduce([1, 2, 3], Integer::sum, 0)` yields `6`
     *
     * @param items Items to reduce.
     * @param scanner (acc, item) -> result
     * @param initial Initial 'seed' value for acc
     * @return Scalar reduced value
     */
    public static <T, R> R reduce(List<T> items, BiFunction<R, T, R> scanner, R initial)
    {
        R acc = initial;
        for (T item : items)
        {
            acc = scanner.apply(acc, item);
        }
        return acc;
    }

    /**
     * Return true if any element returns true for the given predicate
     *
     * @param predicate Predicate to run on each element
     * @return If any elements matched the predicate
     */
    public static <T> boolean anyMatch(List<T> items, Predicate<? super T> predicate)
    {
        for (T item : items)
        {
            if (predicate.test(item)) {return true;}
        }
        return false;
    }

    /**
     * Return true iff every element returns true on the given predicate
     *
     * @param predicate Predicate to run on each element
     * @return If all elements matched the predicate
     */
    public static <T> boolean allMatch(List<T> items, Predicate<? super T> predicate)
    {
        for (T item : items)
        {
            if (!predicate.test(item)) {return false;}
        }
        return true;
    }

    /**
     * Return true iff none of the elements match the predicate
     *
     * @param predicate Predicate to run on each element
     * @return True only if no elements matched the predicate
     */
    public static <T> boolean noneMatch(List<T> items, Predicate<? super T> predicate)
    {
        for (T item : items)
        {
            if (predicate.test(item)) {return false;}
        }
        return true;
    }

    /**
     * Find and return the first element matching the given predicate.
     *
     * @param predicate Predicate which returns `true` if the element is to be returned.
     * @return The first matching element, or None.
     */
    public static <T> Optional<T> find(List<T> items, Predicate<T> predicate)
    {
        for (T item : items)
        {
            if (predicate.test(item)) {return Optional.of(item);}
        }
        return Optional.empty();
    }

    /**
     * Add all of one list to the other list.
     *
     * @param a first list
     * @param b second list
     * @return the merged lists
     */
    public static <T> List<T> concat(List<T> a, List<T> b)
    {
        List<T> result = new ArrayList<>(a);
        result.addAll(b);
        return result;
    }

    /**
     * Transpose a nested list structure
     * @param matrix Matrix/nested list to transpose
     * @return the transposed list
     * @param <T>
     */
    public static <T> List<List<T>> transpose(List<List<T>> matrix)
    {
        int maxLength = matrix.stream().mapToInt(List::size).max().orElse(0);
        if (maxLength == 0) return List.of();

        List<Iterator<T>> rows = matrix.stream().map(List::iterator).toList();
        // for each [0, maxLength), we want to insert the element at the cursor in each row
        return IntStream.range(0, maxLength)
                .mapToObj(_ -> rows.stream()
                        .filter(Iterator::hasNext)
                        .map(Iterator::next)
                        .toList())
                .toList();
    }
}

