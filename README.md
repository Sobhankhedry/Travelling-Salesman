# 🌍 Travelling Salesman Problem

A Java implementation of the **Travelling Salesman Problem (TSP)** using **recursion and bitmasking**.

The project solves a small TSP instance represented by a distance matrix. Starting from a designated city, the algorithm explores possible routes, ensures that every city is visited exactly once, and finally returns to the starting city.

The implementation demonstrates an important technique for representing and tracking visited states using **bitmasks**.

---

# 📌 Overview

The **Travelling Salesman Problem (TSP)** is a classic combinatorial optimization problem.

Given a set of cities and the travel cost between every pair of cities, the objective is to find the minimum-cost tour that:

1. Starts from a city.
2. Visits every other city exactly once.
3. Returns to the starting city.
4. Minimizes the total travel cost.

For example:

```text
        ┌───────┐
        │ City 0│
        └───────┘
         /     \
        /       \
    City 1 ─── City 2
       \         /
        \       /
         City 3
```

The project represents the cities using a distance matrix and searches through the possible combinations recursively.

---

# 🎯 Problem Definition

The project uses:

```java
static private int n = 4;
```

which means the problem contains:

```text
4 Cities
```

The distance matrix is:

```java
static int[][] d = {
    {0, 10, 15, 20},
    {5,  0,  9, 10},
    {6, 13,  0, 12},
    {8,  8,  9,  0}
};
```

Each element:

```text
d[i][j]
```

represents the cost of traveling from city `i` to city `j`.

For example:

```text
d[0][1] = 10
```

means the cost of traveling from:

```text
City 0 → City 1
```

is:

```text
10
```

---

# 🗺️ Distance Matrix

The current problem can be represented as:

| From / To  | City 0 | City 1 | City 2 | City 3 |
| ---------- | -----: | -----: | -----: | -----: |
| **City 0** |      0 |     10 |     15 |     20 |
| **City 1** |      5 |      0 |      9 |     10 |
| **City 2** |      6 |     13 |      0 |     12 |
| **City 3** |      8 |      8 |      9 |      0 |

The matrix is **directed**, meaning:

```text
d[i][j]
```

does not necessarily equal:

```text
d[j][i]
```

For example:

```text
City 0 → City 1 = 10
City 1 → City 0 = 5
```

Therefore, the implementation allows different travel costs in opposite directions.

---

# 🧠 Core Approach

The main algorithm is implemented in:

```java
public static int TSP(int mask, int pos)
```

The two parameters represent the current state:

| Parameter | Meaning                                      |
| --------- | -------------------------------------------- |
| `mask`    | Set of cities that have already been visited |
| `pos`     | Current city                                 |

The state can therefore be described as:

```text
TSP(mask, pos)
```

meaning:

> Find the minimum additional cost required to complete the tour when the currently visited cities are represented by `mask` and the salesman is currently at `pos`.

---

# 🔢 Bitmask Representation

One of the most important concepts in this project is **bitmasking**.

With:

```text
n = 4
```

there are four cities:

```text
City 0
City 1
City 2
City 3
```

Each city corresponds to one bit:

```text
Bit 0 → City 0
Bit 1 → City 1
Bit 2 → City 2
Bit 3 → City 3
```

A binary mask can therefore represent which cities have already been visited.

For example:

```text
0001
```

means:

```text
City 0 → visited
City 1 → not visited
City 2 → not visited
City 3 → not visited
```

While:

```text
1111
```

means:

```text
All cities have been visited
```

---

# 🏁 Initial State

The program starts with:

```java
TSP(1, 0)
```

The initial mask is:

```text
0001
```

which means:

```text
City 0 has been visited
```

and:

```text
pos = 0
```

means the salesman currently starts at:

```text
City 0
```

So the initial state is:

```text
Visited Cities: {0}
Current City:   0
```

---

# ✅ Detecting All Visited Cities

The project defines:

```java
static int ALL_VISITED = (1 << n) - 1;
```

For:

