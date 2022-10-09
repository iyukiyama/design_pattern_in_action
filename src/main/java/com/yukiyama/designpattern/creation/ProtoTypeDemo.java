package com.yukiyama.designpattern.creation;

/**
 * 原型模式
 * 需要获取一个类的多个实例的场景下，若该类的构造器需要做较多的初始化工作，
 * 则创建多个实例会耗时耗资源。可以在创建一个实例后，后续实例都直接拷贝该
 * 实例可以避免每次获取实例时的初始化开销，对于需要修改的字段针对性修改
 * 即可，这就是原型模式。根据对引用类型字段的拷贝程度分为浅拷贝和深拷贝。
 * 
 * 浅拷贝
 * 对原型P的实例p1执行实例p1.clone()方法拷贝给p2时，对p1内的引用类型
 * 字段q(类型Q)，只拷贝其引用。Java中重写clone方法时若只调用super.clone()
 * 即为浅拷贝，此时p1.q == p2.q。注意clone方法在Cloneable接口中是
 * native方法，返回类型是Object，赋值给p2时要向下转型。可以这样描述如下
 * 两条语句：p1.clone()在堆中生成一个新的实例，p2是这个新实例的引用(地址)，
 * 新实例中有p1的基本数据和不变类型(final修饰如String)的完整拷贝，但对于
 * 非不变的引用类型，只拷贝其引用。即如果p1中有一引用字段Q，那么p1.q == p2.q，
 * p1和p2中的q指向同一个实例，即原本p1中的q实例。
 * P p1 = new P();
 * P p2 = (P)p1.clone();
 * 深拷贝
 * 以浅拷贝中的描述为例，深拷贝对p中的非不变引用类型q，也拷贝一个完整的q
 * 到新的地址中(与q并列的Q类型新实例)，此时p1.q !=p2.q。深拷贝要求Q自身
 * 也要实现Cloneable接口并重写clone方法。即在原型P针对q要有类似如下的语句。
 * p.q = (Q)p.getQ().clone();
 * 若Q中还有引用类型R，则R也要实现Cloneable接口并重写clone方法，以此类推。
 * 
 * 本示例定义一个Person类，使其实现Cloneable接口并通过重写clone方法
 * 使其支持浅拷贝。在Person类中定义一个不变引用类型Country和一个非不变
 * 引用类型WorkExperience，展示浅拷贝Person时，对不变类型Country拷贝
 * 出一个新实例，对非不变引用类型WorkExperience拷贝的是它的引用(地址)。
 * 作为对比，本示例定义一个PersonDeep类，该类内有一个非不变引用类型
 * WorkExperienceDeep，使WorkExperienceDeep也实现Cloneable接口并
 * 重写clone方法，且在PersonDeep类的clone方法中，针对非不变引用类型
 * WorkExperienceDeep，执行一条对它的clone方法来得到它的一个实例，从而
 * 实现对PersonDeep的深拷贝。
 * 
 * 结构
 * 原型接口(通常就是Cloneable)
 * 具体原型类
 *   实现Cloneable接口并重写clone方法，分为深拷贝和浅拷贝。
 */
public class ProtoTypeDemo {

