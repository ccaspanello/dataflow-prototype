package org.pentaho.app.main;

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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;

/**
 * Main Application Entry Point
 *
 * Created by ccaspanello on 6/11/18.
 */
public class MainApplication {

  private static final Logger LOG = LoggerFactory.getLogger( MainApplication.class );

  private final BundleContext bundleContext;

  public MainApplication( BundleContext bundleContext ) {
    this.bundleContext = bundleContext;
  }

  @PostConstruct
  public void init() {
    LOG.info( "************************" );
    LOG.info( "Starting Application" );
    LOG.info( "************************" );

    // Fetch Parameters passed in with Karaf execution
    ServerInfo serverInfo = bundleContext.getService( bundleContext.getServiceReference( ServerInfo.class ) );
    String[] args = serverInfo.getArgs();

    LOG.info("Program Arguements: {}", args);

    // Parse Command Line Arguments and Do Stuff
    Options options = createOptions();
    boolean debug = false;
    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse( options, args );
      debug = cmd.hasOption( "d" );
      if ( cmd.hasOption( "h" ) ) {
        printHelp( options );
      } else if ( cmd.hasOption( "k" ) ) {
        doCoolStuff();
      }
    } catch ( ParseException e ) {
      // Assume the user needs help if a parse error happens.
      LOG.error( "{}.  Please refer to the help documentation.", e.getLocalizedMessage() );
      printHelp( options );
    } catch(Exception e){
      LOG.error(" Unexpected error: {}", e);
    }finally {
      // Kill Karaf application if debug flag is not set
      if ( !debug ) {
        kill();
      }
    }
  }

  @PreDestroy
  public void destroy() {
    LOG.info( "************************" );
    LOG.info( "Stopping Application" );
    LOG.info( "************************" );
  }

  private void doCoolStuff() {
    // TODO Do Cool Stuff Here !!
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
