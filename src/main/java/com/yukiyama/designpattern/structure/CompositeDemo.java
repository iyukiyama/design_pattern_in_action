package com.yukiyama.designpattern.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式
 * 一系列对象可以用树来描述，形成树-子树-叶子的层次机构。如一家大型公司，
 * 根节点是总公司，下设若干子公司和若干非公司部门如总公司财经部，总公司
 * 人力资源部门。子公司中如同总公司结构，也包含它自己的子公司和非公司部门。
 * 则子公司对象是所属公司对象的子树，非公司部门对象是所属公司对象的叶子。
 * 客户端对单个对象和组合对象具有一致的访问性。即上层公司A(根)和其子公司B
 * (树枝或子树)，其非子公司部门C(树叶)继承同一个抽象构件类，有相同的方法。
 * 但因为树叶不再有子节点，根据树叶中是否包含针对子节点的方法(例如add
 * 增加儿子，remove，getChild等)，分为透明方式和安全方式。
 * 
 * 本示例以总公司和子公司场景为例，演示透明方式的组合模式。在客户端中将
 * 树枝和树叶机构加入到根(总公司)中，然后调用总公司的方法，实现对其下属
 * 机构的递归调用。
 * 
 * 透明方式
 * 树叶与树枝具有同样的，在抽象构件类中声明的针对子节点的方法时，对客户端
 * 而言，一个对象是树枝还是树叶是透明的，客户端无需关心。这种方式下，树叶
 * 无儿子，却仍需实现针对儿子的各种方法(空方法或抛出异常)，存在安全性问题。
 * 安全方式
 * 与透明方式相对，不在抽象构件类和树叶实现类中声明针对儿子的方法，只在树枝
 * 中实现。缺点是客户端需要提前知道哪些对象是树枝，哪些是树叶，失去透明性。
 * 
 * 结构
 * 抽象构件类
 *   树枝和树叶的抽象类，声明了公共抽象方法，实现默认行为。
 * 具体实现树枝类
 *   继承抽象构件类，具有儿子的对象，包含针对儿子的各种方法。
 * 具体实现树叶类
 *   继承抽象构件，不具有儿子的对象。若包含针对儿子的各种方法为透明方式，
 *   不包含则为安全模式。
 */
public class CompositeDemo {

    public static void main(String[] args) {
        // 声明上海总公司
        Company hq = new ConcreteCompany();
        hq.setName("上海总公司");
        // 声明上海总公司的叶子机构并添加到总公司中
        Company hqHR = new HRDepartment();
        hqHR.setName("上海总公司人力资源部");
        hq.add(hqHR);
        Company hqFi = new FinanceDepartment();
        hqFi.setName("上海总公司财务部");
        hq.add(hqFi);
        
        // 声明上海总公司的树枝机构广州分公司，并完成其下属机构的添加
        Company gzSub = new ConcreteCompany();
        gzSub.setName("广州分公司");
        Company gzHR = new HRDepartment();
        gzHR.setName("广州分公司人力资源部");
        gzSub.add(gzHR);
        Company gzFi = new FinanceDepartment();
        gzFi.setName("广州分公司财务部");
        gzSub.add(gzFi);
        
        // 将广州分公司添加到上海总公司
        hq.add(gzSub);
        // 调用总公司hq的display()方法，递归调用其下分支机构的display()方法
        System.out.println("====机构====");
        hq.display();
        // 调用总公司hq的duty()方法，递归调用其下分支机构的duty()方法
        System.out.println("====职责====");
        hq.duty();
    }

}

/**
 * 抽象构件类
 * 持有实例字段和非抽象实例方法，定义抽象方法。
 * 下例以公司为抽象构件类。
 */
abstract class Company{
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public abstract void add(Company com);
    public abstract void remove(Company com);
    public abstract void display();
    public abstract void duty();
}

/**
 * 树枝类
 * 继承抽象构件类，比抽象类多一个用于保存儿子的List，实现抽象方法。
 */
class ConcreteCompany extends Company{
    private List<Company> subs = new ArrayList<>();
    
    @Override
    public void add(Company com) {
        subs.add(com);
    }
    @Override
    public void remove(Company com) {
        subs.remove(com);
    }
    @Override
    public void display() {
        System.out.println(this.getName());
        for(Company com : subs) {
            com.display();
        }
    }
    @Override
    public void duty() {
        System.out.printf("%s，统筹公司所有事务。\n", this.getName());
        for(Company com : subs) {
            com.duty();
        }
    }
}

/**
 * 树叶类
 * 继承抽象构件类，实现抽象方法，但对于针对儿子的方法，被调用时打印不支持
 * 操作的提示。
 * 下例是HR部门类。
 */
class HRDepartment extends Company{
    @Override
    public void add(Company com) {
        System.out.printf("%s无子机构，不支持此操作。\n", this.getName());
    }
    @Override
    public void remove(Company com) {
        System.out.printf("%s无子机构，不支持此操作。\n", this.getName());
    }
    @Override
    public void display() {
        System.out.println(this.getName());
    }
    @Override
    public void duty() {
        System.out.printf("%s，负责公司员工招聘薪酬管理。\n", this.getName());
    }
}

/**
 * 树叶类
 * 下例是财务部门类。
 */
class FinanceDepartment extends Company{
    @Override
    public void add(Company com) {
        System.out.printf("%s无子机构，不支持此操作。\n", this.getName());
    }
    @Override
    public void remove(Company com) {
        System.out.printf("%s无子机构，不支持此操作。\n", this.getName());
    }
    @Override
    public void display() {
        System.out.println(this.getName());
    }
    @Override
    public void duty() {
        System.out.printf("%s，负责公司财务管理。\n", this.getName());
    }
}
