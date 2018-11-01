package com.github.bioinfo.webes.demo;

public class Demo  implements DemoInterface {
	
	
	public Demo() {
		System.out.println("demo 构造函数被执行了");
	}
	
	public String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void init() {
		System.out.println("Demo class init method");
	}
	
	@Override
	public String sayHelloWorld() {
		return "MyName is " + name;
	}

}
