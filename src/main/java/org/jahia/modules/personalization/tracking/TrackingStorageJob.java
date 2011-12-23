package org.jahia.modules.personalization.tracking;

import org.jahia.services.scheduler.BackgroundJob;
import org.quartz.JobExecutionContext;

/**
 * Background job to persist TrackingData, so that we don't affect performance while storing the data.
 */
public class TrackingStorageJob extends BackgroundJob {
    @Override
    public void executeJahiaJob(JobExecutionContext jobExecutionContext) throws Exception {
    }
}
