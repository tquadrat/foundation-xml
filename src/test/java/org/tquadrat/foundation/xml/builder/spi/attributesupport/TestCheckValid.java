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

package org.tquadrat.foundation.xml.builder.spi.attributesupport;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;

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
 *  @version $Id: TestCheckValid.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestCheckValid.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.testattributesupport.CheckValid" )
public class TestCheckValid extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link AttributeSupport#checkValid(String)}.
     */
    @Test
    final void testCheckValid()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        var candidate = new AttributeSupport( element, false );
        assertFalse( candidate.checksIfValid() );
        assertTrue( candidate.checkValid( "attribute" ) );

        candidate = new AttributeSupport( element, true );
        assertTrue( candidate.checksIfValid() );
        candidate.registerAttributes();
        assertFalse( candidate.checkValid( "attribute" ) );
        assertFalse( candidate.checkValid( "otherAttribute" ) );

        candidate.registerAttributes( "attribute" );
        assertTrue( candidate.checkValid( "attribute" ) );
        assertFalse( candidate.checkValid( "otherAttribute" ) );
    }   //  testCheckValid()

    /**
     *  Tests for
     *  {@link AttributeSupport#checkValid(String)}.
     */
    @Test
    final void testCheckValidWithEmptyArgument()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new AttributeSupport( element, false );

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate.checkValid( EMPTY_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCheckValidWithEmptyArgument()

    /**
     *  Tests for
     *  {@link AttributeSupport#checkValid(String)}.
     */
    @Test
    final void testCheckValidWithNullArgument()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new AttributeSupport( element, false );

        final Class<? extends Throwable> expectedException = NullArgumentException.class;

        try
        {
            candidate.checkValid( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCheckValidWithNullArgument()
}
//  class TestCheckValid

/*
 *  End of File
 */