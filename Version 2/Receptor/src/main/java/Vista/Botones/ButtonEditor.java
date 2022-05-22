package Vista.Botones;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor
{
    JButton button;

    private String label;

    public ButtonEditor(JCheckBox checkBox)
    {
        super(checkBox);
        button = new JButton();
        button.setText("Atender!");
        button.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        //JOptionPane.showMessageDialog(null,"Do you want to modify this line?");
                    }
                }
        );
    }
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column)
    {
        //label = (value == null) ? "Modify" : value.toString();
        //button.setText(label);
        button.setVisible(false);
        return button;
    }
    public Object getCellEditorValue()
    {
        return new String(label);
    }
}