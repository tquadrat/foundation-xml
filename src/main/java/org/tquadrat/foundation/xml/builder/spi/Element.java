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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.composeElementString;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.Namespace;

/**
 *  The definition for an SGML element.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Element.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Element.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public interface Element
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code Element} objects.
     */
    public static final Element [] EMPTY_Element_ARRAY = new Element [0];

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the value for the attribute with the given name.
     *
     *  @param  name    The attribute name.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the value for that attribute.
     */
    public default Optional<String> getAttribute( final String name )
    {
        requireNotEmptyArgument( name, "name" );
        final Optional<String> retValue = Optional.empty();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getAttribute()

    /**
     *  Provides read access to the attributes.
     *
     *  @return A reference to the attributes.
     */
    public default Map<String,String> getAttributes() { return emptyMap(); }

    /**
     *  Provides access to the children for this element; the returned
     *  collection is not modifiable.
     *
     *  @return A reference to the children of this element; if the element
     *      does not have children, an empty collection will be returned.
     */
    public default Collection<? extends Element> getChildren() { return emptyList(); }

    /**
     *  Returns the name of the element.
     *
     *  @return The name of the element.
     */
    public String getElementName();

    /**
     *  Provides access to the namespaces for this element; the returned
     *  collection is not modifiable.
     *
     *  @return A reference to the namespaces of this element; if the element
     *      does not have namespaces assigned, an empty collection will be
     *      returned.
     */
    public default Collection<Namespace> getNamespaces() { return emptySet(); }

    /**
     *  Returns the parent of this element.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the parent.
     */
    public Optional<Element> getParent();

    /**
     *  Returns {@code true} if the element has children, {@code false}
     *  otherwise.
     *
     *  @return {@code true} if the element has children.
     */
    public default boolean hasChildren() { return !getChildren().isEmpty(); }

    /**
     *  Returns the block flag.<br>
     *  <br>This flag is used in the conversion of the element into a String;
     *  it indicates whether the element is 'inline' (like a HTML
     *  <i>&lt;span&gt;</i>) or 'block' (as a HTML <i>&lt;div&gt;</i>). This is
     *  important only for elements where whitespace is relevant, like for HTML
     *  elements, as <i>pretty printing</i> will add additional whitespace
     *  around inline elements that can become visible on parsing (for HTML: on
     *  the rendered page).<br>
     *  <br>XML elements for example will be always <i>block</i> as there
     *  whitespace is not that important.<br>
     *  <br>Obviously, {@code true} indicates a block element, while
     *  {@code false} stands for an inline element.<br>
     *  <br>The default is {@code true}.
     *
     *  @return  The flag.
     */
    public default boolean isBlock() { return true; }

    /**
     *  Sets the parent for this element.
     *
     *  @param  <E> The implementation of {@code Element}.
     *  @param  parent  The parent.
     */
    public <E extends Element> void setParent( final E parent );

    /**
     *  Returns a String representation for this element instance.
     *
     *  @param  indentationLevel    The indentation level.
     *  @param  prettyPrint The pretty print flag.
     *  @return The String representation.
     */
    public default String toString( final int indentationLevel, final boolean prettyPrint )
    {
        final var retValue = composeElementString( indentationLevel, prettyPrint, this, true );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  interface Element

/*
 *  End of File
 */