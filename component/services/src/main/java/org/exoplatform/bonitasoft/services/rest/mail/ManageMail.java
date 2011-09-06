package org.exoplatform.bonitasoft.services.rest.mail;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.mail.MailService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.rest.resource.ResourceContainer;
@Path("mail")
public class ManageMail implements ResourceContainer {
	private static Log logger = ExoLogger.getLogger(ManageMail.class);
	
	/**
	 * send mail to inform user that the node is published
	 * @param userName
	 * @param link
	 * @param commentaires
	 * @throws Exception
	 */
	@POST
	@Path("validate")
	public void sendMail(@FormParam("userName") String userName,@FormParam("link") String link,@FormParam("commentaires") String commentaires) throws Exception {
		String[]tab=link.split("/");
		ExoContainer container = PortalContainer.getInstance();
		OrganizationService service = (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class);
		User user = service.getUserHandler().findUserByName(userName);
		String DEFAULT_MAIL = "exosender@gmail.com";
		String DEFAULT_DESCRIPTION = "You have received a message from ";
		String subject = "Document's validation";              
	    String message ="<table cellspacing='0' cellpadding='0' border='0' width='550' style='max-width: 550px; border-top: 4px solid #FFD400; font: 12px arial,sans-serif; margin: 0pt auto;'><tbody><tr><td> "
	    +"<div style='font:13px arial, sans-serif;width:540px'>" +"<p> Dear "+user.getFullName()+" ;"+""+"</p>"+  "Your Document  <a href='"+System.getProperty("org.exoplatform.runtime.conf.cas.server.name")+ "/portal/private/intranet/editor?path="+link +"'>" +tab[tab.length-1]+"</a> is valid.<br/>Look the last comments on your document :<br/><br/><FIELDSET><LEGEND align=top><b>Last Comments</b></LEGEND>"
         +"<div style=' max-height: 280px; max-width: 660px; overflow : auto;'>"+commentaires+"</div></FIELDSET>"+"</p>"
	    +"<div style='margin-top: 15px; margin-bottom: 10px; border-bottom: 1px solid #FFD400; line-height: 1px;'>&nbsp;</div>"  
	    +"</td></tr></tbody></table>";
	    MailService mailService = (MailService)PortalContainer.getComponent(MailService.class);
	    Session mailSession = mailService.getMailSession();
	    MimeMessage msg = new MimeMessage(mailSession);
	    String from = "";
	    if(message.equals("null") || message.isEmpty()) {
	    	message = DEFAULT_DESCRIPTION + from;
	    }
	    if(from.equals("null") || from.isEmpty()) {
	    	from = DEFAULT_MAIL;
	    }
	    msg.setFrom(new InternetAddress(from));
	    msg.setSubject(subject);
	    msg.setContent(message, "text/html ; charset=ISO-8859-1");
	    String to =user.getEmail();
	    msg.setRecipient(RecipientType.TO, new InternetAddress(to));
	    mailService.sendMessage(msg);
	    if (logger.isDebugEnabled()) {
			logger.debug("### send mail finish...");
		}
	}

}
