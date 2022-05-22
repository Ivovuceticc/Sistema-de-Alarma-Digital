package Vista;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanelConImagen extends JPanel
{
    private static final long serialVersionUID = 1L;
    private ImageIcon fondo = null;

    public void setFondo(ImageIcon fondo)
    {
        this.fondo = fondo;
    }

    @Override
    public void paint(Graphics g)
    {
        if (this.fondo != null)
        {
            g.drawImage(this.fondo.getImage(), 0, 0, null);
            super.paint(g);
        }
    }
}
