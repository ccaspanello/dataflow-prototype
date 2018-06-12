/**
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************
 */
package org.pentaho.app.main;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.karaf.info.ServerInfo;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Application Entry Point
 * <p>
 * Created by ccaspanello on 6/11/18.
 */
public class MainApplication {

  private static final Logger LOG = LoggerFactory.getLogger( MainApplication.class );

  private final BundleContext bundleContext;

  public MainApplication( BundleContext bundleContext ) {
    this.bundleContext = bundleContext;
  }

  public void init() {
    LOG.info( "************************" );
    LOG.info( "Starting Application" );
    LOG.info( "************************" );

    // Fetch Parameters passed in with Karaf execution
    ServerInfo serverInfo = bundleContext.getService( bundleContext.getServiceReference( ServerInfo.class ) );
    String[] args = serverInfo.getArgs();

    LOG.info( "Program Arguements: {}", args );

    // Parse Command Line Arguments and Do Stuff
    Options options = createOptions();
    boolean debug = false;
    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse( options, args );
      debug = cmd.hasOption( "d" );
      if ( cmd.hasOption( "h" ) ) {
        printHelp( options );
      } else {
        doCoolStuff();
      }
    } catch ( ParseException e ) {
      // Assume the user needs help if a parse error happens.
      LOG.error( "{}.  Please refer to the help documentation.", e.getLocalizedMessage() );
      printHelp( options );
    } catch ( Exception e ) {
      LOG.error( " Unexpected error: {}", e );
    } finally {
      // Kill Karaf application if debug flag is not set
      if ( !debug ) {
        kill();
      }
    }
  }

  public void destroy() {
    LOG.info( "************************" );
    LOG.info( "Stopping Application" );
    LOG.info( "************************" );
  }

  private void doCoolStuff() {

    ClassLoader tccl = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader( this.getClass().getClassLoader() );

      PipelineOptionsFactory.register( WordCountOptions.class );
      String[] args = new String[] { "runner=DirectRunner" };
      WordCountOptions options = PipelineOptionsFactory.fromArgs( args ).withValidation().as( WordCountOptions.class );

      Pipeline p = Pipeline.create( options );

      String input = "pom.xml";
      String output = "wordcount";

      LOG.info( "Define Pipeline" );
      p.apply( TextIO.read().from( input ) )
        .apply( TextIO.write().to( output ) );

      LOG.info( "Running Pipeline" );
      p.run().waitUntilFinish();
      LOG.info( "Running complete" );

    } finally {
      Thread.currentThread().setContextClassLoader( tccl );
    }

  }

  private void kill() {
    try {
      bundleContext.getBundle( 0 ).stop();
    } catch ( BundleException e ) {
      e.printStackTrace();
    }
  }

  /*
   * TODO Basic options handling; enhance to encapsulate all possible options as a pojo.
   */
  private Options createOptions() {
    Options options = new Options();

    Option help = Option.builder( "h" )
      .longOpt( "help" )
      .desc( "Dislpays application help." )
      .build();

    Option debug = Option.builder( "d" )
      .longOpt( "debug" )
      .desc( "Debug Flag - Keeps OSGi container alive for debugging purposes only." )
      .build();

    options.addOption( help );
    options.addOption( debug );
    return options;
  }

  private void printHelp( Options options ) {
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp( "launcher", options );
  }
}
