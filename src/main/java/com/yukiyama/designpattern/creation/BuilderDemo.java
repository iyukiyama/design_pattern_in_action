package com.yukiyama.designpattern.creation;

import java.util.ArrayList;
import java.util.List;

/**
 * 建造者模式(生成器模式)
 * 有一些对象，他们由多个部件构成，构成的方式相同，但每个部件在不同对象中
 * 可能有不同的特征细节。举例来说，无论是哪个品牌的电脑，都由CPU，显示器，
 * 硬盘内存，键盘等部件构成，只是对不同品牌不同型号的PC，部件的细节不同，
 * 例如苹果电脑的CPU是苹果自研的，联想电脑CPU是英特尔的。在客户端中完成
 * 对不同对象的建造，最直接的方式是分别声明声明他们，然后调用各自的建造
 * 动作来完成。这样的做法无法利用不同对象建造过程稳定且相同(只是细节不同)
 * 的特点，达不到代码复用的效果。针对这种场景，可以使用建造者模式来实现。
 * 建造者模式提供一个抽象建造者类，类内定义了稳定的产品建造过程，即建造具体
 * 产品各部件的抽象方法。这样具体建造者继承抽象建造者类后，可以根据需要重写
 * 某些部件的建造方法。具体建造者类内持有一个产品类实例，用于保存建造完成
 * 后的产品，并通过他的getter的方法获取该产品的实例。展示产品细节的动作
 * 在产品类中完成。这就是所谓的将复杂对象的构造和表示分离，实际就是建造者类
 * 和产品类分离。至此，若客户端发起建造，就可以声明一个具体建造类，并调用其
 * 建造过程(多个部件的build方法)，但这样一来就对客户端暴露了建造过程，且
 * 加重了客户端的任务。所以另外设置一个指挥者类，该类只有一个produce方法，
 * 方法参数是具体建造者实例，传入该实例，在produce内部调用该实例建造各
 * 部件的方法。如此，客户端就只需要声明一个指挥者，再声明一个具体建造者，将
 * 具体建造者作为指挥者的produce方法参数，指挥者执行其produce完成建造。
 * 执行后需要查看得到的产品细节，再调用产品建造者的getProduce方法即可。
 * 
 * 本示例以汽车生产为例，展示如何利用建造者模式来生产F1赛车和普通家用汽车。
 * 
 * 结构
 * 指挥者类
 *   提供一个指挥生产的方法produce，传入建造者，其内调用建造者的建造方法。
 * 抽象建造者类
 *   提供建造产品各部件的抽象方法和一个返回产品的方法。
 * 具体建造者类
 *   继承抽象建造者类，并持有一个产品实例以供获取。实现产品各部分抽象建造
 *   方法和返回产品的抽象方法。
 * 产品类
 *   持有产品各部件的一个集合实例，有一个增加部件的add方法，一个用于展现
 *   组成部件的show方法。
 */
public class BuilderDemo {

    public static void main(String[] args) {
        // 声明一个指挥者
        Director director = new Director();
        // 声明一个具体建造者
        CarMaker carMaker = new F1CarMaker();
        // 指挥者执行produce方法，传入具体建造者执行建造
        director.produce(carMaker);
        // 通过建造者的get方法得到建造后的产品
        Car car = carMaker.getCar();
        // 产品调用show方法展示建造后的产品细节
        car.show();
    }

}

/**
 * 指挥者类
 * 提供一个指挥生产的方法produce，传入具体建造者，执行既定的建造产品的方法
 * 完成产品制造。
 */
class Director{
    public void produce(CarMaker cm) {
        cm.buildWheels();
        cm.buildEngine();
        cm.buildSeats();
    }
}

/**
 * 抽象建造者类
 * 提供建造产品各部件的抽象方法和一个返回产品的方法。
 * 下例为一个抽象汽车制造者类。
 */
abstract class CarMaker{
    public abstract void buildWheels();
    public abstract void buildEngine();
    public abstract void buildSeats();
    public abstract Car getCar();
}

/**
 * 具体建造者类
 * 继承抽象建造者类，并持有一个产品实例，实现产品各部分抽象建造方法和返回
 * 产品的抽象方法(返回其持有的产品实例)。
 * 下例是F1赛车制造者
 */
class F1CarMaker extends CarMaker{
    private Car car = new Car();
    
    @Override
    public void buildWheels() {
        car.add("赛车轮胎");
    }
    @Override
    public void buildEngine() {
        car.add("赛车引擎");
    }
    @Override
    public void buildSeats() {
        car.add("赛车座椅");
    }
    @Override
    public Car getCar() {
        return car;
    }
}

/**
 * 具体建造者类
 * 按照该建造者的实际情况实现产品各部分建造方法和返回产品方法
 * 下例是家用车制造者
 */
class HomeCarMaker extends CarMaker{
    private Car car = new Car();
    
    @Override
    public void buildWheels() {
        car.add("家用车轮胎");
    }
    @Override
    public void buildEngine() {
        car.add("家用车引擎");
    }
    @Override
    public void buildSeats() {
        car.add("家用车座椅");
    }
    @Override
    public Car getCar() {
        return car;
    }
}

/**
 * 要建造的产品类
 * 持有产品各部件的集合List<String>，定义一个add方法用于添加构件。另定义
 * 一个show方法用于展示建造的产品细节。
 */
class Car{
    private List<String> parts = new ArrayList<>();
    
    public void add(String part) {
        parts.add(part);
    }
    public void show() {
        System.out.println(parts);
    }
}

