/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a pipeline of operations.
 * Operations are lazy, meaning each operation is only executed if a result is required.
 *
 * @param <T> Element type of the flow.
 */
public class Flow<T>
{
    private final List<T> source;
    private final List<Operation<?, ?>> operations;

    private Flow(List<T> source)
    {
        this.source = source;
        this.operations = new ArrayList<>();
    }

    private Flow(List<T> source, List<Operation<?, ?>> operations)
    {
        this.source = source;
        this.operations = operations;
    }

    /**
     * Create a new flow with the given operation appended to it.
     * (This is a fairly low-level method.)
     *
     * @param operation Operation to append.
     * @return New flow.
     */
    public <R> Flow<T> chain(Operation<T, R> operation)
    {
        List<Operation<?, ?>> newOps = new ArrayList<>(operations);
        newOps.add(operation);
        return new Flow<>(source, newOps);
    }

    /**
     * Filter elements based on a predicate - remove if `false`, keep if `true`.
     *
     * @param predicate predicate to run on each element
     * @return new flow
     */
    public Flow<T> filter(Predicate<T> predicate)
    {
        return chain(xs -> ListUtils.filter(xs, predicate));
    }

    /**
     * Transform this flow's elements based on the mapper function.
     *
     * @param mapper Function to transform each element
     * @param <R> Result type of the mapper
     * @return new flow
     */
    public <R> Flow<T> map(Function<? super T, ? extends R> mapper)
    {
        return chain(xs -> ListUtils.map(xs, mapper));
    }

    /**
     * Transform this flow's elements based on the mapper function, margins all the returned items into the resultant flow.
     *
     * @param mapper Function to transform each element into one or more results
     * @param <R> Element result type of the mapper
     * @return new flow
     */
    public <R> Flow<T> flatMap(Function<? super T, List<? extends R>> mapper)
    {
        return chain(xs -> ListUtils.flatMap(xs, mapper));
    }

    /**
     * Split this flow into two based on a predicate.
     *
     * @param decider predicate to decide which branch to pass the given element to
     * @param trueBranch branch for `true` elements
     * @param falseBranch branch for`false` elements
     * @param joiner function to join the two branches together. As many elements will be merged as possible, with the other elements ignored.
     * @param <A> resultant type of the truthy branch
     * @param <B> resultant type of the falsy branch
     * @param <R> resultant type of the joiner
     * @return New flow.
     */
    public <A, B, R> Flow<T> split(
        Predicate<T> decider,
        Function<Flow<T>, Flow<A>> trueBranch,
        Function<Flow<T>, Flow<B>> falseBranch,
        BiFunction<A, B, R> joiner
    )
    {
        return chain(input -> {
            List<T> trueList = new ArrayList<>();
            List<T> falseList = new ArrayList<>();

            for (T item : input)
            {
                if (decider.test(item))
                {
                    trueList.add(item);
                }
                else
                {
                    falseList.add(item);
                }
            }

            return ListUtils.zip(
                trueBranch.apply(of(trueList)).collect(),
                falseBranch.apply(of(falseList)).collect(),
                joiner);
        });
    }

    /**
     * Split this flow into two, providing the elements to each. Note, the inputs are equal, so mutating is not advised.
     *
     * @param leftBranch branch to split elements into
     * @param rightBranch branch to split elements into
     * @param joiner function to join thw two branches together. As many elements will be merged as possible, the rest ignored - e.g., if the left has two elements but the right had three, the final will only have two.
     * @param <A> resultant type of the left branch
     * @param <B> resultant type of the right branch
     * @param <R> resultant type of the joiner
     * @return New flow.
     */
    public <A, B, R> Flow<T> split(
        Function<Flow<T>, Flow<A>> leftBranch,
        Function<Flow<T>, Flow<B>> rightBranch,
        BiFunction<A, B, R> joiner
    )
    {
        return chain(input -> ListUtils.zip(
            leftBranch.apply(of(input)).collect(),
            rightBranch.apply(of(input)).collect(),
            joiner));
    }

    /**
     * Scans through the flow, combining each element with the result of the previous call.
     * E.g., `com.ruben.Flow.of(1, 2, 3).scan((acc, item) -> acc + item, 0).collect()` yields `[1, 3, 6]`
     *
     * @param scanner (accumulator, item) -> result
     * @param initial Initial value (is not included in the result list)
     * @param <R> Result type of the scan, allows for flexibility when R != T.
     */
    public <R> Flow<T> scan(BiFunction<R, T, R> scanner, R initial)
    {
        return chain(xs -> ListUtils.scan(xs, scanner, initial));
    }

