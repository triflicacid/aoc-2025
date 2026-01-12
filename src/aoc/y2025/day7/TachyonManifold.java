/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day7;

import aoc.shared.Grid;
import aoc.shared.Point2d;

import java.util.*;

public class TachyonManifold
{
    public ColissionBehaviour COLISSION_BEHAVIOUR = ColissionBehaviour.MERGE;

    private final Grid<State> grid;
    private final Point2d source;
    private Set<Beam> beams = new HashSet<>(); // store latest location of all tachyon beams
    private int splitCounter = 0;
    private boolean finished = false;

    public TachyonManifold(Grid<State> manifold)
    {
        grid = manifold;
        Grid.Cell<State> origin = manifold.find(cell -> cell.state() == State.SOURCE);
        if (origin == null)
        {
            throw new IllegalStateException("Tachyon manifold has no source");
        }
        this.source = origin.location();
    }

    /**
     * Are we finished with our simulation?
     */
    public boolean isFinished()
    {
        return finished;
    }

    /**
     * Return the number of active beams
     */
    public long countBeams()
    {
        return beams.stream()
                .mapToLong(Beam::instances)
                .sum();
    }

    /**
     * Count the number of times our beam has been split
     */
    public int countSplits()
    {
        return splitCounter;
    }

    /**
     * Reset the simulation
     */
    public void reset()
    {
        beams.clear();
        finished = false;
        splitCounter = 0;
        // instantiate a beam at the origin
        beams.add(new Beam(source));
    }

    /**
     * Perform a single simulation step
     */
    public void simulateStep()
    {
        if (finished) throw new IllegalStateException("Simulation is marked as finished");
        if (beams.isEmpty()) throw new IllegalStateException("No beams found... did you run this.reset()?");

        Set<Beam> newBeams = new HashSet<>();
        beams.forEach(beam -> processBeam(newBeams, beam));

        // if we're done, don't overwrite the beams
        if (newBeams.isEmpty())
        {
            finished = true;
        }
        else
        {
            beams = newBeams;
        }
    }

    /**
     * Simulate the steps until we're done (finished is true)
     * [NOTE this doesn't reset what we've done so far]
     */
    public void simulate()
    {
        while (!finished)
        {
            simulateStep();
        }
    }

    /**
     * Process a beam, adding its new beam 'head' to the beams list.
     * Also handles beam splitting, in which case those are added too.
     */
    private void processBeam(Collection<Beam> beams, Beam beam)
    {
        // advance beam by one
        Point2d newLocation = beam.location().addY(1);
        if (!newLocation.apply(grid::validPosition)) return;

        State state = newLocation.apply(grid::get);
        if (state == State.SPLITTER)
        {
            // create two new beams either side of the splitter
            splitCounter++;
            insertBeam(beams, beam.createNew(newLocation.addX(-1)));
            insertBeam(beams, beam.createNew(newLocation.addX(1)));
            return;
        }
        insertBeam(beams, beam.createNew(newLocation));
    }

    /**
     * Create and insert the given beam if it is valid
     */
    private void insertBeam(Collection<Beam> beams, Beam beam)
    {
        // does the beam already exist (if so, the position is valid by induction)
        if (beams.contains(beam))
        {
            if (COLISSION_BEHAVIOUR == ColissionBehaviour.ADDITIVE)
            {
                // get all beams which exist in this position
                List<Beam> beamInstances = beams.stream()
                        .filter(b -> b.equals(beam))
                        .toList();
                beams.removeAll(beamInstances);

                // for all beams in this position, count how many beam instanes we have
                long collidingBeamCount = beamInstances.stream()
                        .mapToLong(Beam::instances)
                        .sum();

                // add the instances to this new beam
                beams.add(beam.createNew(collidingBeamCount));
            }
            return;
        }

        // verify that we have a valid location
        Point2d location = beam.location();
        if (!location.apply(grid::validPosition)) return;

        // a beam only propagates to empty or other beam cells (i.e., NOT a splitter)
        State state = location.apply(grid::get);
        if (state == State.EMPTY || state == State.BEAM)
        {
            grid.set(location.x(), location.y(), State.BEAM);
            beams.add(beam);
        }
    }

    @Override
    public String toString()
    {
        return grid.toString();
    }

    public void printBeams()
    {
        System.out.println(beams.stream()
                .sorted(Comparator.comparing(b -> b.location().x()))
                .toList()
            + "  total " + countBeams());
    }
}
