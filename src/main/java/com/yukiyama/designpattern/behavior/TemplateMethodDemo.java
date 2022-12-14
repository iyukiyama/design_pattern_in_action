package com.yukiyama.designpattern.behavior;

/**
 * 模板方法模式
 * 有这样一些对象，他们都要执行若干动作，这些动作中多数类似或完全相同，例如
 * 在不同客户在到银行处理业务，都需要取号，排队，与柜员交流，处理后对服务
 * 评分等。在这里取号，排队，评分动作是完全相同的，与柜员交流的细节可能不同，
 * 可能是办理存款业务，也可能是取款，购买基金等。针对此类场景，可以将设置
 * 一个抽象模板类，类内声明相关方法，再定义一个驱动方法来调用这些相关方法。
 * 这样子类继承抽象模板类后自动复用了大部分的代码，对于具体实现中有区别的细节，
 * 在子类中重写即可。
 * 
 * 本示例以大学生和小学生入学活动为例，将入学活动中相同的部分放在抽象模板类
 * 中，子类继承时自动获得，而子类中不同部分在抽象类中定义为抽象方法或钩子方法，
 * 具体实现延迟到子类中。
 * 
 * 结构
 * 抽象模板类
 *   定义方法骨架，并确定一个procedure方法，将需要执行的方法集中放置其中，
 *   客户端调用该procedure即可统一执行多个既定的方法。该类内方法分为普通
 *   方法，抽象方法和钩子方法。
 * 具体实现类
 *   继承抽象模板类，按自身情况实现其中的各种方法。
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
 * false则进入空分支，相当于不执行。boolean类型抽象方法延迟到子类实现，由
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
