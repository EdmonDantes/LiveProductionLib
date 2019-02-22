# LiveProductionLib

## Table of contents
- [Math](#Math)
- [Net](#Net)
- [Utils](#Utils)

## Math
### ExpressionObject
This class created for fast and easy calculate different expression
##### How to use
```java
List<ExpressionObject.Operation<YOUR_CLASS>> list = new ArrayList<>();
list.add(new ExpressionObject.Operation<YOUR_CLASS>(OPERATION_TAG, new String[]{OPEREATION_STRINGS_IN_EXPRESSION}, COUNT_ARGS, new ExpressionObject.OperationMethod<YOUR_CLASS>() {
    @Override
    public YOUR_CLASS execute(YOUR_CLASS[] args){
        return YOUR_CODE;
    }
}));
...
ExpressionObject<YOUR_CLASS> expression = new ExpressionObject<>(YOUR_CLASS.class, list);
expression.calc(YOUR_STRING);
```

or you can use ready templates for ```Integer``` and ```Double```

```java
ExpressionObject.calcInteger("1 + 2 + 3 * (45 ^ 2 / (58 - 5) + 5) - 10");
ExpressionObject.calcDouble("1.2 + 5.8");
```
### EquationObject
This class create for solving equations
##### How to use
```java
Set<Double> set = EquationObject.valueOf("2x2 - 18").getX();
```