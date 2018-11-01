package com.github.bioinfo.webes.demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class DemoBeanFactoryPostProcess implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		System.out.println("beanFactoryPostProcess ÷¥––¡À");
		BeanDefinition definition = beanFactory.getBeanDefinition("demo");
		MutablePropertyValues values = definition.getPropertyValues();
		if(values.contains("name")) {
			values.addPropertyValue("name", "beanFacotryPostProcess changed");
		}
		
	}

}
