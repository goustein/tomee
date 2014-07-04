/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.openejb.bval.util;

import org.apache.bval.jsr303.ApacheValidationProvider;
import org.apache.bval.jsr303.ApacheValidatorConfiguration;
import org.apache.bval.jsr303.ConfigurationImpl;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ConfigurationState;
import javax.validation.spi.ValidationProvider;
import java.io.InputStream;
import java.util.Set;

// simply a provider which is by default apache validation provider
// but it can be changed for test purpose
public class CustomValidatorProvider implements ValidationProvider<ApacheValidatorConfiguration> {
    public static ValidationProvider provider = new ApacheValidationProvider();

    @Override
    public ApacheValidatorConfiguration createSpecializedConfiguration(final BootstrapState state) {
        return (ApacheValidatorConfiguration) provider.createSpecializedConfiguration(state);
    }

    @Override
    public javax.validation.Configuration<?> createGenericConfiguration(final BootstrapState state) {
        return provider.createGenericConfiguration(state);
    }

    @Override
    public ValidatorFactory buildValidatorFactory(final ConfigurationState configurationState) {
        return provider.buildValidatorFactory(configurationState);
    }

    public static class NullValidatorProvider implements ValidationProvider<ApacheValidatorConfiguration> {
        @Override
        public ApacheValidatorConfiguration createSpecializedConfiguration(final BootstrapState state) {
            return new NullConfig(state, provider);
        }

        @Override
        public javax.validation.Configuration<?> createGenericConfiguration(final BootstrapState state) {
            return new NullConfig(state, provider);
        }

        @Override
        public ValidatorFactory buildValidatorFactory(final ConfigurationState configurationState) {
            return null;
        }
    }


    public static class NullConfig extends ConfigurationImpl {
        public NullConfig(final BootstrapState aState, final ValidationProvider<?> aProvider) {
            super(aState, aProvider);
        }

        @Override
        public NullConfig ignoreXmlConfiguration() {
            return null;
        }

        @Override
        public NullConfig messageInterpolator(final MessageInterpolator interpolator) {
            return null;
        }

        @Override
        public NullConfig traversableResolver(final TraversableResolver resolver) {
            return null;
        }

        @Override
        public NullConfig constraintValidatorFactory(final ConstraintValidatorFactory constraintValidatorFactory) {
            return null;
        }

        @Override
        public NullConfig addMapping(final InputStream stream) {
            return null;
        }

        @Override
        public NullConfig addProperty(final String name, final String value) {
            return null;
        }

        @Override
        public MessageInterpolator getDefaultMessageInterpolator() {
            return null;
        }

        @Override
        public TraversableResolver getDefaultTraversableResolver() {
            return null;
        }

        @Override
        public ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
            return null;
        }

        @Override
        public ValidatorFactory buildValidatorFactory() {
            return new CustomValidatorFactory();
        }
    }

    public static class CustomValidatorFactory implements ValidatorFactory {
        @Override
        public Validator getValidator() {
            return new CustomValidator();
        }

        @Override
        public ValidatorContext usingContext() {
            return new CustomValidatorContext();
        }

        @Override
        public MessageInterpolator getMessageInterpolator() {
            return null;
        }

        @Override
        public TraversableResolver getTraversableResolver() {
            return null;
        }

        @Override
        public ConstraintValidatorFactory getConstraintValidatorFactory() {
            return null;
        }

        @Override
        public <T> T unwrap(final Class<T> type) {
            return null;
        }
    }

    public static class CustomValidatorContext implements ValidatorContext {
        @Override
        public ValidatorContext messageInterpolator(final MessageInterpolator messageInterpolator) {
            return null;
        }

        @Override
        public ValidatorContext traversableResolver(final TraversableResolver traversableResolver) {
            return null;
        }

        @Override
        public ValidatorContext constraintValidatorFactory(final ConstraintValidatorFactory factory) {
            return null;
        }

        @Override
        public Validator getValidator() {
            return new CustomValidator();
        }
    }

    public static class CustomValidator implements Validator {
        @Override
        public <T> Set<ConstraintViolation<T>> validate(final T object, final Class<?>... groups) {
            return null;
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validateProperty(final T object, String propertyName, Class<?>... groups) {
            return null;
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validateValue(final Class<T> beanType, final String propertyName, Object value, Class<?>... groups) {
            return null;
        }

        @Override
        public BeanDescriptor getConstraintsForClass(final Class<?> clazz) {
            return null;
        }

        @Override
        public <T> T unwrap(final Class<T> type) {
            return null;
        }
    }
}
