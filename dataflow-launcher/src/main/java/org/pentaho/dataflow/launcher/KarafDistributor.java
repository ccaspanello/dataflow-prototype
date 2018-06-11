package org.pentaho.dataflow.launcher;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Created by ccaspanello on 4/24/18.
 */
public class KarafDistributor {

  // TODO Parameterize on build
  private String builtArtifact = "app-assembly-1.0-SNAPSHOT.zip";
  private File jarDirectory;

  public KarafDistributor() {
    try {
      this.jarDirectory =
        new File( LauncherMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() )
          .getParentFile();
    } catch ( URISyntaxException e ) {
      throw new RuntimeException( "Unable to locate this jar directory.", e );
    }
  }

  public File extractKaraf() {
    InputStream karafZip = LauncherMain.class.getClassLoader().getResourceAsStream( builtArtifact );
    File karafHome = new File( jarDirectory, "app-assembly" );
    if ( karafHome.exists() ) {
      System.out.println( "Karaf Home already karafZip; reusing it." );
    } else {
      ZipUtil.unzip( karafZip, karafHome );
    }
    return karafHome;
  }

}
