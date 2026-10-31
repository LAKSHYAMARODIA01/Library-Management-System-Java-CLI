# 📚 Library Management System (Java CLI)
### 🎓 BIT Mesra, Lalpur — Master of Computer Applications (MCA) | Semester 1
> **Data Structures (DS) End Semester Project**  
> *A High-Performance, Interactive Console Library System Built Entirely from Scratch Using Custom Generic Data Structures (Zero `java.util` Collections / Streams).*

---

## 🏛 Academic Project Context
- **Institution**: Birla Institute of Technology (BIT) Mesra, Lalpur Center, Ranchi
- **Program**: Master of Computer Applications (MCA) — Semester 1
- **Course / Subject**: Data Structures (DS) End Semester Project
- **Developer / Student**: Lakshya Marodia ([@LAKSHYAMARODIA01](https://github.com/LAKSHYAMARODIA01))
- **Objective**: To demonstrate practical, deep mastery of fundamental data structures by designing, engineering, and profiling a complete, production-grade Library Management System without relying on standard Java collection libraries (`ArrayList`, `HashMap`, `LinkedList`, etc.) or streams.

---

## 🌟 Table of Contents
1. [Project Overview](#-project-overview)
2. [Why Build from Scratch?](#-why-build-from-scratch)
3. [Tech Stack](#-tech-stack)
4. [DSA to Real-World Feature Mapping](#-dsa-to-real-world-feature-mapping)
5. [Architecture & System Flow](#-architecture--system-flow)
6. [Directory & File Breakdown](#-directory--file-breakdown)
7. [Data Structures Implementation Deep-Dive](#-data-structures-implementation-deep-dive)
8. [Business Rules & Circulation Logic](#-business-rules--circulation-logic)
9. [How to Run and Use](#-how-to-run-and-use)
10. [Terminal UI Previews & Output Visuals](#-terminal-ui-previews--output-visuals)
11. [Automated Testing & Verification](#-automated-testing--verification)

---

## 📖 Project Overview

The **Library Management System (LMS)** is an end-to-end, menu-driven CLI application engineered in pure Java for the MCA Semester 1 Data Structures curriculum at **BIT Mesra Lalpur**. 

Designed for librarians, this system models the complete circulation lifecycle of a modern library:
- **Catalog Management**: Adding books, stock tracking, case-insensitive keyword search, and $O(1)$ ISBN/ID lookups.
- **Member Management**: Registration, profile lookup, contact management, and active loan tracking.
- **Circulation & Lending**: Book checkout/return with automatic 14-day due dates and overdue fine calculation (Rs 2/day).
- **Fair Reservation Waitlist**: FIFO-based waitlist for out-of-stock titles preventing starvation.
- **Transactional Undo Engine**: Instant, stack-driven rollback for operations (`ADD_BOOK`, `ISSUE`, `RETURN`, `RESERVE`).
- **DSA Profiler & Live Insights**: Real-time console visualization of hash table buckets, load factor, collisions, and stack/queue internals.
- **Data Persistence**: Flat CSV file storage to seamlessly save and restore system state across sessions.

---

## 💡 Why Build from Scratch?

In standard enterprise development, developers routinely utilize library collections (`ArrayList`, `HashMap`, `LinkedList`, etc.). While convenient, this abstracts away critical low-level concepts:
1. Memory allocation and pointer updates.
2. Amortized doubling and table resizing.
3. Hash distribution, bitwise indexing, and collision resolution.
4. LIFO/FIFO scheduling fairness and rollback mechanics.

For this **BIT Mesra DS End Sem Project**, standard collection libraries and streams were strictly **forbidden in business logic**. Every core structure is hand-coded as a reusable generic class in `com.library.ds`:
- **Doubly Linked List** (`CustomLinkedList<T>`) with dual head & tail pointers.
- **Array-Backed Stack** (`CustomStack<T>`) with dynamic geometric doubling.
- **Singly Linked Queue** (`CustomQueue<T>`) with rear enqueue and front priority re-insertion.
- **Separate-Chaining Hash Table** (`CustomHashMap<K, V>`) with polynomial hash distribution and dynamic re-hashing.

---

## 🛠 Tech Stack

| Technology | Specification | Details |
| :--- | :--- | :--- |
| **Language** | Java 17+ (Java 21 LTS Recommended) | Modern, clean object-oriented syntax |
| **Build Tooling** | Native `javac` | Single module, built with standard compiler flags (`-Xlint:all`) |
| **Dependencies** | **0 External Dependencies** | No Maven, Gradle, Guava, Apache Commons, or JUnit |
| **Persistence** | Flat CSV Files | Custom comma-delimited parser & serializer with string escaping |
| **Interface** | Interactive Console (CLI) | Formatted ASCII tables, status indicators (`[OK]`, `[ERROR]`, `[INFO]`) |
| **Curriculum Target** | MCA Sem 1 Data Structures | Birla Institute of Technology, Mesra (Lalpur Center) |

---

## 📊 DSA to Real-World Feature Mapping

| Data Structure | Implementation Details | Where It Is Used | Why |
| :--- | :--- | :--- | :--- |
| **Hash Table** | Separate chaining over bucket chains of `CustomLinkedList<Entry<K,V>>`, polynomial string hashing ($h = h \times 31 + ch$), dynamic resizing on load factor $> 0.75$ | `booksById`, `booksByIsbn`, `membersById` | $O(1)$ average-time lookup for books and members by primary key and unique ISBN |
| **Doubly Linked List** | Bidirectional node links with both `head` and `tail` pointers | Catalog insertion order, member active loans, collision bucket chains, transaction audit history | $O(1)$ insert and removal at both ends, stable sequential traversal, cheap deletion |
| **Queue** | Singly-linked list with front and rear pointers, including `enqueueFront` | Book reservation waitlists per book | First-Come-First-Served (FIFO) fairness for book waitlists; front re-insertion on undo |
| **Stack** | Resizable array-backed LIFO stack with geometric doubling | Undo history stack | Last-In-First-Out (LIFO) reversal of operations with atomic rollback |

---

## 🏗 Architecture & System Flow

The codebase follows a strict **Layered Architecture**:

```
 ┌─────────────────────────────────────────────────────────┐
 │                   User / Terminal                       │
 └────────────────────────────┬────────────────────────────┘
                              │
 ┌────────────────────────────▼────────────────────────────┐
 │                     UI Layer (com.library.ui)           │
 │  - MainMenu, BookMenu, MemberMenu, CirculationMenu      │
 │  - ReservationMenu, ReportMenu, InsightsMenu, AdminMenu │
 │  - ConsoleIO (Validation & EOF Handling), Format        │
 └────────────────────────────┬────────────────────────────┘
                              │
 ┌────────────────────────────▼────────────────────────────┐
 │               Service Layer (com.library.service)       │
 │  - BookService, MemberService, CirculationService       │
 │  - ReservationService, UndoService, ReportService       │
 │  - ClockService, LibraryContext (State Store)           │
 └─────────────┬─────────────────────────────┬─────────────┘
               │                             │
 ┌─────────────▼───────────────┐ ┌───────────▼─────────────┐
 │ Model (com.library.model)   │ │ Storage (persistence)   │
 │ - Book, Member, Loan        │ │ - CsvStorage            │
 │ - Transaction, Action       │ │ - CsvHelper             │
 └─────────────┬───────────────┘ └─────────────────────────┘
               │
 ┌─────────────▼───────────────────────────────────────────┐
 │       Custom Data Structures (com.library.ds)           │
 │  - CustomLinkedList, CustomStack, CustomQueue           │
 │  - CustomHashMap, HashEntry, HashStats                  │
 └─────────────────────────────────────────────────────────┘
```

---

## 📂 Directory & File Breakdown

```
LibraryManagementSystem/
├── .gitignore                          # Git exclusion file (build artifacts, data)
├── README.md                           # Master documentation
├── data/                               # Persistent CSV storage directory
│   ├── books.csv                       # Serialized book catalog
│   ├── loans.csv                       # Active loan records
│   ├── members.csv                     # Registered library patrons
│   ├── reservations.csv               # Active FIFO waitlists
│   └── transactions.csv                # Chronological audit log
├── src/
│   └── com/library/
│       ├── Main.java                   # CLI Entry Point & Global Exception Handler
│       ├── ds/                         # Custom Data Structures (Zero java.util)
│       │   ├── CustomLinkedList.java   # Doubly linked list (Head/Tail, O(1) ends)
│       │   ├── CustomStack.java        # Resizable array stack (Initial cap: 8, doubling)
│       │   ├── CustomQueue.java        # Singly linked FIFO queue with enqueueFront
│       │   ├── CustomHashMap.java      # Separate-chaining hash map (Threshold 0.75)
│       │   ├── HashEntry.java          # Key-value node wrapper
│       │   ├── HashStats.java          # Metrics snapshot (Load Factor, Chains, Resizes)
│       │   └── EmptyStructureException.java # Unchecked exception for empty operations
│       ├── model/                      # Immutable & Validated Entities
│       │   ├── Book.java               # Book entity with copy counters & waitlist queue
│       │   ├── Member.java             # Member entity with active loans linked list
│       │   ├── Loan.java               # Active loan record with due-date logic
│       │   ├── Transaction.java        # Audit record for financial/circulation actions
│       │   ├── TransactionType.java    # Enum: ADD_BOOK, ISSUE, RETURN, RESERVE, etc.
│       │   └── Action.java             # Reversible command record for undo stack
│       ├── service/                    # Business Logic Layer
│       │   ├── LibraryContext.java     # Centralized state container (no globals)
│       │   ├── BookService.java        # Inventory, validation, search algorithms
│       │   ├── MemberService.java      # Patron registration & constraint checks
│       │   ├── CirculationService.java # Borrowing, returning, fine assessment
│       │   ├── ReservationService.java # Fair FIFO reservation waitlists
│       │   ├── UndoService.java        # Stack-driven rollback engine
│       │   ├── ReportService.java      # History filters & linear-scan analytics
│       │   ├── ClockService.java       # Simulation clock for testing overdue fines
│       │   ├── ReturnResult.java       # DTO for return operations (fines, next in queue)
│       │   └── OverdueRecord.java      # DTO for overdue loan reporting
│       ├── ui/                         # Presentation Layer
│       │   ├── ConsoleIO.java          # Robust input scanner with EOF crash-proofing
│       │   ├── Format.java             # Table alignment, padding, and currency helper
│       │   ├── MainMenu.java           # Top-level interactive navigation dashboard
│       │   ├── BookMenu.java           # Sub-menu for catalog operations
│       │   ├── MemberMenu.java         # Sub-menu for member management
│       │   ├── CirculationMenu.java    # Sub-menu for issue, return, overdue list
│       │   ├── ReservationMenu.java    # Sub-menu for waitlist management
│       │   ├── ReportMenu.java         # Sub-menu for audit logs & top borrows
│       │   ├── InsightsMenu.java       # Sub-menu for live DSA memory inspections
│       │   └── AdminMenu.java          # Sub-menu for clock, demo data, CSV persistence
│       ├── persistence/                # Flat File Storage Layer
│       │   ├── CsvStorage.java         # Atomic save/load with state reconstruction
│       │   └── CsvHelper.java          # String escaping and tokenization
│       ├── util/                       # Utilities and Constants
│       │   ├── Constants.java          # Loan periods (14 days), fine rates (Rs 2/day)
│       │   ├── Validator.java          # Strict regex-free validations (email, phone, etc.)
│       │   ├── IdGenerator.java        # Sequential zero-padded IDs (B001, M001, T0001)
│       │   ├── LibraryException.java   # Application-wide domain exception
│       │   └── DemoData.java           # Seed generator (8 books, 4 members, 2 loans)
│       └── test/                       # Autonomous Test Suites
│           ├── DsaSelfTest.java        # Scenarios 1-6 verifying data structures
│           └── ServiceSelfTest.java    # Scenarios 7-14 verifying business logic & CSV
└── tests/                              # Piped Input Automation Scripts
    ├── input_basic.txt                 # Full end-to-end user workflow
    └── input_edge.txt                  # Boundary conditions & error-handling checks
```

---

## 🧠 Data Structures Implementation Deep-Dive

### 1. `CustomHashMap<K, V>` (Hash Table with Separate Chaining)
- **Buckets**: Internal array of `CustomLinkedList<HashEntry<K, V>>`, initial capacity 16.
- **Hash Function**: Dedicated polynomial rolling hash for string keys:
  $$h = \sum_{i=0}^{n-1} s[i] \times 31^{n-1-i}$$
  Index mapped via bitwise masking: `(h & 0x7fffffff) % capacity`.
- **Dynamic Resizing**: Automatically doubles capacity whenever load factor:
  $$\alpha = \frac{\text{size}}{\text{capacity}} > 0.75$$
- **Rehashing**: Every stored entry is re-indexed into the newly sized bucket array.

### 2. `CustomLinkedList<T>` (Doubly Linked List)
- **Node Structure**: Stores `data`, `prev`, and `next` references.
- **Pointers**: Maintains both `head` and `tail` pointers.
- **Performance**:
  - `addFirst(T)` & `addLast(T)`: $O(1)$ constant time.
  - `removeFirst()` & `removeLast()`: $O(1)$ constant time.
  - Traversal & Index lookup: Bidirectional optimization ($O(n/2)$ average).

### 3. `CustomQueue<T>` (Singly Linked FIFO Queue)
- **Fair Waitlists**: Maintains `front` and `rear` pointers for $O(1)$ `enqueue` and `dequeue`.
- **Special Feature (`enqueueFront`)**: When an undo operation reverses a book issue that had dequeued a waiting patron, `enqueueFront(T)` restores that patron to the **front** of the waitlist without losing their priority.

### 4. `CustomStack<T>` (Array-Backed LIFO Stack)
- **Growth Strategy**: Starts at capacity 8 and dynamically doubles on demand.
- **Transactional Rollback**: Stores `Action` records to enable safe, step-by-step reversals of state mutations.

---

## ⚖ Business Rules & Circulation Logic

| ID | Rule Name | Description |
| :--- | :--- | :--- |
| **BR-1** | Loan Duration | Standard loan duration is **14 days** from date of issue. |
| **BR-2** | Overdue Fines | Accrues at **Rs 2 per day** overdue; assessed at book return. |
| **BR-3** | Borrowing Limit | A member can hold a maximum of **3 active loans** simultaneously. |
| **BR-4** | Duplicate Borrowing | A member cannot borrow two copies of the same book at the same time. |
| **BR-5** | Stock Availability | A book can only be issued if `availableCopies > 0`. |
| **BR-6** | Queue Priority | If a book has reservations, only the member at the **front** of the queue may borrow it. |
| **BR-7** | Reservation Eligibility | A member can reserve only if copies are 0, they do not hold it, and are not already queued. |
| **BR-8** | Safe Book Removal | A book cannot be deleted while copies are on loan or reservations exist. |
| **BR-9** | Safe Member Removal | A member cannot be removed with active loans or pending reservations. |
| **BR-10**| Unique ISBN | Every book must possess a unique ISBN across the catalog. |
| **BR-11**| Manual Queue Notification | Returning a reserved book notifies who is next without auto-issuing. |

---

## 🚀 How to Run and Use

### Prerequisites
Make sure **Java 17 or higher** is installed:
```bash
java -version
javac -version
```

### 1. Compile the Source Code

**Windows (PowerShell):**
```powershell
Get-ChildItem -Recurse -Filter "*.java" src | Resolve-Path -Relative | ForEach-Object { '"' + $_.Replace('\', '/') + '"' } | Set-Content -Encoding ascii sources.txt
javac -Xlint:all -d out '@sources.txt'
```

**Windows (Command Prompt):**
```cmd
dir /s /b src\*.java > sources.txt
javac -Xlint:all -d out @sources.txt
```

**macOS / Linux:**
```bash
find src -name "*.java" > sources.txt
javac -Xlint:all -d out @sources.txt
```

---

### 2. Launch the Application
Run the main program:
```bash
java -cp out com.library.Main
```

---

### 3. Step-by-Step User Guide (Recommended First Run)
1. **Load Sample Data**:
   - Type `8` (Admin) $\rightarrow$ `3` (Load Demo Data).
   - Preloads 8 computer science books, 4 members, and 2 active loans.
2. **View Catalog**:
   - Type `1` (Book Management) $\rightarrow$ `8` (List All Books).
3. **Issue a Book**:
   - Type `3` (Circulation) $\rightarrow$ `1` (Issue Book).
   - Enter Book ID (e.g., `B001`) and Member ID (e.g., `M003`).
4. **Advance Date & Test Fines**:
   - Type `8` (Admin) $\rightarrow$ `2` (Advance Simulation Date) $\rightarrow$ enter `19` days.
   - Return book `B001` via `3` $\rightarrow$ `2`. The system computes 5 days late $\times$ Rs 2 = **Rs 10 fine**.
5. **Undo an Operation**:
   - Type `5` (Undo Last Action) $\rightarrow$ type `y`. The fine is refunded and the loan is restored!
6. **Inspect Data Structures**:
   - Type `7` (DSA Insights) $\rightarrow$ view live bucket distributions, load factor, and stack snapshots.

---

## 🖥 Terminal UI Previews & Output Visuals

### Main Dashboard & Header
```text
==============================================
        LIBRARY MANAGEMENT SYSTEM
   Date: 2026-10-26     Undo available: 4
==============================================
 1. Book Management
 2. Member Management
 3. Circulation (Issue / Return)
 4. Reservations
 5. Undo Last Action
 6. Reports and History
 7. DSA Insights
 8. Admin
 0. Exit
Enter choice: 
```

### Formatted Catalog Listing (Doubly Linked List Traversal)
```text
ID     Title                       Author              ISBN            Avail   Total   Reserved 
------------------------------------------------------------------------------------------------
B001   Clean Code                  Robert C. Martin    978-0132350884  2       3       0        
B002   Introduction to Algorithms  Thomas H. Cormen    978-0262033848  2       2       0        
B003   Effective Java              Joshua Bloch        978-0134685991  3       4       0        
B004   Design Patterns             Erich Gamma         978-0201633610  2       2       0        
B005   SICP                        Harold Abelson      978-0262510875  0       1       1        
B006   AI: A Modern Approach       Stuart Russell      978-0136042594  3       3       0        
B007   Operating System Concepts   Abraham Silberschatz978-1118063330  2       2       0        
B008   Computer Networking         James Kurose        978-0133594140  3       3       0        
```

### Hash Table Live Statistics & Collision Bucket Dump
```text
=== CustomHashMap Statistics (Books by ID) ===
  Capacity:           16
  Size (Entries):     8
  Load Factor:        0.50 (Threshold: 0.75)
  Non-Empty Buckets:  8
  Longest Chain:      1
  Resize Count:       0

=== Bucket Chain Dump (Capacity: 16) ===
  Bucket [ 0]: B002
  Bucket [ 1]: B003
  Bucket [ 2]: B004
  Bucket [ 3]: B005
  Bucket [ 4]: B006
  Bucket [ 5]: B007
  Bucket [ 6]: B008
  Bucket [15]: B001
```

### Undo Stack Snapshot (LIFO Reversal)
```text
=== Undo Stack (Top to Bottom, Size: 3) ===
  1. Action: RESERVE (Book: B005, Member: M004, Tx: T0006)
  2. Action: ISSUE (Book: B005, Member: M003, Tx: T0005)
  3. Action: ADD_BOOK (Book: B009, Tx: T0003)
```

---

## 🧪 Automated Testing & Verification

### 1. Data Structures Unit Tests (`DsaSelfTest`)
Verifies scenarios 1 to 6 (large inserts, resize triggers, collisions, FIFO order, middle removals, and stack reversal):
```bash
java -cp out com.library.test.DsaSelfTest
```
**Output:**
```text
=== Starting DSA Self-Tests ===
PASS: Scenario 1: Insert 200 items, all retrieved correctly
PASS: Scenario 1: Resizes occurred (resizeCount > 0)
PASS: Scenario 1: Load factor stays <= 0.75
PASS: Scenario 2: Both colliding keys present
PASS: Scenario 2: Correct values retrieved
PASS: Scenario 2: Bucket chain length is at least 2
PASS: Scenario 2: Remove key1 returns Val1
PASS: Scenario 2: Key1 gone, Key2 still present
PASS: Scenario 3: FIFO enqueue and dequeue order
PASS: Scenario 3: enqueueFront puts item at front
PASS: Scenario 3: Dequeue returns FIRST then X
PASS: Scenario 4: Remove middle item returned true
PASS: Scenario 4: Order of rest maintained (A, C, D)
PASS: Scenario 5: Stack size is 20 after two resizes
PASS: Scenario 5: Pop returns exact reverse order
PASS: Scenario 5: Stack is empty after popping all
PASS: Scenario 6: removeLast returns single item
PASS: Scenario 6: List is empty
PASS: Scenario 6: Second removeLast throws EmptyStructureException
--------------------------------
DSA Self-Test Summary: 19 / 19 checks passed.
ALL DSA TESTS PASSED SUCCESSFULLY.
```

### 2. Business Logic & Persistence Tests (`ServiceSelfTest`)
Verifies scenarios 7 to 14 (lending limits, queue reservations, overdue calculation, undo states, and CSV round-trips):
```bash
java -cp out com.library.test.ServiceSelfTest
```
**Output:**
```text
=== Starting Service Self-Tests ===
PASS: Scenario 7: Issue and return on time, fine is 0
PASS: Scenario 7: Available copies restored
PASS: Scenario 7: Member loans cleared
PASS: Scenario 8: Return 5 days late yields Rs 10 fine
PASS: Scenario 8: Member fines collected updated
PASS: Scenario 9: 4th loan rejected at limit of 3
PASS: Scenario 10: Member B reserved at position 1
PASS: Scenario 10: Member C rejected when B is at front of queue
PASS: Scenario 10: Member B successfully issued book
PASS: Scenario 11: Queue empty after issue to B
PASS: Scenario 11: Undo of issue restores B to front of queue
PASS: Scenario 11: Available copy restored to 1
PASS: Scenario 11: Member B loan removed
PASS: Scenario 12: Fine recorded prior to undo
PASS: Scenario 12: Undo of return restores loan
PASS: Scenario 12: Restored loan has same due date
PASS: Scenario 12: Available copy decreased back to 0
PASS: Scenario 12: Fine deducted back to 0
PASS: Scenario 13: Remove book with active loan rejected (BR-8)
PASS: Scenario 14: CSV round-trip book restored
PASS: Scenario 14: CSV round-trip member restored
PASS: Scenario 14: CSV round-trip loan restored
PASS: Scenario 14: New book ID increments past loaded
PASS: Scenario 14: Loading missing directory fails cleanly
------------------------------------
Service Self-Test Summary: 24 / 24 checks passed.
ALL SERVICE TESTS PASSED SUCCESSFULLY.
```

---

## 👨‍💻 Author & Attribution
- **Lakshya Marodia**
- **Course**: MCA (Semester 1)
- **Department**: Department of Computer Science & Applications
- **College**: Birla Institute of Technology (BIT), Mesra, Lalpur Extension Center
- **GitHub**: [@LAKSHYAMARODIA01](https://github.com/LAKSHYAMARODIA01)

---

## 📄 License
This project is submitted for educational and academic evaluation as part of the MCA curriculum at **BIT Mesra Lalpur**. Licensed under the **MIT License**.
