package org.jahia.modules.personalization.tracking;

import org.jahia.bin.JahiaController;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Spring servlet controller to expose and modify tracking data.
 */
public class TrackingController extends JahiaController {

    private String trackingSessionName = "org.jahia.modules.personalization.trackingData";

    public void setTrackingSessionName(String trackingSessionName) {
        this.trackingSessionName = trackingSessionName;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (request.getMethod().equalsIgnoreCase("get")) {
            sendTrackingData(request, response);
        } else if (request.getMethod().equalsIgnoreCase("post")) {
            updateTrackingData(request, response);
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    private void sendTrackingData(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException {

        TrackingData trackingData = (TrackingData) request.getSession().getAttribute(trackingSessionName);

        JSONObject jsonObject = new JSONObject();
        if (trackingData == null) {
            jsonObject.write(response.getWriter());
            return;
        }

        for (Map.Entry<String,List<String>> trackingEntry : trackingData.getTrackingMap().entrySet()) {
            jsonObject.put(trackingEntry.getKey(), trackingEntry.getValue());
        }

        jsonObject.write(response.getWriter());

    }

    private void updateTrackingData(HttpServletRequest request, HttpServletResponse response) {
        TrackingData trackingData = (TrackingData) request.getSession().getAttribute(trackingSessionName);
        if (trackingData == null) {
            return;
        }

        Map<String,String[]> parameterMap = (Map<String,String[]>) request.getParameterMap();
        for (Map.Entry<String,String[]> parameterMapEntry : parameterMap.entrySet()) {
            for (String parameterValue : parameterMapEntry.getValue()) {
                if (parameterValue.startsWith("+=")) {
                    String incrementStr = parameterValue.substring("+=".length());
                    long increment = Long.parseLong(incrementStr);
                    Long previousValue = trackingData.getLong(parameterMapEntry.getKey());
                    if (previousValue == null) {
                        previousValue = 0L;
                    }
                    trackingData.setLong(parameterMapEntry.getKey(), previousValue + increment);
                } else {
                    trackingData.addValue(parameterMapEntry.getKey(), parameterValue);
                }
            }
        }
    }

}
