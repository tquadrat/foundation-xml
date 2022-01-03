/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
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

package com.howtodoinjava.demo.stax;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The implementation for an employee's data set for the XML tests.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Employee.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: Employee.java 820 2020-12-29 20:34:22Z tquadrat $" )
public class Employee implements Serializable
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code Employee} objects.
     */
    public static final Employee [] EMPTY_Employee_ARRAY = new Employee [0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The employee id.
     */
    private int m_Id;

    /**
     *  The name.
     */
    private String m_Name;

    /**
     *  The title.
     */
    private String m_Title;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Employee} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public Employee()
    {
        //  TODO Implement the constructor!
    }   //  Employee()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( {"NonFinalFieldReferenceInEquals", "AccessingNonPublicFieldOfAnotherObject"} )
    @Override
    public final boolean equals( final Object obj )
    {
        var retValue = this == obj;
        if( !retValue && (obj instanceof Employee other) )
        {
            retValue = m_Id == other.m_Id
                && m_Name.equals( other.m_Name )
                && m_Title.equals( other.m_Title );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "NonFinalFieldReferencedInHashCode" )
    @Override
    public final int hashCode() { return Objects.hash( m_Id, m_Name, m_Title ); }

    /**
     *  Sets the employee id.
     *
     *  @param  id  The id
     */
    public final void setId( final int id ) { m_Id = id; }

    /**
     *  Sets the name of the employee.
     *
     *  @param  name    The name.
     */
    public final void setName( final String name ) { m_Name = name; }

    /**
     *  Sets the employee's title.
     *
     *  @param  title   The title.
     */
    public final void setTitle( final String title ) { m_Title = title; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        return "Employee [m_Id=" + m_Id + ", m_Name=" + m_Name + ", m_Title=" + m_Title + "]";
    }
}
//  class Employee

/*
 *  End of File
 */