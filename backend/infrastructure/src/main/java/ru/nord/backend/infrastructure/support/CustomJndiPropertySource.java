package ru.nord.backend.infrastructure.support;

import ru.nord.backend.infrastructure.utils.StringUtils;
import org.springframework.core.env.PropertyResolver;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.jndi.JndiPropertySource;

public class CustomJndiPropertySource extends JndiPropertySource
{
    private static final String PROPERTY_SUFFIX_JNDI_NAME = ".jndi-name";

    private final PropertyResolver propertyResolver;

    public CustomJndiPropertySource(String name, PropertyResolver propertyResolver)
    {
        super(name, new JndiLocatorDelegate());
        this.propertyResolver = propertyResolver;
    }

    @Override
    public Object getProperty(String name)
    {
        if(name.endsWith(PROPERTY_SUFFIX_JNDI_NAME))
            return null;

        final String jndiName = propertyResolver.getProperty(name+PROPERTY_SUFFIX_JNDI_NAME);
        if(StringUtils.isNullOrEmpty(jndiName)) {
            return null;
        }
        else {
            return super.getProperty(jndiName);
        }
    }
}
