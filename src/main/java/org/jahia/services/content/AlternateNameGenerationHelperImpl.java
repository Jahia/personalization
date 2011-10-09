package org.jahia.services.content;

import org.jahia.services.content.nodetypes.ExtendedNodeType;

/**
 */
public class AlternateNameGenerationHelperImpl extends DefaultNameGenerationHelperImpl {
    public AlternateNameGenerationHelperImpl() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String generatNodeName(JCRNodeWrapper parent, String nodeType) {
        return super.generatNodeName(parent, nodeType);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String generatNodeName(JCRNodeWrapper parent, String defaultLanguage, ExtendedNodeType nodeType) {
        return super.generatNodeName(parent, defaultLanguage, nodeType);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
