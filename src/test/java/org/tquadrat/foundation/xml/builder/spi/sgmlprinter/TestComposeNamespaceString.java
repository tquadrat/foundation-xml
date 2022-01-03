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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.composeNamespaceString;

import java.util.Collection;
import java.util.List;

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
 *  @version $Id: TestComposeNamespaceString.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestComposeNamespaceString.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.sgmlprinter.TestComposeNamespaceString" )
public class TestComposeNamespaceString extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the method
     *  {@link SGMLPrinter#composeNamespaceString(int,boolean,String, Collection)}.
     *
     *  @throws Exception   Something failed unexpectedly.
     */
    @Test
    final void testComposeNamespaceString() throws Exception
    {
        skipThreadTest();

        String actual, expected;
        int indentationLevel;
        boolean prettyPrint;
        Collection<Namespace> namespaces;

        namespaces = emptyList();
        indentationLevel = 0;
        prettyPrint = false;
        expected = EMPTY_STRING;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = EMPTY_STRING;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = EMPTY_STRING;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = EMPTY_STRING;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        namespaces = List.of( new Namespace( "foundation", "tquadrat.org/foundation" ) );
        indentationLevel = 0;
        prettyPrint = false;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );


        namespaces = List.of( new Namespace( "tquadrat.org/global" ), new Namespace( "foundation", "tquadrat.org/foundation" ) );
        indentationLevel = 0;
        prettyPrint = false;
        expected = """
                    xmlns="tquadrat.org/global" xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns="tquadrat.org/global"
                            xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        indentationLevel = 1;
        prettyPrint = false;
        expected = """
                    xmlns="tquadrat.org/global" xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );

        prettyPrint = true;
        expected = """
                    xmlns="tquadrat.org/global"
                                xmlns:foundation="tquadrat.org/foundation"\
                   """;
        actual = composeNamespaceString( indentationLevel, prettyPrint, "element", namespaces );
        assertNotNull( actual );
        assertEquals( expected, actual );
}   //  testComposeNamespaceString()

    /**
     *  Tests for the method
     *  {@link SGMLPrinter#composeNamespaceString(int,boolean,String, Collection)}.
     */
    @Test
    final void testComposeNamespaceStringWithEmptyArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            composeNamespaceString( 0, true, EMPTY_STRING, emptyList() );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testComposeNamespaceStringWithEmptyArgument()

    /**
     *  Tests for the method
     *  {@link SGMLPrinter#composeNamespaceString(int,boolean,String, Collection)}.
     */
    @Test
    final void testComposeNamespaceStringWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            composeNamespaceString( 0, true, null, emptyList() );
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
            composeNamespaceString( 0, true, "Element", null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testComposeNamespaceStringWithNullArgument()
}
//  class TestComposeNamespaceString

/*
 *  End of File
 */