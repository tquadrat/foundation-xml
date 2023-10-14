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

package org.tquadrat.foundation.xml.builder.xmlbuilderutils;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.addValidatorChangeListener;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.removeValidatorChangeListener;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils.ValidatorChangeEvent;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils.ValidatorChangeListener;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link XMLBuilderUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestAddValidatorChangeListener.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestAddValidatorChangeListener.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.xmlbuilderutils.TestAddValidatorChangeListener" )
public class TestAddValidatorChangeListener extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link XMLBuilderUtils#addValidatorChangeListener(ValidatorChangeListener)}
     *  and
     *  {@link XMLBuilderUtils#removeValidatorChangeListener(ValidatorChangeListener)}.
     */
    @SuppressWarnings( "AnonymousInnerClassMayBeStatic" )
    @Test
    final void testAddRemoveValidatorChangeListener()
    {
        skipThreadTest();

        final var listener1 = new ValidatorChangeListener()
        {
            /**
             *  {@inheritDoc}
             */
            @SuppressWarnings( "UseOfConcreteClass" )
            @Override
            public final void validatorChanged( final ValidatorChangeEvent event ) { /* Just exists */ }
        };
        assertNotNull( listener1 );

        final var listener2 = new ValidatorChangeListener()
        {
            /**
             *  {@inheritDoc}
             */
            @SuppressWarnings( "UseOfConcreteClass" )
            @Override
            public final void validatorChanged( final ValidatorChangeEvent event ) { /* Just exists */ }
        };
        assertNotNull( listener2 );

        //---* Nothing should happen *-----------------------------------------
        removeValidatorChangeListener( null );
        removeValidatorChangeListener( listener1 );

        addValidatorChangeListener( listener1 );
        addValidatorChangeListener( listener2 );
        addValidatorChangeListener( listener1 );

        removeValidatorChangeListener( listener1 );
        removeValidatorChangeListener( listener2 );
    }   //  testAddRemoveValidatorChangeListener()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#addValidatorChangeListener(ValidatorChangeListener)}.
     */
    @Test
    final void testAddValidatorChangeListenerWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            addValidatorChangeListener( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddValidatorChangeListenerNull()
}
//  class TestAddValidatorChangeListener

/*
 *  End of File
 */