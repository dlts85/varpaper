package com.github.bioinfo.webes.demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class DemoBeanPostProcess implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("bean postprocess before inital at bean " + beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("bean postprocess after intial at bean " + beanName);
		
		if(bean instanceof DemoInterface) {
			DemoInterface demo = (DemoInterface) bean;
			DemoInterface demoProxy = new DemoInterface() {
				
				@Override
				public String sayHelloWorld() {
					// TODO Auto-generated method stub
					return "proxy " + demo.sayHelloWorld();
				}
			};
			return demoProxy;
		}
		return bean;
	}

}
