package org.exoplatform.bonitasoft.services.rest.publication;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.exoplatform.services.ecm.publication.PublicationService;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ExtendedNode;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

@Path("publicationService")
public class PublicationRestService implements ResourceContainer {

  private static Log log = ExoLogger.getLogger(PublicationRestService.class);
  private PublicationService publicationService;
  private RepositoryService repositoryService;

  public PublicationRestService(RepositoryService repositoryService, PublicationService publicationService) {
    this.publicationService = publicationService;
    this.repositoryService = repositoryService;
  }

  /**
   * change the state of a given node --- When you use POST, you are
   * allowed to use void --- If you use GET void is not allowed
   * 
   * @param path
   * @param userName
   * @param status
   * @return
   */
  @POST
  @Path("publisheddocument")
  public String changeDocumentsStatus(@FormParam("path") String path, @FormParam("userName") String userName,
      @FormParam("status") String status) throws Exception {
    if (log.isInfoEnabled()) {
      log.info("Start ChangePublicationService");
    }

    String[] pathtab = path.split("/");
    String filePath = "";
    for (int i = 3; i < pathtab.length; i++) {
      filePath += "/" + pathtab[i];
    }
    // get the list of nodes related to current path
    ManageableRepository repository = repositoryService.getRepository(pathtab[1]);
    // This bloc has to be synchronized, the use of same session with two
    // threads same time could cause a problem
    synchronized (repository) {
      Session session = null;
      try {
        session = SessionProvider.createSystemProvider().getSession(pathtab[2], repository);

        Node parentNode = (Node) session.getItem(filePath);
        NodeIterator nodeIterator = parentNode.getNodes();
        // process the list of node
        while (nodeIterator.hasNext()) {
          ExtendedNode actionNode = (ExtendedNode) nodeIterator.nextNode();
          ChangePublicationUtils.changeState(actionNode, publicationService, status);
        }
      } finally {
        if (session != null) {
          session.logout();
        }
      }
    }
    if (log.isInfoEnabled()) {
      log.info("ChangePublicationService Completed");
    }
    return path;
  }
}
