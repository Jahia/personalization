package org.jahia.modules.personalization.tracking;

import org.jahia.services.scheduler.BackgroundJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Background job to persist TrackingData, so that we don't affect performance while storing the data.
 */
public class TrackingStorageJob extends BackgroundJob {

    public static final String JOB_TRACKINGDATA = "org.jahia.modules.personalization.tracking.TrackingData";

    private static Logger logger = LoggerFactory.getLogger(TrackingStorageJob.class);

    @Override
    public void executeJahiaJob(JobExecutionContext jobExecutionContext) throws Exception {
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        TrackingData trackingData = (TrackingData) data.get(JOB_TRACKINGDATA);

        long timer = System.currentTimeMillis();

        TrackingHelper.getInstance().getTrackingService().store(trackingData);

        logger.info(
                "Stored tracking data for client ID {} in {} ms",
                new String[] { trackingData.getClientID(),
                        String.valueOf(System.currentTimeMillis() - timer) });
    }
}
