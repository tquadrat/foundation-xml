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

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.spi.AttributeSupport;
import org.tquadrat.foundation.xml.builder.spi.Element;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link AttributeSupport}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestConstructor.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestConstructor.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.attributesupport.TestConstructor" )
public class TestConstructor extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link AttributeSupport#AttributeSupport(Element)}
     *  and
     *  {@link AttributeSupport#AttributeSupport(Element,boolean)}.
     */
    @Test
    final void testConstructor()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        AttributeSupport candidate;
        boolean checkValid;

        candidate = new AttributeSupport( element );
        assertNotNull( candidate );
        assertTrue( candidate.checksIfValid() );

        checkValid = true;
        candidate = new AttributeSupport( element, checkValid );
        assertNotNull( candidate );
        assertEquals( checkValid, candidate.checksIfValid() );

        checkValid = false;
        candidate = new AttributeSupport( element, checkValid );
        assertNotNull( candidate );
        assertEquals( checkValid, candidate.checksIfValid() );
    }   //  testConstructor()

    /**
     *  Tests for
     *  {@link AttributeSupport#AttributeSupport(Element)}
     *  and
     *  {@link AttributeSupport#AttributeSupport(Element,boolean)}.
     */
    @Test
    final void testConstructorWithNullArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" )
        AttributeSupport candidate;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new AttributeSupport( null );
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
            candidate = new AttributeSupport( null, true );
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