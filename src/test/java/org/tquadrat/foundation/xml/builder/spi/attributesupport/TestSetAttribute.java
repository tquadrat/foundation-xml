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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.XMLATTRIBUTE_Id;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.XMLElement.NO_APPEND;
import static org.tquadrat.foundation.xml.builder.spi.AttributeSupport.ID_ALWAYS_FIRST_COMPARATOR;

import java.util.Optional;

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
 *  @version $Id: TestSetAttribute.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestSetAttribute.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.attributesupport.TestSetAttribute" )
public class TestSetAttribute extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link AttributeSupport#setAttribute(String,CharSequence,Optional)}.
     */
    @Test
    final void testSetAttribute()
    {
        skipThreadTest();

        final var elementName = "element";
        final var validAttributeName = "validAttribute";
        final var invalidAttributeName = "invalidAttribute";

        final var element = mockElement( elementName );
        replayAll();

        AttributeSupport candidate;
        Optional<? extends CharSequence> append;

        append = NO_APPEND;
        candidate = new AttributeSupport( element, false );
        candidate.registerAttributes( XMLATTRIBUTE_Id, validAttributeName );
        assertFalse( candidate.checksIfValid() );

        candidate.setAttribute( validAttributeName, "value1", append );
        candidate.setAttribute( invalidAttributeName, "value2", append );

        candidate = new AttributeSupport( element, true );
        candidate.registerAttributes( XMLATTRIBUTE_Id, validAttributeName );
        assertTrue( candidate.checksIfValid() );

        candidate.setAttribute( validAttributeName, "value1", append );
        final Class<? extends Throwable> expectedException = IllegalArgumentException.class;
        try
        {
            candidate.setAttribute( invalidAttributeName, "value2", append );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        String actual, expected;

        append = Optional.of( "," );

        candidate = new AttributeSupport( element, true );
        candidate.registerAttributes( XMLATTRIBUTE_Id, validAttributeName );
        assertTrue( candidate.checksIfValid() );
        candidate.setSortOrder( ID_ALWAYS_FIRST_COMPARATOR );
        candidate.setAttribute( XMLATTRIBUTE_Id, "id", Optional.empty() );

        candidate.setAttribute( validAttributeName, EMPTY_CHARSEQUENCE, append );
        expected = " xml:id='id' validAttribute=''";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );

        candidate.setAttribute( validAttributeName, "value1", append );
        expected = " xml:id='id' validAttribute='value1'";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );

        candidate.setAttribute( validAttributeName, "value2", append );
        expected = " xml:id='id' validAttribute='value1,value2'";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );

        candidate = new AttributeSupport( element, false );
        candidate.registerAttributes( "zeppelin", "adler", "airbus" );
        candidate.setAttribute( "zeppelin", "value", append );
        candidate.setAttribute( "adler", "value", append );
        candidate.setAttribute( "airbus", "value", append );

        candidate = new AttributeSupport( element, true );
        candidate.registerSequence( validAttributeName, XMLATTRIBUTE_Id );
        assertTrue( candidate.checksIfValid() );
        candidate.registerAttributes( validAttributeName );
        candidate.setAttribute( XMLATTRIBUTE_Id, "id", Optional.empty() );
        candidate.setAttribute( validAttributeName, "value1", append );
        expected = " validAttribute='value1' xml:id='id'";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );

        candidate.setAttribute( validAttributeName, "value2", append );
        expected = " validAttribute='value1,value2' xml:id='id'";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );
    }   //  testSetAttribute()

    /**
     *  Tests for
     *  {@link AttributeSupport#setAttribute(String,CharSequence,Optional)}.
     */
    @Test
    final void testSetAttributeWithEmptyArgument()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new AttributeSupport( element );
        final CharSequence value = "value";
        final var append = NO_APPEND;

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;

        final var attributeName = EMPTY_STRING;
        try
        {
            candidate.setAttribute( attributeName, value, append );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testSetAttributeWithEmptyArgument()

    /**
     *  Tests for
     *  {@link AttributeSupport#setAttribute(String,CharSequence,Optional)}.
     */
    @Test
    final void testSetAttributeWithNullArgument()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new AttributeSupport( element );
        final CharSequence value = "value";

        String attributeName;
        Optional<? extends CharSequence> append;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;

        attributeName = null;
        append = NO_APPEND;
        try
        {
            candidate.setAttribute( attributeName, value, append );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        attributeName = "attribute";
        //noinspection OptionalAssignedToNull
        append = null;
        try
        {
            candidate.setAttribute( attributeName, value, append );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testSetAttributeWithNullArgument()
}
//  class TestSetAttribute

/*
 *  End of File
 */