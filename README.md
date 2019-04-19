## To-Do

* Read paper "Efficient Lock-free Binary Search Trees"

* Find replacement for RSTM in java

### Implement Efficient Lock-free Binary Search Tree

sub steps
  * Cat 3 Remove() - Ross test 
  * ~~contains() - Ross test~~
  * Cat 1 + 2 Remove() - Chinh test
  * ~~Add() - Chinh test~~
  * ~~clean mark() - Chinh test~~
  * ~~clean flag() - Chinh~~test
  * ~~locate() - Ross~~ test
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

### Testing and Performance Evaluation - Group Effort

# Overview 

## Run test cases 

1) Navigate to compiled output where *.class files are 
2) run `java -cp .:<path to junit-410.jar> org.junit.runner.JUnitCore LockFreeBSTTest`
    * ex: `java -cp .:../../../lib/junit-4.10.jar org.junit.runner.JUnitCore LockFreeBSTTest`
3) will output something like: 

        JUnit version 4.10
        ..
        Time: 0.006
        
        OK (2 tests)

        
No other output means that the tests ran successfully

Test source code can be found in: LockFreeBSTTest.java