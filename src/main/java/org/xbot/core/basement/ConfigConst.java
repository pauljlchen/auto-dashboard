package org.xbot.core.basement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration("configConst")
@PropertySource(value = { "classpath:/app.properties"  })
public class ConfigConst {
	

}
