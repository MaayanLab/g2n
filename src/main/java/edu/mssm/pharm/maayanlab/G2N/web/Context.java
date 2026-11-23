/**
 * Initializes Hibernate and JSONify when servlet context is created and then destroys them when it is destroyed.
 *
 * @author Edward Y. Chen
 * @since 03/01/2013
 */

package edu.mssm.pharm.maayanlab.G2N.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.mssm.pharm.maayanlab.G2N.enrichment.Network;
import edu.mssm.pharm.maayanlab.common.web.JSONify;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Context implements ServletContextListener {

    private static Gson gson;

    public static JSONify getJSONConverter() {
        return new JSONify(gson);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Just call the static initializer of that class
//		HibernateUtil.getSessionFactory();

        // Register type adapter with JSONify
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Network.Node.class, (new Network()).new NodeToJson());
        gsonBuilder.setDateFormat("yyyy/MM/dd HH:mm:ss");
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        Context.gson = gsonBuilder.create();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}