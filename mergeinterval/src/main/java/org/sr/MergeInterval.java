package org.sr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Merge Interval Lib to check as list of Arrays against overlap
 * 
 * @author Stefan Raabe
 *
 */
class MergeInterval {
    private Map<int[], List<int[]>> graph;
    private Map<Integer, List<int[]>> nodesInComp;
    private Set<int[]> visited;
    
    /**
     * Complex Merge Interval Method
     * 
     * @param intervals
     * @return
     */
    public List<int[]> merge(List<int[]> intervals) {
        buildGraph(intervals);
        buildComponents(intervals);

        // for each component, merge all intervals into one interval.
        List<int[]> merged = new LinkedList<>();
        for (int comp = 0; comp < nodesInComp.size(); comp++) {
            merged.add(mergeNodes(nodesInComp.get(comp)));
        }

        return merged;
    }
    
    /**
     * Simple Merge Interval Method, with sort preparation
     * 
     * @param intervals
     * @return
     */
    public List<int[]> mergeSimple(List<int[]> intVals) {        
        // Sort by interval start pos, necessary with this kind of merge 
    	Collections.sort(intVals, new Comparator<int[]>() {
    	    public int compare(int[] a, int[] b) {
    	        return a[0] - b[0];
    	    }
    	});
        
        List<int[]> result = new ArrayList<int[]>();
        
        // init first entry
        int[] prev = intVals.get(0);

        for (int[] intVal : intVals) {
            // Current interval second is less than value before
            if (prev[1] > intVal[1]) continue;
            //merge with prev, set prev end to current end
            if (prev[1] >= intVal[0]) {
                //merge
                prev[1] = intVal[1];
            } else {
                // If No Merge is needed
                result.add(prev);
                // Set latest to prev
                prev = intVal;
            }
            
        }
        // do not ignore last entry
        result.add(prev);

        return result;
    }

    // build a graph where an undirected edge between intervals u and v exists
    // iff u and v overlap.
    private void buildGraph(List<int[]> intervals) {
        graph = new HashMap<>();
        for (int[] interval : intervals) {
            graph.put(interval, new LinkedList<>());
        }

        for (int[] interval1 : intervals) {
            for (int[] interval2 : intervals) {
                if (checkOverlap(interval1, interval2)) {
                    graph.get(interval1).add(interval2);
                    graph.get(interval2).add(interval1);
                }
            }
        }
    }

    // merges all of the nodes in this connected component into one interval.
    private int[] mergeNodes(List<int[]> nodes) {
        int minStart = nodes.get(0)[0];
        for (int[] node : nodes) {
            minStart = Math.min(minStart, node[0]);
        }

        int maxEnd = nodes.get(0)[1];
        for (int[] node : nodes) {
            maxEnd = Math.max(maxEnd, node[1]);
        }

        return new int[] {minStart, maxEnd};
    }

    // use depth-first search to mark all nodes in the same connected component
    // with the same integer.
    private void markComponentDFS(int[] start, int compNumber) {
        Stack<int[]> stack = new Stack<>();
        stack.add(start);

        while (!stack.isEmpty()) {
            int[] node = stack.pop();
            if (!visited.contains(node)) {
                visited.add(node);

                if (nodesInComp.get(compNumber) == null) {
                    nodesInComp.put(compNumber, new LinkedList<>());
                }
                nodesInComp.get(compNumber).add(node);

                for (int[] child : graph.get(node)) {
                    stack.add(child);
                }
            }
        }
    }

    // gets the connected components of the interval overlap graph.
    private void buildComponents(List<int[]> intervals) {
        nodesInComp = new HashMap<>();
        visited = new HashSet<>();
        int compNumber = 0;

        for (int[] interval : intervals) {
            if (!visited.contains(interval)) {
                markComponentDFS(interval, compNumber);
                compNumber++;
            }
        }
    }
    
    /**
     * Check Overlap if happen return true
     * 
     * @param interval1
     * @param interval2
     * @return
     */
    private boolean checkOverlap(int[] interval1, int[] interval2) {
        return interval1[0] <= interval2[1] && interval2[0] <= interval1[1];
    }
}