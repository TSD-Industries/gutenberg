package org.tsd.gutenberg;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WpInfo {
    private final String username;
    private final String password;
    private final String baseUrl;
}
