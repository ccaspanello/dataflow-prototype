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
