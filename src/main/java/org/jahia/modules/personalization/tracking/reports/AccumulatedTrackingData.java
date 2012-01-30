package org.jahia.modules.personalization.tracking.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that stores accumulated tracking data, as well as method to accumulate
 */
public class AccumulatedTrackingData {

    // format is key, <value,count>
    private Map<String, Map<String,Long>> accumulatedData = new HashMap<String, Map<String, Long>>();

    public void accumulateForKey(String key, List<String> values) {
        if (key == null || values == null) {
            return;
        }
        Map<String,Long> accumulatedValues = accumulatedData.get(key);
        if (accumulatedValues == null) {
            accumulatedValues = new HashMap<String, Long>();
        }
        for (String value : values) {
            Long valueCount = accumulatedValues.get(value);
            if (valueCount == null) {
                valueCount = new Long(0L);
            }
            valueCount++;
            accumulatedValues.put(value, valueCount);
        }
        accumulatedData.put(key, accumulatedValues);
    }

    public Map<String, Map<String, Long>> getAccumulatedData() {
        return accumulatedData;
    }
}
