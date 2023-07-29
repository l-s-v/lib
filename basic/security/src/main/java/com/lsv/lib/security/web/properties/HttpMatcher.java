package com.lsv.lib.security.web.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Config patterns and http methods to verify matchs HttpRequests.
 *
 * @author Leandro da Silva Vieira
 */
@Data
@NoArgsConstructor
public class HttpMatcher {

    /**
     * The patterns to match on.
     */
    private String[] pattern;
    /**
     * The http method to use.
     */
    private String method;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public HttpMatcher(String method, String ... pattern) {
        this.method = method;
        this.pattern = pattern;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static List<HttpMatcher> withPatterns(String ... patterns) {
        return withPatternsAndMethod(null, patterns);
    }

    public static List<HttpMatcher> withPatternsAndMethod(String method, String ... patterns) {
        return Arrays.stream(patterns).map(s -> new HttpMatcher(method, s)).toList();
    }
}