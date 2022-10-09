package com.yukiyama.designpattern.structure;

/**
 * 代理模式
 * 客户端不直接声明真实主题，而是声明代理类，通过调用代理类的
 * 方法来间接执行真实主题类的行为。
 * 
 * 本示例中以代理售房为例，房东需要告知购房者房子类型和价格，但不直接告知
 * 购房者，而是通过代理来完成告知动作。代理类中持有房东实例，客户端只声明
 * 代理类，调用代理类实例中与房东类相同的方法来完成。房东类和代理类都是目标
 * 接口实现类，实现相同的抽象方法，代理类实现该抽象方法的方式是调用其持有
 * 的房东类的方法。
 * 
 * 结构：
 * 抽象主题类：
 *   定义真实主题的方法。
 * 具体主题类：
 *   继承抽象类，实现抽象方法。
 * 代理类：
 *   内部持有一个具体主题类的实例，通过调用该具体主题实例的方法来实现抽象
 *   主题类的抽象方法。
 */
public class ProxyDemo {

    public static void main(String[] args) {
        // 声明一个房屋代理类
        HouseAgent agent = new HouseAgent(new HouseHolder("三室一厅", 100));
        agent.showHouse(); // 调用代理类的方法，输出“三室一厅”
        agent.negotiatePrice(); // 输出“100万”
    }

}

/**
 * 抽象主题类
 * 下例为抽象房东类，有抽象展示房屋方法和抽象价格交涉方法。
 */
abstract class Subject{
    public abstract void showHouse();
    public abstract void negotiatePrice();
}

/**
 * 具体主题类
 * 继承抽象主题类，实现抽象方法。
 * 下例为房东类。
 */
class HouseHolder extends Subject{
    private String house;
    private int price;

    public HouseHolder(String house, int price) {
        this.house = house;
        this.price = price;
    }

    @Override
    public void showHouse() {
        house = "三室一厅";
        System.out.println(house);
    }
    @Override
    public void negotiatePrice() {
        price = 100;
        System.out.println(price + "万");
    }
}

/**
 * 代理类
 * 持有一个具体主题类实例(房东)，实现接口抽象方法，在内部调用真实主题的方法。
 */
class HouseAgent extends Subject{
    HouseHolder holder;

    public HouseAgent(HouseHolder holder) {
        this.holder = holder;
    }

    @Override
    public void showHouse() {
        before();
        holder.showHouse();
    }
    @Override
    public void negotiatePrice() {
        holder.negotiatePrice();
        after();
    }

    public void before(){
        System.out.println("====中介与房东沟通===");
    }
    public void after(){
        System.out.println("====中介与买家沟通===");
    }
}
