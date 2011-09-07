package org.exoplatform.bonitasoft.services.rest.publication;

import java.util.HashMap;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;

import org.exoplatform.services.ecm.publication.IncorrectStateUpdateLifecycleException;
import org.exoplatform.services.ecm.publication.PublicationPlugin;
import org.exoplatform.services.ecm.publication.PublicationService;
import org.exoplatform.services.jcr.core.ExtendedNode;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.publication.lifecycle.stageversion.StageAndVersionPublicationConstant;

public class ChangePublicationUtils {

  private static Log log = ExoLogger.getLogger(ChangePublicationUtils.class);

  /**
   * allow to change the publication state of node to draft or published
   * state
   * 
   * @param node
   * @param publicationService
   */
  public static void changeState(ExtendedNode node, PublicationService publicationService, String status)
      throws ValueFormatException, PathNotFoundException, UnsupportedRepositoryOperationException, LockException,
      RepositoryException, IncorrectStateUpdateLifecycleException, Exception {

    if (node.hasProperty(StageAndVersionPublicationConstant.PUBLICATION_LIFECYCLE_NAME)) {
      String lifeCycleName = node.getProperty(StageAndVersionPublicationConstant.PUBLICATION_LIFECYCLE_NAME).getString();
      PublicationPlugin publicationPlugin = publicationService.getPublicationPlugins().get(lifeCycleName);
      if (log.isDebugEnabled()) {
        log.debug("processing " + node.getPath());
      }
      if (!node.isCheckedOut()) {
        node.checkout();
      }
      HashMap<String, String> map = new HashMap<String, String>();
      map.put(StageAndVersionPublicationConstant.CURRENT_REVISION_NAME, node.getName());

      if (log.isDebugEnabled()) {
        log.debug("try to pass " + node.getName() + " to published");
      }
      publicationPlugin.changeState(node, status, map);

      if (log.isDebugEnabled()) {
        log.debug("node drafted");
      }

    }
  }
}
