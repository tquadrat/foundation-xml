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
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.parse.spi.HandlerProvider;

/**
 *  The interface for an implementation of a parse event handler to be used
 *  with StAX parsing of XML files.<br>
 *  <br>This is a functional interface whose functional method is
 *  {@link #process(XMLEventReader, XMLEvent, Object, HandlerProvider)}.
 *
 *  @param  <T> The type of the target data structure.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLParseEventHandler.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: XMLParseEventHandler.java 820 2020-12-29 20:34:22Z tquadrat $" )
@FunctionalInterface
@API( status = EXPERIMENTAL, since = "0.0.5" )
public interface XMLParseEventHandler<T>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code XMLParseEventHandler} objects.
     */
    @SuppressWarnings( "rawtypes" )
    public static final XMLParseEventHandler [] EMPTY_XMLParseEventHandler_ARRAY = new XMLParseEventHandler [0];

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Handles the given event.
     *
     *  @param  reader  The XML event reader.
     *  @param  xmlEvent    The current XML event.
     *  @param  target  The instance that takes the parse result; can be
     *      {@code null} if the handler will create a new target.
     *  @param  handlerProvider A provider for XML parse event handlers that
     *      are needed to process child elements.
     *  @return The instance that took the parse result; this is either the
     *      object that was provided with the {@code target} argument, or a new
     *      object if {@code target} was {@code null}.
     *  @throws XMLStreamException  Something went wrong.
     */
    public T process( XMLEventReader reader, XMLEvent xmlEvent, T target, HandlerProvider handlerProvider ) throws XMLStreamException;
}
//  class XMLParseEventHandler

/*
 *  End of File
 */