/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2019, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/
package schemacrawler.test;


import static org.junit.Assert.assertThat;
import static schemacrawler.test.utility.FileHasContent.classpathResource;
import static schemacrawler.test.utility.FileHasContent.fileResource;
import static schemacrawler.test.utility.FileHasContent.hasSameContentAs;
import static schemacrawler.test.utility.TestUtility.flattenCommandlineArgs;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import schemacrawler.Main;
import schemacrawler.test.utility.BaseDatabaseTest;
import schemacrawler.test.utility.TestName;
import schemacrawler.test.utility.TestOutputStream;
import schemacrawler.test.utility.TestWriter;

public class CommandLineHelpTest
  extends BaseDatabaseTest
{

  private static final String COMMAND_LINE_HELP_OUTPUT = "command_line_help_output/";

  @Rule
  public TestName testName = new TestName();

  private TestOutputStream out;
  private TestOutputStream err;

  @After
  public void cleanUpStreams()
  {
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
  }

  @Before
  public void setUpStreams()
    throws Exception
  {
    out = new TestOutputStream();
    System.setOut(new PrintStream(out));

    err = new TestOutputStream();
    System.setErr(new PrintStream(err));
  }

  @Test
  public void commandLineHelpDefaults()
    throws Exception
  {
    final Map<String, String> args = new HashMap<>();
    args.put("h", null);

    run(args, null);
  }

  private void run(final Map<String, String> argsMap,
                   final Map<String, String> config)
    throws Exception
  {

    final TestWriter testout = new TestWriter();
    try (final TestWriter out = testout;)
    {
      Main.main(flattenCommandlineArgs(argsMap));
      out.write(this.out.getFileContents());
    }
    assertThat(fileResource(testout),
               hasSameContentAs(classpathResource(COMMAND_LINE_HELP_OUTPUT
                                                  + testName.currentMethodName()
                                                  + ".txt")));
  }

}
