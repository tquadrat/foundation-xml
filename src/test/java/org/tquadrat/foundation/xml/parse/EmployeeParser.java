/*
 * ============================================================================
 * Copyright © 2002-2018 by Thomas Thrien.
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

import static java.lang.System.out;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.List;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.parse.spi.HandlerProvider;
import org.tquadrat.foundation.xml.parse.spi.StAXParserBase;
import org.xml.sax.SAXException;
import com.howtodoinjava.demo.stax.Employee;

/**
 *  A tester for the class
 *  {@link StAXParserBase}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: EmployeeParser.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 11
 */
@ClassVersion( sourceVersion = "$Id: EmployeeParser.java 820 2020-12-29 20:34:22Z tquadrat $" )
public class EmployeeParser extends StAXParserBase<List<Employee>>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code EmployeeParser} instance.
     *
     *  @param  target  The target data structure.
     */
    public EmployeeParser( @SuppressWarnings( "exports" ) final List<Employee> target )
    {
        super( target );

        registerElementHandler( "employees", true, this::handleEmployees );
        registerElementHandler( "employee", false, this::handleEmployee );
        registerElementHandler( "name", false, this::handleName );
        registerElementHandler( "title", false, this::handleTitle );
    }   //  EmployeeParser()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Handles the 'employee' tag.
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
     *  @throws XMLStreamException    Something went wrong.
     */
    private final Employee handleEmployee( final XMLEventReader reader, final XMLEvent xmlEvent, final Object target, final HandlerProvider handlerProvider ) throws XMLStreamException
    {
        final var retValue = new Employee();

        //---* Retrieve the attributes *---------------------------------------

        //---* Process the child elements *------------------------------------
        String elementName = null;
        while( reader.hasNext() )
        {
            final var currentEvent = reader.nextEvent();
            if( currentEvent.isStartElement() )
            {
                final var startElement = currentEvent.asStartElement();
                elementName = startElement.getName().getLocalPart();
                out.println( "<" + elementName );
                @SuppressWarnings( "unchecked" )
                final var handler = (XMLParseEventHandler<Employee>) handlerProvider.retrieveHandler( elementName );
                handler.process( reader, currentEvent, retValue, handlerProvider );
            }
            else if( xmlEvent.isEndElement() )
            {
                final var endElement = xmlEvent.asEndElement();
                elementName = endElement.getName().getLocalPart();
                out.println( elementName + ">" );
                if( !"employee".equals( elementName ) ) reportUnexpectedTag( elementName, currentEvent.getLocation() );
                break;
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleEmployee()

    /**
     *  Handles the 'employees' tag.
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
    private final List<Employee> handleEmployees( final XMLEventReader reader, final XMLEvent xmlEvent, final Object target, final HandlerProvider handlerProvider ) throws XMLStreamException
    {
        @SuppressWarnings( "unchecked" )
        final var retValue = (List<Employee>) requireNonNullArgument( target, "target" );
        var currentEvent = requireNonNullArgument( xmlEvent, "xmlEvent" );

        String elementName = null;
        Employee employee = null;
        while( reader.hasNext() )
        {
            currentEvent = reader.nextEvent();
            if( currentEvent.isStartElement() )
            {
                final var startElement = currentEvent.asStartElement();
                elementName = startElement.getName().getLocalPart();
                if( "employee".equals( elementName ) )
                {
                    @SuppressWarnings( "unchecked" )
                    final var handler = (XMLParseEventHandler<Employee>) handlerProvider.retrieveHandler( elementName );
                    employee = handler.process( reader, currentEvent, employee, handlerProvider );
                    retValue.add( employee );
                }
                else reportUnexpectedTag( elementName, currentEvent.getLocation() );
            }
            else if( xmlEvent.isEndElement() )
            {
                final var endElement = xmlEvent.asEndElement();
                elementName = endElement.getName().getLocalPart();
                if( !"employees".equals( elementName ) ) reportUnexpectedTag( elementName, currentEvent.getLocation() );
                break;
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleEmployees()

    /**
     *  Handles the 'name' tag.
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
     *  @throws XMLStreamException    Something went wrong.
     */
    @SuppressWarnings( "CastToConcreteClass" )
    private final Employee handleName( final XMLEventReader reader, final XMLEvent xmlEvent, final Object target, final HandlerProvider handlerProvider ) throws XMLStreamException
    {
        final var retValue = (Employee) requireNonNullArgument( target, "target" );

        retValue.setName( reader.nextEvent().asCharacters().getData() );
        reader.nextEvent();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleName()

    /**
     *  Handles the 'employee' tag.
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
     *  @throws XMLStreamException    Something went wrong.
     */
    @SuppressWarnings( "CastToConcreteClass" )
    private final Employee handleTitle( final XMLEventReader reader, final XMLEvent xmlEvent, final Object target, final HandlerProvider handlerProvider ) throws XMLStreamException
    {
        final var retValue = (Employee) requireNonNullArgument( target, "target" );

        retValue.setTitle( reader.nextEvent().asCharacters().getData() );
        reader.nextEvent();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleTitle()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "exports" )
    @Override
    public final List<Employee> parse( final XMLEventReader eventReader ) throws SAXException
    {
        final var retValue = super.parse( eventReader );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parse()
}
//  class EmployeeParser

/*
 *  End of File
 */