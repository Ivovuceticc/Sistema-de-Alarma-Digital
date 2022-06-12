package Controller;

import Model.MonitorServer;

import java.io.IOException;

public class Controller {
    private MonitorServer monitorServer;

    public Controller() throws IOException {
        monitorServer = new MonitorServer();

    }
}
