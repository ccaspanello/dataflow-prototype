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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility to help Zip and Unzip files
 *
 * Created by ccaspanello on 4/25/18.
 */
public class ZipUtil {

  private static final Logger LOG = LoggerFactory.getLogger( ZipUtil.class );

  public static void unzip( InputStream inputStream, File destination ) {

    try ( ZipInputStream zis = new ZipInputStream( inputStream ) ) {
      ZipEntry zipEntry = zis.getNextEntry();
      while ( zipEntry != null ) {
        // Get FileName stripping off the parent folder (name of the zip)
        String fileName = zipEntry.getName().substring( zipEntry.getName().indexOf( "/" ) + 1 );
        File newFile = new File( destination, fileName );
        if ( zipEntry.isDirectory() ) {
          newFile.mkdirs();
        } else {
          newFile.createNewFile();
          writeEntry( zis, newFile );
        }
        zipEntry = zis.getNextEntry();
      }
    } catch ( IOException e ) {
      throw new LauncherException( "Unexpected error trying to unzip application assembly.", e );
    }
  }

  private static void writeEntry( ZipInputStream zis, File newFile ) {
    byte[] buffer = new byte[ 1024 ];
    try ( FileOutputStream fos = new FileOutputStream( newFile ) ) {
      int len;
      while ( ( len = zis.read( buffer ) ) > 0 ) {
        fos.write( buffer, 0, len );
      }
    } catch ( IOException e ) {
      throw new LauncherException( "Unable to write zip entry: " + newFile.getAbsoluteFile(), e );
    }
  }

}
