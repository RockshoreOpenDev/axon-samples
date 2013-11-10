package net.rockshore.axon.chat;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Runner {
	public static void main(String[] args) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		Server server = new Server(9090);

		// Enable parsing of jndi-related parts of web.xml and jetty-env.xml
		org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList
				.setServerDefault(server);
		classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration",
				"org.eclipse.jetty.plus.webapp.PlusConfiguration");
		classlist.addBefore(
				"org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration");
		WebAppContext context = new WebAppContext("src/main/webapp", "/chat");
		context.setAttribute(
				"org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				".*/foo-[^/]*\\.jar$|.*/bar-[^/]*\\.jar$|.*/classes/.*");
		context.setClassLoader(Thread.currentThread().getContextClassLoader());
		System.out.println("Runner.main()"+context.getClassPath());
		server.setHandler(context);

		server.start();
		server.join();
	}
}
