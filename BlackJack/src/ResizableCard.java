import javax.swing.*;
import java.awt.*;

public class ResizableCard extends JLabel{
	private ImageIcon icon;
	
	public ResizableCard(ImageIcon icon) {
		super(icon);
		this.icon = icon;
	}
	
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
		this.repaint();
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon != null) {
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
            g.drawImage(scaledImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
