/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

package org.tquadrat.foundation.xml.builder.xmlbuilderutils;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.ASCII;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLDocument;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLElement;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.builder.XMLDocument;
import org.tquadrat.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link XMLBuilderUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestCreateXMLDocument.java 1101 2024-02-18 00:18:48Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestCreateXMLDocument.java 1101 2024-02-18 00:18:48Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.xmlbuilderutils.TestCreateXMLDocument" )
public class TestCreateXMLDocument extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createXMLDocument(String)},
     *  {@link XMLBuilderUtils#createXMLDocument(XMLElement)},
     *  {@link XMLBuilderUtils#createXMLDocument(XMLElement, java.nio.charset.Charset, URI)},
     *  and
     *  {@link XMLBuilderUtils#createXMLDocument(XMLElement, java.nio.charset.Charset, String, URI)}.
     *
     *  @throws URISyntaxException  Something went unexpectedly wrong.
     */
    @Test
    final void testCreateXMLDocument() throws URISyntaxException
    {
        skipThreadTest();

        String actual, expected;
        XMLDocument candidate;

        final var elementName = "element";
        final var element = createXMLElement( elementName );
        final var dtdURI = new URI( "tquadrat.org/foundation" );
        final var dtd = "dtd";

        candidate = createXMLDocument();
        expected = """
                   <?xml version='1.0'
                         encoding='UTF-8'
                         standalone='yes'?>
                   
                   <root/>""";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLDocument( elementName );
        expected = STR."""
                   <?xml version='1.0'
                         encoding='UTF-8'
                         standalone='yes'?>

                   <\{elementName}/>""";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLDocument( element );
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLDocument( element, ASCII, dtdURI );
        expected = STR."""
            <?xml version='1.0'
                  encoding='\{ASCII.name()}'
                  standalone='no'?>

            <!DOCTYPE \{elementName} SYSTEM "\{dtdURI}">
            <\{elementName}/>""";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLDocument( element, ASCII, dtd, dtdURI );
        expected = STR."""
            <?xml version='1.0'
                  encoding='\{ASCII.name()}'
                  standalone='no'?>

            <!DOCTYPE \{elementName} PUBLIC "\{dtd}" "\{dtdURI}">
            <\{elementName}/>""";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );
    }   //  testCreateXMLDocument()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createXMLDocument(String)}
     *  and
     *  {@link XMLBuilderUtils#createXMLDocument(XMLElement,java.nio.charset.Charset,String, URI)}.
     *
     *  @throws URISyntaxException  Something went unexpectedly wrong.
     */
    @Test
    final void testCreateXMLDocumentWithEmptyArgument() throws URISyntaxException
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            createXMLDocument( EMPTY_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        final var element = createXMLElement( "element" );
        final var dtdURI = new URI( "tquadrat.org/foundation" );
        try
        {
            createXMLDocument( element, UTF8, EMPTY_STRING, dtdURI );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCreateXMLDocumentWithEmptyArgument()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createXMLDocument(String)},
     *  {@link XMLBuilderUtils#createXMLDocument(XMLElement)},
     *  {@link XMLBuilderUtils#createXMLDocument(XMLElement, java.nio.charset.Charset, URI)},
     *  and
     *  {@link XMLBuilderUtils#createXMLDocument(XMLElement, java.nio.charset.Charset, String, URI)}.
     *
     *  @throws URISyntaxException  Something went unexpectedly wrong.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    @Test
    final void testCreateXMLDocumentWithNullArgument() throws URISyntaxException
    {
        skipThreadTest();

        final var element = createXMLElement( "element" );
        final var dtdURI = new URI( "tquadrat.org/foundation" );

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            createXMLDocument( (String) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            createXMLDocument( (XMLElement) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            createXMLDocument( null, UTF8, dtdURI );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            createXMLDocument( element, null, dtdURI );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            createXMLDocument( element, UTF8, null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            createXMLDocument( null, UTF8, "dtd", dtdURI );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            createXMLDocument( element, null, "dtd", dtdURI );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            createXMLDocument( element, UTF8, null, dtdURI );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            createXMLDocument( element, UTF8, "dtd", null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCreateXMLDocumentWithNullArgument()
}
//  class TestCreateXMLDocument

/*
 *  End of File
 */