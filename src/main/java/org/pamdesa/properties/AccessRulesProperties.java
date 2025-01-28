package org.pamdesa.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "access-rules")
@Data
public class AccessRulesProperties {

    private Map<String, Map<String, List<String>>> rules = new HashMap<>();

    private List<String> publicPaths = new ArrayList<>();

    private List<String> authedPaths = new ArrayList<>();

}
