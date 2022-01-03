/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
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

package org.tquadrat.foundation.xml.parse;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import javax.xml.stream.XMLEventReader;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.parse.spi.StAXParserBase;

/**
 *  Parses an XML stream to an object of type {@code T}; that object is either
 *  provided with the constructor
 *  {@link #StAXParser(Object)}
 *  or will be created by an instance of
 *  {@link XMLParseEventHandler}.<br>
 *  <br>To start the parsing process, call
 *  {@link #parse(XMLEventReader)}
 *  on the instance of {@code StAXParser}.
 *
 *  @param  <T> The type of the target data structure.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: StAXParser.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: StAXParser.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = EXPERIMENTAL, since = "0.0.5" )
public final class StAXParser<T> extends StAXParserBase<T>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code StAXParser} objects.
     */
    @SuppressWarnings( "rawtypes" )
    public static final StAXParser [] EMPTY_StAXParser_ARRAY = new StAXParser [0];

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code StAXParser} instance.
     */
    public StAXParser() { super(); }

    /**
     *  Creates a new {@code StAXParser} instance.
     *
     *  @param  target  The target data structure.
     */
    public StAXParser( final T target ) { super( target ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds the document handler.<br>
     *  <br>This handler must create or update the target data structure.
     *
     *  @param  elementName The element name for the document.
     *  @param  handler The parse event handler.
     */
    public final void addDocumentHandler( final String elementName, final XMLParseEventHandler<T> handler )
    {
        registerElementHandler( elementName, true, handler );
    }   //  addDocumentHandler()

    /**
     *  Adds an element handler.<br>
     *  <br>These handlers will be called from inside another parse event
     *  handler.
     *
     *  @param  elementName The element name.
     *  @param  handler The parse event handler.
     */
    public final void addElementHandler( final String elementName, final XMLParseEventHandler<?> handler )
    {
        registerElementHandler( elementName, false, handler );
    }   //  addElementHandler()
}
//  class StAXParser

/*
 *  End of File
 */