```text
n = 4
```

this becomes:

```text
(1 << 4) - 1
```

which is:

```text
10000 - 1
```

or:

```text
1111
```

Therefore:

```java
if(mask == ALL_VISITED)
```

checks whether all four cities have been visited.

---

# 🔄 Base Case

Once every city has been visited:

```java
if(mask == ALL_VISITED)
```

the algorithm does not stop immediately.

Instead, it returns:

```java
d[pos][0]
```

This is important because the TSP requires the salesman to return to the starting city.

Conceptually:

```text
All Cities Visited
        │
        ▼
Current City
        │
        ▼
Return to City 0
        │
        ▼
Add Return Cost
```

Therefore, the final leg of the route is included in the total cost.

---

# 🔎 Exploring Unvisited Cities

The algorithm loops through every city:

```java
for (int city = 0; city < n; city++)
```

For each city, it checks whether that city has already been visited.

The condition is:

```java
if ((mask & (1 << city)) == 0)
```

If the result is zero, that city has not yet been visited.

---

# ➕ Updating the Bitmask

When moving to an unvisited city, the project creates a new mask:

```java
mask | (1 << city)
```

For example, suppose:

```text
Current mask = 0001
```

and the algorithm wants to visit:

```text
City 1
```

The corresponding bit is:

```text
0010
```

Applying OR:

```text
0001
  OR
0010
----
0011
```

The new mask becomes:

```text
0011
```

which means:

```text
City 0 → visited
City 1 → visited
City 2 → not visited
City 3 → not visited
```

---

# 💰 Calculating Route Cost

For every possible unvisited city, the algorithm calculates:

```java
int newResult =
    TSP(mask | (1 << city), city)
    + d[pos][city];
```

This consists of two parts:

```text
Cost of moving to the next city
              +
Minimum cost of completing the remaining tour
```

or:

```text
d[pos][city]
        +
TSP(newMask, city)
```

The algorithm then keeps the smallest result.

---

# 🔽 Selecting the Minimum

The current best answer is initialized with:

```java
int ans = Integer.MAX_VALUE;
```

Then each possible route is evaluated:

```java
ans = Math.min(ans, newResult);
```

Conceptually:

```text
              Current City
                   │
       ┌───────────┼───────────┐
       ▼           ▼           ▼
    City A       City B      City C
       │           │           │
       ▼           ▼           ▼
   Route Cost   Route Cost   Route Cost
       │           │           │
       └───────────┼───────────┘
                   ▼
              Minimum Cost
```

This exhaustive recursive search guarantees the minimum for the given finite problem instance.

---

# 🔄 Complete Algorithm

The algorithm can be summarized as:

```text
                    Start
                      │
                      ▼
              TSP(1, 0)
                      │
                      ▼
              Check visited mask
                      │
             All cities visited?
                /           \
              Yes            No
               │              │
               ▼              ▼
        Return to City 0   Find unvisited
                           cities
                               │
                               ▼
                    For every possible city
                               │
                               ▼
                      Update visited mask
                               │
                               ▼
                       Recursive TSP call
                               │
                               ▼
                     Add travel cost
                               │
                               ▼
                       Keep minimum
                               │
                               ▼
                         Return result
```

---

# 🧮 Example Route

For the current four-city matrix, one possible tour is:

```text
0 → 1 → 2 → 3 → 0
```

Its cost is:

```text
0 → 1 = 10
1 → 2 = 9
2 → 3 = 12
3 → 0 = 8
```

Therefore:

```text
10 + 9 + 12 + 8 = 39
```

Another possible tour is:

```text
0 → 1 → 3 → 2 → 0
```

with cost:

```text
10 + 10 + 9 + 6 = 35
```

The algorithm evaluates the possible tours and returns the minimum total cost.

For the provided distance matrix, the optimal tour cost is:

```text
35
```

---

# 💻 Core Implementation

The central method is:

