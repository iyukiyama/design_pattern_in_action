package com.yukiyama.designpattern.structure;

/**
 * 装饰器模式
 * 使用组合关系来创建一个可以被层层包装的对象(即装饰器对象)来包裹被装饰的
 * 主体对象，并在保持主体对象的类结构不变的前提下，通过装饰器对象自己的
 * 方法，为被装饰的主体对象提供额外的功能(装饰)。
 * 
 * 本示例以技术为抽象主体类(TechComponent)，一项核心技术为具体主体类
 * (JavaComponent)。以其他扩展技术为抽象装饰类(TechDecorator)，其
 * 具体装饰类为web技术(WebTechDecorator)和Spring技术
 * (SpringTechDecorator)。演示如何在核心技术上扩展web和Spring技术。
 * 
 * 结构：
 * 主体接口:
 *   定义一种行为方法。
 * 具体主体类:
 *   实现抽象主体接口，实现抽象方法。
 * 抽象装饰器类:
 *   装饰部件抽象类，实现抽象主体接口，实现抽象方法。
 * 具体装饰器类:
 *   继承抽象装饰类，拥有自己的行为方法的同时实现抽象方法，并在其中调用自己
 *   的行为方法以及完成其所装饰的主体的行为方法。即所谓在保持主体对象不变的
 *   前提下，为其提供额外功能。
 */
public class DecoratorDemo {

    public static void main(String[] args) {
        // 声明一个主体com
        Functionable com = new JavaComponent();
        // 声明修饰对象deco1和deco2
        TechDecorator deco1 = new WebTechDecorator();
        TechDecorator deco2 = new SpringTechDecorator();
        // 此时主体为com，要在其上装饰deco1，为deco1传入com
        deco1.decorate(com);
        // 此时的主体为deco1，要在其上装饰deco2，为deco2传入deco1
        deco2.decorate(deco1);
        // 调用最后一层装饰对象的func()，会依次调用之前的所有装饰对象的
        // func()方法，最终调用到原始主体com的func()方法
        deco2.func();
        // 修饰顺序不同，调用的装饰器的功能顺序也不同
    }

}

/**
 * 主体接口
 * 声明主体的行为方法show()
 */
interface Functionable{
    void func();
}

/**
 * 具体主体类
 * 实现抽象主体接口。
 */
class JavaComponent implements Functionable{
    private final String tech = "JavaSE";
    @Override
    public void func() {
        System.out.printf("展示主体技术%s的技术内容。\n", tech);
    }
}

/**
 * 抽象装饰器类
 * 实现抽象主体接口，持有一个被本装饰器类装饰前的主体类，实现一个非抽象装饰
 * 方法addTech传入被修饰对象，一个抽象的主体行为方法。
 */
abstract class TechDecorator implements Functionable{
    protected Functionable component;
    
    public void decorate(Functionable component) {
        this.component = component;
    }
    // 抽象类实现接口，可以不必实现接口内的抽象方法
    @Override
    public abstract void func();
}

/**
 * 具体装饰器类
 * 继承抽象装饰器类，拥有自己的行为方法，实现抽象方法的时候调用自己的行为
 * 方法并完成其所装饰的主体的行为方法。
 * 下例是Web技术。
 */
class WebTechDecorator extends TechDecorator{
    private final String tech = "WebTech";
    
    // 重写func()并在其中执行自身功能，即装饰效果
    @Override
    public void func() {
        myFunc();
        component.func();
    }
    // 具体装饰类自身的功能
    public void myFunc() {
        System.out.printf("展示扩展技术%s的技术内容。\n", tech);
    }
}

/**
 * 具体装饰器类
 * 下例是Spring技术。
 */
class SpringTechDecorator extends TechDecorator{
    private final String tech = "SpringTech";
    
    // 重写func()并在其中执行自身功能，即装饰效果
    @Override
    public void func() {
        myFunc();
        component.func();
    }
    // 具体装饰类自身的功能
    public void myFunc() {
        System.out.printf("展示扩展技术%s的技术内容。\n", tech);
    }
}
