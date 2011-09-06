package org.exoplatform.bonitasoft.services.rest.comment;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

@Path("ManageComment")
public class ManageComment implements ResourceContainer {
	private static Log logger = ExoLogger.getLogger(ManageComment.class);

	/**
	 * return the comment saved on bonita engine
	 * @param link
	 * @param comment
	 */
	@POST
	@Path("addComment")
	public void addComment(@FormParam("link") String link,
			@FormParam("commentaires") String comment) {

		if (logger.isDebugEnabled()) {
			logger.debug("### Starting addComment Action ...");
		}
		try{
			ExoContainer container = PortalContainer.getInstance();
			String[] pathtab;
			String Filename = "";
			pathtab = link.split("/");
			RepositoryService repositoryService = (RepositoryService) container.getComponentInstanceOfType(RepositoryService.class);
			ManageableRepository manageableRepository = repositoryService.getRepository(pathtab[1]);
			Session session = manageableRepository.getSystemSession(pathtab[2]);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			StringBuffer buffer = new StringBuffer();
			for (int i = 3; i < pathtab.length; i++) {
				Filename += "/" + pathtab[i];
			}
			link = Filename;
			if (logger.isDebugEnabled()) {
				logger.debug("### get Node from this path: " + link);
			}
			buffer.append("select * from nt:base where jcr:path= '").append(link).append("'");
			Query query = queryManager.createQuery(buffer.toString(), Query.SQL);
			QueryResult queryResult = query.execute();
			Node node = queryResult.getNodes().nextNode();
			if (node.isNodeType("exo:userComment")) {
				if (node.hasProperty("exo:comment")) {
					node.setProperty("exo:comment", comment);
					node.save();
				}
			}else{
				if (node.canAddMixin("exo:userComment")) {
					node.addMixin("exo:userComment");
					node.save();
					node.setProperty("exo:comment", comment);
			
					}
				}
			session.save();
				
			}catch (Exception e) {
			if(logger.isDebugEnabled()){
				logger.debug(e.getStackTrace());
			}
		}
	}

}
