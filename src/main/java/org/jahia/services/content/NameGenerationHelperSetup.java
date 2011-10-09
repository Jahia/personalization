package org.jahia.services.content;

import org.springframework.beans.factory.InitializingBean;

/**
 */
public class NameGenerationHelperSetup implements InitializingBean {

    private JCRContentUtils jcrContentUtils;
    private NameGenerationHelper nameGenerationHelper;

    public JCRContentUtils getJcrContentUtils() {
        return jcrContentUtils;
    }

    public void setJcrContentUtils(JCRContentUtils jcrContentUtils) {
        this.jcrContentUtils = jcrContentUtils;
    }

    public NameGenerationHelper getNameGenerationHelper() {
        return nameGenerationHelper;
    }

    public void setNameGenerationHelper(NameGenerationHelper nameGenerationHelper) {
        this.nameGenerationHelper = nameGenerationHelper;
    }

    public void afterPropertiesSet() throws Exception {
        jcrContentUtils.setNameGenerationHelper(nameGenerationHelper);
    }
}
