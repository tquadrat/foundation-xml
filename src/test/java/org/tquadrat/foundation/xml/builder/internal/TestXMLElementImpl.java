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

package org.tquadrat.foundation.xml.builder.internal;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.XMLATTRIBUTE_Id;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link XMLElementImpl}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestXMLElementImpl.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestXMLElementImpl.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.internal.TestXMLElementImpl" )
public class TestXMLElementImpl extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link XMLElementImpl#XMLElementImpl(String)}.
     *
     *  @throws URISyntaxException  The URI is invalid.
     */
    @SuppressWarnings( {"OverlyLongMethod", "ImplicitNumericConversion", "GrazieInspection"} )
    @Test
    final void testConstructor() throws URISyntaxException
    {
        skipThreadTest();

        XMLElementImpl candidate;

        final var elementName = "element";
        final var prefix = "prefix";
        final var identifier = "tquadrat.org/foundation";
        final var uri = URI.create( identifier );

        candidate = new XMLElementImpl( elementName );
        assertNotNull( candidate );
        candidate.setNamespace( identifier );

        candidate = new XMLElementImpl( elementName );
        assertNotNull( candidate );
        candidate.setNamespace( uri );

        candidate = new XMLElementImpl( elementName );
        assertNotNull( candidate );
        candidate.setNamespace( prefix, identifier );

        candidate = new XMLElementImpl( elementName );
        assertNotNull( candidate );
        candidate.setNamespace( prefix, uri );

        var namespace = new Namespace( identifier );
        candidate = new XMLElementImpl( elementName );
        assertNotNull( candidate );
        candidate.setNamespace( namespace );

        namespace = new Namespace( prefix, identifier );
        candidate = new XMLElementImpl( elementName );
        assertNotNull( candidate );
        candidate.setNamespace( namespace );

        candidate.addCDATA( true );
        candidate.addCDATA( FALSE );
        candidate.addCDATA( '&' );
        candidate.addCDATA( Character.valueOf( '<' ) );
        candidate.addCDATA( "Dies ist ein Text]]mit zwei schließenden eckigen Klammern" );
        candidate.addCDATA( 3.14D );
        candidate.addCDATA( Thread.State.TERMINATED );
        candidate.addCDATA( Instant.ofEpochMilli( 0L ) );
        candidate.addCDATA( 42 );
        candidate.addCDATA( LocalDate.of( 1970, 1, 1 ) );
        candidate.addCDATA( LocalDateTime.of( 1970, 1, 1, 0, 0, 0 ) );
        candidate.addCDATA( Long.MAX_VALUE );
        candidate.addCDATA( Double.valueOf( 1.4142 ) );
        candidate.addCDATA( ZonedDateTime.of( 1970, 12, 1, 0, 0, 0, 0, ZoneId.of( "UTC" ) ) );

        assertEquals( "\n<element xmlns:prefix=\"tquadrat.org/foundation\">"
                    + "<![CDATA[true]]>"
                    + "<![CDATA[false]]>"
                    + "<![CDATA[&]]>"
                    + "<![CDATA[<]]>"
                    + "<![CDATA[Dies ist ein Text]]>]]<![CDATA[mit zwei schließenden eckigen Klammern]]>"
                    + "<![CDATA[3.14]]>"
                    + "<![CDATA[TERMINATED]]>"
                    + "<![CDATA[1970-01-01T00:00:00Z]]>"
                    + "<![CDATA[42]]>"
                    + "<![CDATA[1970-01-01]]>"
                    + "<![CDATA[1970-01-01T00:00]]>"
                    + "<![CDATA[9223372036854775807]]>"
                    + "<![CDATA[1.4142]]>"
                    + "<![CDATA[1970-12-01T00:00Z[UTC]]>]"
                    + "</element>", candidate.toString() );

        candidate = new XMLElementImpl( elementName );
        assertNotNull( candidate );

        candidate.addText( true );
        candidate.addText( FALSE );
        candidate.addText( '&' );
        candidate.addText( Character.valueOf( '<' ) );
        candidate.addText( "Dies ist ein Text]]mit zwei schließenden eckigen Klammern" );
        candidate.addText( 3.14D );
        candidate.addText( Thread.State.TERMINATED );
        candidate.addText( Instant.ofEpochMilli( 0L ) );
        candidate.addText( 42 );
        candidate.addText( LocalDate.of( 1970, 1, 1 ) );
        candidate.addText( LocalDateTime.of( 1970, 1, 1, 0, 0, 0 ) );
        candidate.addText( Long.MAX_VALUE );
        candidate.addText( Double.valueOf( 1.4142 ) );
        candidate.addText( ZonedDateTime.of( 1970, 12, 1, 0, 0, 0, 0, ZoneId.of( "UTC" ) ) );

        assertEquals( """
                
                      <element>\
                      true\
                      false\
                      &amp;\
                      &lt;\
                      Dies ist ein Text]]mit zwei schlie&#xDF;enden eckigen Klammern\
                      3.14\
                      TERMINATED\
                      1970-01-01T00:00:00Z\
                      42\
                      1970-01-01\
                      1970-01-01T00:00\
                      9223372036854775807\
                      1.4142\
                      1970-12-01T00:00Z[UTC]\
                      </element>""", candidate.toString() );

        candidate = new XMLElementImpl( elementName );
        assertNotNull( candidate );

        candidate.registerAttributeSequence( XMLATTRIBUTE_Id, "attribute1",
            "attribute2", "attribute3", "attribute4", "attribute5",
            "attribute6", "attribute7", "attribute8", "attribute9",
            "attribute10", "attribute11", "attribute12", "attribute13",
            "attribute14", "empty", "notEmpty1", "notEmpty2" );

        candidate.setId( "id" );
        candidate.setAttribute( "attribute1", false );
        candidate.setAttribute( "attribute1", true );
        candidate.setAttribute( "attribute2", TRUE );
        candidate.setAttribute( "attribute2", FALSE );
        candidate.setAttribute( "attribute3", '+' );
        candidate.setAttribute( "attribute3", '&' );
        candidate.setAttribute( "attribute4", Character.valueOf( '>' ) );
        candidate.setAttribute( "attribute4", Character.valueOf( '<' ) );
        candidate.setAttribute( "attribute5", "Dies ist ein Text[[mit zwei öffnenden eckigen Klammern" );
        candidate.setAttribute( "attribute5", "Dies ist ein Text]]mit zwei schließenden eckigen Klammern" );
        candidate.setAttribute( "attribute6", 3.15D );
        candidate.setAttribute( "attribute6", 3.14D );
        candidate.setAttribute( "attribute7", Thread.State.RUNNABLE );
        candidate.setAttribute( "attribute7", Thread.State.TERMINATED );
        candidate.setAttribute( "attribute8", Instant.ofEpochMilli( Long.MIN_VALUE ) );
        candidate.setAttribute( "attribute8", Instant.ofEpochMilli( 0L ) );
        candidate.setAttribute( "attribute9", 24 );
        candidate.setAttribute( "attribute9", 42 );
        candidate.setAttribute( "attribute10", LocalDate.of( 1900, 1, 1 ) );
        candidate.setAttribute( "attribute10", LocalDate.of( 1970, 1, 1 ) );
        candidate.setAttribute( "attribute11", LocalDateTime.of( 1900, 1, 1, 0, 0, 0 ) );
        candidate.setAttribute( "attribute11", LocalDateTime.of( 1970, 1, 1, 0, 0, 0 ) );
        candidate.setAttribute( "attribute12", 0L );
        candidate.setAttribute( "attribute12", Long.MAX_VALUE );
        candidate.setAttribute( "attribute13", Double.valueOf( 2.0 ) );
        candidate.setAttribute( "attribute13", Double.valueOf( 1.4142 ) );
        candidate.setAttribute( "attribute14", ZonedDateTime.of( 1970, 12, 1, 0, 0, 0, 0, ZoneId.of( "Europe/Berlin" ) ) );
        candidate.setAttribute( "attribute14", ZonedDateTime.of( 1970, 12, 1, 0, 0, 0, 0, ZoneId.of( "UTC" ) ) );
        candidate.setAttributeIfNotEmpty( "empty", EMPTY_STRING );
        assertFalse( candidate.getAttribute( "empty" ).isPresent() );
        candidate.setAttributeIfNotEmpty( "notEmpty1", "value" );
        assertTrue( candidate.getAttribute( "notEmpty1" ).isPresent() );
        candidate.setAttributeIfNotEmpty( "empty", Optional.empty() );
        candidate.setAttributeIfNotEmpty( "notEmpty2", Optional.of( "value" ) );

        assertEquals( """
            
                      <element xml:id='id'
                               attribute1='true'
                               attribute2='false'
                               attribute3='38'
                               attribute4='60'
                               attribute5='Dies ist ein Text]]mit zwei schließenden eckigen Klammern'
                               attribute6='3.14'
                               attribute7='TERMINATED'
                               attribute8='1970-01-01T00:00:00Z'
                               attribute9='42'
                               attribute10='1970-01-01'
                               attribute11='1970-01-01T00:00'
                               attribute12='9223372036854775807'
                               attribute13='1.4142'
                               attribute14='1970-12-01T00:00Z[UTC]'
                               notEmpty1='value'
                               notEmpty2='value'/>""", candidate.toString() );

        candidate = new XMLElementImpl( elementName );
        assertNotNull( candidate );

        candidate.addComment( "comment" );
        candidate.addPredefinedMarkup( "<child id='id' />" );

        assertEquals( """
                        
                        <element>
                            <!--
                            comment
                            -->
                            <child id='id' />
                        </element>""", candidate.toString() );
    }   //  testConstructor()

    /**
     *  Tests for
     *  {@link XMLElementImpl#XMLElementImpl(String)}.
     */
    @Test
    final void testConstructorWithEmptyArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" ) final XMLElementImpl candidate;

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate = new XMLElementImpl( EMPTY_STRING );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConstructorWithEmptyArgument()

    /**
     *  Tests for
     *  {@link XMLElementImpl#XMLElementImpl(String)}.
     *
     *  @param  isValid {@code true} if the given element name is valid,
     *      {@code false} otherwise.
     *  @param  elementName The name of the new element.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "/org/tquadrat/foundation/xml/builder/xmlbuilderutils/DefaultElementNameValidator.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testConstructorWithInvalidArgument( final boolean isValid, final String elementName)
    {
        skipThreadTest();

        assumeFalse( isValid ); // We test for failure

        @SuppressWarnings( "unused" ) final XMLElementImpl candidate;

        final Class<? extends Throwable> expectedException = InvalidXMLNameException.class;
        try
        {
            candidate = new XMLElementImpl( elementName );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConstructorWithInvalidArgument()

    /**
     *  Tests for
     *  {@link XMLElementImpl#XMLElementImpl(String)}.
     */
    @Test
    final void testConstructorWithNullArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" ) final XMLElementImpl candidate;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new XMLElementImpl( null );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConstructorWithNullArgument()
}
//  class TestXMLElementImpl

/*
 *  End of File
 */