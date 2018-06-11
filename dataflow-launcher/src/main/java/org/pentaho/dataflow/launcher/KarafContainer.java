package org.pentaho.dataflow.launcher;

import org.apache.karaf.main.Main;

import java.io.File;

/**
 * Karaf Container
 *
 * Configures and launches Karaf instance.
 *
 * Created by ccaspanello on 1/22/18.
 */
public class KarafContainer {

  private final String karafHome;
  private Main main;

  public KarafContainer(String karafHome){
    this.karafHome = karafHome;
  }

  /**
   * Launch Karaf Intance with Parameters
   * @param args CommandLine Arguments
   */
  public void launch( String[] args) {
    try {
      String root = new File( karafHome ).getAbsolutePath();
      System.out.println( "Starting Karaf @ " + root );
      System.setProperty( "karaf.home", root );
      System.setProperty( "karaf.base", root );
      System.setProperty( "karaf.data", root + "/data" );
      System.setProperty( "karaf.etc", root + "/etc" );
      System.setProperty( "karaf.history", root + "/data/history.txt" );
      System.setProperty( "karaf.instances", root + "/instances" );
      System.setProperty( "karaf.startLocalConsole", "false" );
      System.setProperty( "karaf.startRemoteShell", "true" );
      System.setProperty( "karaf.lock", "false" );
      main = new Main( args );
      main.launch();
    } catch ( Exception e ) {
      throw new LauncherException("Unexpected error starting Karaf container.", e);
    }
  }

  /**
   * Blocking call that awaits shutdown of Karaf container.
   */
  public void awaitShutdown(){
    try {
      main.awaitShutdown();
      main.destroy();
    } catch ( Exception e ) {
      throw new LauncherException("Unexpected error waiting for Karaf container to shut down.", e);
    }
  }

}
