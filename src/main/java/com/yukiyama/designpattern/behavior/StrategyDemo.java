package com.yukiyama.designpattern.behavior;

/**
 * 策略模式
 * 处理同样的事情可以有多种方法，例如排序数组，可以用冒泡，选择，插入，希尔，
 * 快速等等排序方法，对于同样的输入有多种策略能来处理并输出不同策略处理的结果。
 * 在程序中使用某一种策略，最直接的方式是在在客户端中将不同的策略写入if-else
 * 或switch-case的分支，然后由客户端进行选择。缺点是客户端职责过大，违背单一
 * 职责原则，其次是某个策略变动需要修改客户端代码，违背开闭原则。针对此类场景
 * 可以使用策略模式。每一种策略单独成类，均继承一个抽象策略类设置一个持有策略
 * 类的环境，客户端不必了解每个策略细节，而是声明某个具体策略后，借由上下文类
 * 来执行其中的策略动作。如需增减策略，只需要增减该策略的类。一个已存在的策略
 * 出现变动，也只需要修改该策略本身，增删改都不会影响已有程序，满足开闭原则。
 * 一个策略类实现一种策略方法，满足单一职责原则。
 * 
 * 本示例以处理收款为例，演示客户端如何通过环境类来指定某种收款策略得到应收结果。
 * 
 * 结构
 * 抽象策略类
 *   定义一个收款抽象方法acceptCash。
 * 具体策略类
 *   继承抽象策略类，实现具体策略。
 * 环境类(上下文类)
 *   持有一个收费策略类，维护一个收款方法，该方法内实际调用收费策略类的收款
 *   方法。
 */
public class StrategyDemo {

    public static void main(String[] args) {
        // 声明收费具体策略类cash1，按85折收费
        Cash cash1 = new CashDiscount(0.15);
        // 声明收费具体策略cash2，正常收费
        Cash cash2 = new CashNormal();
        // 声明收费上下文，通过构造器初始化收费策略为cash1
        CashContext cc1 = new CashContext(cash1);
        // 声明收费上下文，通过构造器初始化收费策略为cash2
        CashContext cc2 = new CashContext(cash2);
        // 付款100，返回cash1策略的应收费用
        double requestMoney1 = cc1.acceptCash(100);
        // 输出“85.0”
        System.out.println(requestMoney1);
        // 付款100，返回cash1策略的应收费用，输出“100”
        double requestMoney2 = cc2.acceptCash(100);
        // 输出“100.0”
        System.out.println(requestMoney2);
    }

}

/**
 * 抽象策略类
 * 定义一个收款抽象方法acceptCash，入参是客户付款，返回应收费用
 */
abstract class Cash{
    public abstract double acceptCash(double money);
}

/**
 * 具体策略类
 * 继承抽象策略类，实现具体策略。
 * 下例是按原价收费。
 */
class CashNormal extends Cash{
    @Override
    public double acceptCash(double money) {
        return money;
    }
}

/**
 * 具体策略类
 * 下例是按折扣收费，折扣在声明具体Cash类时通过构造器传入并初始化。
 */
class CashDiscount extends Cash{
    private double rate;
    
    public CashDiscount(double discount) {
        rate = 1.0 - discount;
    }
    @Override
    public double acceptCash(double money) {
        return money * rate;
    }
}

/**
 * 环境类(上下文类)
 * 持有一个收费策略类，并通过有参构造器传入收费策略实例初始化。
 * 维护一个收款方法，内部调用其所持有的具体收费策略的收款方法。
 */
class CashContext{
    private Cash cash;
    
    public CashContext(Cash cash) {
        this.cash = cash;
    }
    public double acceptCash(double money) {
        return cash.acceptCash(money);
    }
}
