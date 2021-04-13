package runner;

import view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zefeng Wang - wangz217
 * @brief View Registry Module
 * @details This class aims to eliminates the coupling between the concrete
 * View modules and the game controller. This modules works as a proxy.
 */

public class ViewRegistry {
    /**
     * inner variant, stores the view and the view name
     */
    private final Map<String, View> viewMap;

    /**
     * @brief initialize the view registry
     */
    public ViewRegistry() {
        viewMap = new HashMap<>();
    }

    /**
     * @brief get view by view name
     * @details if the view name doesn't exist, return default view
     * @param key view name
     * @return the corresponding view
     */
    public View get(String key) {
        if (viewMap.get(key) == null) {
            return ()->"Unknown View.";
        }
        return this.viewMap.get(key);
    }

    /**
     * @brief register view to this registry
     * @details this method return self for chaining programming
     * @param name view name
     * @param view view
     * @return this
     */
    public ViewRegistry register(String name, View view) {
        this.viewMap.put(name, view);
        return this;
    }
}
