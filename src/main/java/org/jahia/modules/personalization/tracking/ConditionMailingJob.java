package org.jahia.modules.personalization.tracking;

import org.jahia.services.scheduler.BackgroundJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Background job that sends an email to all users matching a condition
 */
public class ConditionMailingJob extends BackgroundJob {

    public static final String JOB_CONDITIONPATH = "org.jahia.modules.personalization.tracking.ConditionMailingJob.conditionPath";
    public static final String JOB_MAILCONTENTPATH = "org.jahia.modules.personalization.tracking.ConditionMailingJob.mailContentPath";

    private static Logger logger = LoggerFactory.getLogger(ConditionMailingJob.class);

    @Override
    public void executeJahiaJob(JobExecutionContext jobExecutionContext) throws Exception {
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        String conditionPath = (String) data.get(JOB_CONDITIONPATH);

        long timer = System.currentTimeMillis();

        logger.info(
                "Stored tracking data for client ID {} in {} ms",
                new String[] { null,
                        String.valueOf(System.currentTimeMillis() - timer) });
    }

}
