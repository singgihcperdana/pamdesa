package org.pamdesa.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "access-rules")
@Data
public class AccessRulesProperties {

    private Map<String, List<Path>> rules = new HashMap<>();

    private List<Path> authedPaths = new ArrayList<>();

    private List<Path> publicPaths = new ArrayList<>();

    @Getter
    @Setter
    public static class Path {

        private String path;

        private List<String> methods;

    }
}
