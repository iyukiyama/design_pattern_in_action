package com.yukiyama.designpattern.creation;

/**
 * 抽象工厂模式
 * 工厂方法模式的扩展，结构相似，但工厂方法中的工厂类只能生产一种产品，抽象
 * 工厂的工厂类可以生产多种产品。形式上，若工厂模式中的抽象工厂类，有不止一个
 * 生产产品的抽象方法，且这些产品(方法返回类型)不同，具体工厂类实现该抽象工厂
 * 类时重写所有抽象方法，那它就是抽象工厂模式。
 * 
 * 本示例以四则运算为例，演示客户端如何声明一个具体的运算工厂并获取多种不同
 * 的运算产品，然后这些产品执行其产品方法(执行运算)返回结果。
 * 
 * 结构
 * 抽象工厂类
 *   定义多个创建产品的抽象方法。有多少种产品就写多少个方法。
 * 具体工厂类
 *   继承抽象工厂类，实现创建产品的抽象方法。
 * 抽象产品类
 *   定义抽象产品方法。
 * 具体产品类
 *   继承抽象产品类，实现抽象产品方法。
 */
public class AbstractFactoryDemo {

    public static void main(String[] args) {
        // 声明一个具体运算工厂，加法工厂
        IFactory fa = new OperationAddFactory();
        // 该加法工厂生产int型加法产品(运算)
        OperationInt operInt = fa.createOperationInt();
        // 该加法工厂生产另一种产品(运算)，double型产品
        OperationDouble operDouble = fa.createOperationDouble();
        // int型产品执行其产品方法(执行运算)
        System.out.println(operInt.result(4, 5));
        // double型产品执行其产品方法(执行运算)
        System.out.println(operDouble.result(1.2, 1.7));
    }

}

/**
 * 抽象工厂类
 * 定义多个创建产品的抽象方法。有多少种产品就写多少个方法。
 */
abstract class IFactory{
    public abstract OperationInt createOperationInt();
    // 工厂方法模式的产品(运算)只有int型的，抽象工厂模式中多了double型产品
    public abstract OperationDouble createOperationDouble();
}

/**
 * 具体工厂类
 * 继承抽象工厂类，实现创建产品的抽象方法。
 * 下例是加法工厂。
 */
class OperationAddFactory extends IFactory{
    @Override
    public OperationInt createOperationInt() {
        return new OperationIntAdd();
    }
    @Override
    public OperationDouble createOperationDouble() {
        return new OperationDoubleAdd();
    }
}

/**
 * 抽象产品类
 * 定义抽象产品方法。
 * 下例是int类型运算产品的抽象类。
 */
abstract class OperationInt{
    public abstract int result(int a, int b);
}

/**
 * 抽象产品类
 * 下例是double类型运算产品的抽象类。
 */
abstract class OperationDouble{
    public abstract double result(double a, double b);
}

/**
 * 具体产品类
 * 继承抽象产品类，实现抽象产品方法。
 * 下例是int加法产品。
 */
class OperationIntAdd extends OperationInt {
    @Override
    public int result(int a, int b) {
        return a + b;
    }
}

/**
 * 具体产品类
 * 下例是double加法类。
 */
class OperationDoubleAdd extends OperationDouble {
    @Override
    public double result(double a, double b) {
        return a + b;
    }
}