    /**
     * Reduce the flow into a singular result (same as retrieving the final item after `::scan`)
     *
     * @param reducer (accumulator, item) -> result
     * @param initial Initial value
     * @return The reduced value
     */
    public <R> R reduce(BiFunction<R, T, R> reducer, R initial)
    {
        return ListUtils.reduce(collect(), reducer, initial);
    }

    /**
     * Run the given consumer on each element but do not edit the flow.
     *
     * @param consumer Accept each element.
     * @return New flow.
     */
    public Flow<T> tee(Consumer<T> consumer)
    {
        return chain(input -> {
            for (T item : input)
            {
                consumer.accept(item);
            }
            return input;
        });
    }

    /**
     * Pass the evaluated flow to the consumer but do not edit the flow.
     *
     * @param consumer Accept the flow.
     * @return New flow.
     */
    public Flow<T> teeFlow(Consumer<Flow<T>> consumer)
    {
        return chain(input -> {
            consumer.accept(of(List.copyOf(input)));
            return input;
        });
    }

    /**
     * Evaluate the flow, then return if any element matches the predicate
     *
     * @param predicate Predicate to run on each element
     * @return If any elements matched the predicate
     */
    public boolean anyMatch(Predicate<? super T> predicate)
    {
        return ListUtils.anyMatch(collect(), predicate);
    }

    /**
     * Evaluate the flow, then return true iff all elements matche the predicate
     *
     * @param predicate Predicate to run on each element
     * @return If all elements matched the predicate
     */
    public boolean allMatch(Predicate<? super T> predicate)
    {
        return ListUtils.allMatch(collect(), predicate);
    }

    /**
     * Evaluate the flow, then return iff none of the elements matched the predicate
     *
     * @param predicate Predicate to run on each element
     * @return True only if no elements matched the predicate
     */
    public boolean noneMatch(Predicate<? super T> predicate)
    {
        return ListUtils.noneMatch(collect(), predicate);
    }

    /**
     * Return the size of the current (unevaluated) flow. This WILL NOT evaluate any pending operations.
     *
     * @return The number of elements in the flow (prior to evaluation.)
     */
    public int size()
    {
        return source.size();
    }

    /**
     * Evaluate the flow, then return the resultant elements as a list.
     *
     * @return List of evaluated elements.
     */
    public <R> List<R> collect()
    {
        return evaluate();
    }

    private <R> List<R> evaluate()
    {
        List result = source;
        for (Operation op : operations)
        {
            result = op.apply(result);
        }
        return (List<R>) result;
    }

    /**
     * Force evaluation of the flow, applying any pending operations.
     *
     * @return The CURRENT flow, after it has been evaluated.
     */
    public Flow<T> flush()
    {
        evaluate();
        return this;
    }

    /**
     * Merge the given flow into this one (both flows are fully evaluated before merging)
     *
     * @param other com.ruben.Flow to append to this one.
     * @return New merged flow
     */
    public Flow<T> append(Flow<T> other)
    {
        List<T> merged = new ArrayList<>(collect());
        merged.addAll(other.collect());
        return new Flow<>(merged);
    }

    /**
     * (Potentially dangerous) add elements into the source without evaluating any operations.
     *
     * @param newElements elements to append to this source
     * @return New flow with eements appended
     */
    public Flow<T> append(List<T> newElements)
    {
        List<T> newSource = new ArrayList<>(source);
        newSource.addAll(newElements);
        return new Flow<>(newSource, operations);
    }

    /**
     * Evaluate and take the first `n` elements of the flow.
     *
     * @param n The number of elements to keep from the start of the flow
     * @return New flow
     */
    public Flow<T> take(int n)
    {
        if (n <= 0) {return empty();}

        List<T> evaluated = collect();
        List<T> result = evaluated.subList(0, Math.min(n, evaluated.size()));
        return new Flow<>(result);
    }

    /**
     * Evaluate and drop the first `n` elements of the flow.
     *
     * @param n The number of elements to discard from the start of the flow
     * @return New flow
     */
    public Flow<T> drop(int n)
    {
        List<T> evaluated = collect();
        List<T> result = n <=
            0 ? evaluated : evaluated.subList(Math.min(n, evaluated.size()), evaluated.size());
        return new Flow<>(result);
    }

    /**
     * Find and return the first element matching the given predicate.
     *
     * @param predicate Predicate which returns `true` if the element is to be returned.
     * @return The first matching element, or None.
     */
    public Optional<T> find(Predicate<T> predicate)
    {
        return ListUtils.find(collect(), predicate);
    }

