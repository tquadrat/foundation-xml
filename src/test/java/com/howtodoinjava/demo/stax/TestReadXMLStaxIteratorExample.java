package com.howtodoinjava.demo.stax;

import static java.lang.System.err;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
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
 *  @author Lokesh Gupta
 *  @modified   Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestReadXMLStaxIteratorExample.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
public class TestReadXMLStaxIteratorExample
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Retrieves the input file.
     *
     *  @return The input file.
     *  @throws IOException Cannot canonicalize the file name.
     */
    public static final File getFile() throws IOException
    {
        final var retValue = new File( "src/test/resources/employees.xml" ).getCanonicalFile().getAbsoluteFile();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getFile()

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
            final var file = getFile();

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
    @SuppressWarnings( "incomplete-switch" )
    public static final String process( final File file ) throws FileNotFoundException, IOException, XMLStreamException
    {
        //---* All read employees objects will be added to this list *---------
        final Collection<Employee> employeeList = new ArrayList<>();

        //---* Instance of the class which helps on reading tags *-------------
        final var factory = XMLInputFactory.newInstance();

        //---* Initialising the handler to access the tags in the XML file *---
        try( final var fileReader = new FileReader( file, UTF8 ) )
        {
            final var eventReader = factory.createXMLEventReader( fileReader );

            /*
             * The reference for the Employee object. It will get all the
             * data using setter methods. And at last, it will be stored in
             * above 'employeeList'.
             */
            Employee employee = null;

            //---* Checking the availability of the next tag *-----------------
            while( eventReader.hasNext() )
            {
                final var xmlEvent = eventReader.nextEvent();

                if( xmlEvent.isStartElement() )
                {
                    final var startElement = xmlEvent.asStartElement();

                    /*
                     * As soon as the employee tag is opened, create new an
                     * Employee object.
                     */
                    if( "employee".equalsIgnoreCase( startElement.getName().getLocalPart() ) )
                    {
                        employee = new Employee();
                    }

                    //---* Read all attributes when start tag is being read *--
                    final var iterator = startElement.getAttributes();

                    while( iterator.hasNext() )
                    {
                        final var attribute = iterator.next();
                        final var name = attribute.getName();
                        if( "id".equalsIgnoreCase( name.getLocalPart() ) )
                        {
                            employee.setId( Integer.valueOf( attribute.getValue() ) );
                        }
                    }

                    /*
                     * Now every time when content tags are found, move the
                     * iterator and read data.
                     */
                    //noinspection SwitchStatementWithoutDefaultBranch
                    switch( startElement.getName().getLocalPart() )
                    {
                        case "name" ->
                        {
                            final var nameDataEvent = (Characters) eventReader.nextEvent();
                            employee.setName( nameDataEvent.getData() );
                        }
                        case "title" ->
                        {
                            final var titleDataEvent = (Characters) eventReader.nextEvent();
                            employee.setTitle( titleDataEvent.getData() );
                        }
                    }
                }

                if( xmlEvent.isEndElement() )
                {
                    final var endElement = xmlEvent.asEndElement();

                    /*
                     * If the employee tag is closed then add the employee
                     * object to the list and be ready to read the next
                     * employee data set.
                     */
                    if( "employee".equalsIgnoreCase( endElement.getName().getLocalPart() ) )
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
        final var expected = "[Employee [m_Id=101, m_Name=Lokesh Gupta, m_Title=Author], Employee [m_Id=102, m_Name=Brian Lara, m_Title=Cricketer], Employee [m_Id=103, m_Name=Thomas Thrien, m_Title=Programmer]]";
        assertEquals( expected, process( getFile() ) );
    }   //  testProcess()
}
//  class TestReadXMLStaxIteratorExample

/*
 *  End of File
 */