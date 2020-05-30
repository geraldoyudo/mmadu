package com.mmadu.identity.models.authorization;

import java.util.List;
import java.util.Map;


public interface RedirectData {

    Map<String, List<String>> toParams();
}
