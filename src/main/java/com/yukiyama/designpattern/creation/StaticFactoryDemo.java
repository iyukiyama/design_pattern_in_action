package com.yukiyama.designpattern.creation;

/**
 * 简单工厂模式(静态工厂模式)
 * 当客户端需要根据某个变量的不同值来声明不同的对象时，可以使用简单工厂模式。
 * 该模式设置了一个生产产品(前述的对象)的工厂类，此工厂类通过静态方法(所以
 * 称为静态工厂)，传入变量参数，方法体内有选择分支(if-else或switch-case)，
 * 根据参数返回具体的产品。举个例子，天气是变量，客户端根据天气的不同情况，
 * 令服装工厂生产不同的服装，下雨生产雨衣，低温天气生产羽绒服，高温天气生产
 * 泳衣等等。在这个例子中服装工厂就是静态工厂，不同的雨衣是不同的具体产品。
 * 天气情况就是静态方法的参数。让不同的产品都继承同一个抽象产品类，那么这个
 * 抽象产品类，具体的产品类和静态工厂类就形成了简单工厂的完整结构。
 * 注：简单工厂模式不在GoF的23种设计模式之列。
 * 
 * 当产品类有增删改时，需要在静态工厂类中修改选择分支，因此简单工厂模式不满足
 * 开闭原则。这个问题要留到工厂方法模式解决。
 * 
 * 本示例以四则运算为例，演示客户端如何通过运算符从运算工厂中获取相应的运算
 * 产品，然后执行该产品的产品方法(执行运算)返回结果。
 * 
 * 结构
 * 静态工厂类
 *   维护一个静态方法，根据参数返回具体产品类的实例(返回类型是抽象产品类型)。
 * 抽象产品类
 *   定义产品字段和方法(操作数a,b和运算方法result())
 * 具体产品类
 *   继承抽象产品类，实现具体运算方法
 */
public class StaticFactoryDemo {

    public static void main(String[] args) {
        // 通过静态工厂类静态方法返回一个具体产品。
        Operation oper = OperationFactory.operate("*");
        // 执行该产品的方法。输出“12”
        System.out.println(oper.result(3, 4));
    }

}

/**
 * 静态工厂类
 * 通过一个类静态方法，根据输入返回相应的工厂类。内部通常为if-else或
 * switch-case。
 */
class OperationFactory{
    public static Operation operate(String sign) {
        Operation oper = null;
        switch (sign) {
        case "+":
            oper = new OperationAdd();
            break;
        case "-":
            oper = new OperationSub();
            break;
        case "*":
            oper = new OperationMul();
            break;
        case "/":
            oper = new OperationDiv();
            break;
        default:
            System.out.println("不支持此运算。");
        }
        return oper;
    }
}

/**
 * 抽象产品类
 * 定义产品的抽象方法。
 * 下例以四则运算为产品，内有对两个int数的运算方法。
 */
abstract class Operation{
    public abstract int result(int a, int b);
}

/**
 * 具体产品类
 * 继承抽象产品类，实现抽象方法。
 * 下例为加法产品。
 */
class OperationAdd extends Operation{
    @Override
    public int result(int a, int b) {
        return a + b;
    }
}

/**
 * 具体产品类，减法
 */
class OperationSub extends Operation{
    @Override
    public int result(int a, int b) {
        return a - b;
    }
}

/**
 * 具体产品类，乘法
 */
class OperationMul extends Operation{
    @Override
    public int result(int a, int b) {
        return a * b;
    }
}

/**
 * 具体产品类，除法
 */
class OperationDiv extends Operation{
    @Override
    public int result(int a, int b) {
        if(b == 0) {
            System.err.println("除数不能为0.");
            throw new IllegalArgumentException();
        }
        return a / b;
    }
}
