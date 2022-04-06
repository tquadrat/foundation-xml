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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.addValidatorChangeListener;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getAttributeNameValidator;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getElementNameValidator;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getNMTokenValidator;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getPrefixValidator;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.removeValidatorChangeListener;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.restoreDefaultValidators;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.setAttributeNameValidator;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.setElementNameValidator;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.setNMTokenValidator;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.setPrefixValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils.Validator;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils.ValidatorChangeEvent;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils.ValidatorChangeListener;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link XMLBuilderUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestSetValidators.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestSetValidators.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.xmlbuilderutils.TestSetValidators" )
public class TestSetValidators extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
   /**
     *  Tests for
     *  {@link XMLBuilderUtils#setAttributeNameValidator(Predicate)},
     *  {@link XMLBuilderUtils#setElementNameValidator(Predicate)},
     *  {@link XMLBuilderUtils#setNMTokenValidator(Predicate)},
     *  and
     *  {@link XMLBuilderUtils#setPrefixValidator(Predicate)}.
     */
    @Test
    final void testSetValidators1()
    {
        skipThreadTest();

        final var value = "value"; // Is always valid ... hope so ...
        final var failAlways = (Predicate<CharSequence>) s -> false;

        assertTrue( getAttributeNameValidator().test( value ) );
        setAttributeNameValidator( failAlways );
        assertFalse( getAttributeNameValidator().test( value ) );
        assertSame( Validator.VALIDATOR_AttributeName.getCurrent(), getAttributeNameValidator() );

        assertTrue( getElementNameValidator().test( value ) );
        setElementNameValidator( failAlways );
        assertFalse( getElementNameValidator().test( value ) );
        assertSame( Validator.VALIDATOR_ElementName.getCurrent(), getElementNameValidator() );

        assertTrue( getNMTokenValidator().test( value ) );
        setNMTokenValidator( failAlways );
        assertFalse( getNMTokenValidator().test( value ) );
        assertSame( Validator.VALIDATOR_NMToken.getCurrent(), getNMTokenValidator() );

        assertTrue( getPrefixValidator().test( value ) );
        setPrefixValidator( failAlways );
        assertFalse( getPrefixValidator().test( value ) );
        assertSame( Validator.VALIDATOR_Prefix.getCurrent(), getPrefixValidator() );
    }   //  testSetValidators1()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#setAttributeNameValidator(Predicate)},
     *  {@link XMLBuilderUtils#setElementNameValidator(Predicate)},
     *  {@link XMLBuilderUtils#setNMTokenValidator(Predicate)},
     *  and
     *  {@link XMLBuilderUtils#setPrefixValidator(Predicate)},
     *  as well as for
     *  {@link XMLBuilderUtils#addValidatorChangeListener(ValidatorChangeListener)}
     *  and
     *  {@link XMLBuilderUtils#removeValidatorChangeListener(ValidatorChangeListener)}.
     */
    @Test
    final void testSetValidators2()
    {
        skipThreadTest();

        //---* The events queue *----------------------------------------------
        final List<ValidatorChangeEvent> events = new ArrayList<>();

        //---* The listener *--------------------------------------------------
        final var listener = (ValidatorChangeListener) events::add;

        //---* Add the listener *----------------------------------------------
        addValidatorChangeListener( listener );

        //---* The new validator *---------------------------------------------
        final var failAlways = (Predicate<CharSequence>) s -> false;

        final var value = "value"; // Is always valid ... hope so ...

        ValidatorChangeEvent event;

        assertTrue( getAttributeNameValidator().test( value ) );
        setAttributeNameValidator( failAlways );
        assertFalse( getAttributeNameValidator().test( value ) );
        assertFalse( events.isEmpty() );
        assertEquals( 1, events.size() );
        event = events.get( 0 );
        assertNotNull( event );
        assertEquals( Validator.VALIDATOR_AttributeName, event.getSource() );
        assertSame( Validator.VALIDATOR_AttributeName, event.getValidator() );
        assertSame( Validator.VALIDATOR_AttributeName.getCurrent(), event.getNewValidator() );
        assertNotNull( event.getOldValidator() );
        events.clear();

        assertTrue( getElementNameValidator().test( value ) );
        setElementNameValidator( failAlways );
        assertFalse( getElementNameValidator().test( value ) );
        assertFalse( events.isEmpty() );
        assertEquals( 1, events.size() );
        event = events.get( 0 );
        assertNotNull( event );
        assertEquals( Validator.VALIDATOR_ElementName, event.getSource() );
        assertSame( Validator.VALIDATOR_ElementName, event.getValidator() );
        assertSame( Validator.VALIDATOR_ElementName.getCurrent(), event.getNewValidator() );
        assertNotNull( event.getOldValidator() );
        events.clear();

        assertTrue( getNMTokenValidator().test( value ) );
        setNMTokenValidator( failAlways );
        assertFalse( getNMTokenValidator().test( value ) );
        assertFalse( events.isEmpty() );
        assertEquals( 1, events.size() );
        event = events.get( 0 );
        assertNotNull( event );
        assertEquals( Validator.VALIDATOR_NMToken, event.getSource() );
        assertSame( Validator.VALIDATOR_NMToken, event.getValidator() );
        assertSame( Validator.VALIDATOR_NMToken.getCurrent(), event.getNewValidator() );
        assertNotNull( event.getOldValidator() );
        events.clear();

        assertTrue( getPrefixValidator().test( value ) );
        setPrefixValidator( failAlways );
        assertFalse( getAttributeNameValidator().test( value ) );
        assertFalse( events.isEmpty() );
        assertEquals( 1, events.size() );
        event = events.get( 0 );
        assertNotNull( event );
        assertEquals( Validator.VALIDATOR_Prefix, event.getSource() );
        assertSame( Validator.VALIDATOR_Prefix, event.getValidator() );
        assertSame( Validator.VALIDATOR_Prefix.getCurrent(), event.getNewValidator() );
        assertNotNull( event.getOldValidator() );
        events.clear();

        restoreDefaultValidators();
        assertFalse( events.isEmpty() );
        assertEquals( 4, events.size() );
        events.clear();

        removeValidatorChangeListener( listener );

        assertTrue( getAttributeNameValidator().test( value ) );
        setAttributeNameValidator( failAlways );
        assertFalse( getAttributeNameValidator().test( value ) );
        assertTrue( events.isEmpty() );

        assertTrue( getElementNameValidator().test( value ) );
        setElementNameValidator( failAlways );
        assertFalse( getElementNameValidator().test( value ) );
        assertTrue( events.isEmpty() );

        assertTrue( getNMTokenValidator().test( value ) );
        setNMTokenValidator( failAlways );
        assertFalse( getNMTokenValidator().test( value ) );
        assertTrue( events.isEmpty() );

        assertTrue( getPrefixValidator().test( value ) );
        setPrefixValidator( failAlways );
        assertFalse( getAttributeNameValidator().test( value ) );
        assertTrue( events.isEmpty() );
    }   //  testSetValidators2()
}
//  class TestSetValidators

/*
 *  End of File
 */