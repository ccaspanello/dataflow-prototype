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
package org.pentaho.app.karaf.support;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by ccaspanello on 6/11/18.
 */
public class AppBundleListener implements BundleListener {

  private static final Logger LOG = LoggerFactory.getLogger( AppBundleListener.class );
  private final BundleContext bundleContext;

  public AppBundleListener( BundleContext bundleContext ) {
    this.bundleContext = bundleContext;
  }

  @PostConstruct
  public void init() throws Exception {
    bundleContext.addBundleListener( this );
  }

  @Override
  public void bundleChanged( BundleEvent event ) {
    Bundle bundle = event.getBundle();
    String name = bundle.getSymbolicName();
    switch ( event.getType() ) {
      case BundleEvent.INSTALLED:
        LOG.info( "INSTALLED: {}", name );
        break;
      case BundleEvent.STARTED:
        LOG.info( "STARTED: {}", name );
        break;
      case BundleEvent.STOPPED:
        LOG.info( "STOPPED: {}", name );
        break;
      case BundleEvent.UPDATED:
        LOG.info( "UPDATED: {}", name );
        break;
      case BundleEvent.UNINSTALLED:
        LOG.info( "UNINSTALLED: {}", name );
        break;
      case BundleEvent.RESOLVED:
        LOG.info( "RESOLVED: {}", name );
        break;
      case BundleEvent.UNRESOLVED:
        LOG.info( "UNRESOLVED: {}", name );
        break;
      case BundleEvent.STARTING:
        LOG.info( "STARTING: {}", name );
        break;
      case BundleEvent.STOPPING:
        LOG.info( "STOPPING: {}", name );
        break;
      case BundleEvent.LAZY_ACTIVATION:
        LOG.info( "LAZY_ACTIVATION: {}", name );
        break;
      default:
        LOG.info( "UNKNOWN: {}", name );
        break;
    }
  }

}