    public static void main(String[] args) {
        System.out.println("====如下是深拷贝示例====");
        // 声明一个支持深拷贝的实例p1
        PersonDeep pd1 = new PersonDeep();
        // 通过原型类中的clone()方法拷贝p1
        PersonDeep pd2 = (PersonDeep) pd1.clone();
        // p1和p2是两个不同的实例，输出“false”
        System.out.println(pd1 == pd2);
        // 设置p1
        pd1.setName("张三");
        pd1.setOccupation("程序员");
        pd1.setWorkExperienceDeep("Alibaba");
        // 设置拷贝而来的p2
        pd2.setName("李四");
        pd2.setOccupation("老师");
        pd2.setWorkExperienceDeep("Tsinghua");
        // 以下3行分别输出“张三”， “程序员”， “Alibaba”
        // WorkExperienceDeep没有被pd2修改为"Tsinghua"，说明PersonDeep是深拷贝类型
        System.out.println(pd1.getName());
        System.out.println(pd1.getOccupation());
        System.out.println(pd1.getWorkExperience().getCompany());
        // 以下3行分别输出“李四”， “老师”， “Tsinghua”
        System.out.println(pd2.getName());
        System.out.println(pd2.getOccupation());
        System.out.println(pd2.getWorkExperience().getCompany());
        // 如下输出“false”，说明p1和p2中的引用类型WorkExperience是不同的实例
        System.out.println(pd1.getWorkExperience() == pd2.getWorkExperience());
        
        System.out.println("====如下是浅拷贝示例====");
        Person p1 = new Person();
        Person p2 = (Person) p1.clone();
        // p1和p2是两个不同的实例，输出“false”
        System.out.println(p1 == p2);
        // 设置p1
        p1.setName("张三");
        p1.setOccupation("程序员");
        p1.setWorkExperience("Alibaba");
        p1.setCountry("CHINA");
        // 设置拷贝而来的p2
        p2.setName("李四");
        p2.setOccupation("老师");
        p2.setWorkExperience("Tsinghua");
        p2.setCountry("SINGAPORE");
        // 以下4行分别输出“张三”， “程序员”， “Tsinghua”，“CHINA”
        // WorkExperience被p2修改为"Tsinghua"，说明Person是浅拷贝类型
        // 另外，p1中的CHINA并没有因为p2设置为了SINGAPORE而受到影响，
        // 说明自定义的不变类Country在浅拷贝中也拷贝到了新实例，而不是只拷贝其引用
        System.out.println(p1.getName());
        System.out.println(p1.getOccupation());
        System.out.println(p1.getWorkExperience().getCompany());
        System.out.println(p1.getCountry());
        // 以下4行分别输出“李四”， “老师”， “Tsinghua”，“SINGAPORE”
        System.out.println(p2.getName());
        System.out.println(p2.getOccupation());
        System.out.println(p2.getWorkExperience().getCompany());
        System.out.println(p2.getCountry());
        // 如下输出“true”，说明p1和p2中的引用类型WorkExperience是相同的实例
        System.out.println(p1.getWorkExperience() == p2.getWorkExperience());
        // 如下输出“false”，说明p1和p2中的引用类型WCountry是不同的实例
        System.out.println(p1.getCountry() == p2.getCountry());
    }
}

/**
 * 深拷贝版
 * 原型类
 * 实现Cloneable接口并重写clone方法
 * 本类中的非不变引用类型字段也通过该字段自身的clone()进行拷贝
 */
class PersonDeep implements Cloneable{
    private String name;
    private String occupation;
    private int age;
    private WorkExperienceDeep workExperience;
    
    // 在构造器中实例化引用类型WorkExperience
    public PersonDeep() {
        workExperience = new WorkExperienceDeep();
    }
    
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public WorkExperienceDeep getWorkExperience() {
        return workExperience;
    }
    public void setWorkExperienceDeep(String company) {
        this.workExperience.setCompany(company);
    }
    // 重写clone()使得原型类Person能够执行clone()从而被拷贝
    @Override
    protected Object clone() {
        PersonDeep person = null;
        try {
            person = (PersonDeep) super.clone();
            person.workExperience = (WorkExperienceDeep) person.getWorkExperience().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return person;
    }
}

/**
 * 深拷贝版
 * 原型类型中的引用类型字段
 */
class WorkExperienceDeep implements Cloneable{
    private String company;

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    @Override
    protected Object clone() {
        WorkExperienceDeep we = null;
        try {
            we = (WorkExperienceDeep) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return we;
    }
}

/**
 * 浅拷贝版
 * 原型类，实现Cloneable接口并重写clone方法
 */
class Person implements Cloneable{
    private String name;
    private String occupation;
    private int age;
    private WorkExperience workExperience;
    private Country country;
    
    // 在构造器中实例化引用类型WorkExperience
    public Person() {
        workExperience = new WorkExperience();
    }
    
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public WorkExperience getWorkExperience() {
        return workExperience;
    }
    public String getCountry() {
        return country.getCountryName();
    }
    public void setCountry(String countryName) {
        this.country = new Country(countryName);
    }

    public void setWorkExperience(String company) {
        this.workExperience.setCompany(company);
    }
    // 重写clone()使得原型类Person能够对外拷贝
    @Override
    protected Object clone() {
        Person person = null;
        try {
            person = (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return person;
    }
}

/**
 * 浅拷贝版
 * 原型类型中的引用类型字段
 */
class WorkExperience{
    private String company;

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
}

/**
 * 浅拷贝版
 * 自定义不变类，用于演示在浅拷贝中对不变类的拷贝结果
 */
class Country{
    private String countryName;
    
    public Country(String countryName) {
        this.countryName = countryName;
    }
    public String getCountryName() {
        return countryName;
    }
}
