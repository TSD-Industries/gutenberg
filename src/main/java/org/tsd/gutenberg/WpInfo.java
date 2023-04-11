package org.tsd.gutenberg;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class WpInfo {
    private final String username;
    private final String password;
    private final String baseUrl;
}
