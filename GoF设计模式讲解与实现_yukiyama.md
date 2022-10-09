# GoF设计模式讲解与实现

> v20220419。本材料由 [yukiyama](https://github.com/iyukiyama/) 完成。
>

本材料旨在记录和分享设计模式的学习过程和总结，试图提炼每一种模式的核心要点，并给出经过验证的展现模式工作过程的代码。期望读者在细读每一个模式的讲解后，能够理解每一种模式的工作原理，并能够根据理解独立实现该模式的代码。

学习过程中我主要参考了程杰的《大话设计模式》和[这个网站](https://link.zhihu.com/?target=https%3A//link.juejin.cn/%3Ftarget%3Dhttp%3A%2F%2Fc.biancheng.net%2Fview%2F1317.html)。前者以生活场景化，对话化的形式讲解，在设计模式初学者中广受好评，但无关主旨的背景介绍较多，且无Java示例代码。后者网站虽然用的是Java，但每一个示例都用窗体展示，夹杂了大量无关模式的awt和swing代码，不利于集中理解模式的核心内容。另外，网上许多关于设计模式的文章，总是充斥着很多抽象说明和到处复制粘贴的UML图，对初学者理解模式本身反而造成干扰。例如下面这种抽象描述，实际上是对模式完全理解之后的高度概括，很多文章上来就写这种抽象定义，纯属浪费时间。这种描述对模式识别的理解不但不是必须的，对初学者来说甚至是有害的。

> 状态（State）模式的定义：对有状态的对象，把复杂的“判断逻辑”提取到不同的状态对象中，允许状态对象在其内部状态发生改变时改变其行为。

在本材料中我讲侧重于每一个模式的代码实现而非连篇累牍的文字来讲解，代码示例都已经过验证。每一个模式讲解顺序如下：

- **模式说明**：简要说明模式使用场景和该模式的优点，例如相比简单粗暴方法（通常是if-esle或者单个类封装等）的好处，另外像组合模式里有透明方式和安全方式，单例模式里有饿汉方式和懒汉方式，模板模式里有钩子方法等，也会简要说明这些不同的方式方法。
- **结构**：列明该模式用到的抽象和具体类。
- **代码演示**：可执行测试的该模式的示例代码，关键语句附有注释。

在进入具体模式讲解前，先给出模式一览，对每个模式做简要说明，也给出设计原则一览，可在具体模式讲解中体会各项原则。

<br />

## 设计模式一览

每一种模式的「说明」都是对该模式的总结概括，因此在未学习该模式前，「说明」内容可能会比较抽象且难以理解。建议在学习该模式后再重新阅读体会。

| 模式名称                         | 说明                                                         |
| -------------------------------- | ------------------------------------------------------------ |
| **创建型**                       | **创建型模式用于创建对象实例。**                             |
| 单例 (Singleton)                 | 某个类只能生成一个实例，该类提供了一个静态方法供外部获取唯一实例，且无法通过new来创建实例。 |
| 原型 (Prototype)                 | 将一个对象作为原型，通过对其进行复制而克隆出多个和基于原型的新实例。 |
| 简单工厂(Simple Factory) *       | 通过不同变量来获取不同产品实例。<br />*该模式不属于GoF23种设计模式之一。 |
| 工厂方法 (Factory Method)        | 定义一个用于创建产品的接口，由子类决定生产什么产品。         |
| 抽象工厂 (AbstractFactory)       | 提供一个创建产品族的接口，其每个子类可以生产一系列相关的产品。 |
| 建造者 (Builder)                 | 将一个复杂对象分解成多个相对简单的部分，然后根据不同需要分别创建它们，最后构建成该复杂对象。 |
| **结构型**                       | **结构型模式用于将类或对象按某种方式组成更大的结构。**       |
| 代理 (Proxy)                     | 为某对象提供一种代理以控制对该对象的访问。客户端通过代理间接地访问该对象，从而限制、增强或修改该对象的一些特性。 |
| 适配器 (Adapter)                 | 将一个类的接口转换成客户端希望的另外一个接口，使得原本由于接口不兼容而不能一起工作的那些类能一起工作。 |
| 桥接 (Bridge)                    | 将抽象与实现分离，使它们可以独立变化。它是用组合关系代替继承关系来实现，从而降低了抽象和实现这两个可变维度的耦合度。 |
| 装饰 (Decorator)                 | 动态的给对象增加一些职责，即增加其额外的功能。               |
| 外观 (Facade)                    | 为多个复杂的子系统提供一个一致的接口，使这些子系统更加容易被访问。 |
| 享元 (Flyweight)                 | 运用共享技术来有效地支持大量细粒度对象的复用。               |
| 组合 (Composite)                 | 将对象组合成树状层次结构，使用户对单个对象和组合对象具有一致的访问性。 |
| **行为型**                       | **行为型模式用于描述多个对象间如何互相协作，完成单个对象难以完成的任务。** |
| 模板方法 (TemplateMethod)        | 定义一个操作中的算法骨架，而将算法的一些步骤延迟到子类中，使得子类可以不改变该算法结构的情况下重定义该算法的某些特定步骤。 |
| 策略 (Strategy)                  | 定义了一系列算法，并将每个算法封装起来，使它们可以相互替换，且算法的改变不会影响使用算法的客户。 |
| 命令 (Command)                   | 将一个请求封装为一个对象，使发出请求的责任和执行请求的责任分割开。 |
| 职责链 (Chain of Responsibility) | 把请求从链中的一个对象传到下一个对象，直到请求被响应为止。通过这种方式去除对象之间的耦合。 |
| 状态 (State)                     | 允许一个对象在其内部状态发生改变时改变其行为能力。           |
| 观察者 (Observer)                | 多个对象间存在一对多关系，当一个对象发生改变时，把这种改变通知给其他多个对象，从而影响其他对象的行为。 |
| 中介者 (Mediator)                | 定义一个中介对象来简化原有对象之间的交互关系，降低系统中对象间的耦合度，使原有对象之间不必相互了解。 |
| 迭代器 (Iterator)                | 提供一种方法来顺序访问聚合对象中的一系列数据，而不暴露聚合对象的内部表示。 |
| 访问者 (Visitor)                 | 在不改变集合元素的前提下，为一个集合中的每个元素提供多种访问方式，即每个元素有多个访问者对象访问。 |
| 备忘录 (Memento)                 | 在不破坏封装性的前提下，获取并保存一个对象的内部状态，以便以后恢复它。 |
| 解释器 (Interpreter)             | 提供如何定义语言的文法，以及对语言句子的解释方法，即解释器。 |

<br />

## 设计原则一览

| 设计原则                                            | 一句话归纳                                                   | 目的                                         |
| --------------------------------------------------- | ------------------------------------------------------------ | -------------------------------------------- |
| 开闭原则 (Open Closed Principle，OCP）              | 对扩展开放，对修改关闭。                                     | 降低维护带来的新风险。                       |
| 依赖倒置原则 (Dependence Inversion Principle, DIP)  | 高层不应该依赖低层，要面向接口编程。                         | 更利于代码结构的升级扩展。                   |
| 单一职责原则 (Single Responsibility Principle, SRP) | 一个类只干一件事，实现类要单一。                             | 便于理解，提高代码的可读性。                 |
| 接口隔离原则 (Interface Segregation Principle, ISP) | 一个接口只干一件事，接口要精简单一。                         | 功能解耦，高聚合、低耦合。                   |
| 迪米特法则 (Law of Demeter, LoD)                    | 不该知道的不要知道，一个类应该保持对其它对象最少的了解，降低耦合度。 | 只和朋友交流，不和陌生人说话，减少代码臃肿。 |
| 里氏替换原则 （Liskov Substitution Principle，LSP） | 不要破坏继承体系，子类重写方法功能发生改变，不应该影响父类方法的含义。 | 防止继承泛滥。                               |
| 合成复用原则 (Composite Reuse Principle, CRP)       | 尽量使用组合或者聚合关系实现代码复用，少使用继承。           | 降低代码耦合。                               |

<br />

## 创建型设计模式

创建型模式用于创建对象实例，包含6种创建型模式（其中“简单工厂模式”不在GoF23种之列）。

- 单例 / 原型 / 简单工厂(静态工厂) / 工厂方法 / 抽象工厂 / 建造者

<br />

### 单例模式

#### 模式说明

对于一个对象只能有唯一实例的场景，可以通过单例模式实现。由于只能有一个实例，因此该类的构造器必须用`private`修饰来禁止通过`new`操作符来新建实例。类内持有一个`private`修饰的本类实例，声明为类成员(静态字段)，如该类类名`Singleton`，可声明为：
`private static Singleton instance;`
通过类内的一个`public`修饰的`getIntance`方法来获取实例，该方法也是`static`的。根据实例产生的时机分为饿汉模式和懒汉模式，懒汉模式中有线程安全和不安全两种写法。

**饿汉模式**
类属性Singleton instance在声明时就实例化。
`private static Singleton instance = new Singleton();`

**懒汉模式**
类属性`Singleton`只声明但不实例化，将实例化动作放到`getInstance`方法中，只有调用`getInstance`时才能获取`Singleton`的实例。懒汉模式因为在调用`getInstance`时才获得实例，存在多线程竞争的问题，因此可以结合`volatile`(避免指令重排)和`synchronized`(保证原子性)以双锁检测方式将`Singleton`写成线程安全的类。

本示例演示饿汉写法，非线程安全懒汉写法和双锁检测线程安全懒汉写法。

<br />

#### 结构

| 类/接口 | 描述                                              |
| ------- | ------------------------------------------------- |
| 单例类  | 持有一个本类的类属性，维护一个`getInstance`方法。 |

<br />

#### 代码演示

```java
/**
 * 单例模式
 */
public class SingletonDemo {

	public static void main(String[] args) {
		SingletonSimple s1 = SingletonSimple.getInstance();
		SingletonSimple s2 = SingletonSimple.getInstance();
		// 输出“true”，说明s1与s2是同一个实例
		System.out.println(s1 == s2);
		SingletonHungry s3 = SingletonHungry.getInstance();
		SingletonHungry s4 = SingletonHungry.getInstance();
		// 输出“true”，说明s3与s4是同一个实例
		System.out.println(s3 == s4);
		Singleton s5 = Singleton.getInstance();
		Singleton s6 = Singleton.getInstance();
		// 输出“true”，说明s3与s4是同一个实例
		System.out.println(s5 == s6);
	}

}

/**
 * 饿汉模式
 * 在类加载时创建常量化实例，不存在多线程导致可能出现多个实例的问题
 */
class SingletonHungry{
	// 在定义SingletonHungry类型的属性时直接实例化，类内可以访问private构造器
	private static final SingletonHungry instance = new SingletonHungry();
	
	// 将构造器声明为private，外部无法用new获取
	private SingletonHungry() {}
	// 外部通过一个public的getInstance()方法获取该类实例
	public static SingletonHungry getInstance() {
		return instance;
	}
}

/**
 * 懒汉模式
 * 非线程安全版
 */
class SingletonSimple {
	private static SingletonSimple instance;
	
	// 将构造器声明为private，外部无法用new获取
	private SingletonSimple() {}
	// 外部通过一个public的getInstance()方法获取该类实例
	public static SingletonSimple getInstance() {
		// 每次获取前判断该类实例是否已存在，若无则new一个
		if (instance == null) {
			instance = new SingletonSimple();
		}
		return instance;
	}

}

/**
 * 多线程下的双锁检测(Double-Check Locking)单例
 * 懒汉模式
 */
class Singleton{
	// 以volatile修饰
	private static volatile Singleton instance;
	
	private Singleton() {}
	public static Singleton getInstance() {
		// 第一次判断的目的是避免每次getInstance()都加锁
		// 若已经存在实例，直接返回
		if(instance == null) {
			synchronized (Singleton.class) {
				// 再次判断是防止两个线程在instance==null时
				// 同时进入第一个if内，由于加锁，其中一个先new了
				// 实例，此时必须再判断一次防止第二个也new一个实例
				if(instance == null) {
					instance = new Singleton();
				}
			}
		}
		return instance;
	}
}
```

<br />

### 原型模式

#### 模式说明

需要获取一个类的多个实例的场景下，若该类的构造器需要做较多的初始化工作，则创建多个实例，会因为每次创建都需要执行构造器中的初始化过程，耗时耗资源。如果在创建第一个实例后，后续实例都直接拷贝该实例，就可以避免每次获取实例时的初始化开销，对于需要修改的字段针对性修改即可，这就是原型模式。根据对拷贝对象中的引用类型字段的拷贝程度分为浅拷贝和深拷贝。

**浅拷贝**


对原型`P`的实例`p1`执行实例`p1.clone()`方法拷贝给`p2`时(`P p2 = p1.clone()`)，对`p1`内的引用类型字段(准确地说是可变引用类型)`q`(类型`Q`)，只拷贝其引用，这就是浅拷贝。Java中重写`clone`方法时若只调用`super.clone()`，则该`clone`方法在使用时即为浅拷贝，即上述`p1.q == p2.q`。注意`clone`方法在`Cloneable`接口中是`native`方法，返回类型是`Object`，赋值给`p2`时要向下转型。


```java
// 第一个实例通过new创建
P p1 = new P();
// 此后通过拷贝创建
P p2 = (P)p1.clone();
```


可以这样描述：`p1.clone()`在堆中生成一个新的实例，p2是这个新实例的引用(地址)，新实例中有`p1`的基本数据和不变类型(`final`修饰的引用类型如`String`)的独立的完整拷贝，但对于可变的引用类型，只拷贝其引用。即如果`p1`中有一可变引用字段`Q`，那么`p1.q == p2.q，p1`和`p2`中的`q`指向同一个实例，即原本`p1`中的`q`实例。

**深拷贝**

以上述浅拷贝中的描述为例，深拷贝对`p`中的可变引用类型`q`，也拷贝一个完整的`q`到新实例`p2`中(与`q`并列的独立的`Q`类型新实例)，此时`p1.q !=p2.q`。深拷贝要求`Q`自身也要实现`Cloneable`接口并重写`clone`方法。并在原型`P`的重写`clone`方法内针对`q`要有类似如下的语句。

```java
p.q = (Q)p.getQ().clone();
```

<br />

本示例定义一个`Person`类，使其实现`Cloneable`接口并通过重写`clone`方法使其支持浅拷贝。在`Person`类中定义一个不变引用类型`Country`和一个可变引用类型`WorkExperience`，展示浅拷贝`Person`时，对不变类型`Country`拷贝出一个新实例，对可变引用类型`WorkExperience`拷贝的是它的引用(地址)。作为对比，本示例定义一个`PersonDeep`类，该类内有一个可变引用类型`WorkExperienceDeep`，使`WorkExperienceDeep`也实现`Cloneable`接口并重写`clone`方法，且在`PersonDeep`类的`clone`方法中，针对可变引用l类型`WorkExperienceDeep`，执行一条对它的`clone`方法来得到它的一个实例，从而实现对`PersonDeep`的深拷贝。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**原型接口**: 通常为`Cloneable`。
**具体原型类**: 实现`Cloneable`接口并重写`clone`方法，分为深拷贝和浅拷贝。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.creation;

/**
 * 原型模式
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
		// 如下输出“false”，说明p1和p2中的引用类型Country是不同的实例
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
```

<br />

### 简单(静态)工厂模式

#### 模式说明

当客户端需要根据 **某个变量的不同值来获取不同的对象实例** 时，可以使用简单工厂模式。该模式设置了一个生产产品 (前述的对象实例) 的工厂类，此工厂类通过静态方法 (所以称为静态工厂) 传入变量参数，方法体内有选择分支 (`if-else` 或 `switch-case`)，根据参数返回具体的产品。

例如，天气是变量，客户端根据天气的不同情况，令服装工厂生产不同的服装，雨天生产雨衣，低温天气生产羽绒服，高温天气生产泳衣等等。在这个例子中服装工厂就是静态工厂，不同的衣服是不同的具体产品。 **天气情况就是工厂类里静态方法的参数** 。让不同的产品都继承同一个抽象产品类，那么抽象产品类，具体的产品类和静态工厂类就形成了简单工厂模式的完整结构。

> 注：简单工厂模式不在GoF的23种设计模式之列。

当产品类有增删改时，需要在静态工厂类中修改选择分支，因此 **简单工厂模式不满足开闭原则** 。后续的工厂方法模式将解决这个问题。

本示例以四则运算为例，演示客户端如何通过运算符从运算工厂中获取相应的运算产品，然后执行该产品的产品方法 (执行运算) 返回结果。

<br />

#### 结构

| 类/接口    | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| 静态工厂类 | 维护一个静态方法，根据参数返回具体产品类的实例 (返回类型是抽象产品类型) 。 |
| 抽象产品类 | 定义产品字段和方法(操作数`a,b`和运算方法`result()`) 。       |
| 具体产品类 | 继承抽象产品类，实现具体运算方法                             |

<br />

#### 代码演示

```java
/**
 * 简单工厂模式(静态工厂模式)
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
 * 通过一个类静态方法，根据输入返回相应的工厂类。内部通常为 if-else 或 switch-case。
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
 * 继承抽象产品类，实现抽象方法。下例为加法产品。
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
```

<br />

### 工厂方法模式

#### 模式说明

在简单工厂模式中，当出现产品类的增删改时，均需要 **修改静态产品工厂类内的选择分支** ，破坏了开闭原则。针对这个不足，可以将生产不同产品的工厂也单独成类，他们继承同一个抽象工厂类。这样在出现新产品时只增加相应的产品类和生产他的工厂类，对其他代码均无需改动其他。等到要使用这个新工厂时，再在客户端中声明这个工厂的实例，由它来生产产品即可。这就是 **符合开闭原则的工厂方法模式** 。

本示例以四则运算为例，演示客户端如何声明一个具体的运算工厂并通过该工厂生产相应的运算产品，然后执行该产品的产品方法 (执行运算) 返回结果。

<br />

#### 结构

| 类/接口    | 描述                                     |
| ---------- | ---------------------------------------- |
| 抽象工厂类 | 定义一个创建产品的抽象方法。             |
| 具体工厂类 | 继承抽象工厂类，实现创建产品的抽象方法。 |
| 抽象产品类 | 定义抽象产品方法。                       |
| 具体产品类 | 继承抽象产品类，实现抽象产品方法。       |

<br />

#### 代码演示

```java
/**
 * 工厂方法模式
 */
public class FactoryMethodDemo {

    public static void main(String[] args) {
        // 声明一个具体运算工厂
        OperFactory fa = new AddFactory();
        // 由该工厂提供运算产品
        Oper oper = fa.createOper();
        // 执行产品的运算方法得到结果，输出“7”
        System.out.println(oper.result(3, 4));
    }

}

/**
 * 抽象工厂类
 * 定义一个创建产品的抽象方法。
 */
abstract class OperFactory{
    public abstract Oper createOper();
}

/**
 * 具体工厂类
 * 继承抽象工厂类，实现创建产品的抽象方法。
 * 下例是加法工厂。
 */
class AddFactory extends OperFactory{
    @Override
    public Oper createOper() {
        return new OperAdd();
    }
}

/**
 * 具体工厂类
 * 下例是减法工厂。
 */
class SubFactory extends OperFactory{
    @Override
    public Oper createOper() {
        return new OperSub();
    }
}

/**
 * 下例是乘法工厂
 */
class MulFactory extends OperFactory{
    @Override
    public Oper createOper() {
        return new OperMul();
    }
}

/**
 * 下例是除法工厂
 */
class DivFactory extends OperFactory{
    @Override
    public Oper createOper() {
        return new OperDiv();
    }
}

/**
 * 抽象产品类
 * 定义抽象产品方法。
 * 本示例的产品为四则运算，产品方法为运算过程。
 */
abstract class Oper{
    public abstract int result(int a, int b);
}

/**
 * 具体产品类
 * 继承抽象产品类，实现抽象产品方法。
 * 下例是加法产品。
 */
class OperAdd extends Oper{
    @Override
    public int result(int a, int b) {
        return a + b;
    }
}

/**
 * 具体产品类
 * 下例是减法产品。
 */
class OperSub extends Oper{
    @Override
    public int result(int a, int b) {
        return a - b;
    }
}

/**
 * 具体产品类
 * 下例是乘法产品。
 */
class OperMul extends Oper{
    @Override
    public int result(int a, int b) {
        return a * b;
    }
}

/**
 * 具体产品类
 * 下例是除法产品。
 */
class OperDiv extends Oper{
    @Override
    public int result(int a, int b) {
        if(b == 0) {
            System.err.println("除数不能为0.");
            throw new IllegalArgumentException();
        }
        return a / b;
    }
}
```

<br />

### 抽象工厂模式

#### 模式说明

工厂方法模式的扩展，结构相似，但工厂方法中的工厂类只能生产一种产品，**抽象工厂的工厂类可以生产多种产品** 。形式上，若工厂模式中的抽象工厂类，有不止一个生产产品的抽象方法，且这些产品 (方法返回类型) 不同，具体工厂类实现该抽象工厂类时重写所有抽象方法，那它就是抽象工厂模式。

本示例以四则运算为例，演示客户端如何声明一个具体的运算工厂并获取多种不同的运算产品，然后这些产品执行其产品方法(执行运算)返回结果。

<br />

#### 结构

| 类/接口    | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| 抽象工厂类 | 定义多个创建产品的抽象方法。有多少种产品就写多少个方法。     |
| 具体工厂类 | 继承抽象工厂类，实现创建产品的抽象方法。                     |
| 抽象产品类 | 抽象工厂中有多少种不同产品的抽象方法，就有多少个抽象产品类。定义抽象产品方法。 |
| 具体产品类 | 继承抽象产品类，实现抽象产品方法。                           |

<br />

#### 代码演示

```java
/**
 * 抽象工厂模式
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
```

<br />

### 建造者模式

#### 模式说明

有一些同类型的不同对象，他们由多个部件构成，构成的方式相同，但每个部件在不同对象中可能有不同的特征细节。举例来说，无论是哪个品牌的电脑，都由CPU，显示器，硬盘内存，键盘等部件构成，只是对不同品牌不同型号的PC，部件的细节不同，例如苹果电脑的CPU是苹果自研的，联想电脑CPU是英特尔的。在客户端中完成对不同对象的建造，最直接的方式是分别声明他们，然后调用各自的建造动作来完成。但这样的做法无法利用不同对象建造过程稳定且相同(只是细节不同)的特点，达不到代码复用的效果。针对这种场景，可以使用建造者模式来实现。

建造者模式提供一个抽象建造者类，类内定义了稳定的产品建造过程，即建造具体产品各部件的抽象方法。这样具体建造者继承抽象建造者类后，可以根据需要重写某些部件的建造方法。具体建造者类内持有一个产品类实例，用于保存建造完成后的产品，并通过他的getter的方法获取该产品的实例。展示产品细节的动作在产品类中完成，这就是所谓的将复杂对象(即本模式中的产品类)的构造和表示分离，实际就是建造者类(负责产品建造)和产品类(负责表示)分离。客户端发起产品建造时，可以声明一个具体建造类，并调用其建造过程(多个部件的`build`方法)，但这样一来就对客户端暴露了建造过程，且加重了客户端的任务。避免这个缺点的办法是另外设置一个指挥者类，该类只有一个`produce`方法，方法参数是具体建造者实例，传入该实例，在`produce`内部调用该实例建造各部件的方法。通过设置指挥者类，将本来暴露在客户端中的建造者对建造过程的调用转移到了指挥者类内部。至此，客户端只需要声明一个指挥者，再声明一个具体建造者，将具体建造者作为指挥者的`produce`方法参数，指挥者执行其`produce`完成建造。执行后需要查看得到的产品细节，再调用产品建造者的`getProduce`方法即可。

本示例以汽车生产为例，展示如何利用建造者模式来生产F1赛车和普通家用汽车。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**指挥者类**: 提供一个指挥生产的方法`produce`，传入建造者，其内调用建造者的建造方法。
**抽象建造者类**: 提供建造产品各部件的抽象方法和一个返回产品的方法。
**具体建造者类**: 继承抽象建造者类，并持有一个产品实例以供获取。实现产品各部分抽象建造方法和返回产品的抽象方法。
**产品类**: 持有产品各部件的一个集合实例，有一个增加部件的`add`方法，一个用于展现组成部件的`show`方法。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.creation;

import java.util.ArrayList;
import java.util.List;

/**
 * 建造者模式(生成器模式)
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
```

<br />

## 结构型设计模式

结构型模式用于将类或对象按某种方式组成更大的结构，包含如下7种模式。

- 代理 / 适配器 / 桥接 / 装饰器 / 外观 / 享元 / 组合。

<br />

### 代理模式

#### 模式说明

客户端不直接声明实际对象 (主体类) ，而是声明代理类对象，通过 **调用代理类对象的方法来间接执行真实主体类的行为** 。

本示例中以代理售房场景为例。该场景要求房东告知房屋信息如房子类型，价格等给购房者。在客户端中不方便暴露房东时，即可采用代理模式，通过房屋中介来完成告知动作。**代理类持有主体类实例** ，即房屋中介持有一个房东实例 ，客户端只声明代理类，调用代理类实例中与房东类相同的方法来完成。房东类和代理类都是目标接口实现类，实现相同的抽象方法，代理类实现该抽象方法的方式仅仅是调用其持有的房东类的方法。



JDK 中的 Thread 即实现了代理模式。

<br />

#### 结构

| 类/接口    | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| 抽象主体类 | 定义真实主体的方法。                                         |
| 具体主体类 | 继承抽象类，实现抽象方法。                                   |
| 代理类     | 内部持有一个具体主体类的实例，通过调用该具体主题实例的方法来实现抽象主题类的抽象方法。 |

<br />

#### 代码演示

```java
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
```

<br />

### 适配器模式

#### 模式说明

当前有一工具(一个已存在的类)，能实现若干功能(方法)，需要在某平台(目标)上使用，但与平台的接口不同(已存在的类的方法与接口方法名不同)，需提供一个适配器(适配器类)转换为平台接口。这种场景可以使用适配器模式实现。与代理模式比较类似，但代理类中的`RealSubject`和`Proxy`都需要实现`Subject`抽象类。而适配器模式中的`Adaptee`类是原本就已存在的类，内部的方法与接口中定义的抽象方法不同(行为一致，方法名不同)。

本示例中，以`Typec`为抽象目标类，其内有标准充电方法`typecChange`和标准数据传输方`typecDataTran`。现有`Typeb`类和`Lightning`类，均有自己的充电方法和数据传输方法。演示如何为他们设置适配器类，使得客户端能够通过调用适配器中的符合`Typec`的标准方法来间接使用`Typeb`和`Lightning`的充电和数据传输功能(方法)。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象目标类**: 定义抽象标准方法。
**被适配类**: 有与目标类相同功能的方法，但接口(方法名)不符合标准接口(与目标类内定义的方法的方法名不同)。
**适配器类**: 继承目标抽象类，内部持有一个被适配类实例，实现目标抽象类的抽象方法，在方法内部调用其持有的被适配类的原有方法。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.structure;

/**
 * 适配器模式
 */
public class AdapterDemo {

    public static void main(String[] args) {
        // 使用非标准物件时，声明其适配器
        Typec t1 = new AdapterB2C();
        Typec t2 = new AdapterL2C();
        // 通过调用对应适配器的标准方法，间接使用
        // 输出“使用TypeB接口充电”
        t1.typecCharge();
        // 输出“使用TypeB接口传输数据”
        t1.typecDataTrans();
        // 输出“使用TypeB接口充电”
        t2.typecCharge();
        // 输出“使用TypeB接口传输数据”
        t2.typecDataTrans();
    }

}

/**
 * 抽象目标类
 * 定义抽象标准方法。
 * 下例是Typec方式的充电方法和数据传输方法。
 */
abstract class Typec{
    public abstract void typecCharge();
    public abstract void typecDataTrans();
}

/**
 * 抽象目标类的继承类
 * 在本模式中不是必须的，只是为了展示无需适配的情况。
 */
class HuaweiTypeC extends Typec{
    @Override
    public void typecCharge() {
        System.out.println("使用TypeC接口充电");
    }
    @Override
    public void typecDataTrans() {
        System.out.println("使用TypeC接口传输数据");
    }
}

/**
 * 被适配类(需要适配的类)
 * 有与目标类相同功能的方法，但接口(方法名)不符合标准接口(与目标类内定义
 * 的方法的方法名不同)。
 * 下例是Typeb类，使用Typeb方式的充电和数据传输方法。
 */
class Typeb{
    public void typebCharge() {
        System.out.println("使用TypeB接口充电");
    }
    public void typebDataTrans() {
        System.out.println("使用TypeB接口传输数据");
    }
}

/**
 * 适配器类
 * 继承目标抽象类，内部持有一个被适配类实例，实现目标抽象类的抽象方法，在
 * 方法内部调用其持有的被适配类的原有方法。
 * 下例是Typeb转Typec的适配器。
 */
class AdapterB2C extends Typec{
    Typeb typeb = new Typeb();
    
    @Override
    public void typecCharge() {
        typeb.typebCharge();
    }
    @Override
    public void typecDataTrans() {
        typeb.typebDataTrans();
    }
}

/**
 * 被适配类(需要适配的类)
 * 下例是Lightning类，使用Lightning方式的充电和数据传输方法。
 */
class Lightning{
    public void lightningCharge() {
        System.out.println("使用Lightning接口充电");
    }
    public void lightningDataTrans() {
        System.out.println("使用Lightning接口传输数据");
    }
}

/**
 * 适配器类
 * 下例是Lightning转Typec的适配器。
 */
class AdapterL2C extends Typec{
    Lightning ln = new Lightning();
    
    @Override
    public void typecCharge() {
        ln.lightningCharge();;
    }
    @Override
    public void typecDataTrans() {
        ln.lightningDataTrans();
    }
}
```

<br />

### 桥接模式

#### 模式说明

当一个事物可以通过多个维度描述时，要避免多个维度层层继承该事物的抽象类，而应该将这些不同的维度从事物中分离出来独立变化。例如电脑可以通过不同品牌和不同软件描述，可以将电脑作为一个`PC`抽象类，以品牌为主体，不同品牌继承该`PC`类如`ApplePC`。软件分离出来成为一个独立的抽象类`Software`，具体软件继承该`Software`抽象类，例如`Browser`类。在PC抽象类中以组合形式拥有`Software`，并通过调用`Software`的方法，实施`Software`中的行为。在UML图中分离事物抽象类通过带组合箭头的线与主体事物抽象类连接，故名桥接。

本示例演示上述PC场景。如果希望增加一个华为品牌PC只需增加继承`PC`类的`HuaweiPC`类即可，无需改动其他类。同理希望增加视频软件时，也只需要增加`VideoPlayer`类而无需关注品牌。这就是各自独立变化。另，本示例中主体事物`PC`只持有一个`Software`实例，如果想同时持有多个`Software`实例，可以声明为集合类型(如`List`)，然后增加相应的`add`方法。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象主体事物类**: 主体事物，类内声明一个以protected修饰的分离事物(以抽象类型声明)。声明一个用于实施分离事物行为的抽象方法。
**具体主体事物类**: 继承抽象主体事物类，实现抽象方法。
**抽象分离事物类**: 能够描述主体事物的某个维度的事物，声明自己的行为。
**具体分离事物类**: 继承抽象分离事物类，实现抽象方法。

<br />

#### 代码示例

```java
package com.yukiyama.pattern.structure;

/**
 * 桥接模式
 */
public class BridgeDemo {

    public static void main(String[] args) {
        // 声明一个PC(主体事物)
        PC pc = new ApplePC();
        // 声明一个软件(分离事物)
        Software soft1 = new Browser();
        // 将软件组合进PC中(主体持有分离)
        pc.setSoftware(soft1);
        // 通过调用主体事物的方法来间接执行分离事物的方法
        // 输出"启动: Browser"
        pc.run();
        // 增加一个软件后，同上
        Software soft2 = new MusicPlayer();
        pc.setSoftware(soft2);
        // 输出"启动: MusicPlayer"
        pc.run();
    }

}

/**
 * 主体事物抽象类
 * 以PC为例，内部声明protected修饰的分离事物Software。非抽象方法
 * setSoftware()传入分离事物使得主体持有分离。声明用于实施Software
 * 行为抽象方法run()。
 */
abstract class PC{
    protected Software soft;
    
    public void setSoftware(Software soft) {
        this.soft = soft;
    }
    public abstract void run();
}

/**
 * 主体事物具体类
 * 继承抽象主体事物类，实现抽象方法run，内部实际调用Software自身的run。
 */
class ApplePC extends PC{
    @Override
    public void run() {
        this.soft.run();
    }
}

/**
 * 抽象分离事物类
 * 有自身的属性和方法。
 * 下例是从软件维度描述PC的软件抽象类。
 */
abstract class Software{
    private String name;
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public abstract void run();
}

/**
 * 具体分离事物类
 * 继承抽象分离事物类，实现抽抽象方法。
 * 下例是浏览器类。
 */
class Browser extends Software{
    public Browser() {
        this.setName("Browser");
    }
    @Override
    public void run() {
        System.out.println("启动: "+this.getName());
    }
}

/**
 * 具体分离事物类
 * 下例是音乐播放器类。
 */
class MusicPlayer extends Software{
    public MusicPlayer() {
        this.setName("MusicPlayer");
    }
    @Override
    public void run() {
        System.out.println("启动: "+this.getName());
    }
}
```

<br />

### 装饰器模式

#### 模式说明

使用组合关系来创建一个可以被层层包装的对象(即装饰对器象)来包裹被装饰的主体对象，并在保持主体对象的类结构不变的前提下，通过装饰器对象自己的方法，为被装饰的主体对象提供额外的功能(装饰)。

本示例以技术为抽象主体类(`TechComponent`)，一项核心技术为具体主体类(`JavaComponent`)。以其他扩展技术为抽象装饰类(`TechDecorator`)，其具体装饰类为web技术(`WebTechDecorator`)和Spring技术(`SpringTechDecorator`)。演示如何在核心技术上扩展"web"和"Spring"技术。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**主体接口**: 定义一种行为方法。
**具体主体类**: 实现抽象主体接口，实现抽象方法。
**抽象装饰器类**: 装饰部件抽象类，实现抽象主体接口，实现抽象方法。
**具体装饰器类**: 继承抽象装饰类，拥有自己的行为方法的同时实现抽象方法，并在其中调用自己的行为方法以及完成其所装饰的主体的行为方法。即所谓在保持主体对象不变的前提下，为其提供额外功能。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.structure;

/**
 * 装饰器模式
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
 * 声明主体的行为方法funcc()
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
```

<br />

### 外观模式

#### 模式说明

当客户端需要通过调用多个对象各自不同的方法时，可以建立一个持有所有这些对象的外观类，根据客户端对上述多个对象的需要，外观类内有多个方法，这些方法内部调用多个不同对象的方法以满足客户端的不同需求。如此客户端就只需要调用外观类的方法，而无需访问多个对象。例如投资者买股票，投资者是客户端，不同公司的股票形成数量众多的对象。此时投资者可以通过基金经理来间接买股票，基金经理就是外观类，他通过不同的投资策略封装了多个股票的买入和卖出等方法，投资者只需要调用基金经理不同的策略方法即可，无需了解每一支股票。

本示例以上述投资者买股票场景为例，演示外观模式的使用。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**外观类**: 持有所有子系统的信息，并为客户端提供了一系列访问子系统的方法，这些方法内根据需要调用一个或多个子系统的方法。
**子系统类**: 可以有多个子系统，每个子系统都有各自的方法。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.structure;

/**
 * 外观模式
 */
public class FacadeDemo {

    public static void main(String[] args) {
        // 声明一个外观类
        Fund f = new Fund();
        System.out.println("====执行基金策略1====");
        // 执行既定策略1
        f.strategy1();
        System.out.println("====执行基金策略2====");
        // 执行既定策略2
        f.strategy2();
    }

}

/**
 * 外观类
 * 下例为基金，持有三支股票对象，有两个策略方法，封装对三只股票的不同行为组合。
 */
class Fund{
    private StockApple sa = new StockApple();
    private StockMaotai sm = new StockMaotai();
    private NationalDebt nd = new NationalDebt();
    
    public void strategy1() {
        sa.buy();
        sm.toYuebao();
        nd.sell();
    }
    
    public void strategy2() {
        sa.sell();
        sm.sell();
        nd.buy();
    }
}

/**
 * 子系统类
 * 苹果公司股票
 */
class StockApple{
    public void sell() {
        System.out.println("卖出Apple股票");
    }
    public void buy() {
        System.out.println("买入Apple股票");
    }
}

/**
 * 子系统类
 * 茅台股票
 */
class StockMaotai{
    public void toYuebao() {
        System.out.println("Maotai股票转入余额宝");
    }
    public void sell() {
        System.out.println("买入Maotai股票");
    }
}

/**
 * 子系统类
 * 国债
 */
class NationalDebt{
    public void sell() {
        System.out.println("卖出国债");
    }
    public void buy() {
        System.out.println("买入国债");
    }
}
```

<br />

### 享元模式

#### 模式说明

某些对象相似度很高，只有少量字段或方法不同，当需要大量这类对象时，若为每一次需求都创建实例，内存开销会很大。此时可以提炼出这些对象的相同部分，作为所谓的享元，创建一个享元工厂类，持有享元实例。需要使用上述对象时，从享元工厂中获取该实例。若这些对象有不同部分，提取这些不同部分单独成类，使用时作为享元中的方法参数传入享元(使相同和不同部分结合)。这样无论使用多少次，对于享元而言，只有一个实例的开销。

本示例以网站为抽象享元，博客网站和电子商务网站为具体享元，用户为外部非享元。展示如何通过享元工厂类添加和获取享元，如何将非享元作为享元方法的参数传入到享元内。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象享元类**: 被使用对象中提炼出的相同内容的集合，定义享元的抽象方法。
**具体享元类**: 继承抽象享元类，实现抽象方法。
**非享元类**: 需要使用但不能被提炼为享元内容的部分，单独成类，需要时传入享元。
**享元工厂类**: 持有具体享元实例，有添加享元和获取享元的方法。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.structure;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元模式
 */
public class FlyweightDemo {

    public static void main(String[] args) {
        // 声明享元工厂
        WebsiteFactory fa = new WebsiteFactory();
        // 声明具体享元博客网站和电子商务网站
        Website blog = new BlogWebsite("Blog");
        Website ec = new BlogWebsite("EC");
        // 向享元工厂中添加如上两种享元
        fa.addFlyweight(blog);
        fa.addFlyweight(ec);
        // 通过享元工厂，创建两个博客网站和两个电子商务网站
        Website blog1 = fa.getFlyweight("Blog");
        Website blog2 = fa.getFlyweight("Blog");
        Website ec1 = fa.getFlyweight("EC");
        Website ec2 = fa.getFlyweight("EC");
        // 将两个博客网站和两个电子商务网站分给不同的使用者
        blog1.use(new User("莫小言"));
        blog2.use(new User("金大庸"));
        ec1.use(new User("马风"));
        ec2.use(new User("刘强西"));
        // 如下均输出“true”，即通过享元工厂获取的多个享元以及
        // 工厂内持有的享元均为同一个。
        System.out.println(blog == blog1 && blog1 == blog2);
        System.out.println(ec == ec1 && ec1 == ec2);
    }

}

/**
 * 享元工厂类
 * 以Map数据结构持有享元，key为网站类型，value为Website实例。
 * 实现添加享元，获取享元的方法。
 * 客户端声明享元工厂后，需要继续创建享元并将其添加进享元工厂中。
 */
class WebsiteFactory{
    private Map<String, Website> flyweights = new HashMap<>();
    
    public void addFlyweight(Website web) {
        if(!flyweights.containsKey(web.getCatagory())) {
            flyweights.put(web.getCatagory(), web);
        } else {
            System.out.println("已存在该享元。");
        }
    }
    
    public Website getFlyweight(String key) {
        if(!flyweights.containsKey(key)) {
            System.out.println("无此享元。");
            return null;
        } else {
            return flyweights.get(key);
        }
    }
    
}

/**
 * 享元抽象类
 * 声明享元的字段和相关方法。
 */
abstract class Website{
    private String catagory;
    
    public Website(String catagory) {
        this.catagory = catagory;
    }
    public String getCatagory() {
        return catagory;
    }
    public abstract void use(User user);
}

/**
 * 具体享元类
 * 继承享元抽象类，实现抽象方法。
 * 如下是博客网站类。
 */
class BlogWebsite extends Website{
    public BlogWebsite(String catagory) {
        super(catagory);
    }
    @Override
    public void use(User user) {
        System.out.printf("这是一个%s网站，提供文章发布服务。\n", getCatagory());
        System.out.printf("网站用户为%s。\n", user.getUser());
    }
}

/**
 * 具体享元类
 * 如下是电子商务网站类。
 */
class ECWebsite extends Website{
    public ECWebsite(String catagory) {
        super(catagory);
    }
    @Override
    public void use(User user) {
        System.out.printf("这是一个%s网站，提供商品发布服务。\n", getCatagory());
        System.out.printf("网站用户为%s。\n", user.getUser());
    }
}

/**
 * 非享元类
 * 享元Website需要结合非享元User使用，例如对博客网站来说，他们可能共用
 * 相同的文章编辑器控件(作为享元的一部分)，但各自使用的用户不同(非享元)。
 */
class User{
    private String user;
    
    public User(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }
}
```

<br />

### 组合模式

#### 模式说明

一系列对象可以用树来描述，形成树-子树-叶子的层次机构。如一家大型公司，根节点是总公司，下设若干子公司和若干非子公司的直属部门如总公司财经部，总公司人力资源部门。子公司中如同总公司结构，也包含它自己的子公司和直属部门。则子公司对象是所属公司对象的子树，直属部门对象是所属公司对象的叶子。客户端对单个对象和组合对象具有一致的访问性。即上层公司A(根)和其子公司B(子树)，其直属部门C(树叶)继承同一个抽象构件类，有相同的方法。但因为树叶不再有子节点，根据树叶中是否包含针对子节点的方法(例如`add`增加儿子，`remove`，`getChild`等)，分为透明方式和安全方式。

本示例以总公司和子公司场景为例，演示透明方式的组合模式。在客户端中将树枝和树叶机构加入到根(总公司)中，然后调用总公司的方法，实现对其下属机构的递归调用。

**透明方式**
树叶与树枝具有同样的，在抽象构件类中声明的针对子节点的方法时，对客户端而言，一个对象是树枝还是树叶是透明的，客户端无需关心。这种方式下，树叶无儿子，却仍需实现针对儿子的各种方法(空方法或抛出异常)，存在安全性问题。

**安全方式**
与透明方式相对，不在抽象构件类和树叶实现类中声明针对儿子的方法，只在树枝中实现。缺点是客户端需要提前知道哪些对象是树枝，哪些是树叶，失去透明性。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象构件类**: 树枝和树叶的抽象类，声明了公共抽象方法，实现默认行为。
**具体实现树枝类**: 继承抽象构件类，具有儿子的对象，包含针对儿子的各种方法。
**具体实现树叶类**: 继承抽象构件，不具有儿子的对象。若包含针对儿子的各种方法为透明方式，不包含则为安全模式。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式
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
```

<br />

## 行为型设计模式

行为型模式用于描述多个对象间如何互相协作，完成单个对象难以完成的任务，包含如下11种行为型模式。

- 模板方法 / 策略 / 命令 / 责任链 / 状态 / 观察者 / 中介者 / 迭代器 / 访问者 / 备忘录 / 解释器

<br />

### 模板方法模式

#### 模式说明

有这样一些对象，他们都要执行若干动作，这些动作中多数类似或完全相同，例如不同客户到银行处理业务，都需要取号，排队，与柜员交流，处理后对服务评分等。在这里取号，排队，评分动作是完全相同的，与柜员交流的细节可能不同，可能是办理存款业务，也可能是取款，购买基金等。针对此类场景，可以将设置一个抽象模板类，类内声明相关方法，再定义一个驱动方法来调用这些相关方法。这样子类继承抽象模板类后自动复用了大部分的代码，对于具体实现中有区别的细节，在子类中重写即可。

本示例以大学生和小学生入学活动为例，将入学活动中相同的部分放在抽象模板类中，子类继承时自动获得，而子类中不同部分在抽象类中定义为抽象方法或钩子方法，具体实现延迟到子类中。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象模板类**: 定义方法骨架，并确定一个`procedure`方法，将需要执行的方法集中放置其中，客户端调用该`procedure`即可统一执行多个既定的方法。该类内方法分为普通方法，抽象方法和钩子方法。
**具体实现类**: 继承抽象模板类，按自身情况实现其中的各种方法。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

/**
 * 模板方法模式
 */
public class TemplateMethodDemo {

    public static void main(String[] args) {
        Admission ad1 = new CollegeAdmission();
        System.out.println("====大学生的入学活动====");
        ad1.templateProcedure();
        System.out.println("====小学生的入学活动====");
        Admission ad2 = new PrimarySchoolAdmission();
        ad2.templateProcedure();
    }

}

/**
 * 抽象模板类
 * 定义方法骨架，并确定一个procedure方法，将需要执行的方法集中放置，客户端
 * 调用该procedure即可统一执行多个既定的方法。该类内方法分为普通方法，抽象
 * 方法和钩子方法。普通方法是所有子类均的共同步骤，且细节一致。抽象方法也是
 * 共同步骤，但细节不同，延迟到子类中实现。钩子方法分为简单钩子和挂载钩子方法。
 * 对于简单钩子方法，抽象类中将其声明为空方法体的普通方法，子类可以选择重写
 * 扩展，若不重写则执行空方法体(相当于不执行)。对于挂载钩子方法，抽象类中先
 * 定义一个用于判断是否执行挂载钩子方法的返回boolean类型的抽象方法。再在挂载
 * 钩子方法中通过一个if-else判断上述boolean类型方法的返回值，true则执行，
 * false则进入空分支(相当于不执行)。boolean类型抽象方法延迟到子类实现，由
 * 子类决定是否进入执行挂载钩子方法的执行分支。挂载钩子方法本身在抽象类中可以
 * 作为普通方法，这样子类中就不必重写了。
 */
abstract class Admission{
    public void templateProcedure() {
        healthCheck();
        payTuition();
        militaryTraining();
        morningExercise();
        registerClasses();
    }
    public void healthCheck() {
        System.out.println("到教育部指定体检机构体检。");
    }
    public void payTuition() {
        System.out.println("通过教育部统一学费缴纳平台缴费。");
    }
    // 将军训设置为一个简单钩子方法，具体实现类可选择实现
    public void militaryTraining() {};
    // 将早操设置为一个挂载钩子方法，具体实现类中先实现hasMorningExcercise，
    // true则执行(要做早操)，false不执行(进入空分支，相当于不执行)
    public abstract boolean hasMorningExcercise();
    public void morningExercise() {
        if(hasMorningExcercise()) {
            System.out.println("要做早操。");
        } else {}
    }
    public abstract void registerClasses();
    public abstract void choseDormitory();
}

/**
 * 具体实现类
 * 继承抽象模板类，按自身情况实现其中的各种方法。
 * 下例是大学生入学活动类。
 */
class CollegeAdmission extends Admission {
    // 具体实现类中扩展钩子方法
    @Override
    public void militaryTraining() {
        System.out.println("高校新生入学后要参加军训。");
    }
    // 重写抽象类中的挂载钩子方法的触发方法
    @Override
    public boolean hasMorningExcercise() {
        return false;
    }
    @Override
    public void registerClasses() {
        System.out.println("高校新生课程注册由新生自行完成。");
    }

    @Override
    public void choseDormitory() {
        System.out.println("高校新生入学后请在校内宿舍系统内选择宿舍。");
    }
}

/**
 * 具体实现类
 * 下例是小学生入学活动类。
 */
class PrimarySchoolAdmission extends Admission{
    // 没有实现抽象类中的简单钩子方法militaryTraining
    // 重写抽象类中的挂载钩子方法的触发方法
    @Override
    public boolean hasMorningExcercise() {
        return true;
    }
    @Override
    public void registerClasses() {
        System.out.println("小学新生课程注册由班主任完成。");
    }
    
    @Override
    public void choseDormitory() {
        System.out.println("小学新生原则上不提供校内住宿，特殊情况请单独申请。");
    }
}
```

<br />

### 策略模式

#### 模式说明

处理同样的事情可以有多种方法，例如排序数组，可以用冒泡，选择，插入，希尔，快速等等排序方法，对于同样的输入有多种策略能来处理并输出不同策略处理的结果。在程序中使用某一种策略，最直接的方式是在在客户端中将不同的策略写入`if-else`或`switch-case`的分支，然后由客户端进行选择。缺点是客户端职责过大，违背单一职责原则，其次是某个策略变动需要修改客户端代码，违背开闭原则。针对此类场景可以使用策略模式。每一种策略单独成类，均继承一个抽象策略类，设置一个持有策略类的环境，客户端不必了解每个策略细节，而是声明某个具体策略后，借由上下文类来执行其中的策略动作。如需增减策略，只需要增减相应的策略类。一个已存在的策略出现变动，也只需要修改该策略类本身，增删改都不会影响已有客户端程序，满足开闭原则。一个策略类实现一种策略方法，满足单一职责原则。

本示例以处理收款为例，演示客户端如何通过环境类来指定某种收款策略得到应收结果。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象策略类**: 定义一个收款抽象方法`acceptCash`。
**具体策略类**: 继承抽象策略类，实现具体策略。
**环境类(上下文类):** 持有一个收费策略类，维护一个收款方法，该方法内实际调用收费策略类的收款方法。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

/**
 * 策略模式
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
```

<br />

### 命令模式

#### 模式说明

请求某个命令执行者执行命令时，最直接的方式是客户端直接传入命令给执行者，但这样就无法管理命令(或者说只能由客户端来管理)，加重了客户端的负担。此类场景可以将命令封装成类，其内持有命令执行者实例，另有一个`execute`方法，该方法调用命令执行者实例的执行方法。再引入一个命令请求者(或称命令传递者，命令管理者)类来持有命令集合实例，其内有追加命令方法，删除命令方法，最重要的是有一个传递命令的方法`request`，该方法通过调用其持有的命令对象的`execute`方法来实现请求(`execute`内部调用了命令执行者自身的执行方法)。

本示例展示点菜场景下，命令请求者服务员(`Waiter`类)如何管理命令(`Order`类)，并通过`request`调用`Order`内的`excute`方法，让厨师(`Chef`类)执行做菜方法。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象命令类**: 持有一个命令执行者`Chef`类，并通过带参构造方法初始化`Chef`属性。有一个抽象方法`execute`。
**具体命令类**: 继承抽象命令类，实现`execute`方法，内部调用Chef的做菜方法。
**命令执行类**: 执行命令的角色`Chef`，内有做菜方法(本例中的`makeMutton`和`makeChickenWings`)。
**命令请求者类**: 管理命令和请求执行者执行命令的角色，类内以`List`持有多个命令实例。维护追加命令方法`addOrder`，撤销命令方法`cancelOrder`和请求方法`request`。`request`内遍历`Order`并调用其`execute`方法。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令模式
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
```

<br />

### 责任链模式

#### 模式说明

工作中可能有这样的请求处理场景，例如假期审批，会根据申请假期的天数长短，由不同权限的主管者审批，但提交申请时总是提交给自己的直接主管，由直接主管来判断是否需要上升到更高权限的管理者来审批。类似场景可以描述为一个请求根据其内容由不同级别的处理者处理，申请入口总是最低级别的处理者。在客户端中处理此类请求，最简单直接的做法用`if-else`语句遍询所有处理者，最终总能够被某一权限的处理者处理。这种做法的缺点是客户端责任太大，违背了单一职责原则，另外可扩展性也很差，当需要修改或删减处理者时，需要修改客户端代码。对于这种场景可以使用责任链模式实现，创建每一层级的处理者类，内部均实现一个处理方法，请求从最底层处理者开始，每一层级处理者处理请求时，如判断不在权限范围内，则向后继处理者传递该请求，直至最后一层。这种做法很好地实践了单一职责原则，由于每一层级处理者均为一类，对处理者的变化也有良好的扩展能力。

本示例以假期申请为例，演示不同天数的申请如何通过责任链传递从最低层级管理者传递到具有相应权限的管理者处并得到处理。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象处理者类**: 处理请求的对象，类内实现一个设置后继处理者的普通方法，定义一个处理请求的抽象方法。
**具体处理者类**: 继承抽象处理者类，实现抽象方法。
**请求类(可选)**: 当请求比较复杂时，可以将其封装成类。当请求比较简单，如申请假期的场景，可以只用`int`表示请求。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

/**
 * 责任链模式
 */
public class ResponsibilityChainDemo {

    public static void main(String[] args) {
        // 声明责任链上所有级别的处理者
        // L1处理者有小于3天的请假审批权
        Manager L1 = new L1Manager("一级主管");
        // L2处理者有3到10天的请假审批权
        Manager L2 = new L2Manager("二级主管");
        // L3处理者有20天以内的请假审批权，超过则驳回
        Manager L3 = new L3Manager("三级主管");
        // 从低到高，每个层级的处理者设置自己的上级(后继)处理者
        // 使请求能够从最低级处理者开始传递到最高级处理者
        L1.setSuperior(L2);
        L2.setSuperior(L3);
        
        // 声明一个请求并设置请求的内容
        Application app = new Application();
        app.setDaysNum(2);
        // 每次申请均只需由最低级处理者一级主管L1来执行申请
        // 输出"一级主管批准2天请假申请。"
        L1.apply(app);
        
        app.setDaysNum(5);
        // 输出“二级主管批准5天请假申请。”
        L1.apply(app);
        
        app.setDaysNum(15);
        // 输出“三级主管批准15天请假申请。”
        L1.apply(app);
        
        app.setDaysNum(21);
        // 输出“该请假申请天数为21天，三级主管驳回超过20天假期申请。”
        L1.apply(app);
    }

}

/**
 * 抽象处理者类
 * 持有一个name属性，并在有参构造器中初始化该属性。持有自己的后继处理者
 * superior，通过普通方法setSuperior设置。定义了一个处理申请的抽象
 * 方法apply(Application app)。
 */
abstract class Manager{
    protected String name;
    protected Manager superior;
    
    public Manager(String name) {
        this.name = name;
    }
    public void setSuperior(Manager superior) {
        this.superior = superior;
    }
    public abstract void apply(Application app);
}

/**
 * 具体处理者类
 * 继承抽象处理者类，实现了抽象方法apply，根据申请内容的不同，不能处理时
 * 调用后继处理者的apply方法交由后继处理者处理。
 * 下例是最低级别的处理者L1。
 */
class L1Manager extends Manager{
    public L1Manager(String name) {
        super(name);
    }
    @Override
    public void apply(Application app) {
        if(app.getDaysNum() < 3) {
            System.out.printf("%s批准%d天请假申请。\n", name, app.getDaysNum());
        } else {
            if(superior != null) {
                superior.apply(app);
            }
        }
    }
}

/**
 * 具体处理者类
 * 下例是L1的后继处理者L2。
 */
class L2Manager extends Manager{
    public L2Manager(String name) {
        super(name);
    }
    @Override
    public void apply(Application app) {
        if(app.getDaysNum() >= 3 && app.getDaysNum() <= 10) {
            System.out.printf("%s批准%d天请假申请。\n", name, app.getDaysNum());
        } else {
            if(superior != null) {
                superior.apply(app);
            }
        }
    }
}

/**
 * 具体处理者类
 * 下例是L2的后继处理者L3。
 */
class L3Manager extends Manager{
    public L3Manager(String name) {
        super(name);
    }
    @Override
    public void apply(Application app) {
        if(app.getDaysNum() > 10 && app.getDaysNum() <= 20) {
            System.out.printf("%s批准%d天请假申请。\n", name, app.getDaysNum());
        } else {
            System.out.printf("该请假申请天数为%d天，%s驳回超过20天假期申请。\n", app.getDaysNum(), name);
        }
    }
}

/**
 * 请求类(可选)
 * 当请求包含较多属性时，可以将请求封装成类。当请求比较简单如申请放假天数，
 * 也可以只用基本数据类型。
 */
class Application{
    private int daysNum;

    public int getDaysNum() {
        return daysNum;
    }
    public void setDaysNum(int daysNum) {
        this.daysNum = daysNum;
    }
    
}
```

<br />

### 状态模式

#### 模式说明

一个对象根据自身属性的变化而做出相应的动作，生活中该场景很常见，例如根据心情的不同，人(心情是人的属性)会做出不同的事情，例如高兴时手舞足蹈，悲伤时哭泣等等。在客户端实现此类场景时，最简单的做法是`if-else`或`switch-case`做分支判断/选择。缺点是显而易见的，当变量变化的种类增加时，对象需要相应地增加处理分支，又或者某一变化的动作需要修改时，也要改动源码，这将违背开闭原则。客户端责任过多，违背单一职责原则，另外程序也不易扩展。对于此类场景，可以使用状态模式实现。以一个环境类(上下文类)代表前述对象，其内部持有当前状态属性值和当前状态实例。环境类主要维护三个方法，状态属性值设置方法，状态实例设置方法，对应当前状态的响应方法，该响应方法内部调用不同状态类的响应方法。不同状态单独成类，均继承自一个抽象状态类。客户端使用时，声明环境类，设置状态值后执行环境类的响应方法。方法内实际调用由声明环境类时通过构造器初始化的默认状态实例的响应方法。每个状态类的响应方法会判断状态值，符合本状态实例的要求就处理，否则将自身状态修改为下一个状态(新状态实例赋值给状态对象属性)。如此就可以实现状态的自动转移，直到某个能响应的状态或到最后一个状态也未能响应，处理结束。

**状态模式与责任链模式比较**
与责任链模式的相似点是都有处理传递的动作，不同之处是责任链模式中所有层级的处理者对象共存，从低往高传。状态模式则是一个环境对象从某个状态开始响应状态值，在当前状态不能响应的情况下，自身状态改变为下一状态，不存在多状态共存的情况

<br />

本示例以根据分数定等级的场景为例，演示如下内容。客户端为一个分数评级类(环境类)
`ScoreLevel`设置分数后，执行该评级类的响应方法`queryLevel`返回该分数对应的等级。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象状态类**: 持有状态的一些属性，定义一个状态响应方法。
**具体状态类**: 继承抽象状态类，实现抽象状态类的抽象方法。结合传入的分数评级类(环境类)判断响应或转移状态。
**环境类(上下文类)**: 持有当前状态值和当前状态实例。维护四个方法，`getScore/setScore`，`setScoreState`用于设置当前状态实例，`queryLevel`响应当前状态，返回评级结果。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

/**
 * 状态模式
 */
public class StateDemo {

    public static void main(String[] args) {
        // 声明一个环境类
        ScoreLevel scoreLevel = new ScoreLevel();
        // 给出当前状态值
        scoreLevel.setScore(50);
        // 输出“该成绩等级为D。”
        String level1 = scoreLevel.queryLevel();
        System.out.printf("该成绩等级为%s。\n", level1);
        
        scoreLevel.setScore(60);
        String level2 = scoreLevel.queryLevel();
        // 输出“该成绩等级为C。”
        System.out.printf("该成绩等级为%s。\n", level2);
        
        scoreLevel.setScore(80);
        String level3 = scoreLevel.queryLevel();
        // 输出“该成绩等级为B。”
        System.out.printf("该成绩等级为%s。\n", level3);
        
        scoreLevel.setScore(90);
        String level4 = scoreLevel.queryLevel();
        // 输出“该成绩等级为A。”
        System.out.printf("该成绩等级为%s。\n", level4);
    }

}

/**
 * 抽象状态类
 * 持有状态的一些属性，定义一个状态响应方法，参数是环境类实例。
 * 本示例只有一个等级属性。
 */
abstract class ScoreState{
    protected String level;
    public abstract String queryLevel(ScoreLevel sl);
}

/**
 * 具体状态类
 * 实现抽象状态类的抽象方法。通过传入的分数评级类(环境类)获取到当前状态值
 * (分数)，判断是否可以响应，可以时返回响应结果，否则调用分数评级类的的
 * setScoreState方法，将当前状态实例设置为C等级状态(状态转移)。
 * 下例是D等级的状态类，为D对应的分数做出响应，令level属性值为D并返回。
 * 判断不能响应时将状态改为C等级状态。
 */
class DScoreState extends ScoreState{
    @Override
    public String queryLevel(ScoreLevel sl) {
        if(sl.getScore() < 60) {
            level = "D";
            return level;
        } else {
            sl.setScoreState(new CScoreState());
            return sl.queryLevel();
        }
    }
}

/**
 * 具体状态类
 * 下例是C等级的状态类，为C对应的分数做出响应，令level属性值为C并返回。
 * 判断不能响应时将状态改为B等级状态。
 */
class CScoreState extends ScoreState{
    @Override
    public String queryLevel(ScoreLevel sl) {
        if(sl.getScore() >= 60 && sl.getScore() < 80) {
            level = "C";
            return level;
        } else {
            sl.setScoreState(new BScoreState());
            return sl.queryLevel();
        }
    }
}

/**
 * 具体状态类
 * 下例是B等级的状态类，为B对应的分数做出响应，令level属性值为B并返回。
 * 判断不能响应时将状态改为A等级状态。
 */
class BScoreState extends ScoreState{
    @Override
    public String queryLevel(ScoreLevel sl) {
        if(sl.getScore() >= 80 && sl.getScore() < 90) {
            level = "B";
            return level;
        } else {
            sl.setScoreState(new AScoreState());
            return sl.queryLevel();
        }
    }
}

/**
 * 具体状态类
 * 下例是A等级的状态类，为A对应的分数做出响应，令level属性值为A并返回。
 * 最终状态，无转移。
 */
class AScoreState extends ScoreState{
    @Override
    public String queryLevel(ScoreLevel sl) {
        level = "A";
        return level;
    }
}

/**
 * 环境类(上下文类)
 * 持有当前状态值int score和当前状态实例ScoreState state。通过无参
 * 构造器初始化state为DScoreState。维护四个方法，getScore/setScore
 * 方法是score的getter/setter；setScoreState用于设置当前状态实例，
 * 状态转移时使用；queryLevel是状态响应方法，通过调用当前状态实例的
 * queryLevel方法返回评级结果。
 * 下例是一个分数评级类。
 */
class ScoreLevel{
    private int score;
    private ScoreState state;
    
    // 通过构造器初始化当前状态为DScoreState
    public ScoreLevel(){
        state = new DScoreState();
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        if(score >= 0 && score <= 100) {
            this.score = score;
        } else {
            System.out.println("分数输入有误。");
        }
    }
    public void setScoreState(ScoreState state) {
        this.state = state;
    }
    public String queryLevel() {
        return state.queryLevel(this);
    }
}
```

<br />

### 观察者模式

#### 模式说明

当一个对象变化时，所有与他有关联关系的对象都将因为这个变化而变化，将前者称为目标对象，后者称为观察者对象。例如以人行道上的红绿灯为目标对象，行人和车辆为观察者对象。当红绿灯从红灯变为绿灯时，行人因为这个变化而从等待转变为过马路，而汽车则从通过转变为等待，这些观察者都对目标的变化做出了反映。此类场景可以用观察者模式实现。

本示例以人民币汇率为目标对象，进口公司和出口公司作为观察者对象。演示目标对象变化时，如何通知所有观察者做出其响应的动作。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象目标类**: 观察者需要观察的对象，类内实现添加和删除观察者的普通方法，定义一个通知所有观察者的抽象方法。
**具体目标类**: 继承抽象目标类，实现抽象方法。
**抽象观察者类**: 持有一个`name`属性，并有对应的`getter/setter`。定义了一个响应目标对象变化的抽象方法`action`，被目标类通知时执行。
**具体观察者类**: 继承抽象观察者类，实现抽象方法`action`。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式
 */
public class ObserverDemo {

    public static void main(String[] args) {
        // 声明一个具体目标对象，人民币汇率
        Rate rate = new RMBRate();
        // 声明两个不同的观察者对象，进口公司和出口公司
        Company watcher1 = new ImportCompany();
        Company watcher2 = new ExportCompany();
        // 将观察者都加入到目标对象中，以便目标能够在自身产生变化时通知所有观察者
        rate.add(watcher1);
        rate.add(watcher2);
        // 当汇率升高3.5%时，进出口公司分别对这个变化做出应对
        rate.change(0.035);
        // 当汇率下降6.8%时，进出口公司分别对这个变化做出应对
        rate.change(-0.068);
    }

}

/**
 * 抽象目标类(被观察事物)
 * 持有一个观察者类实例的集合，实现两个非抽象方法，add方法用来添加观察者
 * 实例，remove方法用来删除观察者实例。另有一个抽象方法change，传入变化。
 */
abstract class Rate{
    protected List<Company> companies = new ArrayList<>();
    
    public void add(Company company) {
        companies.add(company);
    }
    public void remove(Company company) {
        companies.remove(company);
    }
    public abstract void change(double changedRate);
}

/**
 * 具体目标类
 * 继承抽象目标类，实现抽象方法change，在方法内遍历其持有的所有观察者，
 * 传入变化，执行观察者的action方法。
 */
class RMBRate extends Rate{
    @Override
    public void change(double changedRate) {
        System.out.printf("人民币汇率变动%.1f%%\n", changedRate*100);
        for(Company company : this.companies) {
            company.action(changedRate);
        }
    }
}

/**
 * 抽象观察者类
 * 持有一个name属性，并有对应的getter/setter。定义了一个action抽象方法。
 * 下例以公司为抽象观察者类。
 */
abstract class Company{
    private String name;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public abstract void action(double changedRate);
}

/**
 * 具体观察者类
 * 通过无参构造器初始化name。实现抽象观察者类中的抽象方法action。
 * 下例是进口公司观察者，action内实现当人民币汇率降低时减少相应比例
 * 的进口量，升高时增加相应比例的进口量。
 */
class ImportCompany extends Company{
    public ImportCompany() {
        this.setName("进口公司");
    }
    @Override
    public void action(double changedRate) {
        System.out.printf("%s动作：\n", this.getName());
        if(changedRate < 0) {
            System.out.printf("人民币汇率下降了%.1f%%，减少%.1f%%进口量。\n", 
                    -changedRate*100, -changedRate*100);
        } else {
            System.out.printf("人民币汇率上升了%.1f%%，增加%.1f%%进口量。\n", 
                    changedRate*100, changedRate*100);
        }
    }
}

/**
 * 具体观察者类
 * 下例是出口公司观察者，action内实现当人民币汇率降低时增加相应比例
 * 的进口量，升高时减少相应比例的进口量。
 */
class ExportCompany extends Company{
    public ExportCompany() {
        this.setName("出口公司");
    }
    @Override
    public void action(double changedRate) {
        System.out.printf("%s动作：\n", this.getName());
        if(changedRate < 0) {
            System.out.printf("人民币汇率下降了%.1f%%，增加%.1f%%出口量。\n", 
                    -changedRate*100, -changedRate*100);
        } else {
            System.out.printf("人民币汇率上升了%.1f%%，下降%.1f%%出口量。\n", 
                    changedRate*100, changedRate*100);
        }
    }
}
```

<br />

### 中介者模式

#### 模式说明

存在许多需要两两交互信息的同类对象，如果互相之间直接交互，将会形成网状结构，每一个对象都需要知道所有其他对象，且若某对象需要修改，那么其他对象可能也要做出修改，造成牵一发动全身的不良后果。针对这种场景，可以设置一个中介者，将对象间的网状两两交互，转变为通过中介者来居中传递信息，能够大大降低同类对象间的耦合，系统也会变得易扩展易维护。例如新增一个需要通信的成员(本设计模式中所谓的同事)，只需要创建它并注册到中介者中即可，不影响任何其他成员(同事)。

本示例展示中介者闲鱼平台如何向卖家转发买家的商品需求信息，向买家转发卖家的商品上架信息。当有一个买家发布一条商品需求信息时，所有卖家均能收到。当一个卖家发布一条商品上架信息时，所有买家均能收到。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象中介者类**: 定义管理同事和传递信息的方法。本示例中为注册同事的方法`register(User user)`，转发信息的方法`relay(User user, String message)`。
**具体中介者类**: 继承抽象中介者。持有一个已完成注册的同事的集合List\<User>，实现抽象中介者类中的抽象方法`register`和`relay`。
**抽象同事类**: 持有一个中介者实例属性，一个名字属性，通过有参构造器初始化名字属性。定义三个抽象方法，设置中介者的方法`setMediator`，发送消息的方法`send`，接收消息的方法`receive`。
**具体同事类**: 继承抽象同事类。实现抽象同事类中的三个抽象方法。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 中介者模式
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
```

<br />

### 迭代器模式

#### 模式说明

对于由多个相同元素聚合而成的对象，需要遍历内部元素时，最直接的做法是在客户端通过一个`for`循环来实现遍历操作。该做法的缺点是对客户端暴露了聚合对象的内部，且增加了客户端的负担。另一种做法是在聚合对象内封装一个遍历方法，在客户端调用该方法。缺点同样明显，当要改变遍历方式(例如原本是从前往后，改成从后往前)，就需要修改聚合类中的遍历方法，违背开闭原则。针对这种场景，结合前两种方式的特点，可以将遍历方法分离出来，但不是在客户端中实现，而是创建一个迭代器类，类中持有聚合对象，并实现迭代方法。这样对客户端不用暴露聚合对象内部，又实现了遍历方法和聚合对象的分离。

本示例展示如何在客户端中通过自定义迭代器遍历自定义聚合类内的元素。客户端声明一个具体聚合类(以抽象聚合接口类型声明)，然后初始化该聚合的元素。再通过聚合类的`getIterator`方法获取具体迭代器类实例(以抽象迭代器类型)。最后调用该`Iterator`的相关方法完成遍历。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**迭代器接口**: 定义迭代器角色的方法，获取聚合内第一个元素的方法`first`，获取下一个元素的方法`next`，判断是否有下一个元素的方法`hasNext`。
**具体迭代器类**: 迭代器接口的实现类。持有聚合元素的集合`List<Object>`，持有当前处理元素的下标`index`。实现`fist`，`next`和`hasNext`三个接口方法。
**抽象聚合接口**: 定义聚合角色的方法，增加聚合元素的方法add，移除聚合元素的方法`remove`，获取一个迭代器示例的方法`getIterator`。
**具体聚合类**: 抽象聚合接口的实现类，以`List<Object>`持有一个聚合示例。实现抽象聚合接口中的`add`，`remove`，`getIterator`方法

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 迭代器模式
 */
public class IteratorDemo {

    public static void main(String[] args) {
        // 声明一个聚合实例
        AbstractAggregation agg = new MyAggregation();
        // 通过add方法初始化这个聚合
        agg.add("刘备");
        agg.add("关羽");
        agg.add("张飞");
        // 声明一个迭代器实例
        Iterator iter = agg.getIterator();
        // 取出第一个元素
        System.out.println("第一个元素: " + iter.first().toString());
        // 调用迭代器的hasNext和next方法迭代遍历，用toString将元素转为String输出
        System.out.println("====开始迭代遍历====");
        while(iter.hasNext()) {
            System.out.println(iter.next().toString());
        }
    }

}

/**
 * 迭代器接口
 * 定义迭代器角色的方法，获取聚合内第一个元素的方法first，获取下一个元素
 * 的方法next，判断是否有下一个元素的方法hasNext。
 */
interface Iterator{
    Object first();
    Object next();
    boolean hasNext();
}

/**
 * 具体迭代器实现类
 * 迭代器接口的实现类。持有聚合元素的集合List<Object>，并通过有参构造器
 * 初始化该集合。持有当前处理元素的下标index，初始值为-1，表示初识时未
 * 处理任何元素。实现fist，next和hasNext三个接口方法。
 */
class MyIterator implements Iterator{
    private List<Object> list;
    private int index = -1;
    
    public MyIterator(List<Object> list) {
        this.list = list;
    }
    @Override
    public Object first() {
        return list.get(0);
    }
    @Override
    public Object next() {
        Object obj = null;
        // 判断有下一个元素时，获取下一个元素，当前元素下标index+1
        if(this.hasNext()) {
            obj = list.get(index + 1);
            index++;
        }
        return obj;
    }
    @Override
    public boolean hasNext() {
        if(index < list.size() - 1) {
            return true;
        } else {
            return false;
        }
    }
}

/**
 * 抽象聚合类接口
 * 定义三个聚合角色的方法，增加聚合元素的方法add，移除聚合元素的方法remove，
 * 获取一个迭代器实例的方法getIterator。
 */
interface AbstractAggregation{
    void add(Object obj);
    void remove(Object obj);
    public Iterator getIterator();
}

/**
 * 具体聚合实现类
 * 抽象聚合接口的实现类，以List<Object>持有一个聚合实例。实现抽象聚合接口
 * 中的add，remove，getIterator方法。其中getIterator获取一个具体迭代器
 * 类的实例，获取方式是通过有参构造器new一个具体迭代类的实例并返回，构造器
 * 参数是聚合类内部持有的聚合对象List<Object> list。
 */
class MyAggregation implements AbstractAggregation{
    private List<Object> list = new ArrayList<>();
    
    @Override
    public void add(Object obj) {
        list.add(obj);
    }
    @Override
    public void remove(Object obj) {
        list.remove(obj);
    }
    @Override
    public Iterator getIterator() {
        return new MyIterator(list);
    }
}
```

<br />

### 访问者模式

#### 模式说明

以不同地方的厨师对相同食材的处理为例描述访问者模式。例如对于鸡，广东厨师会按粤菜做法做成白斩鸡，而四川厨师会做成川菜辣子鸡。对于鱼也类似，广东厨师将其做成清蒸鱼而四川厨师会处理成水煮鱼。可以把厨师描述为访问者，食材是被访问对象，不同的访问者访问相同的对象有不同的结果。具有这种特征的行为模式就是访问者模式。

本示例中，厨师为抽象访问者，广东厨师和四川厨师为具体访问者。被访问事物是鸡和鱼，它们都继承一个抽象访问对象类。广东厨师处理(访问)鸡和鱼会做出白斩鸡和清蒸鱼，而四川厨师处理鸡和鱼会做出辣子鸡和水煮鱼。设置一个访问对象结构类，其内持有所有的访问对象，有管理访问对象的方法`add`(增加访问对象)和`remove`(移除访问对象)。在客户端声明一个访问对象结构，然后`add`具体的访问对象。接着声明具体访问者，调用访问对象结构的访问方法`accpet`(传入具体访问者)。最终得到该访问者访问所有访问对象的结果。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象访问者类**: 定义对所有访问对象的抽象访问方法。
**具体访问者类**: 继承抽象访问者类，实现抽象访问方法。
**抽象访问对象类**: 定义抽象访问方法`accept`，用于接受一个具体的访问者类的访问。
**具体访问对象类**: 继承抽象访问对象类，实现抽象方法`accept`。
**访问对象结构类**: 持有访问对象的集合`List<Material>`，有若干管理访问对象的方法，有一个传入访问者的访问方法，方法内遍历其持有的访问对象并执行访问动作。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问者模式
 */
public class VisitorDemo {

	public static void main(String[] args) {
		// 声明一个访问对象结构实例
		ObjectStructure obj = new ObjectStructure();
		// 增加访问对象至结构中
		obj.add(new ChickenMaterial());
		obj.add(new FishMaterial());
		// 声明访问者
		Cook gdCook = new GuangdongCook();
		Cook scCook = new SichuanCook();
		// 访问对象结构执行其accept方法，接收具体访问者的访问
		// 输出“粤菜白斩鸡”，“粤菜清蒸鱼”
		obj.accept(gdCook);
		// 输出”川菜辣子鸡“，”川菜水煮鱼“
		obj.accept(scCook);
	}

}
/**
 * 抽象访问者
 * 定义对所有访问对象的抽象访问方法，参数是所要访问的对象。
 * 下例抽象访问者为厨师，访问对象是鸡和鱼。
 */
abstract class Cook{
	public abstract void make(ChickenMaterial chicken);
	public abstract void make(FishMaterial fish);
}

/**
 * 具体访问者
 * 继承抽象访问者类，实现抽象方法。
 * 下例是广东厨师访问鸡和鱼的动作,分别处理为粤菜白斩鸡和粤菜清蒸鱼。
 */
class GuangdongCook extends Cook {
	@Override
	public void make(ChickenMaterial chicken) {
		System.out.println("粤菜白斩鸡");
	}
	@Override
	public void make(FishMaterial fish) {
		System.out.println("粤菜清蒸鱼");
	}
}

/**
 * 具体访问者
 * 下例是四川厨师访问鸡和鱼的动作，分别处理为川菜辣子鸡和川菜水煮鱼。
 */
class SichuanCook extends Cook {
	@Override
	public void make(ChickenMaterial chicken) {
		System.out.println("川菜辣子鸡");
	}
	@Override
	public void make(FishMaterial fish) {
		System.out.println("川菜水煮鱼");
	}
}

/**
 * 抽象访问对象
 * 定义抽象访问方法accept(Cook cook)，用于接受一个具体的访问者类的访问。
 */
abstract class Material{
	public abstract void accept(Cook cook);
}

/**
 * 具体访问对象
 * 实现抽象类中的抽象方法accept(Cook cook)。传入具体访问者，调用访问者
 * 的行为方法(传入此时的访问对象指针this)。
 */
class ChickenMaterial extends Material{
	@Override
	public void accept(Cook cook) {
		cook.make(this);
	}
}

/**
 * 具体访问对象
 */
class FishMaterial extends Material{
	public void accept(Cook cook) {
		cook.make(this);
	}
}

/**
 * 访问对象结构类
 * 持有访问对象的集合List<Material>，管理访问对象，有增加访问对象方法add，
 * 移除访问对象方法remove，执行访问者对访问对象的访问动作accept方法。该
 * 方法内用for-each遍历该结构类持有的访问对象集合，并对每一个访问对象执行
 * 其accpet方法。
 */
class ObjectStructure{
	private List<Material> materials = new ArrayList<>();
	
	public void add(Material material) {
		if(!materials.contains(material)){
			materials.add(material);
		}
	}
	public void remove(Material material) {
		if(materials.contains(material)) {
			materials.remove(material);
		}
	}
	public void accept(Cook cook) {
		for(Material m : materials) {
			m.accept(cook);
		}
	}
}
```

<br />

### 备忘录模式

#### 模式说明

以游戏中的存档和恢复存档为例，在游戏过程中需要保存当前游戏状态(包括各种游戏角色属性)作为存档，在之后的某个时刻能够恢复这个存档。文档中的撤销操作也有类似的需求。此类场景可以用备忘录模式(也叫快照模式)实现。该模式涉及三个角色，发起人类，备忘录类和备忘录管理者类。发起人类管理系统当前状态，并有一个保存为备忘的存档方法，一个恢复为指定备忘版本的恢复存档方法。备忘录类只管理该备忘的状态信息，而备忘录管理者只管理备忘录实例。符合单一职责原则。

本示例客户端中展示备忘录使用过程。声明一个发起者，该发起者设置系统状态后存档。然后创建当前系统存档(备忘录)，并让一个备忘录管理者持有。接着该发起者修改系统状态，最后调用存档恢复方法恢复到修改前状态。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**发起人类**: 持有所有状态信息，并有对应的`getter/setter`方法。维护一个存档方法`createMemento`，返回记录当前系统状态信息的`Memento`。另有一个恢复指定存档的方法`restoreMomento(Memento m)`，通过参数执行要恢复的`Memento`。
**备忘录类**: 管理一个备忘中的系统状态信息。本例中只设置了一个状态信息`String state`，通过构造器初始化该`state`，并有该`state`的`getter/setter`。
**备忘录管理者类**: 持有一个备忘录实例，并有该备忘录实例的`getter/setter`。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

/**
 * 备忘录模式
 */
public class MementoDemo {

    public static void main(String[] args) {
        // 声明一个发起者实例
        Originator ori = new Originator();
        // 设置状态为“Alive”
        ori.setState("Alive");
        // 初始时状态为"Alive"，输出"当前状态为:Alive"
        ori.display();
        // 声明一个备忘录管理者并让其持有当前memento
        MementoManager manager = new MementoManager();
        manager.setMemento(ori.CreateMemento());
        // 修改ori中的状态为"Dead"
        ori.setState("Dead");
        // 此时ori中的状态为"Dead"，输出"当前状态为:Dead"
        ori.display();
        // 通过管理者持有的备忘录恢复存档
        ori.restoreMemento(manager.getMemento());
        // 恢复后状态为存档时的"Alive"，输出"当前状态为:Alive"
        ori.display();
    }

}

/**
 * 发起人类
 * 持有所有状态信息，并有对应的getter/setter方法。维护一个存档方法
 * createMemento，返回记录当前系统状态信息的Memento。另有一个恢复指定
 * 存档的方法restoreMomento(Memento m)，通过参数执行要恢复的Memento。
 */
class Originator{
    private String state;

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public Memento CreateMemento() {
        return new Memento(state);
    }
    public void restoreMemento(Memento memento) {
        this.state = memento.getState();
    }
    public void display() {
        System.out.println("当前状态为:" + state);
    }
}

/**
 * 备忘录类
 * 管理一个备忘中的系统状态信息。本例中只设置了一个状态信息String state，
 * 通过构造器初始化该state，并有该state的getter/setter。
 */
class Memento{
    private String state;
    
    public Memento(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    
}

/**
 * 备忘录管理者类
 * 持有一个备忘录实例，并有该备忘录实例的getter/setter。
 */
class MementoManager{
    private Memento memento;

    public Memento getMemento() {
        return memento;
    }
    public void setMemento(Memento memento) {
        this.memento = memento;
    }
    
}
```

<br />

### 解释器模式

#### 模式说明

解释器用来处理这样一类对象，该对象可不断划分为更小的对象，使得对该对象的处理和对再分之后的部分的处理类似，且对于不断再分后得到到的最小单位，有最终处理。例如输入歌曲解析出简谱，每个曲子可以划分为更短的曲子，但解析是类似的，直至单个音符可以直接输出简谱符号(或按定义好的规则转换)。该模式类似组合模式，但解释器模式处理对象的元素通常比组合模式的要多，且组合模式是对象结构模式，而解释器模式是类行为模式。在使用上，组合模式在客户端以树的结构将所有节点按层次组合为一个整体再处理。而对于解释器模式，在客户端直接输入整体，处理时再循环或迭代解释(处理) 非终结表达式直到将迭代至终结符表达式，且所有终结符表达式都被解释完毕。

**非终结符：**可再分为非终结符和终结符的元素，例如“我吃饭”可再分为“我(代词，主语)”,“吃饭(动词，谓语)”,“饭(名词，宾语)”。
**终结符：**不可再分的语法元素，例如上述的“我”，“吃”，“饭”。
参考：
[https://en.wikipedia.org/wiki/Terminal_and_nonterminal_symbols](https://en.wikipedia.org/wiki/Terminal_and_nonterminal_symbols)

<br />

本示例定义一种关于加法运算的规则，只有符号`"a","b","A","B"`之间的加减法是合法运算。客户端将输入一些表达式，通过解释器来判断(解释)该表达式是否符合语法定义，合法输出`true`，不合法输出`false`。客户端声明一个上下文类`Context`，将要解释的表达式传入`Context`的`interpret`方法中，返回解释结果。`Context`类内持有一个非终结符表达式实例(以抽象表达式类声明)`NonterminalExpression`，并通过无参构造器初始化该实例`NonterminalExpression`的构造器是有参的，参数是终结符表达式实例`TerminalExpression`。

<br />

#### 结构

| 类/接口 | 描述 |
| ------- | ---- |
|         |      |
|         |      |
|         |      |



**抽象表达式类**: 定义一个抽象解释方法`interpret(String expression)`。
**终结符表达式类**: 继承抽象表达式类。持有一个`Set<String>`泛型的终结符集合。实现抽象表达式类的抽象解释方法`interpret`。
**非终结符表达式类**: 继承抽象表达式。持有所有种类的终结符表达式实例(以`AbstractExpress`类型)。实现抽象表达式类中的抽象解释方法`interpret`。
**环境类(上下文类)**: 持有所有种类的终结符集合(以`String`数组类型)，持有一个非终结符实例(以`AbstractExpression`类型)。维护一个`interpret`方法，调用非终结符的`interpret`方法返回解释结果。

<br />

#### 代码演示

```java
package com.yukiyama.pattern.behavior;

import java.util.HashSet;
import java.util.Set;

/**
 * 解释器模式
 */
public class InterpreterDemo {

    public static void main(String[] args) {
        // 声明一个上下文实例
        Context context = new Context();
        // 合法表达式，输出“true”
        System.out.println(context.interpret("a"));
        // 合法表达式，输出“true”
        System.out.println(context.interpret("A+b"));
        // 合法表达式，输出“true”
        System.out.println(context.interpret("A+b-a+a-B"));
        // 非法表达式，输出“false”
        System.out.println(context.interpret("C+a"));
        // 非法表达式，输出“false”
        System.out.println(context.interpret("b+a*B"));
        // 非法表达式，输出“false”
        System.out.println(context.interpret("3"));
    }

}

/**
 * 抽象表达式类
 * 定义一个抽象解释方法interpret(String expression)。
 */
abstract class AbstractExpression{
    public abstract boolean interpret(String expression);
}

/**
 * 终结符表达式类
 * 继承抽象表达式类。持有一个Set<String>泛型的终结符集合。并通过有参构造器
 * 初始化。实现抽象表达式类的抽象解释方法interpret。解释过程很简单，判断该
 * 传入的expression是否包含在其持有的终结符集合里，包含则true，否则false。
 */
class TerminalExpression extends AbstractExpression{
    private Set<String> terminals = new HashSet<>();
    
    public TerminalExpression(String[] terminals) {
        for (int i = 0; i < terminals.length; i++) {
            this.terminals.add(terminals[i]);
        }
    }
    public Set<String> getTerminals() {
        return terminals;
    }
    public void setTerminals(Set<String> terminals) {
        this.terminals = terminals;
    }
    @Override
    public boolean interpret(String expression) {
        return terminals.contains(expression);
    }
}

/**
 * 非终结表达式类
 * 继承抽象表达式。持有所有终结符(以AbstractExpress类型)，并通过有参构造器
 * 传入终结符集合来初始化。本示例定义两种终结符，小写字母"a"和"b"是一种，大写
 * 字母"A"和"B是另一种。所以本例的非终结符表达式持有两个终结符表达式lowerExp
 * 和upperExp，通过构造器传入lowerExp和upperExp来初始化。
 * 实现抽象表达式类中的抽象解释方法interpret。借助已知的合法的运算符号("+","-")
 * 通过字符串的split方法将原始表达式分割成一个终结符和一个非终结符，对终结符调用
 * 终结符表达式类的interpret判断，一旦非法立即返回false。对非终结符，递归调用
 * 非终结符表达式的interpret。
 */
class NonterminalExpression extends AbstractExpression{
    private AbstractExpression lowerExp;
    private AbstractExpression upperExp;
    
    public NonterminalExpression(AbstractExpression lowerExp, AbstractExpression upperExp) {
        this.lowerExp = lowerExp;
        this.upperExp = upperExp;
    }
    @Override
    public boolean interpret(String exp) {
        boolean isLegal;
        // exp多于1个字符时
        if(exp.length() > 1) {
            // 将表达式分割为两部分，elements[0]为第一个合法运算符号左边的1个字符
            // elements[1]为第一个合法运算符号右边的部分
            String[] elements = exp.split("\\+|\\-", 2);
            // 判断(解释)elements[0]是否合法
            isLegal = lowerExp.interpret(elements[0]) || upperExp.interpret(elements[0]);
            // lements[0]不合法直接返回false
            if(!isLegal){
                return isLegal;
            }
            // 递归调用当前非终结表达式实例的interpret方法判断右边部分
            return this.interpret(elements[1]);
        } else {
            // 若表达式exp等于或少于一个字符时，调用终结符的interpret返回解释结果
            return lowerExp.interpret(exp) || upperExp.interpret(exp);
        }
    }
}

/**
 * 环境类(上下文类)
 * 持有所有种类的终结符集合(以String数组类型)，持有一个非终结符实例
 * (以AbstractExpression类型)。无参构造器中初始化非终结符实例，方法如下：
 * 先通过两个String数组实例化两种终结符，再向非终结符构造参数中传入这两个
 * 终结符实例。
 * 维护一个interpret方法，调用非终结符的interpret方法返回解释结果。
 */
class Context{
    private String[] lowers = {"a", "b"};
    private String[] uppers = {"A", "B"};
    private AbstractExpression exp;
    
    public Context() {
        AbstractExpression lowerExp = new TerminalExpression(lowers);
        AbstractExpression upperExp = new TerminalExpression(uppers);
        exp = new NonterminalExpression(lowerExp, upperExp);
    }
    public boolean interpret(String expression) {
        return this.exp.interpret(expression);
    }
}
```

<br />