    /**
     * Retrieves the first element of the flow.
     *
     * @return The first evaluated element in the flow.
     */
    public Optional<T> getFirst()
    {
        List<T> evaluated = collect();
        return evaluated.isEmpty() ? Optional.empty() : Optional.of(evaluated.getFirst());
    }

    /**
     * Retrieves the last element of the flow.
     *
     * @return The last evaluated element in the flow.
     */
    public Optional<T> getLast()
    {
        List<T> evaluated = collect();
        return evaluated.isEmpty() ? Optional.empty() : Optional.of(evaluated.getLast());
    }

    /**
     * Create a flow seeded with the initial elements (note the list is not copied.)
     *
     * @param source Initial elements.
     * @param <T> Type of the flow's elements.
     * @return A flow.
     */
    public static <T> Flow<T> of(List<? super T> source)
    {
        return new Flow<>((List<T>) source);
    }

    /**
     * Create an *immutable* flow seeded with the initial elements.
     *
     * @param elements The flow's elements.
     * @param <T> Type of the flow's elements.
     * @return A flow.
     */
    public static <T> Flow<T> of(T... elements)
    {
        return new Flow<>(List.of(elements));
    }

    /**
     * Create an empty but mutable flow (elements can be added via `::append`)
     *
     * @param <T> Type of the flow's elements.
     * @return A flow.
     */
    public static <T> Flow<T> empty()
    {
        return new Flow<>(new ArrayList<>());
    }

    /**
     * Combine/'zip' two flows together using a zipper function.
     *
     * @param a First flow to combine.
     * @param b Second flow to combine.
     * @param zipper Function to combine elements from each flow.
     * @param <A> Element type of first flow.
     * @param <B> Element type of second flow.
     * @param <R> Element type of resultant flow.
     * @return New flow.
     */
    public static <A, B, R> Flow<R> zip(Flow<A> a, Flow<B> b, BiFunction<A, B, R> zipper)
    {
        return of(ListUtils.zip(a.collect(), b.collect(), zipper));
    }

    /**
     * `Flow.zip` with this flow provided as the first (so lazy).
     *
     * @param b Second flow to zip. Once given to this method, it should NOT be interacted with. It is only evaluated when needed.
     * @param zipper Function to combine elements from each flow.
     */
    public <B, R> Flow<T> zip(Flow<B> b, BiFunction<T, B, R> zipper)
    {
        return chain(input -> ListUtils.zip(input, b.collect(), zipper));
    }

    /**
     * Similar to `::zip`, but for each item in `a`, zip with each item in `b`.
     *
     * @param a First flow to permute.
     * @param b Second flow to permute.
     * @param zipper Function to combine elements from each flow.
     * @param <A> Element type of first flow.
     * @param <B> Element type of second flow.
     * @param <R> Element type of resultant flow.
     * @return New flow.
     */
    public static <A, B, R> Flow<R> permute(Flow<A> a, Flow<B> b, BiFunction<A, B, R> zipper)
    {
        return of(ListUtils.permute(a.collect(), b.collect(), zipper));
    }

    /**
     * `Flow.permute` with this flow provided as the first (so lazy).
     *
     * @param b Second flow to permute. Once given to this method, it should NOT be interacted with. It is only evaluated when needed.
     * @param zipper Function to combine elements from each flow.
     */
    public <B, R> Flow<T> permute(Flow<B> b, BiFunction<T, B, R> zipper)
    {
        return chain(input -> ListUtils.permute(input, b.collect(), zipper));
    }

    /**
     * Interleave two flows together: [a1, b1, a2, b2, ...]. Excess items from either flow will be added to the end.
     *
     * @param a First flow to interleave.
     * @param b Second flow to interleave.
     * @param <T> Element type of each flow.
     * @return New flow.
     */
    public static <T> Flow<T> interleave(Flow<T> a, Flow<T> b)
    {
        return of(ListUtils.interleave(a.collect(), b.collect()));
    }

    /**
     * `Flow.interleave` with this flow provided as the first.
     */
    public Flow<T> interleave(Flow<T> other)
    {
        return chain(xs -> ListUtils.interleave(xs, other.collect()));
    }

    /**
     * Defines an operation that transforms a list of elements.
     *
     * @param <T> Original element type.
     * @param <R> Resultant element type.
     */
    public interface Operation<T, R>
    {
        List<R> apply(List<T> input);
    }
}
