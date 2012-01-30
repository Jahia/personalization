package org.jahia.modules.personalization.tracking;

import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracking data persistence storage service.
 */
public class TrackingService {

    private static Logger logger = LoggerFactory.getLogger(TrackingService.class);

    public static final String ROOT_TRACKING_NODE_PATH = "tracking";

    private JahiaUserManagerService jahiaUserManagerService;

    private TrackingDataFactory trackingDataFactory;

    public void setJahiaUserManagerService(JahiaUserManagerService jahiaUserManagerService) {
        this.jahiaUserManagerService = jahiaUserManagerService;
    }

    public void setTrackingDataFactory(TrackingDataFactory trackingDataFactory) {
        this.trackingDataFactory = trackingDataFactory;
    }

    public TrackingData getByClientId(final String trackingClientId) {
        try {
            execute(new JCRCallback<TrackingData>() {
                public TrackingData doInJCR(JCRSessionWrapper session) throws RepositoryException {
                    String[] trackingIdParts = TrackingHelper.getInstance().getClientIDParts(trackingClientId);
                    JCRNodeWrapper trackingRootNode = getTrackingRootNode(session);
                    try {
                        JCRNodeWrapper trackingNode = trackingRootNode;
                        for (String trackingIdPart : trackingIdParts) {
                            trackingNode = trackingNode.getNode(trackingIdPart);
                        }
                        // @todo normally we should also now retrieve and merge user tracking data if it is associated.

                        TrackingData trackingData = trackingDataFactory.getTrackingData(trackingNode);
                        if (trackingData.getAssociatedUserKey() != null) {
                            TrackingData userTrackingData = getUserTrackingData(trackingData.getAssociatedUserKey());
                            if (userTrackingData != null) {
                                logger.info("Merging user tracking data for user " + trackingData.getAssociatedUserKey());
                                trackingData = userTrackingData.merge(trackingData);
                            }
                        }
                        return trackingData;
                    } catch (PathNotFoundException pnfe) {
                        return null;
                    }
                }
            });
        } catch (RepositoryException e) {
            logger.error("Error retrieving tracking data for id " + trackingClientId, e);
        }
        return null;
    }

