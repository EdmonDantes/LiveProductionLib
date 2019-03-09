# LiveProductionLib

## Table of contents
- [Math](#Math)
    - [Expression](#Expression)
- [Net](#Net)
- [Utils](#Utils)

## Math
### Expression
#### Operation
For operations with classes
##### How to use
**Constructor with maximum arguments:**
```java
new Operation<CLASS>(String operationTag, Set<String> operationStringSynonyms,
 int countOperationArgs, OperationFunction<CLASS> operationFunction,
  String argumentSeparator, boolean suffixForm);
```
*You can use ``List<String>`` or ``String[]`` in ``operationStringSynonyms``*

``argumentSeparator`` work only with operation witch have > 2 arguments

``suffixForm`` work only with operation witch have 1 argument. (Example: "2!" - is suffix form)

**For create operations with 1 or 2 arguments use:**
```java
new Operation<CLASS>(String operationTag, Set<String> operationStringSynonyms, 
int countOperationArgs, OperationFunction<CLASS> operationFunction,
 boolean suffixForm);
```
or
```java
Operation<CLASS>(String operationTag, Set<String> operationStringSynonyms,
 int countOperationArgs, OperationFunction<CLASS> operationFunction);
```
**For create operations with 2 arguments use:**
```java
new Operation<CLASS>(String operationTag, Set<String> operationStringSynonyms,
 OperationFunction<CLASS> operationFunction);
```
**For create operations with 2 arguments and for 1 string synonym use:**
```java
Operation<CLASS>(String operationTag, String operationStringSynonym,
 OperationFunction<CLASS> operationFunction);
```
#### OperationManager
For manage operation and operation priority
##### How to use
**You can create 2 or more operation with one priority:**
```java
new OperationManager<CLASS>(new Operation[][]{
        new Operation[]{new Operation<CLASS>(...), new Operation<CLASS>(...), new Operation<CLASS>(...)},
        new Operation[]{new Operation<CLASS>(...), new Operation<CLASS>(...)}
});
```
*You can use ``List<List<Operation>>`` if you want, but you need add null argument
(Example: ``new OperationManager<CLASS>(List<List<Operation<CLASS>>, null);``)*

**Or each operation will have own priority**

*Witch operation added first, it have higher priority*
```java
new OperationManager(new Operation[]{
        new Operation<CLASS>(...),
        new Operation<CLASS>(...)
});
```
*Or you can use ``List<Operation<CLASS>>`` (Example: ``new OperationManager(List<Operation<CLASS>> ...);``)*
#### ExpressionObject
This class created for fast and easy calculate different string`s expression
##### How to use
```java
new ExpressionObject(new OperationManager<CLASS>(...), CLASS.class).calculate(String expression);
```

or
```java
ExpressionObject.calculateInteger("1 + 2 + 3 * (-8 + 8 log 2) ^ (10 / 5)");
```

or
```java
ExpressionObject.calculateDouble("1.5 + 2.5");
```
#### EquationObject
This class create for solving equations
##### How to use
```java
Set<Double> set = EquationObject.valueOf("2x2 - 18").getX();
```