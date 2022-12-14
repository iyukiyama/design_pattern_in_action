package com.yukiyama.designpattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 中介者模式
 * 存在许多需要两两交互信息的同类对象，如果互相之间直接交互，将会形成网状
 * 结构，每一个对象都需要知道所有其他对象，且若某对象需要修改，那么其他对象
 * 可能也要做出修改，造成牵一发动全身的不良后果。针对这种场景，可以设置一个
 * 中介者，将对象间的网状两两交互，转变为通过中介者来居中传递信息，能够大大
 * 降低同类对象间的耦合，系统也会变得易扩展易维护。例如新增一个需要通信的
 * 成员(本设计模式中所谓的同事)，只需要创建它并注册到中介者中即可，不影响
 * 任何其他成员(同事)。
 * 
 * 本示例展示中介者闲鱼平台如何向卖家转发买家的商品需求信息，向买家转发卖家
 * 的商品上架信息。当有一个买家发布一条商品需求信息时，所有卖家均能收到。当
 * 一个卖家发布一条商品上架信息时，所有买家均能收到。
 * 
 * 结构
 * 抽象中介者类
 *   定义管理同事和传递信息的方法。本示例中为注册同事的方法register(User user)，
 *   转发信息的方法relay(User user, String message)。
 * 具体中介者类
 *   继承抽象中介者。持有一个已完成注册的同事的集合List<User>，实现抽象
 *   中介者类中的抽象方法register和relay。
 * 抽象同事类
 *   持有一个中介者实例属性，一个名字属性，通过有参构造器初始化名字属性。
 *   定义三个抽象方法，设置中介者的方法setMediator，发送消息的方法send，
 *   接收消息的方法receive。
 * 具体同事类
 *   继承抽象同事类。实现抽象同事类中的三个抽象方法。
 */
public class MediatorDemo {

    public static void main(String[] args) {
        // 声明一个具体中介者实例闲鱼平台
        Mediator xianyu = new XianyuMediator();
        // 声明卖家1
        User seller1 = new Seller("卖家1");
        // 声明买家1
        User buyer1 = new Buyer("买家1");
        // 声明卖家2
        User seller2 = new Seller("卖家2");
        // 声明买家2
        User buyer2 = new Buyer("买家2");
        // 将买家和卖家都注册到闲鱼中介者上
        xianyu.register(seller1);
        xianyu.register(buyer1);
        xianyu.register(seller2);
        xianyu.register(buyer2);
        // 当卖家1发布一条商品上架信息时，所有买家都能收到通过闲鱼中介者转发的该消息
        seller1.send("上架了一台小米手机。");
        // 当买家1发布一条商品需求信息时，所有卖家都能收到通过闲鱼中介者转发的该消息
        buyer1.send("想要一个皮卡丘手办。");
    }

}

/**
 * 抽象中介者类
 * 定义管理同事和传递信息的方法。本示例中为注册同事的方法register(User user)，
 * 转发信息的方法relay(User user, String message)。
 */
abstract class Mediator{
    public abstract void register(User user);
    public abstract void relay(User user, String message);
}

/**
 * 具体中介者类
 * 继承抽象中介者。持有一个已完成注册的同事的集合List<User>，实现抽象
 * 中介者类中的抽象方法register和relay。relay方法参数为用户User和消息
 * message，判断User属于卖家Seller还是买家Buyer，如果是卖家则该消息是
 * 一则商品上架消息，通知所有买家该消息。如果是买家则该消息是一则商品需求
 * 消息，通知所有卖家该消息。
 */
class XianyuMediator extends Mediator{
    private List<User> users = new ArrayList<>();
    
    //将user添加到中介者持有的User类集合中，添加时执行User的setMediator方法
    @Override
    public void register(User user) {
        if(!users.contains(user)) {
            users.add(user);
            user.setMediator(this);
        }
    }
    @Override
    public void relay(User user, String message) {
        // 打印该用户发出的消息
        System.out.println(user.getName()+message);
        // 判断用户类型，如果是卖家
        if(user instanceof Seller) {
            // 所有买家接收该消息
            for(User u : users) {
                if(u instanceof Buyer) {
                    u.receive(user.getName()+message);
                }
            }
        }
        // 判断用户类型，如果是买家
        if(user instanceof Buyer) {
            // 所有卖家接收该消息
            for(User u : users) {
                if(u instanceof Seller) {
                    u.receive(user.getName()+message);
                }
            }
        }
    }
}

/**
 * 抽象同事类
 * 持有一个中介者实例属性，一个名字属性，通过有参构造器初始化名字属性，
 * 并有一个名字属性的getter。
 * 定义三个抽象方法，设置中介者的方法setMediator，发送消息的方法send，
 * 接收消息的方法receive。
 * 本例的同事类为User类。
 */
abstract class User{
    protected Mediator mediator;
    private String name;
    
    public User(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public abstract void setMediator(Mediator mediator);
    public abstract void send(String message);
    public abstract void receive(String message);
}

/**
 * 具体同事类
 * 继承抽象同事类。实现抽象同事类中的三个抽象方法。setMediator方法传入
 * 中介者实例，赋值给该同事类持有的中介者属性。send方法调用中介者的转发
 * 方法relay，传入当前同事实例(this)和message。receive方法接收message
 * 并打印出来。
 * 下例是卖家类。
 */
class Seller extends User{
    public Seller(String name) {
        super(name);
    }
    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    @Override
    public void send(String message) {
        this.mediator.relay(this, message);
    }
    @Override
    public void receive(String message) {
        System.out.println(this.getName() + "收到一条商品需求信息：" + message);
    }
}

/**
 * 具体同事类
 * 下例是买家类。
 */
class Buyer extends User{
    public Buyer(String name) {
        super(name);
    }
    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    @Override
    public void send(String message) {
        this.mediator.relay(this, message);
    }
    @Override
    public void receive(String message) {
        System.out.println(this.getName() + "收到一条商品发布信息：" + message);
    }
}