```java
public static int TSP(int mask, int pos) {

    if (mask == ALL_VISITED) {
        return d[pos][0];
    }

    int ans = Integer.MAX_VALUE;

    for (int city = 0; city < n; city++) {

        if ((mask & (1 << city)) == 0) {

            int newResult =
                TSP(mask | (1 << city), city)
                + d[pos][city];

            ans = Math.min(ans, newResult);
        }
    }

    return ans;
}
```

This compact implementation contains the essential TSP logic:

* State representation
* Base case
* Visited-city checking
* Bitmask update
* Recursive exploration
* Cost accumulation
* Minimum selection

---

# 🧩 Main Method

The program starts the calculation with:

```java
public static void main(String[] args) {
    System.out.println(TSP(1, 0));
}
```

This means:

```text
Starting city = 0
Visited cities = {0}
```

The final result is printed directly to the console.

---

# 📚 Concepts Demonstrated

This project is particularly useful for practicing algorithmic problem solving.

### Java Concepts

* Static methods
* Two-dimensional arrays
* Integer arithmetic
* Bitwise operators
* Recursion
* Loops
* Conditional statements
* `Integer.MAX_VALUE`
* Console output

### Algorithm Concepts

* Travelling Salesman Problem
* Combinatorial optimization
* Recursive search
* Exhaustive search
* State representation
* Bitmasking
* Minimum-cost path selection
* Dynamic-programming-style state formulation

---

# 🧠 Why Bitmasking?

A straightforward way of representing visited cities would be an array:

```text
visited[0]
visited[1]
visited[2]
visited[3]
```

However, bitmasking compresses the same information into a single integer.

For four cities:

```text
0000
```

can represent:

```text
No city visited
```

while:

```text
1111
```

represents:

```text
All cities visited
```

This makes state transitions efficient:

```java
mask | (1 << city)
```

and membership checks simple:

```java
(mask & (1 << city)) == 0
```

Bitmasking is a common technique in combinatorial optimization and subset-based dynamic programming problems.

---

# ⏱️ Complexity

The current implementation explores possible city orders recursively.

For `n` cities, the number of possible permutations is approximately:

```text
(n - 1)!
```

when fixing the starting city.

The recursive bitmask formulation has a state space of approximately:

```text
O(n × 2^n)
```

possible `(mask, pos)` states.

However, the current implementation **does not memoize these states**.

Therefore, the same states can be recomputed multiple times, making the actual recursive implementation exponential and potentially closer to factorial behavior in terms of the explored routes.

A standard memoized bitmask-DP implementation would reduce the time complexity to approximately:

```text
O(n² × 2^n)
```

with:

```text
O(n × 2^n)
```

memory.

The current implementation should therefore be understood as a **recursive bitmask search**, not a fully memoized TSP dynamic-programming implementation.

---

# ⚠️ Implementation Notes

The current project is intentionally small and educational.

A few characteristics of the implementation are worth noting:

### Fixed Number of Cities

The program currently uses:

```java
static private int n = 4;
```

and a fixed:

```java
4 × 4
```

distance matrix.

---

### No Memoization

Although the state is naturally represented by:

```text
(mask, pos)
```

the current implementation does not maintain a memoization table.

Consequently, identical states can be calculated repeatedly.

---

### No Route Reconstruction

The current implementation returns:

```text
Minimum Total Cost
```

but does not store the actual sequence of cities that produced that cost.

A future implementation could maintain a `parent` or `nextCity` structure to reconstruct the optimal tour.

---

# 🔧 Possible Improvements

The project can be significantly extended while preserving the current algorithmic idea.

### Algorithm

* [ ] Add memoization for `(mask, pos)` states
* [ ] Implement standard bitmask Dynamic Programming
* [ ] Compare recursive and memoized versions
* [ ] Add branch-and-bound optimization
* [ ] Compare with brute-force permutation search

### Input

* [ ] Allow the number of cities to be entered dynamically
* [ ] Accept the distance matrix from user input
* [ ] Load distance data from a file
* [ ] Validate the input matrix

### Route Reconstruction

