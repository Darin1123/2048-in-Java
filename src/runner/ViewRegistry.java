package runner;

import view.View;

import java.util.HashMap;
import java.util.Map;

public class ViewRegistry {
    private final Map<String, View> viewMap;

    public ViewRegistry() {
        viewMap = new HashMap<>();
    }

    public View get(String key) {
        return this.viewMap.get(key);
    }

    public ViewRegistry register(String name, View view) {
        this.viewMap.put(name, view);
        return this;
    }
}
