package com.yukiyama.designpattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问者模式
 * 以不同地方的厨师对相同食材的处理为例描述访问者模式。例如对于鸡，广东厨师会
 * 按粤菜做法做成白斩鸡，而四川厨师会做成川菜辣子鸡。对于鱼也类似，广东厨师
 * 将其做成清蒸鱼而四川厨师会处理成水煮鱼。可以把厨师描述为访问者，食材是被
 * 访问对象，不同的访问者访问相同的对象有不同的结果。具有这种特征的行为模式
 * 就是访问者模式。
 * 
 * 本示例中，厨师为抽象访问者，广东厨师和四川厨师为具体访问者。被访问事物是鸡
 * 和鱼，它们都继承一个抽象访问对象类。广东厨师处理(访问)鸡和鱼会做出白斩鸡
 * 和清蒸鱼，而四川厨师处理鸡和鱼会做出辣子鸡和水煮鱼。设置一个访问对象结构
 * 类，其内持有所有的访问对象，有管理访问对象的方法add(增加访问对象)和remove
 * (移除访问对象)。在客户端声明一个访问对象结构，然后add具体的访问对象。接着
 * 声明具体访问者，调用访问对象结构的访问方法accpet(传入具体访问者)。最终
 * 得到该访问者访问所有访问对象的结果。
 * 
 * 结构
 * 抽象访问者类
 *   定义对所有访问对象的抽象访问方法。
 * 具体访问者类
 *   继承抽象访问者类，实现抽象访问方法。
 * 抽象访问对象类
 *   定义抽象访问方法accept，用于接受一个具体的访问者类的访问。
 * 具体访问对象类
 *   继承抽象访问对象类，实现抽象方法accept。
 * 访问对象结构类
 *   持有访问对象的集合List<Material>，有若干管理访问对象的方法，有一个
 *   传入访问者的访问方法，方法内遍历其持有的访问对象并执行访问动作。
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