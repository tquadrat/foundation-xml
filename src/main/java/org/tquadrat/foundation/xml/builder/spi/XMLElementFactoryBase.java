/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.xml.builder.spi;

import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.xml.builder.XMLElementFactory;

/**
 *  The default implementation of the interface
 *  {@link XMLElementFactory}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLElementFactoryBase.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"AbstractClassNeverImplemented", "AbstractClassWithoutAbstractMethods", "preview"} )
@ClassVersion( sourceVersion = "$Id: XMLElementFactoryBase.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public abstract non-sealed class XMLElementFactoryBase implements XMLElementFactory
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code XMLElementFactoryImpl} objects.
     */
    public static final XMLElementFactoryBase[] EMPTY_XMLElementFactoryBase_ARRAY = new XMLElementFactoryBase[0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The namespace that is used by this element factory.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private final Optional<Namespace> m_Namespace;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code XMLElementFactoryImpl} instance that does not use
     *  a namespace.
     */
    protected XMLElementFactoryBase() { m_Namespace = Optional.empty(); }

    /**
     *  Creates a new {@code XMLElementFactoryImpl} instance that uses the
     *  given namespace.
     *
     *  @param  namespace   The namespace that is used by this XML element
     *      factory.
     */
    protected XMLElementFactoryBase( final Namespace namespace )
    {
        m_Namespace = Optional.of( requireNonNullArgument( namespace, "namespace" ) );
    }   //  XMLElementFactoryImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Optional<Namespace> getNamespace() { return m_Namespace; }
}
//  class XMLElementFactoryBase

/*
 *  End of File
 */