# CO2 – Answers for Q1–Q7

Q1: String is immutable; repeated concatenation creates intermediate strings and may copy O(n²) total characters. StringBuilder is amortized O(n) and not synchronized; StringBuffer synchronizes methods and can contend. Use StringBuilder for local ticket text and a synchronized logger for shared output. StringJoiner is useful for delimiters; String.format is expressive but generally heavier. CO2Solutions benchmarks 5, 50 and 500 fields; repeat runs and record heap data locally.

Q2: ArrayList append amortized O(1), indexed get O(1), front/middle insert/remove O(n), iteration O(n). LinkedList end operations O(1) with node reference, indexed get O(n), iteration O(n), more per-node memory. ArrayDeque add/remove at both ends amortized O(1), no indexed access. Use ArrayList for playlist browsing and ArrayDeque for Up Next.

Q3: HashSet uses hashCode to select a bucket, then equals among candidates. Equal objects must have equal hashes. The completed Student uses immutable regNo and matching hashCode. Mutating a key after insertion can make it unreachable. HashSet average O(1), TreeSet O(log n), both O(n) space.

Q4: HashMap get/put average O(1), no order. TreeMap get/put O(log n), sorted iteration O(n). LinkedHashMap average O(1), insertion-order iteration O(n). Use TreeMap for code-order reports and LinkedHashMap for insertion order. Hashtable is legacy. Sorted array has O(log n) binary search and O(n) insertion.

Q5: Leaderboard<T> accepts Comparator<? super T>, copies and sorts without changing insertion order. Comparator chain: points descending, goal difference descending, name ascending. Output order: Eagles, Bisons, Panthers, Cobras. Bisons precedes Panthers alphabetically after ties. Sorting O(n log n), copied list O(n) extra space.

Q6: PriorityQueue orders severity ascending (1 critical), then arrival sequence. Offer/poll O(log n), peek O(1), storage O(n). Printing PriorityQueue directly does not guarantee sorted iteration; poll a copy. Critical arrivals can starve minor patients; use aging or bounded waiting. Five FIFO queues give O(1) admission and next selection by checking at most five heads.

Q7: Separate playlist browsing (ArrayList) from urgent/up-next operations (ArrayDeque). Diagnose actual bottleneck using the same workload before choosing a redesign. Measure repeated averages at multiple sizes, preserve raw data, predict first, then compare. Report JDK, machine and workload. Explain trade-offs even if redesign does not win.