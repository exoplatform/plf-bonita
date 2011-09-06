package org.exoplatform.bonitasoft.services.rest.publication;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.exoplatform.services.rest.resource.ResourceContainer;

/**
 * Created by IntelliJ IDEA. User: Racha Touzi Date: 28 MArs. 2011 Time:
 * 16:46:17 To change this template use File | Settings | File Templates.
 */
@Path("publicationService")
public class PublicationRestService implements ResourceContainer {

	/**
	 * change the statate of a given node
	 * --- When you use POST, you are allowed to use void --- If you use GET
	 * void is not allowed
	 * @param path
	 * @param userName
	 * @param status
	 * @return
	 */
	@POST
	@Path("publisheddocument")
	public String publisheddocument(@FormParam("path") String path,@FormParam("userName") String userName,@FormParam("status") String status) {

		ChangePublicationService changepublication = new ChangePublicationService();
		changepublication.start(path,status);
		return path;
	}
}
