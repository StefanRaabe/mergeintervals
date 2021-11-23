package org.sr;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

public class MergeIntervalTest {
	private static final Logger log = Logger.getLogger(MergeIntervalTest.class.getName());
	/**
	 * Input: [25,30] [2,19] [14, 23] [4,8]  Output: [2,23] [25,30]
	 */
	@Test
    void doMergeTest() {
        List<int[]> intervalsList = initIntervalList();

        MergeInterval mi = new MergeInterval();
        
        List<int[]> resList = mi.merge(intervalsList);
        for (int[] res : resList) {
        	log.info(Arrays.toString(res));
        }      
        
        // Sort as a sorted List is recommended for Comparation
        Comparator<int[]> nameSorter = (a, b) -> a[0] - b[0];
        Collections.sort(resList, nameSorter);
    	
		List<int[]> expList = initExpList();
        
        assertArrayEquals(resList.toArray(), expList.toArray());
    }
	
	@Test
    void doMergeSimpleTest() {   
        List<int[]> intervalsList = initIntervalList();

        MergeInterval mi = new MergeInterval();
        
        List<int[]> resList = mi.mergeSimple(intervalsList);
        for (int[] res : resList) {
        	log.info(Arrays.toString(res));
        }
        
		List<int[]> expList = initExpList();
        
        assertArrayEquals(resList.toArray(), expList.toArray());
    }

	private List<int[]> initExpList() {
		List<int[]> expList = new ArrayList<int[]>();
        expList.add(new int[] {2, 23});
		expList.add(new int[] {25, 30});
		return expList;
	}

	private List<int[]> initIntervalList() {
		List<int[]> intervalsList = new ArrayList<int[]>();
        intervalsList.add(new int[] {25, 30});
        intervalsList.add(new int[] {2, 19});
        intervalsList.add(new int[] {14, 23});
        intervalsList.add(new int[] {4, 8});
		return intervalsList;
	}
}