* [ ] Store the selected next city
* [ ] Reconstruct the optimal route
* [ ] Print the complete tour
* [ ] Print the total distance alongside the route

For example:

```text
Optimal Route:
0 → 1 → 3 → 2 → 0

Minimum Cost:
35
```

### Code Quality

* [ ] Replace single-letter variables such as `d`, `r`, and `n`
* [ ] Encapsulate TSP logic inside a dedicated class
* [ ] Replace global/static state where appropriate
* [ ] Add unit tests
* [ ] Add edge-case handling

---

# 🧪 Suggested Test Cases

The algorithm can be tested with different distance matrices.

### Small Graph

```text
2 × 2
```

Useful for verifying the base logic.

### Three Cities

```text
3 × 3
```

Useful for manually checking all possible tours.

### Four Cities

The current implementation:

```text
4 × 4
```

provides a simple example where the recursive search explores multiple possible routes.

### Larger Instances

Increasing the number of cities can demonstrate the rapid growth of the search space.

This also makes the performance difference between:

```text
Recursive Search
```

and:

```text
Memoized Bitmask DP
```

much more visible.

---

# 📁 Project Structure

The repository currently has a compact IntelliJ/Java structure:

```text
Travelling-Salesman/
│
├── .idea/
│
├── src/
│   └── Main.java
│
├── TSP.iml
│
└── README.md
```

The complete implementation is currently contained in:

```text
src/Main.java
```

The repository currently contains **3 commits**.

---

# 🚀 Getting Started

## Prerequisites

You need:

* Java JDK
* IntelliJ IDEA or another Java IDE

---

## Clone the Repository

```bash
git clone https://github.com/Sobhankhedry/Travelling-Salesman.git
```

Navigate into the project:

```bash
cd Travelling-Salesman
```

Open the project in IntelliJ IDEA.

Run:

```text
src/Main.java
```

The program prints the minimum tour cost to the console.

---

# 🎯 Learning Objectives

The main objectives of this project are:

* Understanding the Travelling Salesman Problem
* Practicing recursive search
* Learning bitmask representation
* Representing visited subsets efficiently
* Working with distance matrices
* Exploring combinatorial optimization
* Understanding state-based recursion
* Calculating minimum-cost tours
* Understanding why memoization is important for exponential problems

---

# 📌 Project Status

**Status:** Educational / Algorithmic Project

This repository contains a compact implementation of the Travelling Salesman Problem using:

```text
Recursion
+
Bitmasking
+
Minimum-Cost Search
```

It is designed primarily for learning and experimenting with **TSP state representation and recursive optimization**, rather than being a production-grade route-optimization system.

---

# 👨‍💻 Author

**Sobhan Khedry**

Computer Engineering Graduate Student
Backend Development Enthusiast

GitHub: [@Sobhankhedry](https://github.com/Sobhankhedry)

---

# ⭐ Key Takeaways

The project demonstrates how the TSP can be represented using two pieces of information:

```text
(mask, currentCity)
```

where:

```text
mask
 ↓
Which cities have been visited
```

and:

```text
currentCity
 ↓
Where the salesman currently is
```

The recursive process is:

```text
                  Start
                    │
                    ▼
              Current State
             (mask, position)
                    │
                    ▼
             All Cities Visited?
                /          \
              Yes           No
               │             │
               ▼             ▼
        Return to City 0   Find Unvisited
                              Cities
                                │
                     ┌──────────┼──────────┐
                     ▼          ▼          ▼
                   City A     City B     City C
                     │          │          │
                     ▼          ▼          ▼
                 Recursive   Recursive   Recursive
                   Search      Search      Search
                     │          │          │
                     └──────────┼──────────┘
                                ▼
                         Choose Minimum
                                │
                                ▼
                           Final Cost
```

For the current four-city example, the algorithm finds a minimum tour cost of:

```text
35
```

The project is a useful demonstration of how **bitmasking and recursion can be combined to solve a classic combinatorial optimization problem**.
