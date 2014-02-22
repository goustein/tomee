/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openejb.cdi;

import org.apache.openejb.AppContext;
import org.apache.openejb.BeanContext;
import org.apache.openejb.Injection;
import org.apache.openejb.InjectionProcessor;
import org.apache.openejb.util.LogCategory;
import org.apache.openejb.util.Logger;
import org.apache.openejb.util.PassthroughFactory;
import org.apache.webbeans.component.ResourceBean;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ResourceInjectionService;
import org.apache.webbeans.spi.api.ResourceReference;
import org.apache.xbean.recipe.ObjectRecipe;
import org.apache.xbean.recipe.Option;

import javax.enterprise.inject.spi.Bean;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class CdiResourceInjectionService implements ResourceInjectionService {
    private Logger logger = Logger.getInstance(LogCategory.OPENEJB.createChild("cdi"), CdiResourceInjectionService.class);

    private final CdiPlugin ejbPlugin;
    private final List<BeanContext> compContexts = new ArrayList<BeanContext>();

    public CdiResourceInjectionService(final WebBeansContext context) {
        ejbPlugin = CdiPlugin.class.cast(context.getPluginLoader().getEjbPlugin());
    }

    public void setAppContext(final AppContext appModule) {
        for (final BeanContext beanContext : appModule.getBeanContexts()) {
            if (beanContext.getBeanClass().equals(BeanContext.Comp.class)) {
                compContexts.add(beanContext);
            }
        }
    }

    @Override
    public <X, T extends Annotation> X getResourceReference(final ResourceReference<X, T> resourceReference) {
        final Class<X> type = resourceReference.getResourceType();
        final String name = resourceReference.getJndiName();

        try {
            return type.cast(new InitialContext().lookup(name));
        } catch (final NamingException e) {

            for (final BeanContext beanContext : compContexts) {
                try {
                    final String relativeName = name.replace("java:", "");
                    return type.cast(beanContext.getJndiContext().lookup(relativeName));
                } catch (final NamingException e1) {
                    // fine for now
                }
            }
//            throw new WebBeansException("Could not look up resource at " + resourceReference.getJndiName(), e);
       }
        return null;
    }

    @Override
    public void injectJavaEEResources(final Object managedBeanInstance) {
        if (managedBeanInstance != null && ejbPlugin.isSessionBean(managedBeanInstance.getClass())) { // already done
            return;
        }

        final ObjectRecipe receipe = PassthroughFactory.recipe(managedBeanInstance);
        receipe.allow(Option.FIELD_INJECTION);
        receipe.allow(Option.PRIVATE_PROPERTIES);
        receipe.allow(Option.IGNORE_MISSING_PROPERTIES);
        receipe.allow(Option.NAMED_PARAMETERS);

        fillInjectionProperties(receipe, managedBeanInstance);

        receipe.create();
    }

    @SuppressWarnings("unchecked")
    private void fillInjectionProperties(final ObjectRecipe objectRecipe, final Object managedBeanInstance) {

        final boolean usePrefix = true;
        final Class<?> clazz = managedBeanInstance.getClass();

        for (final BeanContext beanContext : compContexts) {

            for (final Injection injection : beanContext.getInjections()) {
                if (injection.getTarget() == null) {
                    continue;
                }
                if (!injection.getTarget().isAssignableFrom(clazz)) {
                    continue;
                }
                try {
                    final Object value = lookup(beanContext, injection);

                    final String prefix;
                    if (usePrefix) {
                        prefix = injection.getTarget().getName() + "/";
                    } else {
                        prefix = "";
                    }

                    objectRecipe.setProperty(prefix + injection.getName(), value);
                } catch (final NamingException e) {
                    logger.warning("Injection data not found in JNDI context: jndiName='" + injection.getJndiName() + "', target=" + injection.getTarget().getName() + "/" + injection.getName());
                }

            }
        }
    }

    private Object lookup(final BeanContext beanContext, final Injection injection) throws NamingException {
        String jndiName = injection.getJndiName();

        try {
            return beanContext.getJndiContext().lookup(jndiName);
        } catch (final NamingException e) {

            if (!jndiName.startsWith("java:")) {
                jndiName = "java:" + jndiName;
            }

            Object value;
            try {

                value = InjectionProcessor.unwrap(beanContext.getJndiContext()).lookup(jndiName);
            } catch (final NamingException e1) {
                // Fallback and try the Context on the current thread
                //
                // We attempt to create a Context instance for each
                // individual CDI bean.  This isn't really accurate
                // however, and in a webapp all the beans will share
                // the same JNDI Context.  This fallback will cover
                // the situation where we did not accurately create
                // a Context for each bean and instead the Context
                // on the thread (the webapp context) has the data
                // we need to lookup.

                try {
                    value = new InitialContext().lookup(jndiName);
                } catch (final NamingException e2) {
                    throw e;
                }
            }
            return value;
        }
    }

    @Override
    public void clear() {
        compContexts.clear();
    }

    /**
      * delegation of serialization behavior
      */
     public <T> void writeExternal(final Bean<T> bean, final T actualResource, final ObjectOutput out) throws IOException {
         //do nothing
     }

     /**
      * delegation of serialization behavior
      */
     public <T> T readExternal(final Bean<T> bean, final ObjectInput out) throws IOException,
             ClassNotFoundException {
         return (T) ((ResourceBean)bean).getActualInstance();
     }
    

}
