package org.exoplatform.bonitasoft.services.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.exoplatform.bonitasoft.services.utils.Constants;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import com.thoughtworks.xstream.XStream;

public class ProcessManager {
	private static Log logger = ExoLogger.getLogger(ProcessManager.class);
	private static final String VAR_LINK = "link";
//	private static final String VAR_USER = "user";
	
	/**
	 * start an instance of publicationProcess
	 * @param node
	 * @param request
	 * @throws Exception
	 */
	public void startProcess(Node node, HttpServletRequest request) throws Exception{
		// **Construction de l'URL de Binding*/
		StringBuffer link = new StringBuffer();
		String restUrl = "/bonita-server-rest/API/runtimeAPI/instantiateProcessWithVariables";
		link.append(restUrl);
		// TODO it will be better to put the name of the process on an xml file
		String processName = "/PublicationProcess--1.0";
		link.append(processName);

		PostMethod httpMethod = new PostMethod(link.toString());
		List<String> messages = new ArrayList<String>();
		try{
			HttpClient httpClient = null;
	        HostConfiguration hostConfiguration = new HostConfiguration();
	        hostConfiguration.setHost(Constants.HOST, Constants.PORT);
	
	        HttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
	        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
	        int maxHostConnections = 20;
	        params.setMaxConnectionsPerHost(hostConfiguration, maxHostConnections);
	        connectionManager.setParams(params);
	
	        httpClient = new HttpClient(connectionManager);
	
	        Credentials credsCredentials = new UsernamePasswordCredentials(Constants.REST_USER, Constants.REST_PASS);
	        httpClient.getState().setCredentials(AuthScope.ANY, credsCredentials);
	        httpClient.setHostConfiguration(hostConfiguration);
	
	        httpMethod.setDoAuthentication(true);
	        httpMethod.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
	        
	        NameValuePair nameValuePair = new NameValuePair("options", "user:" + request.getRemoteUser());
	        NameValuePair[] nameValuePairs = new NameValuePair[2];
	        nameValuePairs[0] = nameValuePair;
	        
	        /** variables à passer pour instancier le process*/
			Map<String, Object> variables = new HashMap<String, Object>();
			StringBuffer href = new StringBuffer();
			
			RepositoryImpl repositoryImpl = (RepositoryImpl) node.getSession().getRepository();
			href.append("/").append(repositoryImpl.getName());
			href.append("/" + node.getSession().getWorkspace().getName());
			href.append(node.getPath());
			variables.put(VAR_LINK, href.toString());
//			variables.put(VAR_USER, request.getRemoteUser());

			XStream xstream = new XStream();
			NameValuePair nameValuePair2 = new NameValuePair("variables", xstream.toXML(variables));
			nameValuePairs[1] = nameValuePair2;

			httpMethod.setRequestBody(nameValuePairs);
	        httpClient.executeMethod(httpMethod);
	        
	        String response = httpMethod.getResponseBodyAsString();
	        if (logger.isInfoEnabled()) {
                logger.info(response);
            }
	        messages.add("ok");
		}catch (Exception e) {
			if(logger.isDebugEnabled()){
				logger.debug(e.getStackTrace());
			}
			messages.add("ko");
		}finally{
			httpMethod.releaseConnection();
			request.setAttribute("messages", messages);
		}
	}

}
