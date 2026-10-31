package com.library.test;

import com.library.ds.CustomHashMap;
import com.library.ds.CustomLinkedList;
import com.library.ds.CustomQueue;
import com.library.ds.CustomStack;
import com.library.ds.EmptyStructureException;
import com.library.ds.HashStats;

/**
 * Self-test suite verifying the core data structures against SRS section 11.2 scenarios 1 to 6.
 */
public class DsaSelfTest {

    private static int totalChecks = 0;
    private static int failedChecks = 0;

    /**
     * Helper to verify a condition and report PASS or FAIL.
     * Complexity: O(1)
     *
     * @param name      the name of the check
     * @param condition true if passed, false if failed
     */
    public static void check(String name, boolean condition) {
        totalChecks++;
        if (condition) {
            System.out.println("PASS: " + name);
        } else {
            System.out.println("FAIL: " + name);
            failedChecks++;
        }
    }

    /**
     * Executes all DSA test scenarios and returns non-zero exit code on failure.
     * Complexity: O(n)
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Starting DSA Self-Tests ===");
        testScenario1();
        testScenario2();
        testScenario3();
        testScenario4();
        testScenario5();
        testScenario6();

        System.out.println("--------------------------------");
        System.out.println("DSA Self-Test Summary: " + (totalChecks - failedChecks)
                + " / " + totalChecks + " checks passed.");
        if (failedChecks > 0) {
            System.out.println("FAILURE: " + failedChecks + " test(s) failed.");
            System.exit(1);
        } else {
            System.out.println("ALL DSA TESTS PASSED SUCCESSFULLY.");
        }
    }

    private static void testScenario1() {
        CustomHashMap<String, String> map = new CustomHashMap<>();
        for (int i = 1; i <= 200; i++) {
            String key = "B" + String.format("%03d", i);
            map.put(key, "Title " + i);
        }
        boolean allFound = true;
        for (int i = 1; i <= 200; i++) {
            String key = "B" + String.format("%03d", i);
            if (!("Title " + i).equals(map.get(key))) {
                allFound = false;
                break;
            }
        }
        HashStats stats = map.stats();
        check("Scenario 1: Insert 200 items, all retrieved correctly", allFound);
        check("Scenario 1: Resizes occurred (resizeCount > 0)", stats.getResizeCount() > 0);
        check("Scenario 1: Load factor stays <= 0.75", stats.getLoadFactor() <= 0.75);
    }

    private static void testScenario2() {
        CustomHashMap<TestKey, String> map = new CustomHashMap<>();
        TestKey key1 = new TestKey("Key1", 7);
        TestKey key2 = new TestKey("Key2", 7);
        map.put(key1, "Val1");
        map.put(key2, "Val2");

        check("Scenario 2: Both colliding keys present", map.containsKey(key1) && map.containsKey(key2));
        check("Scenario 2: Correct values retrieved", "Val1".equals(map.get(key1)) && "Val2".equals(map.get(key2)));
        check("Scenario 2: Bucket chain length is at least 2", map.stats().getLongestChain() >= 2);

        String removed1 = map.remove(key1);
        check("Scenario 2: Remove key1 returns Val1", "Val1".equals(removed1));
        check("Scenario 2: Key1 gone, Key2 still present", !map.containsKey(key1) && "Val2".equals(map.get(key2)));
    }

    private static void testScenario3() {
        CustomQueue<String> queue = new CustomQueue<>();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        boolean fifoOrder = "A".equals(queue.dequeue())
                && "B".equals(queue.dequeue())
                && "C".equals(queue.dequeue());
        check("Scenario 3: FIFO enqueue and dequeue order", fifoOrder);

        queue.enqueue("X");
        queue.enqueue("Y");
        queue.enqueueFront("FIRST");
        check("Scenario 3: enqueueFront puts item at front", "FIRST".equals(queue.peek()));
        check("Scenario 3: Dequeue returns FIRST then X", "FIRST".equals(queue.dequeue()) && "X".equals(queue.dequeue()));
    }

    private static void testScenario4() {
        CustomQueue<String> queue = new CustomQueue<>();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        boolean removed = queue.remove("B");
        check("Scenario 4: Remove middle item returned true", removed);
        check("Scenario 4: Order of rest maintained (A, C, D)",
                "A".equals(queue.dequeue())
                && "C".equals(queue.dequeue())
                && "D".equals(queue.dequeue())
                && queue.isEmpty());
    }

    private static void testScenario5() {
        CustomStack<Integer> stack = new CustomStack<>();
        for (int i = 1; i <= 20; i++) {
            stack.push(i);
        }
        check("Scenario 5: Stack size is 20 after two resizes", stack.size() == 20);
        boolean reverseOrder = true;
        for (int i = 20; i >= 1; i--) {
            if (stack.pop() != i) {
                reverseOrder = false;
                break;
            }
        }
        check("Scenario 5: Pop returns exact reverse order", reverseOrder);
        check("Scenario 5: Stack is empty after popping all", stack.isEmpty());
    }

    private static void testScenario6() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("ONLY");
        String removed = list.removeLast();
        check("Scenario 6: removeLast returns single item", "ONLY".equals(removed));
        check("Scenario 6: List is empty", list.isEmpty() && list.size() == 0);
        boolean threwException = false;
        try {
            list.removeLast();
        } catch (EmptyStructureException e) {
            threwException = true;
        }
        check("Scenario 6: Second removeLast throws EmptyStructureException", threwException);
    }

    private static class TestKey {
        private final String name;
        private final int fixedHash;

        TestKey(String name, int fixedHash) {
            this.name = name;
            this.fixedHash = fixedHash;
        }

        @Override
        public int hashCode() {
            return fixedHash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof TestKey other)) return false;
            return name.equals(other.name);
        }
    }
}
