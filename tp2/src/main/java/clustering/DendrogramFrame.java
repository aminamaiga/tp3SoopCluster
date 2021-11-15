package clustering;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import vizualise.DendrogramPanel;

public class DendrogramFrame extends JFrame {

	public DendrogramFrame(Cluster cluster) {
		setSize(800, 400);
		setLocation(100, 200);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel content = new JPanel();
		DendrogramPanel dp = new DendrogramPanel();
		ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("./logo.png"));
		setIconImage(img.getImage());
		setContentPane(content);
		content.setLayout(new BorderLayout());
		content.add(dp, BorderLayout.CENTER);
		dp.setScaleValueDecimals(0);
		dp.setScaleValueInterval(1);
		dp.setShowDistances(false);

		dp.setModel(cluster);
		setVisible(true);
	}

}
