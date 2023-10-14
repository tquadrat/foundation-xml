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

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLElement;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link Text}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestText.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestText.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.internal.TestText" )
public class TestText extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the constructor
     *  {@link Text#Text(CharSequence, Function)}.
     */
    @Test
    final void testConstructor()
    {
        skipThreadTest();

        Text candidate;
        String text;

        final var escape = (Function<CharSequence,String>) CharSequence::toString;

        text = EMPTY_STRING;
        candidate = new Text( text, XMLBuilderUtils::escapeXML );
        assertNotNull( candidate );
        assertEquals( text, candidate.toString( 0, false ) );
        assertEquals( text, candidate.toString() );

        candidate = new Text( text, escape );
        assertNotNull( candidate );
        assertEquals( text, candidate.toString( 0, false ) );
        assertEquals( text, candidate.toString() );

        text = "one line";
        candidate = new Text( text, escape );
        assertNotNull( candidate );
        assertEquals( text, candidate.toString( 0, false ) );
        assertEquals( text, candidate.toString() );

        text = "one line\ntwo lines";
        candidate = new Text( text, escape );
        assertNotNull( candidate );
        assertEquals( text, candidate.toString( 0, false ) );
        assertEquals( text, candidate.toString() );

        assertEquals( "[TEXT]", candidate.getElementName() );
        var parent = candidate.getParent();
        assertNotNull( parent );
        assertFalse( parent.isPresent() );
        candidate.setParent( createXMLElement( "element" ) );
        parent = candidate.getParent();
        assertNotNull( parent );
        assertTrue( parent.isPresent() );
        assertFalse( candidate.isBlock() );
    }   //  testConstructor()

    /**
     *  Tests for the constructor
     *  {@link Text#Text(CharSequence, Function)}.
     */
    @Test
    final void testConstructorWithNullArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" )
        Text candidate = null;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new Text( null, XMLBuilderUtils::escapeXML );
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
            candidate = new Text( EMPTY_CHARSEQUENCE, null );
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
            candidate = new Text( "text", null );
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

    /**
     *  Tests for the method
     *  {@link Text#setParent(org.tquadrat.foundation.xml.builder.spi.Element)}.
     */
    @Test
    final void testSetParentWithNullArgument()
    {
        skipThreadTest();

        final var candidate = new Text( "text", CharSequence::toString );

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate.setParent( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testSetParentWithNullArgument()
}
//  class TestText

/*
 *  End of File
 */