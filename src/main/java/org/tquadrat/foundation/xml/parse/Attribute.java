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

package org.tquadrat.foundation.xml.parse;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.apiguardian.api.API.Status.STABLE;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  An attribute.
 *
 *  @param  qName   The XML qualified (prefixed) name for the attribute.
 *  @param  localName   An instance of
 *      {@link Optional}
 *      that holds the local name for the attribute; will be
 *      {@linkplain Optional#empty() empty} if Namespace processing is not
 *      being performed.
 *  @param  uri An instance
 *      {@link Optional}
 *      that holds the Namespace URI for the attribute; will be
 *      {@linkplain Optional#empty() empty} if  none is available.
 *  @param  type    The attribute type. If the parser has not read a
 *      declaration for the attribute, or if the parser does not report
 *      attribute types, then it must return the value
 *      {@link Type#CDATA CDATA}
 *      as stated in the XML 1.0 Recommendation (clause 3.3.3,
 *      &quot;Attribute-Value Normalization&quot;). For an enumerated attribute
 *      that is not a notation
 *      ({@link Type#NOTATION NOTATION}),
 *      the parser will report the type as
 *      {@link Type#NMTOKEN NMTOKEN}.
 *  @param  value   The value of the attribute.
 *  @param  index   The index of the attribute in the attributes list for the
 *      element.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Attribute.java 1030 2022-04-06 13:42:02Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Attribute.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public record Attribute( String qName, Optional<String> localName, Optional<URI> uri, Type type, String value, int index )
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The attribute types.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: Attribute.java 1030 2022-04-06 13:42:02Z tquadrat $
     *  @since 0.1.0
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( "NewClassNamingConvention" )
    @ClassVersion( sourceVersion = "$Id: Attribute.java 1030 2022-04-06 13:42:02Z tquadrat $" )
    @API( status = STABLE, since = "0.1.0" )
    public enum Type
    {
            /*------------------*\
        ====** Enum Declaration **=============================================
            \*------------------*/
        /**
         *  Character data.
         */
        CDATA,

        /**
         *  A unique element identifier.
         */
        ID,

        /**
         *  The value of a unique ID type attribute, as a reference to the
         *  respective element.
         */
        IDREF,

        /**
         *  A list of
         *  {@link #IDREF}s.
         */
        IDREFS,

        /**
         *  An XML name token.
         */
        NMTOKEN,

        /**
         *  Multiple
         *  {@link #NMTOKEN}s.
         */
        NMTOKENS,

        /**
         *  An entity as declared in the DTD.
         */
        ENTITY,

        /**
         *  Multiple
         *  {@linkplain #ENTITY entities}.
         */
        ENTITIES,

        /**
         *  A notation as declared in the DTD.
         */
        NOTATION
    }
    //  enum Type

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  If the attribute is of type
     *  {@link Type#ENTITY}
     *  or
     *  {@link Type#ENTITIES},
     *  this method will return a list of the entity values.
     *
     *  @return The entity values; the return value is empty, if the attribute
     *      is not of type
     *      {@link Type#ENTITY}
     *      or
     *      {@link Type#ENTITIES}.
     */
    public Collection<String> getEntities()
    {
        final Collection<String> retValue = switch( type )
            {
                case ENTITY -> List.of( value );
                case ENTITIES -> List.of( value.split( "\\s" ) );
                //required by Eclipse
                case CDATA, ID, IDREF, IDREFS, NMTOKEN, NMTOKENS, NOTATION -> emptyList();
                default -> emptyList();
            };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getEntities()

    /**
     *  If the attribute is of type
     *  {@link Type#IDREF}
     *  or
     *  {@link Type#IDREFS},
     *  this method will return a list of the ID references.
     *
     *  @return The ID references; the return value is empty, if the attribute
     *      is not of type
     *      {@link Type#IDREF}
     *      or
     *      {@link Type#IDREFS}.
     */
    public Collection<String> getIDReferences()
    {
        final Collection<String> retValue = switch( type )
            {
                case IDREF -> Set.of( value );
                case IDREFS -> Set.of( value.split( "\\s" ) );
                //required by Eclipse
                case CDATA, ENTITIES, ENTITY, ID, NMTOKEN, NMTOKENS, NOTATION -> emptyList();
                default -> emptySet();
            };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getIDReferences()

    /**
     *  If the attribute is of type
     *  {@link Type#NMTOKEN}
     *  or
     *  {@link Type#NMTOKENS},
     *  this method will return a list of the name tokens.
     *
     *  @return The name tokens; the return value is empty, if the attribute
     *      is not of type
     *      {@link Type#NMTOKEN}
     *      or
     *      {@link Type#NMTOKENS},
     */
    public Collection<String> getNameTokens()
    {
        final Collection<String> retValue = switch( type )
            {
                case NMTOKEN -> List.of( value );
                case NMTOKENS -> List.of( value.split( "\\s" ) );
                //required by Eclipse
                case CDATA, ENTITIES, ENTITY, ID, IDREF, IDREFS, NOTATION -> emptyList();
                default -> emptyList();
            };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getNameTokens()
}
//  record Attribute

/*
 *  End of File
 */