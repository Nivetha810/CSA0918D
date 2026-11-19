# CO1 – Answers for Tasks 1–12

## Task 1 – Problem analysis
Inputs: registration, vehicle category, user/pass, entry/exit timestamps, floor layout and EV energy. Outputs: ticket ID, assigned floor/slot, occupancy and fee. Candidate classes: ParkingFacility allocates slots; ParkingSlot represents location/occupancy; Vehicle stores identity and fee behaviour; ParkingTicket stores unique ID and entry; Pass checks eligibility; FeePolicy owns rates; ParkingService coordinates entry/exit. Rules: one vehicle per slot, unique active registration/ticket IDs, valid coordinates, non-negative duration, valid student/staff pass gives free parking. Assumption: campus local time and started-hour billing. Parallel arrays require synchronized indices, search O(n), and each new attribute requires another array; objects cost more memory but encapsulate state.

## Task 2 – data representation
Registration String; floor/slot/minutes int; ticket sequence long; timestamps LocalDateTime; production money BigDecimal. Jagged String[][] supports unequal floor sizes. Scanner for console input. Integer 130/60 truncates to 2; (minutes+59)/60 gives 3 started hours. A flat array needs prefix offsets for unequal floors. Coordinate access O(1), total storage O(S).

## Task 3 – control logic
Allocate pseudocode: validate registration; for each floor and slot, if empty store registration and return coordinates; after loops return FULL. Fee pseudocode: reject negative minutes; hours ← (minutes+59) DIV 60; fee ← hours × hourlyRate; apply daily cap if defined. Allocation scan O(S) worst case, O(1) auxiliary space. Free-slot stacks make allocate/release O(1) amortized with O(S) extra references. ParkingFacility.java completes both supplied TODOs.

## Task 4 – constructors
Use validated parameterized constructors and this-constructor chaining. A copied ticket must not generate a second ticket ID; reprint the same ticket. Local references live in stack frames; objects live on heap and become GC-eligible when unreferenced. Factory methods enforce invariants; default construction followed by setters permits invalid intermediate state.

## Task 5 – static members
Ticket sequence should be static/atomic if IDs are shared across gates; if gate-scoped, use facility ID plus local counter. Static counter uses O(1) space; k instance counters use O(k). Remove active-ticket references after exit; retain audit history if required.

## Task 6 – inheritance
Vehicle abstract superclass; TwoWheeler and Car subclasses; ElectricCar extends Car and implements Chargeable. Hierarchical and multilevel inheritance. Constructor trace: Vehicle → Car → ElectricCar. Composition alternative stores a VehicleType/FeePolicy reference; new types can be configured. Fee dispatch O(1); composition adds one reference per vehicle.

## Task 7 – overriding
Subclasses override fee/rate with @Override; same signature and compatible return type; access cannot be reduced. Registration and ticket ID can be final. equals/hashCode must use the same stable registration key. A switch over k categories may inspect k branches and must be edited for new types.

## Task 8 – polymorphism
Overloads such as calculateFee(int) and calculateFee(LocalTime,LocalTime) are compile-time polymorphism. Vehicle[] holds subclasses; runtime dispatch chooses the override. FeeDemo.java completes all TODOs. For 130 minutes, billable hours = 3: two-wheeler Rs 30, car Rs 90, EV Rs 90 + Rs 120 charging = Rs 210. The last true follows equality by registration; a production system may include category in equality.

## Task 9 – abstraction
Vehicle is abstract because it has common state and constructor validation. Chargeable is an interface because it is an independent capability and permits multiple interfaces. Interface default methods can share charging workflow; static methods can validate kWh. Making Vehicle an interface loses ordinary constructors/instance fields; making Chargeable an abstract class prevents extending another class.

## Task 10 – packages and encapsulation
Suggested packages: parking.model, parking.policy, parking.service, parking.app. Fields private/final; API methods public; subclass hooks protected. Validate in constructors/setters. Integer wrappers may be null; compare values with equals/unboxing, not reference identity.

## Task 11 – implementation and tests
SmartParkingSystem.java implements the changed model. Test last slot, full lot, zero minutes, 60-minute boundary, negative duration, duplicate registration, invalid slot, missing active ticket and valid pass. Demo hand calculation: e-bike flat Rs 25; daytime delivery van two hours at double car rate = Rs 120; valid one-day pass = Rs 0. Array/ArrayList active ticket search O(n); HashMap registration lookup O(1) average and O(n) storage.

## Task 12 – Day-4 challenge
Decision: MODIFY. Retain abstract vehicle model and jagged floors; add ParkingPass and extendable fee behaviour. E-bike uses flat pricing, delivery van uses double-car daytime pricing, OneDayPass checks date. Jagged array supports five unequal floors. Future pricing should be injected as a strategy rather than a growing conditional chain.

### UML (text)
ParkingFacility 1—* ParkingSlot; ParkingService 1—* ParkingTicket; Vehicle <|-- TwoWheeler; Vehicle <|-- Car <|-- ElectricCar; Chargeable <|.. ElectricCar; ParkingPass <|.. OneDayPass.
