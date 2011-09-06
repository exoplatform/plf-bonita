eXo.require("eXo.projects.Module");
eXo.require("eXo.projects.Product");

function getModule(params) {

	var kernel = params.kernel;
	var core = params.core;
	var jcr = params.eXoJcr;
	var ws = params.ws;
	var platform = params.platform;
	var module = new Module();
	module.version = "${project.version}";
	module.relativeMavenRepo = "org/exoplatform/bonita";
	module.relativeSRCRepo = "exo.bonita";
	module.name = "exo.bonita";
	
	module.extension =
		new Project("org.exoplatform.bonita", "exo.bonita.extension.webapp", "war", module.version).
		addDependency(new Project("org.exoplatform.bonita", "exo.bonita.extension.config", "jar", module.version));
	module.extension.deployName = "bonita-extension";
	
	module.portlet = {};
	module.portlet = new Project("org.exoplatform.bonita", "exo.bonita.extension.portlet", "war", module.version);
	module.portlet.deployName = "bonita-portlet";

	module.web = {};
	module.web.bonita = new Project("org.exoplatform.bonita", "exo.bonita.web.bonita", "war", module.version).
		addDependency(new Project("com.h2database", "h2", "jar", "${h2.version}")).
		addDependency(new Project("org.ccil.cowan.tagsoup", "tagsoup", "jar", "${tagsoup.version}")).
		addDependency(new Project("com.drewnoakes", "metadata-extractor", "jar", "${metadata.version}"));
	
	module.web.bonitaServerRest = new Project("org.exoplatform.bonita", "exo.bonita.web.bonita-server-rest", "war", module.version);
	
	module.component = {};
	module.component.services = new Project("org.exoplatform.bonita", "exo.bonita.component.services", "jar", module.version);
	module.component.uiextension = new Project("org.exoplatform.bonita", "exo.bonita.component.uiextension", "jar", module.version);
	
	module.patch = {};
	module.patch.tomcat = new Project("org.exoplatform.bonita", "exo.bonita.server.tomcat.patch", "jar", module.version);

	return module;
}
