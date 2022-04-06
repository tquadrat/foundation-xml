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

package org.tquadrat.foundation.xml.builder.spi.childsupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.builder.spi.ChildSupport;
import org.tquadrat.foundation.xml.builder.spi.Element;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link ChildSupport}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestConstructor.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestConstructor.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.childsupport.TestConstructor" )
public class TestConstructor extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link ChildSupport#ChildSupport(Element)},
     *  {@link ChildSupport#ChildSupport(Element,boolean)},
     *  {@link ChildSupport#ChildSupport(Element,java.util.function.Function)},
     *  and
     *  {@link ChildSupport#ChildSupport(Element, boolean, boolean, boolean, java.util.function.Function)}.
     */
    @Test
    final void testConstructor()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        ChildSupport candidate;
        boolean allowChildren, allowText, checkValid;

        candidate = new ChildSupport( element );
        assertNotNull( candidate );
        assertFalse( candidate.allowsChildren() );
        assertFalse( candidate.allowsText() );
        assertFalse( candidate.checksIfValid() );

        checkValid = true;
        candidate = new ChildSupport( element, checkValid );
        assertNotNull( candidate );
        assertTrue( candidate.allowsChildren() );
        assertFalse( candidate.allowsText() );
        assertEquals( checkValid, candidate.checksIfValid() );

        checkValid = false;
        candidate = new ChildSupport( element, checkValid );
        assertNotNull( candidate );
        assertTrue( candidate.allowsChildren() );
        assertFalse( candidate.allowsText() );
        assertEquals( checkValid, candidate.checksIfValid() );

        candidate = new ChildSupport( element, XMLBuilderUtils::escapeXML );
        assertNotNull( candidate );
        assertFalse( candidate.allowsChildren() );
        assertTrue( candidate.allowsText() );
        assertFalse( candidate.checksIfValid() );

        allowChildren = true;
        allowText = true;
        checkValid = true;
        candidate = new ChildSupport( element, checkValid, allowChildren, allowText, XMLBuilderUtils::escapeXML );
        assertNotNull( candidate );
        assertEquals( allowChildren, candidate.allowsChildren() );
        assertEquals( allowText, candidate.allowsText() );
        assertEquals( allowChildren && checkValid, candidate.checksIfValid() );

        allowChildren = false;
        allowText = true;
        checkValid = true;
        candidate = new ChildSupport( element, checkValid, allowChildren, allowText, XMLBuilderUtils::escapeXML );
        assertNotNull( candidate );
        assertEquals( allowChildren, candidate.allowsChildren() );
        assertEquals( allowText, candidate.allowsText() );
        assertEquals( allowChildren && checkValid, candidate.checksIfValid() );

        allowChildren = true;
        allowText = false;
        checkValid = true;
        candidate = new ChildSupport( element, checkValid, allowChildren, allowText, null );
        assertNotNull( candidate );
        assertEquals( allowChildren, candidate.allowsChildren() );
        assertEquals( allowText, candidate.allowsText() );
        assertEquals( allowChildren && checkValid, candidate.checksIfValid() );

        allowChildren = false;
        allowText = false;
        checkValid = true;
        candidate = new ChildSupport( element, checkValid, allowChildren, allowText, null );
        assertNotNull( candidate );
        assertEquals( allowChildren, candidate.allowsChildren() );
        assertEquals( allowText, candidate.allowsText() );
        assertEquals( allowChildren && checkValid, candidate.checksIfValid() );

        allowChildren = true;
        allowText = true;
        checkValid = false;
        candidate = new ChildSupport( element, checkValid, allowChildren, allowText, XMLBuilderUtils::escapeXML );
        assertNotNull( candidate );
        assertEquals( allowChildren, candidate.allowsChildren() );
        assertEquals( allowText, candidate.allowsText() );
        assertEquals( allowChildren && checkValid, candidate.checksIfValid() );

        allowChildren = false;
        allowText = false;
        checkValid = false;
        candidate = new ChildSupport( element, checkValid, allowChildren, allowText, null );
        assertNotNull( candidate );
        assertEquals( allowChildren, candidate.allowsChildren() );
        assertEquals( allowText, candidate.allowsText() );
        assertEquals( allowChildren && checkValid, candidate.checksIfValid() );
    }   //  testConstructor()

    /**
     *  Tests for
     *  {@link ChildSupport#ChildSupport(Element)},
     *  {@link ChildSupport#ChildSupport(Element,boolean)},
     *  {@link ChildSupport#ChildSupport(Element,java.util.function.Function)},
     *  and
     *  {@link ChildSupport#ChildSupport(Element, boolean, boolean, boolean, java.util.function.Function)}.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    @Test
    final void testConstructorWithNullArgument()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        ChildSupport candidate;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new ChildSupport( null );
            assertNotNull( candidate );
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
            candidate = new ChildSupport( null, true );
            assertNotNull( candidate );
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
            candidate = new ChildSupport( null, CharSequence::toString );
            assertNotNull( candidate );
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
            candidate = new ChildSupport( element, null );
            assertNotNull( candidate );
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
            candidate = new ChildSupport( null, true, true, true, CharSequence::toString );
            assertNotNull( candidate );
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
            /*
             * A NullArgumentException will be thrown only when the element
             * allows text - meaning the fourth argument needs to be true for
             * this test.
             */
            candidate = new ChildSupport( element, true, true, true, null );
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
//  class TestConstructor

/*
 *  End of File
 */