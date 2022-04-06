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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.parse.AbstractXMLReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *  This class implements the sample from the class definition of
 *  {@link AbstractXMLReader}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PropertiesReader.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: PropertiesReader.java 1030 2022-04-06 13:42:02Z tquadrat $" )
public final class PropertiesReader extends AbstractXMLReader
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new PropertiesReader instance for the given content
     *  handler.
     *
     *  @param  contentHandler The content handler.
     */
    public PropertiesReader( final ContentHandler contentHandler ) { super( contentHandler ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected void process( final BufferedReader input ) throws IOException, SAXException
    {
        final var handler = getContentHandler();

        //---* Load the properties *-------------------------------------------
        final var properties = new Properties();
        properties.load( input );

        //---* Create the document *-------------------------------------------
        handler.startDocument();
        handler.startElement( null, null, "properties", NO_ATTRIBUTES );

        //---* Process the properties *----------------------------------------
        AttributesImpl attributes;
        char [] value;

        /*
         * Each property will be treated as a value with the key as its
         * attribute.
         */
        for( final var name : properties.stringPropertyNames() )
        {
            //---* Start the element *-----------------------------------------
            attributes = new AttributesImpl();
            attributes.addAttribute( null, null, "name", "ID", name );
            handler.startElement( null, null, "property", attributes );

            //---* The element contents *--------------------------------------
            value = properties.getProperty( name ).toCharArray();
            handler.characters( value, 0, value.length );

            //---* End the element *-------------------------------------------
            handler.endElement( null, null, "property" );
        }

        //---* Finish the document *-------------------------------------------
        handler.endElement( null, null, "properties" );
        handler.endDocument();
    }   //  process()
}
//  class PropertiesReader

/*
 *  End of File
 */