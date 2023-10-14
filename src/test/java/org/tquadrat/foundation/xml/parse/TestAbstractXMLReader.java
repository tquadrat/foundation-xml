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

package org.tquadrat.foundation.xml.parse;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.stream.Collectors.joining;
import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.xml.parse.AbstractXMLReader.NO_ATTRIBUTES;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.helper.XMLTestBase;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *  Test class for
 *  {@link AbstractXMLReader}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestAbstractXMLReader.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.parse.TestAbstractXMLReader" )
public class TestAbstractXMLReader extends XMLTestBase
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  This class implements the sample from the class definition.
     */
    private static final class PropertiesReader extends AbstractXMLReader
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new PropertiesReader instance for the given content
         *  handler.
         *
         *  @param  contentHandler The content handler.
         */
        public PropertiesReader( final ContentHandler contentHandler ) { super( contentHandler ); }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        protected void process( final BufferedReader input ) throws IOException, SAXException
        {
            final var handler = getContentHandler();

            //---* Load the properties *---------------------------------------
            final var properties = new Properties();
            properties.load( input );

            //---* Create the document *---------------------------------------
            handler.startDocument();
            handler.startElement( null, null, "properties", NO_ATTRIBUTES );

            //---* Process the properties *------------------------------------
            AttributesImpl attributes;
            char [] value;

            /*
             * Each property will be treated as a value with the key as its
             * attribute.
             */
            for( final var name : properties.stringPropertyNames() )
            {
                //---* Start the element *-------------------------------------
                attributes = new AttributesImpl();
                attributes.addAttribute( null, null, "name", "ID", name );
                handler.startElement( null, null, "property", attributes );

                //---* The element contents *----------------------------------
                value = properties.getProperty( name ).toCharArray();
                handler.characters( value, 0, value.length );

                //---* End the element *---------------------------------------
                handler.endElement( null, null, "property" );
            }

            //---* Finish the document *---------------------------------------
            handler.endElement( null, null, "properties" );
            handler.endDocument();
        }   //  process()
    }
    //  class PropertiesReader

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The number of properties: {@value}.
     */
    public static final int NUMBER_OF_PROPERTIES = 15;

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The content handler to use.
     */
    private ContentHandler m_ContentHandler;

    /**
     *  The properties "Stream".
     */
    @SuppressWarnings( "StaticVariableMayNotBeInitialized" )
    private static String m_Properties;

    	/*---------*\
    ====** Methods **==========================================================
    	\*---------*/
    /**
     *  Prepares a single test, the call to one of the test methods.
     *
     *  @throws Exception   Failing the preparation.
     */
    @BeforeEach
    final void setUpEach() throws Exception
    {
        //---* Create the mock objects *---------------------------------------
        m_ContentHandler = createMock( ContentHandler.class );

        //---* Prime the content handler mock *--------------------------------
        m_ContentHandler.setDocumentLocator( anyObject() );

        //---* Start the document *--------------------------------------------
        m_ContentHandler.startDocument();
        m_ContentHandler.startElement( isNull(), isNull(), eq( "properties" ), same( NO_ATTRIBUTES ) );

        //---* Parse the elements *--------------------------------------------
        m_ContentHandler.startElement( isNull(), isNull(), eq( "property" ), anyObject() );
        expectLastCall().times( NUMBER_OF_PROPERTIES );
        m_ContentHandler.characters( anyObject(), eq( 0 ), anyInt() );
        expectLastCall().times( NUMBER_OF_PROPERTIES );
        m_ContentHandler.endElement( isNull(), isNull(), eq( "property" ) );
        expectLastCall().times( NUMBER_OF_PROPERTIES );

        //---* Finish the document *-------------------------------------------
        m_ContentHandler.endElement( isNull(), isNull(), eq( "properties" ) );
        m_ContentHandler.endDocument();

        //---* And ... Action! *-----------------------------------------------
        replayAll();
    }   //  setUpEach()

    /**
     *  Performs the setup for all tests in the class.
     *
     *  @throws Exception   Something has gone wrong during the setup.
     */
    @BeforeAll
    static final void setUpAll() throws Exception
    {
        //---* Setup the test data *-------------------------------------------
        m_Properties = IntStream
            .range( 0, NUMBER_OF_PROPERTIES )
            .mapToObj( i -> format( "Key%1$d=Value%1$d\n", i + 1 ) )
            .collect( joining() );
    }   //  setUpAll()

    /**
     *  Cleans up after a single test.
     *
     *  @throws Exception   Problem while cleaning up after a test method was
     *      executed.
     */
    @AfterEach
    public void tearDownEach() throws Exception
    {
        //---* Check the content handler *-------------------------------------
        verify( m_ContentHandler );
    }   //  tearDownEach()

    /**
     *  This method mainly performs the test for the class
     *  {@link AbstractXMLReader}
     *  as far as this is possible for an abstract class.
     *
     *  @throws SAXException    Something went wrong unexpectedly.
     */
    @Test
    final void testParse() throws SAXException
    {
        skipThreadTest();

        //---* Create the input source *---------------------------------------
        final var stringReader = new StringReader( m_Properties );
        final var inputSource = new InputSource( stringReader );

        //---* Create the PropertiesReader object *----------------------------
        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        PropertiesReader xmlReader;
        try
        {
            xmlReader = new PropertiesReader( null );
            assertNotNull( xmlReader );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        xmlReader = new PropertiesReader( m_ContentHandler );
        assertNotNull( xmlReader );

        //---* Parse *---------------------------------------------------------
        try
        {
            xmlReader.parse( inputSource );
        }
        catch( final IOException e )
        {
            fail( "IOException was thrown: " + e.getLocalizedMessage() );
        }
        catch( final SAXException e )
        {
            fail( "SAXException was thrown: " + e.getLocalizedMessage() );
        }
    }   //  testParse()
}
//  class TestAbstractXMLReader

/*
 *  End of File
 */