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
package org.apache.openejb.jee;

import org.metatype.sxc.jaxb.JAXBObject;
import org.metatype.sxc.jaxb.LifecycleCallback;
import org.metatype.sxc.jaxb.RuntimeContext;
import org.metatype.sxc.util.Attribute;
import org.metatype.sxc.util.XoXMLStreamReader;
import org.metatype.sxc.util.XoXMLStreamWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.namespace.QName;

@SuppressWarnings({
    "StringEquality"
})
public class FacesDefaultLocale$JAXB
    extends JAXBObject<FacesDefaultLocale> {


    public FacesDefaultLocale$JAXB() {
        super(FacesDefaultLocale.class, null, new QName("http://java.sun.com/xml/ns/javaee".intern(), "faces-config-default-localeType".intern()));
    }

    public static FacesDefaultLocale readFacesDefaultLocale(final XoXMLStreamReader reader, RuntimeContext context)
        throws Exception {
        return _read(reader, context);
    }

    public static void writeFacesDefaultLocale(final XoXMLStreamWriter writer, FacesDefaultLocale facesDefaultLocale, RuntimeContext context)
        throws Exception {
        _write(writer, facesDefaultLocale, context);
    }

    public void write(final XoXMLStreamWriter writer, FacesDefaultLocale facesDefaultLocale, final RuntimeContext context)
        throws Exception {
        _write(writer, facesDefaultLocale, context);
    }

    public final static FacesDefaultLocale _read(final XoXMLStreamReader reader, RuntimeContext context)
        throws Exception {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        FacesDefaultLocale facesDefaultLocale = new FacesDefaultLocale();
        context.beforeUnmarshal(facesDefaultLocale, LifecycleCallback.NONE);


        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType != null) {
            if (("faces-config-default-localeType" != xsiType.getLocalPart()) || ("http://java.sun.com/xml/ns/javaee" != xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, FacesDefaultLocale.class);
            }
        }

        // Read attributes
        for (final Attribute attribute : reader.getAttributes()) {
            if (("id" == attribute.getLocalName()) && (("" == attribute.getNamespace()) || (attribute.getNamespace() == null))) {
                // ATTRIBUTE: id
                final String id = Adapters.collapsedStringAdapterAdapter.unmarshal(attribute.getValue());
                context.addXmlId(reader, id, facesDefaultLocale);
                facesDefaultLocale.id = id;
            } else if (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI != attribute.getNamespace()) {
                context.unexpectedAttribute(attribute, new QName("", "id"));
            }
        }

        // VALUE: value
        facesDefaultLocale.value = null;

        context.afterUnmarshal(facesDefaultLocale, LifecycleCallback.NONE);

        return facesDefaultLocale;
    }

    public final FacesDefaultLocale read(final XoXMLStreamReader reader, RuntimeContext context)
        throws Exception {
        return _read(reader, context);
    }

    public final static void _write(final XoXMLStreamWriter writer, final FacesDefaultLocale facesDefaultLocale, RuntimeContext context)
        throws Exception {
        if (facesDefaultLocale == null) {
            writer.writeXsiNil();
            return;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        if (FacesDefaultLocale.class != facesDefaultLocale.getClass()) {
            context.unexpectedSubclass(writer, facesDefaultLocale, FacesDefaultLocale.class);
            return;
        }

        context.beforeMarshal(facesDefaultLocale, LifecycleCallback.NONE);


        // ATTRIBUTE: id
        String idRaw = facesDefaultLocale.id;
        if (idRaw != null) {
            String id = null;
            try {
                id = Adapters.collapsedStringAdapterAdapter.marshal(idRaw);
            } catch (Exception e) {
                context.xmlAdapterError(facesDefaultLocale, "id", CollapsedStringAdapter.class, String.class, String.class, e);
            }
            writer.writeAttribute("", "", "id", id);
        }

        // VALUE: value
        final FacesLocale value = facesDefaultLocale.value;

        context.afterMarshal(facesDefaultLocale, LifecycleCallback.NONE);
    }

}
