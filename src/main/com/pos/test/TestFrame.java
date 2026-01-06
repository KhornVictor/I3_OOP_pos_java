package main.com.pos.test;

import javax.swing.JFrame;
import main.com.pos.view.product.ProductPanel;

public class TestFrame extends JFrame{
    
    public TestFrame() {
        setTitle("Test Frame");
        setSize(400, 300);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        add(new ProductPanel());
    }
}