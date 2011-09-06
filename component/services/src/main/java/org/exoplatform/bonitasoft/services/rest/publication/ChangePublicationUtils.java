package org.exoplatform.bonitasoft.services.rest.publication;

import java.util.HashMap;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.ecm.publication.NotInPublicationLifecycleException;
import org.exoplatform.services.ecm.publication.PublicationPlugin;
import org.exoplatform.services.ecm.publication.PublicationService;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.config.RepositoryConfigurationException;
import org.exoplatform.services.jcr.core.ExtendedNode;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.publication.lifecycle.stageversion.StageAndVersionPublicationConstant;

public class ChangePublicationUtils {

	private static Log log = ExoLogger.getLogger(ChangePublicationUtils.class);

	/**
	 * allow you to get the list of nodes on the specific path witch have an
	 * information about publication state
	 * 
	 * @param path
	 * @param exoContainer
	 * @return
	 */
	@SuppressWarnings("finally")
	public static QueryResult getNode(String path, ExoContainer exoContainer) {
		QueryResult queryResult = null;
		try {
			String[] pathtab;
			String Filename = "";
			pathtab = path.split("/");
			RepositoryService repositoryService = (RepositoryService) exoContainer.getComponentInstanceOfType(RepositoryService.class);
			ManageableRepository manageableRepository = repositoryService.getRepository(pathtab[1]);
			Session session = manageableRepository.getSystemSession(pathtab[2]);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			StringBuffer buffer = new StringBuffer();
			for (int i = 3; i < pathtab.length; i++) {
				Filename += "/" + pathtab[i];
			}
			path = Filename;
			if(log.isInfoEnabled()){
				log.info("Process node on " + path);
			}
			buffer.append("select * from nt:base where jcr:path= '").append(path).append("'");

			Query query = queryManager.createQuery(buffer.toString(), Query.SQL);
			queryResult = query.execute();
			return queryResult;
		} catch (RepositoryException e) {
			if(log.isDebugEnabled()){
				log.debug(e.getStackTrace());
			}
		} catch (RepositoryConfigurationException e) {
			if(log.isDebugEnabled()){
				log.debug(e.getStackTrace());
			}
		} finally {
			return queryResult;
		}
	}

	/**
	 * allow to change the publication state of node to draft or published state
	 * 
	 * @param node
	 * @param publicationService
	 */
	public static void changeState(ExtendedNode node, PublicationService publicationService, String status) {
		try {

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
				map.put(StageAndVersionPublicationConstant.CURRENT_REVISION_NAME,node.getName());

				if (log.isDebugEnabled()) {
					log.debug("try to pass " + node.getName()+ " to published");
				}
				publicationPlugin.changeState(node,status, map);
				
				if (log.isDebugEnabled()) {
					log.debug("node drafted");
				}

			}
		} catch (NotInPublicationLifecycleException e) {
			if(log.isDebugEnabled()){
				log.debug(e.getStackTrace());
			}
		} catch (RepositoryException e) {
			if(log.isDebugEnabled()){
				log.debug(e.getStackTrace());
			}
		} catch (Exception e) {
			if(log.isDebugEnabled()){
				log.debug(e.getStackTrace());
			}
		}
	}
}
