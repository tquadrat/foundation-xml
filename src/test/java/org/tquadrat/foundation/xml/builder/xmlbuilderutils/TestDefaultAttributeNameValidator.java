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
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getAttributeNameValidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link XMLBuilderUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestDefaultAttributeNameValidator.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestDefaultAttributeNameValidator.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.xmlbuilderutils.TestDefaultAttributeNameValidator" )
public class TestDefaultAttributeNameValidator extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the default attribute name validator.
     *
     *  @param  result  The result of the test: {@code true} if the given
     *      attribute name is valid, {@code false} otherwise.
     *  @param  attributeName   The attribute name to test.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "DefaultAttributeNameValidator.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testDefaultAttributeNameValidator( final boolean result, final String attributeName )
    {
        skipThreadTest();

        final var validator = getAttributeNameValidator();
        assertEquals( result, validator.test( attributeName ) );
    }   //  testDefaultAttributeNameValidator()
}
//  class TestDefaultAttributeNameValidator

/*
 *  End of File
 */