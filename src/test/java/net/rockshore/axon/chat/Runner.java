package net.rockshore.axon.chat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jetty.annotations.AbstractDiscoverableAnnotationHandler;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.AnnotationDecorator;
import org.eclipse.jetty.annotations.AnnotationParser;
import org.eclipse.jetty.annotations.AnnotationParser.DiscoverableAnnotationHandler;
import org.eclipse.jetty.annotations.ClassNameResolver;
import org.eclipse.jetty.annotations.WebFilterAnnotationHandler;
import org.eclipse.jetty.annotations.WebListenerAnnotationHandler;
import org.eclipse.jetty.annotations.WebServletAnnotationHandler;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.TagLibConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

public class Runner {
	public static void main(String[] args) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		Server server = new Server(9090);
		// Enable parsing of jndi-related parts of web.xml and jetty-env.xml
		// org.eclipse.jetty.webapp.Configuration.ClassList classlist =
		// org.eclipse.jetty.webapp.Configuration.ClassList
		// .setServerDefault(server);
		// classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
		// "org.eclipse.jetty.plus.webapp.EnvConfiguration",
		// "org.eclipse.jetty.plus.webapp.PlusConfiguration");
		// classlist.addBefore(
		// "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
		// "org.eclipse.jetty.annotations.AnnotationConfiguration");
		WebAppContext context = new WebAppContext("src/main/webapp", "/chat");
		// context.setConfigurations(new Configuration[]{
		// new AnnotationConfiguration()});

		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		// context.getMetaData().addContainerResource(new FileResource(new
		// File("./target/classes").toURI()));
		// context.setConfigurations(new Configuration[]{
		// new WebXmlConfiguration(),
		// new AnnotationConfiguration()
		// });
		context.setConfigurations(new Configuration[] {
				// This is necessary because Jetty out-of-the-box does not scan
				// the classpath of your project in Eclipse, so it doesn't find
				// your WebAppInitializer.
				new AnnotationConfiguration() {
					@Override
					public void configure(WebAppContext context)
							throws Exception {
						boolean metadataComplete = context.getMetaData()
								.isMetaDataComplete();
						context.addDecorator(new AnnotationDecorator(context));

						// Even if metadata is complete, we still need to scan
						// for ServletContainerInitializers - if there are any
						AnnotationParser parser = null;
						if (!metadataComplete) {
							// If metadata isn't complete, if this is a servlet
							// 3 webapp or isConfigDiscovered is true, we need
							// to search for annotations
							if (context.getServletContext()
									.getEffectiveMajorVersion() >= 3
									|| context.isConfigurationDiscovered()) {
								_discoverableAnnotationHandlers
										.add(new WebServletAnnotationHandler(
												context));
								_discoverableAnnotationHandlers
										.add(new WebFilterAnnotationHandler(
												context));
								_discoverableAnnotationHandlers
										.add(new WebListenerAnnotationHandler(
												context));
							}
						}

						// Regardless of metadata, if there are any
						// ServletContainerInitializers with @HandlesTypes, then
						// we need to scan all the
						// classes so we can call their onStartup() methods
						// correctly
						createServletContainerInitializerAnnotationHandlers(
								context, getNonExcludedInitializers(context));

						if (!_discoverableAnnotationHandlers.isEmpty()
								|| _classInheritanceHandler != null
								|| !_containerInitializerAnnotationHandlers
										.isEmpty()) {
							parser = createAnnotationParser();

							parse(context, parser);

							for (DiscoverableAnnotationHandler h : _discoverableAnnotationHandlers)
								context.getMetaData()
										.addDiscoveredAnnotations(
												((AbstractDiscoverableAnnotationHandler) h)
														.getAnnotationList());
						}

					}

					private void parse(final WebAppContext context,
							AnnotationParser parser) throws Exception {
						List<Resource> _resources = getResources(getClass()
								.getClassLoader());

						for (Resource _resource : _resources) {
							if (_resource == null)
								return;

							parser.clearHandlers();
							for (DiscoverableAnnotationHandler h : _discoverableAnnotationHandlers) {
								if (h instanceof AbstractDiscoverableAnnotationHandler)
									((AbstractDiscoverableAnnotationHandler) h)
											.setResource(null); //
							}
							parser.registerHandlers(_discoverableAnnotationHandlers);
							parser.registerHandler(_classInheritanceHandler);
							parser.registerHandlers(_containerInitializerAnnotationHandlers);

							parser.parse(_resource, new ClassNameResolver() {
								public boolean isExcluded(String name) {
									if (context.isSystemClass(name))
										return true;
									if (context.isServerClass(name))
										return false;
									return false;
								}

								public boolean shouldOverride(String name) {
									// looking at webapp classpath, found
									// already-parsed class of same name - did
									// it come from system or duplicate in
									// webapp?
									if (context.isParentLoaderPriority())
										return false;
									return true;
								}
							});
						}
					}

					private List<Resource> getResources(ClassLoader aLoader)
							throws IOException {
						if (aLoader instanceof URLClassLoader) {
							List<Resource> _result = new ArrayList<Resource>();
							URL[] _urls = ((URLClassLoader) aLoader).getURLs();
							for (URL _url : _urls)
								_result.add(Resource.newResource(_url));

							return _result;
						}
						return Collections.emptyList();
					}
				}, new WebXmlConfiguration(), new WebInfConfiguration(),
				new TagLibConfiguration(), new PlusConfiguration(),
				new MetaInfConfiguration(), new FragmentConfiguration(),
				new EnvConfiguration() });
		context.setAttribute(
				"org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				".*/foo-[^/]*\\.jar$|.*/bar-[^/]*\\.jar$|.*/classes/.*");
		server.setHandler(context);
		server.start();
		server.join();
	}
}
