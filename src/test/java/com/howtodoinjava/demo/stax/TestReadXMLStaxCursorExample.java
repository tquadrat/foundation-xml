package com.howtodoinjava.demo.stax;

import static java.lang.System.err;
import static java.lang.System.out;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

/**
 *  This sample was taken from the page
 *  <a href="https://howtodoinjava.com/xml/read-xml-stax-parser-cursor-iterator/">howtodoinjava.com</a>
 *  and modified in some aspects; now it serves as a reference implementation
 *  for our own implementations.<br>
 *  <br>The expected output is:<pre><code>[Employee [id=101, name=Lokesh Gupta, title=Author], Employee [id=102, name=Brian Lara, title=Cricketer]]</code></pre>
 *  for the original XML file.
 *
 *  @extauthor Lokesh Gupta
 *  @modified   Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestReadXMLStaxCursorExample.java 820 2020-12-29 20:34:22Z tquadrat $
 */
public class TestReadXMLStaxCursorExample
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
     *  @throws XMLStreamException  Cannot parse the given file.
     */
    public static final String process( final File file ) throws FileNotFoundException, IOException, XMLStreamException
    {
        //---* All read employees objects will be added to this list *---------
        final Collection<Employee> employeeList = new ArrayList<>();

        //---* Instance of the class which helps on reading tags *-------------
        final var factory = XMLInputFactory.newInstance();

        try( final var fileReader = new FileReader( file ) )
        {
            final var streamReader = factory.createXMLStreamReader( fileReader );

            /*
             * The reference for the Employee object. It will get all the
             * data using setter methods. And at last, it will be stored in
             * above 'employeeList'.
             */
            Employee employee = null;

            while( streamReader.hasNext() )
            {
                //---* Move to next event *------------------------------------
                streamReader.next();

                //---* Check if its 'START_ELEMENT' *--------------------------
                if( streamReader.getEventType() == START_ELEMENT )
                {
                    //---* employee tag - opened
                    if( "employee".equalsIgnoreCase( streamReader.getLocalName() ) )
                    {

                        //---* Create a new employee object as tag is open *---
                        employee = new Employee();

                        //---* Read attributes within employee tag *-----------
                        if( streamReader.getAttributeCount() > 0 )
                        {
                            final var id = streamReader.getAttributeValue( null, "id" );
                            employee.setId( Integer.valueOf( id ) );
                        }
                    }

                    //---* Read name data *------------------------------------
                    if( "name".equalsIgnoreCase( streamReader.getLocalName() ) )
                    {
                        employee.setName( streamReader.getElementText() );
                    }

                    //---* Read title data *-----------------------------------
                    if( "title".equalsIgnoreCase( streamReader.getLocalName() ) )
                    {
                        employee.setTitle( streamReader.getElementText() );
                    }
                }

                /*
                 * If the employee tag is closed then add the employee object
                 * to the list.
                 */
                if( streamReader.getEventType() == XMLStreamConstants.END_ELEMENT )
                {
                    if( "employee".equalsIgnoreCase( streamReader.getLocalName() ) )
                    {
                        employeeList.add( employee );
                        employee = null;
                    }
                }
            }
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
    @SuppressWarnings( "static-method" )
    @Test
    final void testProcess() throws Exception
    {
        final var file = TestReadXMLStaxIteratorExample.getFile();
        final var expected = TestReadXMLStaxIteratorExample.process( file );
        assertEquals( expected, process( file ) );
    }   //  testProcess()
}
//  class TestReadXMLStaxCursorExample

/*
 *  End of File
 */