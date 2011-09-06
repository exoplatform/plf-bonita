package org.exoplatform.bonitasoft.services.rest.BonitaLifecycle;

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

@Path("ManageBonita")
public class GetBonitaLifeCycle implements ResourceContainer{

	private static Log logger = ExoLogger.getLogger(GetBonitaLifeCycle.class);

	/**
	 * allow to add a property to given node have the information that this node is enrolled on bonita process
	 * @param link
	 * @param inlife
	 */
	@POST
	@Path("lifecycle")
	public void getCycle(@FormParam("link") String link,
			@FormParam("inlife") String inlife) {

		if (logger.isDebugEnabled()) {
			logger.debug("### Starting getCycle Action ...");
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
			if (node.isNodeType("exo:bonitaLifecycle")) {
				if (node.hasProperty("exo:bonitaEnrolledIn")) {
					node.setProperty("exo:bonitaEnrolledIn", inlife);
					node.save();
				}
			} else {
				if (node.canAddMixin("exo:bonitaLifecycle") && inlife.equals("true")) {
					node.addMixin("exo:bonitaLifecycle");
					node.save();
					node.setProperty("exo:bonitaEnrolledIn", inlife);
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
