package org.exoplatform.bonitasoft.services.rest.social;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.activity.model.Activity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;

/**
 * Created by IntelliJ IDEA. User: khemais Date: 16 févr. 2011 Time:
 * 16:46:17 To change this template use File | Settings | File Templates.
 */
@Path("activitiesStreamManager")
public class ActivitiesStreamManager implements ResourceContainer {
  private static Log logger = ExoLogger.getLogger(ActivitiesStreamManager.class);

  /**
   * when we use LeaveApplication process, an new entry is added to the
   * activity stream of logged user
   * 
   * @param userName
   * @param comment
   */
  @POST
  @Path("saveActivity")
  @Produces({ MediaType.APPLICATION_JSON })
  public void saveActivity(@FormParam("userName") String userName, @FormParam("comment") String comment) {

    if (logger.isInfoEnabled()) {
      logger.info("### Request for [" + userName + "] adding [" + comment + "] ...###");
    }
    // Get current container
    PortalContainer container = PortalContainer.getInstance();

    // Get IdentityManager to handle identity operation
    IdentityManager identityManager = (IdentityManager) container.getComponentInstance(IdentityManager.class);

    // Get ActivityManager to handle activity operation
    ActivityManager activityManager = (ActivityManager) container.getComponentInstanceOfType(ActivityManager.class);

    // Get existing user or create a new one
    try {
      // TODO case user null
      Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, userName);

      // Create new activity for this user
      Activity activity = new Activity();
      activity.setUserId(userIdentity.getId());
      activity.setTitle(comment);
      // Save activity into JCR using ActivityManager
      activityManager.saveActivity(activity);
    } catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug(e.getStackTrace());
        logger.debug("#### can not write on activity stream of [" + userName + "] ####");
      }
    }
    if (logger.isInfoEnabled()) {
      logger.info("### Request for [" + userName + "] adding [" + comment + "] completed ###");
    }
  }

}
