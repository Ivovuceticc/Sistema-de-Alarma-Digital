package Controller;

import Model.MonitorServer;
import View.ViewMonitor;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    private MonitorServer monitorServer;
    private ViewMonitor viewMonitor;

    public Controller() throws IOException {
        viewMonitor = new ViewMonitor();
        monitorServer = new MonitorServer(this);

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == null)
        {
            this.viewMonitor.agregaNuevaConexion((String)arg);
        }
    }
}
