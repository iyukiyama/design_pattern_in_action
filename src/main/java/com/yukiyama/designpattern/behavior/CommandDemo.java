package com.yukiyama.designpattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令模式
 * 请求某个命令执行者执行命令时，最直接的方式是客户端直接传入命令给执行者，
 * 但这样就无法管理命令(或者说只能由客户端来管理)，加重了客户端的负担。此类
 * 场景可以将命令封装成类，其内持有命令执行者实例，另有一个execute方法，该
 * 方法调用命令执行者实例的执行方法。再引入一个命令请求者(或称命令传递者，
 * 命令管理者)类来持有命令集合实例，其内有追加命令方法，删除命令方法，最重要
 * 的是有一个传递命令的方法request，该方法通过调用其持有的命令对象的execute
 * 方法来实现请求(execute内部调用了命令执行者自身的执行方法)。
 * 
 * 本示例展示点菜场景下，命令请求者服务员(Waiter类)如何管理命令(Order类)，
 * 并通过request调用Order内的excute方法，让厨师(Chef类)执行做菜方法。
 * 
 * 结构
 * 抽象命令类:
 *   持有一个命令执行者Chef类，并通过带参构造方法初始化Chef属性。有一个抽象
 *   方法execute。
 * 具体命令类
 *   继承抽象命令类，实现execute方法，内部调用Chef的做菜方法。
 * 命令执行类
 *   执行命令的角色Chef，内有做菜方法(本例中的makeMutton和makeChickenWings)。
 * 命令请求者类
 *   管理命令和请求执行者执行命令的角色，类内以List持有多个命令实例。维护
 *   追加命令方法addOrder，撤销命令方法cancelOrder和请求方法request。
 *   request内遍历Order并调用其execute方法。
 */
public class CommandDemo {

    public static void main(String[] args) {
        // 声明命令执行者
        Chef chef = new Chef();
        // 声明具体命令，通过构造器传入该命令对应的执行者(本例只有一个执行者)
        Order muttonOrder = new MuttonOrder(chef);
        Order wingsOrder = new ChickenWingsOrder(chef);
        // 声明命令请求者
        Waiter waiter = new Waiter();
        // 将命令传入请求者内
        waiter.addOrder(muttonOrder);
        waiter.addOrder(wingsOrder);
        // 命令请求者执行request方法，输出"烤羊肉串"，“烤鸡翅”
        waiter.request();
    }

}

/**
 * 抽象命令类
 * 持有命令执行者类Chef，通过有参构造器传入Chef来初始化。有一个execute抽象方法。
 */
abstract class Order{
    protected Chef chef;
    
    public Order(Chef chef){
        this.chef = chef;
    }
    public abstract void execute();
}

/**
 * 具体命令类
 * 实现抽象命令类，实现execute方法，方法内部调用其持有的Chef的做菜方法。
 * 下例是点羊肉串的命令MuttonOrder，execute方法内调用Chef的做羊肉串方法
 * makeMutton。
 */
class MuttonOrder extends Order{
    public MuttonOrder(Chef chef) {
        super(chef);
    }
    @Override
    public void execute() {
        chef.makeMutton();
    }
}

/**
 * 具体命令类
 * 下例是点鸡翅的命令ChickenWingsOrder，execute方法内调用Chef的做鸡翅
 * 方法makeChickenWings。
 */
class ChickenWingsOrder extends Order{
    public ChickenWingsOrder(Chef chef) {
        super(chef);
    }
    public void execute() {
        chef.makeChickenWings();
    }
}

/**
 * 命令请求者类
 * 持有一个命令实例集合List<Order>。维护管理命令的方法addOrder，cancel，
 * request。其中传递命令的请求方法request遍历该类持有的命令集合，并执行命令
 * 类的execute方法。
 */
class Waiter{
    private List<Order> orders = new ArrayList<>();
    
    public void addOrder(Order order) {
        orders.add(order);
    }
    public void cancelOrder(Order order) {
        orders.remove(order);
    }
    public void request() {
        for(Order order : orders) {
            order.execute();
        }
    }
}

/**
 * 命令执行者类
 * 真正执行命令的角色，维护做菜方法。
 * 下例是烤羊肉串MakeMutton和烤鸡翅MakeChickenWings方法。
 */
class Chef{
    public void makeMutton() {
        System.out.println("烤羊肉串");
    }
    public void makeChickenWings() {
        System.out.println("烤鸡翅");
    }
}
