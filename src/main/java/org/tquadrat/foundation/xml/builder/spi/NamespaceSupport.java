/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.xml.builder.spi;

import static java.util.Arrays.stream;
import static java.util.Collections.sort;
import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.composeNamespaceString;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.MountPoint;
import org.tquadrat.foundation.lang.Objects;
import org.tquadrat.foundation.util.LazySet;
import org.tquadrat.foundation.xml.builder.Namespace;

/**
 *  This class provides the support for namespaces to elements.<br>
 *  <br>Elements that also do support attributes should use
 *  {@link AttributeSupport}
 *  instead; that includes support for namespaces. In fact, that class extends
 *  this one.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: NamespaceSupport.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "preview" )
@ClassVersion( sourceVersion = "$Id: NamespaceSupport.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public sealed class NamespaceSupport
    permits AttributeSupport
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The namespaces for the element.
     */
    private final Set<Namespace> m_Namespaces;

    /**
     *  The element that owns this {@code NamespaceSupport} instance.
     */
    private final Element m_Owner;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code NamespaceSupport} instance.
     *
     *  @param  owner   The element that owns this {@code NamespaceSupport}
     *      instance.
     */
    public NamespaceSupport( final Element owner )
    {
        m_Owner = requireNonNullArgument( owner, "owner" );
        m_Namespaces = LazySet.use( HashSet::new );
    }   //  NamespaceSupport()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Provides access to the namespaces for this element; the returned
     *  collection is not modifiable.
     *
     *  @return A reference the children of this element; if the element does
     *      not have children, an empty collection will be returned.
     */
    public final Collection<Namespace> getNamespaces()
    {
        final List<Namespace> namespaces = new ArrayList<>( m_Namespaces );
        sort( namespaces );
        final var retValue = List.copyOf( namespaces );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getNamespaces()

    /**
     *  Returns the reference to the owner of this instance of
     *  {@code NamespaceSupport}.
     *
     *  @return The owner.
     */
    protected final Element getOwner() { return m_Owner; }

    /**
     *  Sets the given namespace.
     *
     *  @param  namespace   The namespace.
     */
    public final void setNamespace( final Namespace... namespace )
    {
        stream( requireNonNullArgument( namespace, "namespace" ) )
            .filter( Objects::nonNull )
            .forEach( m_Namespaces::add );
    }   //  setNamespace()

    /**
     *  Sets the given namespace.
     *
     *  @param  identifier  The namespace identifier.
     */
    public final void setNamespace( final URI identifier )
    {
        setNamespace( new Namespace( identifier ) );
    }   //  setNamespace()

    /**
     *  Sets the given namespace.
     *
     *  @param  identifier  The namespace identifier.
     *  @throws URISyntaxException  The provided URI String is invalid.
     */
    public final void setNamespace( final String identifier ) throws URISyntaxException
    {
        setNamespace( new Namespace( identifier ) );
    }   //  setNamespace()

    /**
     *  Sets the given namespace.<br>
     *  <br>The given prefix is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getPrefixValidator()}.
     *
     *  @param  prefix   The namespace prefix.
     *  @param  identifier  The namespace identifier.
     */
    public final void setNamespace( final String prefix, final URI identifier )
    {
        setNamespace( new Namespace( prefix, identifier ) );
    }   //  setNamespace()

    /**
     *  Sets the given namespace.<br>
     *  <br>The given prefix is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getPrefixValidator()}.
     *
     *  @param  prefix   The namespace prefix.
     *  @param  identifier  The namespace identifier.
     *  @throws URISyntaxException  The provided URI String is invalid.
     */
    public final void setNamespace( final String prefix, final String identifier ) throws URISyntaxException
    {
        setNamespace( new Namespace( prefix, identifier ) );
    }   //  setNamespace()

    /**
     *  Returns the namespaces as a single formatted string.
     *
     *  @param  indentationLevel    The indentation level.
     *  @param  prettyPrint The pretty print flag.
     *  @return The attributes string.
     */
    @MountPoint
    public String toString( final int indentationLevel, final boolean prettyPrint )
    {
        final var retValue = composeNamespaceString( indentationLevel, prettyPrint, m_Owner.getElementName(), getNamespaces() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class NamespaceSupport

/*
 *  End of File
 */