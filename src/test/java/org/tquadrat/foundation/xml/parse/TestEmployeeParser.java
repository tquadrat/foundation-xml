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

package org.tquadrat.foundation.xml.parse;

import static java.lang.System.err;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.helper.XMLTestBase;
import org.xml.sax.SAXException;
import com.howtodoinjava.demo.stax.Employee;
import com.howtodoinjava.demo.stax.TestReadXMLStaxIteratorExample;

/**
 *  Some tests for an implementation of
 *  {@link org.tquadrat.foundation.xml.parse.spi.StAXParserBase}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestEmployeeParser.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestEmployeeParser.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.parse.TestEmployeeParser" )
public class TestEmployeeParser extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The program entry point.
     *
     *  @param  args    The command line arguments.
     */
    public static void main( final String... args )
    {
        try
        {
            //---* The input file *--------------------------------------------
            final var file = TestReadXMLStaxIteratorExample.getFile();

            //---* Parse the file *--------------------------------------------
            final var result = process( file );

            //---* Print the result *------------------------------------------
            out.println( result );
        }
        catch( final Throwable t )
        {
            //---* Handle any previously unhandled exceptions *----------------
            t.printStackTrace( err );
        }
    }   //  main()

    /**
     *  Parses the given file.
     *
     *  @param  file    The file to parse.
     *  @return The parse result.
     *  @throws IOException Problems reading the file.
     *  @throws FileNotFoundException   The given file does not exist.
     *  @throws SAXException  Cannot parse the given file.
     */
    public static final String process( final File file ) throws FileNotFoundException, IOException, SAXException
    {
        //---* All read employees objects will be added to this list *---------
        final List<Employee> employeeList = new ArrayList<>();

        //---* Instance of the class which helps on reading tags *-------------
        final var factory = XMLInputFactory.newInstance();

        //---* Create the parser *---------------------------------------------
        final var parser = new EmployeeParser( employeeList );

        //---* Parse *---------------------------------------------------------
        try( final var fileReader = new FileReader( file ) )
        {
            final var eventReader = factory.createXMLEventReader( fileReader );
            parser.parse( eventReader );
        }
        catch( final XMLStreamException e )
        {
            throw new SAXException( e );
        }

        //---* Verify read data *----------------------------------------------
        final var retValue = employeeList.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  process()

    /**
     *  Verifies the expected output.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    @Disabled
    final void testProcess() throws Exception
    {
        skipThreadTest();

        final var file = TestReadXMLStaxIteratorExample.getFile();
        final var expected = TestReadXMLStaxIteratorExample.process( file );
        assertEquals( expected, process( file ) );
    }   //  testProcess()
}
//  class TestEmployeeParser

/*
 *  End of File
 */