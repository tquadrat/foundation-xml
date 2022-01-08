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

import static org.apiguardian.api.API.Status.MAINTAINED;

import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.internal.XMLElementImpl;

/**
 *  The abstract base class for specialised implementations of
 *  {@link org.tquadrat.foundation.xml.builder.XMLElement}.
 *  Because it is derived from
 *  {@link XMLElementImpl},
 *  it supports attributes, namespaces, children, text, {@code CDATA} and
 *  comments.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLElementAdapter.java 980 2022-01-06 15:29:19Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "AbstractClassExtendsConcreteClass" )
@ClassVersion( sourceVersion = "$Id: XMLElementAdapter.java 980 2022-01-06 15:29:19Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public abstract non-sealed class XMLElementAdapter extends XMLElementImpl
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code XMLElementAdapter} instance.}</p>
     *  <p>The given element name is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getElementNameValidator()}.</p>
     *  <p>The new element allows attributes and children, but will not
     *  validate them. It also allows text.</p>
     *
     *  @param  elementName The element name.
     */
    protected XMLElementAdapter( final String elementName ) { super( elementName ); }

    /**
     *  <p>{@summary Creates a new {@code XMLElementAdapter} instance.}</p>
     *  <p>The given element name is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getElementNameValidator()}.</p>
     *
     *  @note   This constructor is used for the implementation of XML
     *      specialisations, like SVG or HTML (although this not really XML).
     *
     *  @param  elementName The element name.
     *  @param  flags   The configuration flags for the new element.
     *
     *  @see org.tquadrat.foundation.xml.builder.XMLElement.Flags
     *  @see AttributeSupport#registerAttributes(String...)
     *  @see AttributeSupport#registerSequence(String...)
     *  @see ChildSupport#registerChildren(String...)
     */
    protected XMLElementAdapter( final String elementName, final Set<Flags> flags )
    {
        super( elementName, flags );
    }   //  XMLElementAdapter()
}
//  class XMLElementAdapter

/*
 *  End of File
 */