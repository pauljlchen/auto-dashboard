package org.xbot.core.encrypt;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyPlaceholderConfigurerExt extends PropertyPlaceholderConfigurer {
	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		String username = props.getProperty("connection.user");
		String password = props.getProperty("connection.password");
	//	this.setIgnoreUnresolvablePlaceholders( true );
		/*
		MessageDigest md = MessageDigest.getInstance("SHA-512");  
        // 执行摘要方法  
		md.
        byte[] digest = md.digest(data);  
        return new HexBinaryAdapter().marshal(digest);  
		*/
		// 解密jdbc.password属性值，并重新设置
		props.setProperty("connection.user", (username));
		props.setProperty("connection.password", (password));
		super.processProperties(beanFactory, props);
	}
}