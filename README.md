# Overview 

Made using java 11.0.2

#### Dependencies
* deuceAgent-1.3.0.jar
* hamcrest-all-1.3.jar
* jcommon-1.0.23.jar
* jfreechart-1.0.19.jar
* junit-4.10.jar

## compiling

1) Navigate to src folder

2) run `javac -cp .:../lib/junit-4.10.jar:../lib/deuceAgent-1.3.0.jar:../lib/jfreechart-1.0.19.jar:../lib/jcommon-1.0.23.jar *.java` 

## Generate Throughput Graph

1) Navigate to compiled files 

2) run (from src) `java -cp .:../lib/junit-4.10.jar:../lib/deuceAgent-1.3.0.jar:../lib/jfreechart-1.0.19.jar:../lib/jcommon-1.0.23.jar ThroughputTest`

It may take a minuet or two to generate the graph.  

## Run correctness test cases 

1) Navigate to compiled output where *.class files are 
2) run `java -cp .:<path to junit-410.jar> org.junit.runner.JUnitCore BSTTester`
    * ex (from src folder): `java -cp .:../lib/junit-4.10.jar org.junit.runner.JUnitCore BSTTester`
3) will output something like: 

        JUnit version 4.10
        ..
        Time: 0.006
        
        OK (2 tests)

        
No other output means that the tests ran successfully

Test source code can be found in: BSTTester.java

# To-Do

* Read paper "Efficient Lock-free Binary Search Trees"

* Find replacement for RSTM in java

### Implement Efficient Lock-free Binary Search Tree

sub steps
  * ~~Remove() - Ross~~ test RIP 
  * ~~contains() - Ross test~~
  * ~~Add() - Chinh test~~
  * ~~clean mark() - Chinh test~~
  * ~~clean flag() - Chinh~~test
  * ~~locate() - Ross test~~
  * ~~tryflag() - Ross~~ test
  * ~~tryMark() - Ross~~ test

### Report

  * progress  guarantee - Ross
  * correctness condition - Chinh
  * synchronization  techniques - Ross
  * advantages and disadvantages of this data structure compared to its alternatives - Chinh
  * improvements - Ross
  * obstacles - Chinh

### Transactional Data Structure Implementation Using STM - Group Effor
DeuceAgent 1.3.0
### Testing and Performance Evaluation - Group Effort