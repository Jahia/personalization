package org.jahia.modules.personalization.tracking;

import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRTemplate;

import javax.jcr.RepositoryException;

/**
 * Tracking data persistence storage service.
 */
public class TrackingService {

    public TrackingData getById(String trackingId) {
        return null;
    }

    public boolean store(TrackingData trackingData) {
        return true;
    }

    public boolean merge(TrackingData trackingData) {
        return false;
    }

    private boolean execute(JCRCallback<Boolean> jcrCallback) throws RepositoryException {
        return JCRTemplate.getInstance().doExecuteWithSystemSession(jcrCallback);
    }

}
