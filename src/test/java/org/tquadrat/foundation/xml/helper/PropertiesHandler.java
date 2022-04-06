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

package org.tquadrat.foundation.xml.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.parse.AdvancedContentHandler;
import org.xml.sax.SAXException;

/**
 *  The implementation for the
 *  {@link AdvancedContentHandler}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PropertiesHandler.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: PropertiesHandler.java 1030 2022-04-06 13:42:02Z tquadrat $" )
public final class PropertiesHandler extends AdvancedContentHandler
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}<br>
     *  <br>This implementation does nothing.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    protected void openElement( final Element element ) throws SAXException
    {
        //  Does nothing!
    }   //  openElement()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    protected void processElement( final Element element ) throws SAXException
    {
        final var qName = element.getQName();
        final var path = element.getPath();
        final var attributes = element.getAttributes();
        final var data = element.getData();

        if( "properties".equals( qName ) )
        {
            assertEquals( "/properties", path );
            assertTrue( attributes.isEmpty() );
            assertNotNull( data );
            assertTrue( data.isEmpty() );
        }
        else if( "property".equals( qName ) )
        {
            assertEquals( "/properties/property", path );
            assertFalse( attributes.isEmpty() );
            assertNotNull( data );
            assertFalse( data.isEmpty() );
            assertTrue( data.startsWith( "Value" ) );

            final var attribute = "name";
            assertTrue( attributes.containsKey( attribute ) );
            final var name = attributes.get( attribute );
            assertNotNull( name );
            assertTrue( name.value().startsWith( "Key" ) );
        }
        else
        {
            fail( "Unknown/illegal element" );
        }
    }   //  processElement()
}
//  class PropertiesHandler

/*
 *  End of File
 */