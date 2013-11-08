package net.rockshore.axon.chat;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class Runner {
	public static void main(String[] args) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		Server server = new Server(9090);
		WebAppContext context = new WebAppContext("src/main/webapp", "/chat");

/*		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		resource_handler.setResourceBase(".");*/

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { //resource_handler,
				context });
		server.setHandler(handlers);

		server.start();
		server.join();
	}
}
