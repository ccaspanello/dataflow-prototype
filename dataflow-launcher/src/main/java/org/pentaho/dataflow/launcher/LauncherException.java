package org.pentaho.dataflow.launcher;

/**
 * Launcher Exception
 *
 * Created by ccaspanello on 1/22/18.
 */
public class LauncherException extends RuntimeException {
  public LauncherException( String message, Throwable throwable ) {
    super( message, throwable );
  }
}
