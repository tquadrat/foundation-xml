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

package org.tquadrat.foundation.xml.builder.spi.namespacesupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.xml.builder.spi.NamespaceSupport;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the method
 *  {@link NamespaceSupport#getNamespaces()}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestGetNamespaces.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestGetNamespaces.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.namespacesupport.TestGetNamespaces" )
public class TestGetNamespaces extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests for the method
     *  {@link NamespaceSupport#getNamespaces()}.
     */
    @SuppressWarnings( "LocalVariableNamingConvention" )
    @DisplayName( "NamespaceSupport.getNamespaces()" )
    @Test
    final void testGetNamespaces()
    {
        skipThreadTest();

        final var owner = mockElement( "owner" );
        replayAll();
        final var candidate = new NamespaceSupport( owner );
        final var identifier = URI.create( "tquadrat.org/foundation" );

        final var n1 = new Namespace( identifier );
        final var n2 = new Namespace( "n2", identifier );
        final var n3 = new Namespace( "n3", identifier );
        final var n4 = new Namespace( "n4", identifier );
        final var n5 = new Namespace( "n5", identifier );
        final var n6 = new Namespace( "n6", identifier );
        final var n7 = new Namespace( "n7", identifier );
        final var n8 = new Namespace( "n8", identifier );

        candidate.setNamespace( n8, n7, n6, n5, n4, n3, n2, n1 );
        final var expected = """
                              xmlns="tquadrat.org/foundation"
                                    xmlns:n2="tquadrat.org/foundation"
                                    xmlns:n3="tquadrat.org/foundation"
                                    xmlns:n4="tquadrat.org/foundation"
                                    xmlns:n5="tquadrat.org/foundation"
                                    xmlns:n6="tquadrat.org/foundation"
                                    xmlns:n7="tquadrat.org/foundation"
                                    xmlns:n8="tquadrat.org/foundation"\
                             """;
        final var actual = candidate.toString( 0, true );
        assertEquals( expected, actual );
    }   //  testGetNamesSpaces()
}
//  class TestGetNamespaces

/*
 *  End of File
 */