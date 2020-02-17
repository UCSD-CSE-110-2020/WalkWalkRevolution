package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;


@RunWith(AndroidJUnit4.class)
public class RoutesManagerTest {

    Context appContext;
    RoutesManager manager;
    ArrayList<Route> routes;

    @Before
    public void setUp() throws Exception {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Setup RouteManager
        manager = new RoutesManager(appContext);

        manager.addRoute(new Route());
        manager.addRoute(new Route("kms", "londong"));
        manager.addRoute(new Route("wt00000f", "ok"));
        manager.addRoute(new Route("zebra", "ok"));
        manager.addRoute(new Route("aaaa", "ok"));
        manager.addRoute(new Route("adsad", "londong"));
        manager.addRoute(new Route("gadgdsg", "ok"));
        manager.addRoute(new Route("zczc", "ok"));
        manager.addRoute(new Route("aaadfdaaa", "ok"));

    }

    @Test
    public void loadAll() {
        routes = manager.loadAll();
        assertEquals(routes.size(), 9);
    }

    @Test
    public void clearRoutes() {
        manager.clearRoutes();
        routes = manager.loadAll();
        assertEquals(routes.size(), 0);
    }

    @Test
    public void addRoute() {
        manager.clearRoutes();
        manager.addRoute(new Route("qqqqe", "ok"));
        routes = manager.loadAll();
        assertEquals(routes.size(), 1);
    }

    @Test
    public void deleteRoute() {
        manager.clearRoutes();
        manager.addRoute(new Route("qqqqe", "ok"));
        manager.deleteRoute("qqqqe");
        routes = manager.loadAll();
        assertEquals(routes.size(), 0);
    }




}
