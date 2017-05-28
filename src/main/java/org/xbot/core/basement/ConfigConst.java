package org.xbot.core.basement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration("configConst")
@PropertySource(value = { "classpath:/app.properties"  })
public class ConfigConst {
    @Value("${team_confidence_code}")
    private String teamConfidenceCode;

    public String getTeamConfidenceCode() {
        return teamConfidenceCode;
    }
}
