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

package org.tquadrat.foundation.xml.builder.spi.sgmlprinter;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.composeAttributesString;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.xml.builder.spi.SGMLPrinter;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link SGMLPrinter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestComposeAttributesString.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestComposeAttributesString.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.sgmlprinter.TestComposeAttributesString" )
public class TestComposeAttributesString extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the method
     *  {@link SGMLPrinter#composeAttributesString(int,boolean,String,Map,Collection)}.
     *
     *  @throws Exception   Something failed unexpectedly.
     */
    @Test
    final void testComposeAttributesString() throws Exception
    {
        skipThreadTest();

        String actual, expected;
        int indentationLevel;
        boolean prettyPrint;
        Map<String,String> attributes;
        Collection<Namespace> namespaces;

        namespaces = emptyList();
        attributes = emptyMap();
        indentationLevel = 0;
        prettyPrint = false;
        expected = EMPTY_STRING;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = EMPTY_STRING;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = EMPTY_STRING;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = EMPTY_STRING;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        namespaces = emptyList();
        attributes = Map.of( "name1", "value1" );
        indentationLevel = 0;
        prettyPrint = false;
        expected = " name1='value1'";
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = " name1='value1'";
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = " name1='value1'";
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = " name1='value1'";
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        namespaces = List.of( new Namespace( "foundation", "tquadrat.org/foundation" ) );
        attributes = emptyMap();
        indentationLevel = 0;
        prettyPrint = false;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        namespaces = emptyList();
        attributes = new TreeMap<>( Map.of( "name1", "value1", "name2", "value2" ) );
        indentationLevel = 0;
        prettyPrint = false;
        expected = " name1='value1' name2='value2'";
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    name1='value1'
                            name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = " name1='value1' name2='value2'";
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    name1='value1'
                                name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );


        namespaces = List.of( new Namespace( "tquadrat.org/global" ), new Namespace( "foundation", "tquadrat.org/foundation" ) );
        attributes = emptyMap();
        indentationLevel = 0;
        prettyPrint = false;
        expected = """
                    xmlns="tquadrat.org/global" xmlns:foundation="tquadrat.org/foundation"\
                   """;

        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns="tquadrat.org/global"
                            xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = """
                    xmlns="tquadrat.org/global" xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns="tquadrat.org/global"
                                xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        namespaces = List.of( new Namespace( "foundation", "tquadrat.org/foundation" ) );
        attributes = Map.of( "name1", "value1" );
        indentationLevel = 0;
        prettyPrint = false;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation" name1='value1'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"
                            name1='value1'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = """ 
                    xmlns:foundation="tquadrat.org/foundation" name1='value1'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """ 
                    xmlns:foundation="tquadrat.org/foundation"
                                name1='value1'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        namespaces = List.of( new Namespace( "foundation", "tquadrat.org/foundation" ) );
        attributes = new TreeMap<>( Map.of( "name1", "value1", "name2", "value2" ) );
        indentationLevel = 0;
        prettyPrint = false;
        expected = """ 
                    xmlns:foundation="tquadrat.org/foundation" name1='value1' name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"
                            name1='value1'
                            name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = """ 
                    xmlns:foundation="tquadrat.org/foundation" name1='value1' name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"
                                name1='value1'
                                name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        namespaces = List.of( new Namespace( "tquadrat.org/global" ), new Namespace( "foundation", "tquadrat.org/foundation" ) );
        attributes = Map.of( "name1", "value1" );
        indentationLevel = 0;
        prettyPrint = false;
        expected = """ 
                    xmlns="tquadrat.org/global" xmlns:foundation="tquadrat.org/foundation" name1='value1'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """ 
                    xmlns="tquadrat.org/global"
                            xmlns:foundation="tquadrat.org/foundation"
                            name1='value1'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = """ 
                    xmlns="tquadrat.org/global" xmlns:foundation="tquadrat.org/foundation" name1='value1'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """ 
                    xmlns="tquadrat.org/global"
                                xmlns:foundation="tquadrat.org/foundation"
                                name1='value1'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );


        namespaces = List.of( new Namespace( "tquadrat.org/global" ), new Namespace( "foundation", "tquadrat.org/foundation" ) );
        attributes = new TreeMap<>( Map.of( "name1", "value1", "name2", "value2" ) );
        indentationLevel = 0;
        prettyPrint = false;
        expected = """
                    xmlns="tquadrat.org/global" xmlns:foundation="tquadrat.org/foundation" name1='value1' name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """ 
                    xmlns="tquadrat.org/global"
                            xmlns:foundation="tquadrat.org/foundation"
                            name1='value1'
                            name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = """ 
                    xmlns="tquadrat.org/global" xmlns:foundation="tquadrat.org/foundation" name1='value1' name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """ 
                    xmlns="tquadrat.org/global"
                                xmlns:foundation="tquadrat.org/foundation"
                                name1='value1'
                                name2='value2'\
                   """;
        actual = composeAttributesString( indentationLevel, prettyPrint, "element", attributes, namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );
}   //  testComposeAttributesString()

    /**
     *  Tests for the method
     *  {@link SGMLPrinter#composeAttributesString(int,boolean,String, Map, Collection)}.
     */
    @Test
    final void testComposeAttributesStringWithEmptyArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            composeAttributesString( 0, true, EMPTY_STRING, emptyMap(), emptyList() );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testComposeAttributesStringWithEmptyArgument()

    /**
     *  Tests for the method
     *  {@link SGMLPrinter#composeAttributesString(int,boolean,String, Map, Collection)}.
     */
    @DisplayName( "SGMLPrinter.composeAttributesString() with null argument" )
    @Test
    final void testComposeAttributesStringWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            composeAttributesString( 0, true, null, emptyMap(), emptyList() );
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
            composeAttributesString( 0, true, "Element", null, emptyList() );
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
            composeAttributesString( 0, true, "Element", emptyMap(), null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testComposeAttributesStringWithNullArgument()
}
//  class TestComposeAttributesString

/*
 *  End of File
 */