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

package org.tquadrat.foundation.xml.parse.spi;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import javax.xml.stream.XMLStreamException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.parse.XMLParseEventHandler;

/**
 *  The interface for a function that returns an instance of
 *  {@link XMLParseEventHandler}
 *  for an XML element.<br>
 *  <br>This is a functional interface whose functional method is
 *  {@link #retrieveHandler(String)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: HandlerProvider.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: HandlerProvider.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = EXPERIMENTAL, since = "0.0.5" )
@FunctionalInterface
public interface HandlerProvider
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code HandlerProvider} objects.
     */
    public static final HandlerProvider [] EMPTY_HandlerProvider_ARRAY = new HandlerProvider [0];

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Retrieves the XML parse event handler for the given element name.
     *
     *  @param  elementName The name of the element to handle.
     *  @return The requested instance of
     *      {@link XMLParseEventHandler}.
     *  @throws XMLStreamException  There is no registered handler for the
     *      given element name.
     */
    public XMLParseEventHandler<?> retrieveHandler( final String elementName ) throws XMLStreamException;
}
//  class HandlerProvider

/*
 *  End of File
 */