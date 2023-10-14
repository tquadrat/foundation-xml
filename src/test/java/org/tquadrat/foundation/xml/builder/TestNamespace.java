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

package org.tquadrat.foundation.xml.builder;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link Namespace}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestNamespace.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestNamespace.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.TestNamespace" )
public class TestNamespace extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for methods
     *  {@link Namespace#equals(Object)}
     *  and
     *  {@link Namespace#hashCode()}.
     *
     *  @throws Exception   Something unexpected failed.
     */
    @Test
    final void testEquals() throws Exception
    {
        skipThreadTest();

        final var prefix = "prefix";
        final var identifierString = "tquadrat.org/namespace";
        final var identifier1 = new URI( identifierString );
        final var identifier2 = new URI( "ibm.com/java" );

        final Namespace candidate1;
        final Namespace candidate2;
        final Namespace candidate3;
        final Namespace candidate4;

        candidate1 = new Namespace( identifierString );
        assertNotNull( candidate1 );
        candidate2 = new Namespace( identifier1 );
        assertNotNull( candidate2 );
        candidate3 = new Namespace( prefix, identifier1 );
        assertNotNull( candidate3 );
        candidate4 = new Namespace( prefix, identifier2 );
        assertNotNull( candidate4 );

        //noinspection SimplifiableAssertion
        assertFalse( candidate1.equals( null ) );
        //noinspection SimplifiableAssertion
        assertTrue( candidate1.equals( candidate1 ) );

        //noinspection SimplifiableAssertion
        assertTrue( candidate1.equals( candidate2 ) );
        assertEquals( candidate1.hashCode(), candidate2.hashCode() );

        //noinspection SimplifiableAssertion
        assertFalse( candidate1.equals( candidate3 ) );

        //noinspection SimplifiableAssertion
        assertFalse( candidate3.equals( candidate4 ) );

        //noinspection SimplifiableAssertion
        assertFalse( candidate1.equals( new Object() ) );
    }   //  testEquals()

    /**
     *  Tests for the methods in
     *  {@link Namespace}.
     *
     *  @throws Exception   Something unexpected failed.
     */
    @Test
    final void testNamespace() throws Exception
    {
        skipThreadTest();

        final var prefix = "prefix";
        final var identifierString = "tquadrat.org/namespace";
        final var identifier = new URI( identifierString );

        Namespace candidate1;
        Namespace candidate2;

        candidate1 = new Namespace( identifierString );
        assertNotNull( candidate1 );
        candidate2 = new Namespace( identifier );
        assertNotNull( candidate2 );

        assertEquals( candidate1, candidate2 );

        assertNotNull( candidate1.getPrefix() );
        assertNotNull( candidate2.getPrefix() );
        assertFalse( candidate1.getPrefix().isPresent() );
        assertFalse( candidate2.getPrefix().isPresent() );

        assertNotNull( candidate1.getIdentifier() );
        assertNotNull( candidate2.getIdentifier() );
        assertEquals( identifier, candidate1.getIdentifier() );
        assertEquals( identifier, candidate2.getIdentifier() );

        assertEquals( candidate1.toString(), candidate2.toString() );

        assertEquals( "xmlns=\"tquadrat.org/namespace\"", candidate1.toString() );
        assertEquals( "xmlns=\"tquadrat.org/namespace\"", candidate2.toString() );


        candidate1 = new Namespace( prefix, identifierString );
        assertNotNull( candidate1 );
        candidate2 = new Namespace( prefix, identifier );
        assertNotNull( candidate2 );

        assertEquals( candidate1, candidate2 );

        assertNotNull( candidate1.getPrefix() );
        assertNotNull( candidate2.getPrefix() );
        assertTrue( candidate1.getPrefix().isPresent() );
        assertTrue( candidate2.getPrefix().isPresent() );
        assertEquals( prefix, candidate1.getPrefix().get() );
        assertEquals( prefix, candidate2.getPrefix().get() );

        assertNotNull( candidate1.getIdentifier() );
        assertNotNull( candidate2.getIdentifier() );
        assertEquals( identifier, candidate1.getIdentifier() );
        assertEquals( identifier, candidate2.getIdentifier() );

        assertEquals( candidate1.toString(), candidate2.toString() );

        assertEquals( "xmlns:prefix=\"tquadrat.org/namespace\"", candidate1.toString() );
        assertEquals( "xmlns:prefix=\"tquadrat.org/namespace\"", candidate2.toString() );
    }   //  testNamespace()

    /**
     *  Tests for
     *  {@link Namespace#Namespace(String)},
     *  {@link Namespace#Namespace(String,String)},
     *  and
     *  {@link Namespace#Namespace(String,java.net.URI)}.
     */
    @Test
    final void testNamespaceWithEmptyArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( EMPTY_STRING );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( EMPTY_STRING, new URI( "tquadrat.org/namespace" ) );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( EMPTY_STRING, "tquadrat.org/namespace" );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( "prefix", EMPTY_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testNamespaceWithEmptyArgument()

    /**
     *  Tests for
     *  {@link Namespace#Namespace(String,URI)}
     *  and
     *  {@link Namespace#Namespace(String,String)}.
     *
     *  @param  prefix  Some invalid prefixes.
     */
    @ParameterizedTest
    @ValueSource( strings =
        {
            ":", ":prefix", "1243", "1prefix", "Anton:Berta", "\n",
            "This has a blank", ".", ".prefix", " ", " prefix", "\t"
        })
    final void testNamespaceWithInvalidPrefixArgument( final String prefix )
    {
        skipThreadTest();

        final var identifier = "tquadrat.org/foundation";
        final Class<? extends Throwable> expectedException = InvalidXMLNameException.class;
        try
        {
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( prefix, identifier );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( prefix, new URI( identifier ) );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testNamespaceWithInvalidPrefixArgument()

    /**
     *  Tests for
     *  {@link Namespace#Namespace(String)}
     *  and
     *  {@link Namespace#Namespace(String,String)}.
     */
    @Test
    final void testNamespaceWithInvalidURIArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = URISyntaxException.class;
        try
        {
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( ":,/" );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( "prefix", ":,/" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testNamespaceWithInvalidURIArgument()

    /**
     *  Tests for
     *  {@link Namespace#Namespace(String)},
     *  {@link Namespace#Namespace(URI)},
     *  {@link Namespace#Namespace(String,String)},
     *  and
     *  {@link Namespace#Namespace(String,java.net.URI)}.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    @DisplayName( "Namespace() with null argument" )
    @Test
    final void testNamespaceWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( (String) null );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( (URI) null );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( null, new URI( "tquadrat.org/namespace" ) );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( null, "tquadrat.org/namespace" );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( "prefix", (String) null );
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
            @SuppressWarnings( "unused" )
            final var candidate = new Namespace( "prefix", (URI) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testNamespaceWithNullArgument()
}
//  class TestNamespace

/*
 *  End of File
 */