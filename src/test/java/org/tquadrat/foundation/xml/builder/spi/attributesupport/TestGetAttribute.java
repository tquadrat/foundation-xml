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

package org.tquadrat.foundation.xml.builder.spi.attributesupport;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.XMLElement.NO_APPEND;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.spi.AttributeSupport;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link AttributeSupport}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestGetAttribute.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestGetAttribute.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.attributesupport.TestGetAttribute" )
public class TestGetAttribute extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link AttributeSupport#getAttribute(String)}
     */
    @Test
    final void testGetAttribute()
    {
        skipThreadTest();

        final var elementName = "element";
        final var attributeName = "attribute";

        final var element = mockElement( elementName );
        replayAll();

        final var append = NO_APPEND;

        final AttributeSupport candidate;

        candidate = new AttributeSupport( element, false );
        assertFalse( candidate.checksIfValid() );

        assertFalse( candidate.getAttribute( attributeName ).isPresent() );

        candidate.setAttribute( attributeName, "value1", append );
        assertTrue( candidate.getAttribute( attributeName ).isPresent() );
    }   //  testSetAttribute()

    /**
     *  Tests for
     *  {@link AttributeSupport#getAttribute(String)}
     */
    @Test
    final void testGetAttributeWithEmptyArgument()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new AttributeSupport( element );

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate.getAttribute( EMPTY_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testGetAttributeWithEmptyArgument()

    /**
     *  Tests for
     *  {@link AttributeSupport#getAttribute(String)}.
     */
    @Test
    final void testGetAttributeWithNullArgument()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new AttributeSupport( element );

        final Class<? extends Throwable> expectedException = NullArgumentException.class;

        try
        {
            candidate.getAttribute( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testGetAttributeWithNullArgument()
}
//  class TestGetAttribute

/*
 *  End of File
 */