    public TrackingData store(final TrackingData trackingData) {
        try {
            return execute(new JCRCallback<TrackingData>() {
                public TrackingData doInJCR(JCRSessionWrapper session) throws RepositoryException {
                    TrackingData existingTrackingData = getByClientId(trackingData.getClientID());
                    TrackingData newTrackingData = trackingData;
                    JCRNodeWrapper targetTrackingNode = null;
                    if (existingTrackingData != null) {
                        targetTrackingNode = session.getNodeByIdentifier(existingTrackingData.getStorageID());
                        newTrackingData = existingTrackingData.merge(trackingData);
                    } else {
                        targetTrackingNode = createTargetTrackingNode(session, newTrackingData);
                    }
                    trackingDataFactory.toJCRNode(newTrackingData, targetTrackingNode);

                    if (newTrackingData.getAssociatedUserKey() != null) {
                        final JahiaUser jahiaUser = jahiaUserManagerService.lookupUserByKey(newTrackingData.getAssociatedUserKey());
                        if (jahiaUser != null) {
                            final String userJCRPath = jahiaUser.getLocalPath();
                            newTrackingData = storeUserTrackingData(session, userJCRPath, newTrackingData);
                        }
                    }
                    session.save();
                    return newTrackingData;  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
        } catch (RepositoryException re) {
            logger.error("Error retrieving storing tracking data for id " + trackingData.getClientID(), re);
        }
        return null;
    }

    public TrackingData getUserTrackingData(final String userKey) {
        final JahiaUser jahiaUser = jahiaUserManagerService.lookupUserByKey(userKey);

        if (jahiaUser == null) {
            return null;
        }
        final String userJCRPath = jahiaUser.getLocalPath();

        try {
            TrackingData trackingData = execute(new JCRCallback<TrackingData>() {
                public TrackingData doInJCR(JCRSessionWrapper session) throws RepositoryException {
                    JCRNodeWrapper userNode = session.getNode(userJCRPath);
                    if (userNode.hasNode("trackingData")) {
                        JCRNodeWrapper userTrackingDataNode = userNode.getNode("trackingData");
                        TrackingData trackingData = trackingDataFactory.getTrackingData(userTrackingDataNode);
                        return trackingData;
                    } else {
                        return null;
                    }
                }
            });
            return trackingData;
        } catch (RepositoryException re) {
            logger.error("Error while retrieving tracking data for user " + userKey, re);
        }
        return null;
    }

    public TrackingData storeUserTrackingData(final String userKey, final TrackingData trackingData) {
        final JahiaUser jahiaUser = jahiaUserManagerService.lookupUserByKey(userKey);

        final String userJCRPath = jahiaUser.getLocalPath();

        try {
            return execute(new JCRCallback<TrackingData>() {
                public TrackingData doInJCR(JCRSessionWrapper session) throws RepositoryException {
                    TrackingData newTrackingData  = TrackingService.this.storeUserTrackingData(session, userJCRPath, trackingData);
                    session.save();
                    return newTrackingData;
                }
            });
        } catch (RepositoryException re) {
            logger.error("Error while retrieving tracking data for user " + userKey, re);
        }
        return null;
    }

    private TrackingData storeUserTrackingData(JCRSessionWrapper session, String userJCRPath, TrackingData trackingData) throws RepositoryException {
        logger.info("Storing user tracking data at " + userJCRPath);
        JCRNodeWrapper userNode = session.getNode(userJCRPath);
        JCRNodeWrapper userTrackingDataNode = null;
        TrackingData newTrackingData = trackingData;
        if (!userNode.hasNode("trackingData")) {
            userTrackingDataNode = userNode.addNode("trackingData", "jnt:trackingData");
        } else {
            userTrackingDataNode = userNode.getNode("trackingData");
            TrackingData existingUserTrackingData = trackingDataFactory.getTrackingData(userTrackingDataNode);
            newTrackingData = existingUserTrackingData.merge(trackingData);
        }
        trackingDataFactory.toJCRNode(newTrackingData, userTrackingDataNode);
        return newTrackingData;
    }

    // Private methods

    private <T> T execute(JCRCallback<T> jcrCallback) throws RepositoryException {
        return JCRTemplate.getInstance().doExecuteWithSystemSession(jcrCallback);
    }

    private JCRNodeWrapper getTrackingRootNode(JCRSessionWrapper session) throws RepositoryException {
        JCRNodeWrapper rootNode = session.getRootNode();
        JCRNodeWrapper trackingRootNode = null;
        try {
            trackingRootNode = session.getNode("/" + ROOT_TRACKING_NODE_PATH);
        } catch (PathNotFoundException pnfe) {
            trackingRootNode = rootNode.addNode(ROOT_TRACKING_NODE_PATH);
            session.save();
        }
        return trackingRootNode;
    }

    private JCRNodeWrapper createTargetTrackingNode(JCRSessionWrapper session, TrackingData newTrackingData) throws RepositoryException {
        String[] trackingIdParts = TrackingHelper.getInstance().getClientIDParts(newTrackingData.getClientID());
        JCRNodeWrapper trackingRootNode = getTrackingRootNode(session);
        JCRNodeWrapper trackingNode = trackingRootNode;
        int i=0;
        for (String trackingIdPart : trackingIdParts) {
            try {
                trackingNode = trackingNode.getNode(trackingIdPart);
            } catch (PathNotFoundException pnfe) {
                if (i < (trackingIdParts.length-1)) {
                    trackingNode = trackingNode.addNode(trackingIdPart);
                } else {
                    trackingNode = trackingNode.addNode(trackingIdPart, "jnt:trackingData");

                }
            }
            i++;
        }
        return trackingNode;
    }


}
