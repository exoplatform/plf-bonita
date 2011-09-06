package org.exoplatform.bonitasoft.services.rest.publication;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.query.QueryResult;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.ecm.publication.PublicationService;
import org.exoplatform.services.jcr.core.ExtendedNode;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;


public class ChangePublicationService  {
	
	private static Log log = ExoLogger.getLogger(ChangePublicationService.class);

	/**
	 * this method allow to start the changePublication service to get the list of node 
	 * to change there publicationState 
	 * @param path
	 * @param status
	 */
	public void start(String path, String status) {
		
		try {
			if(log.isInfoEnabled()){
				log.info("Start ChangePublicationService");
			}
			ExoContainer exoContainer=ExoContainerContext.getCurrentContainer();
			PublicationService publicationService = (PublicationService)exoContainer.getComponentInstanceOfType(PublicationService.class);
			
			// get the list of nodes related to current path
			QueryResult queryResult = ChangePublicationUtils.getNode(path, exoContainer);
			if(queryResult != null && queryResult.getNodes().getSize() > 0){
				if(log.isDebugEnabled()){
					log.debug("Found " + queryResult.getNodes().getSize() + " nodes to process on " + path);
				}
				// process the list of node
				for (NodeIterator iter = queryResult.getNodes(); iter.hasNext();) {
					Node actionNode = iter.nextNode();
					ExtendedNode node = (ExtendedNode) actionNode;
					ChangePublicationUtils.changeState(node, publicationService,status);
				}
			}
			if(log.isInfoEnabled()){
				log.info("ChangePublicationService Completed");
			}
		} catch (Exception e) {
			if(log.isDebugEnabled()){
				log.debug(e.getStackTrace());
			}
		}

	}
}