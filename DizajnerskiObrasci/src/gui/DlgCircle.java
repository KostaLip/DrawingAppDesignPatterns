package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import geometry.Circle;
import geometry.Point;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgCircle extends JDialog {

	private final JPanel contentPanel = new JPanel();
	protected JTextArea txtCenterX = new JTextArea();
	protected JTextArea txtCenterY = new JTextArea();
	protected JTextArea txtRadius = new JTextArea();
	Circle circle = null;
	private Color circleColor ;
	private boolean colorChanged = true;
	JButton btnColor = new JButton("CHOSE COLOR");
	private int maxX = 1905;
	private int maxY = 885;
	protected JButton btnInnerColor = new JButton("InnerColor");
	private boolean innerColorChanged;
	private Color circleInnerColor;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			DlgCircle dialog = new DlgCircle();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	
	public DlgCircle(Color color, Color innerColor) {
		circleColor = color;
		circleInnerColor = innerColor;
		setTitle("EDIT CIRCLE");
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.PINK);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblCenterX = new JLabel("X coordinate of Center");
			GridBagConstraints gbc_lblCenterX = new GridBagConstraints();
			gbc_lblCenterX.insets = new Insets(0, 0, 5, 5);
			gbc_lblCenterX.anchor = GridBagConstraints.WEST;
			gbc_lblCenterX.gridx = 0;
			gbc_lblCenterX.gridy = 0;
			contentPanel.add(lblCenterX, gbc_lblCenterX);
		}
		{
			GridBagConstraints gbc_txtCenterX = new GridBagConstraints();
			gbc_txtCenterX.insets = new Insets(0, 0, 5, 0);
			gbc_txtCenterX.fill = GridBagConstraints.BOTH;
			gbc_txtCenterX.gridx = 1;
			gbc_txtCenterX.gridy = 0;
			JScrollPane scrollTxtCenterX = new JScrollPane(txtCenterX);
			contentPanel.add(scrollTxtCenterX, gbc_txtCenterX);
			txtCenterX.setLineWrap(true);
		}
		{
			JLabel lblCenterY = new JLabel("Y coordinate of Center");
			GridBagConstraints gbc_lblCenterY = new GridBagConstraints();
			gbc_lblCenterY.insets = new Insets(0, 0, 5, 5);
			gbc_lblCenterY.gridx = 0;
			gbc_lblCenterY.gridy = 1;
			contentPanel.add(lblCenterY, gbc_lblCenterY);
		}
		{
			GridBagConstraints gbc_txtCenterY = new GridBagConstraints();
			gbc_txtCenterY.insets = new Insets(0, 0, 5, 0);
			gbc_txtCenterY.fill = GridBagConstraints.BOTH;
			gbc_txtCenterY.gridx = 1;
			gbc_txtCenterY.gridy = 1;
			JScrollPane scrollTxtCenterY = new JScrollPane(txtCenterY);
			contentPanel.add(scrollTxtCenterY, gbc_txtCenterY);
			txtCenterY.setLineWrap(true);
		}
		{
			JLabel lblRadius = new JLabel("Radius");
			GridBagConstraints gbc_lblRadius = new GridBagConstraints();
			gbc_lblRadius.insets = new Insets(0, 0, 5, 5);
			gbc_lblRadius.gridx = 0;
			gbc_lblRadius.gridy = 2;
			contentPanel.add(lblRadius, gbc_lblRadius);
		}
		{
			GridBagConstraints gbc_txtRadius = new GridBagConstraints();
			gbc_txtRadius.insets = new Insets(0, 0, 5, 0);
			gbc_txtRadius.fill = GridBagConstraints.BOTH;
			gbc_txtRadius.gridx = 1;
			gbc_txtRadius.gridy = 2;
			JScrollPane scrollTxtRadius = new JScrollPane(txtRadius);
			contentPanel.add(scrollTxtRadius, gbc_txtRadius);
			txtRadius.setLineWrap(true);
		}
		{
			btnColor.setForeground(Color.WHITE);
			btnColor.setBackground(circleColor);
			btnColor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color temp;
					temp = JColorChooser.showDialog(null, "Select a color", circleColor);
					if(temp != null) {
						circleColor = temp;
						btnColor.setBackground(circleColor);
						colorChanged = true;
					}
				}
			});
			{
				btnInnerColor.setBackground(circleInnerColor);
				btnInnerColor.setForeground(Color.BLACK);
				btnInnerColor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { 
						Color temp = JColorChooser.showDialog(null, "Select a inner color", circleInnerColor);
						if(temp != null) {
							circleInnerColor = temp;
							btnInnerColor.setBackground(circleInnerColor);
							innerColorChanged = true;
						}
					}
				});
				GridBagConstraints gbc_btnInnerRadius = new GridBagConstraints();
				gbc_btnInnerRadius.insets = new Insets(0, 0, 0, 5);
				gbc_btnInnerRadius.gridx = 0;
				gbc_btnInnerRadius.gridy = 3;
				contentPanel.add(btnInnerColor, gbc_btnInnerRadius);
			}
			GridBagConstraints gbc_btnColor = new GridBagConstraints();
			gbc_btnColor.gridx = 1;
			gbc_btnColor.gridy = 3;
			contentPanel.add(btnColor, gbc_btnColor);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.CYAN);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (txtCenterX.getText().isEmpty() || txtCenterY.getText().isEmpty()
								|| txtRadius.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "YOU MUST ENTER ALL DATA", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNumeric(txtCenterX.getText())) {
							JOptionPane.showMessageDialog(null, "X COORDINATE MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNumeric(txtCenterY.getText())) {
							JOptionPane.showMessageDialog(null, "Y COORDINATE MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNumeric(txtRadius.getText())) {
							JOptionPane.showMessageDialog(null, "RADIUS MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (isNumeric(txtCenterX.getText()) && isNumeric(txtCenterY.getText())
								&& isNumeric(txtRadius.getText())) {
							int x = Integer.parseInt(txtCenterX.getText());
							int y = Integer.parseInt(txtCenterY.getText());
							int radius = Integer.parseInt(txtRadius.getText());
							if (x < 0) {
								JOptionPane.showMessageDialog(null, "X COORDINATE MUST BE GREATER THAN 0", "ERROR",
										JOptionPane.ERROR_MESSAGE);
							} else if (y < 0) {
								JOptionPane.showMessageDialog(null, "Y COORDINATE MUST BE GREATER THAN 0", "ERROR",
										JOptionPane.ERROR_MESSAGE);
							} else if (x >= maxX) {
								JOptionPane.showMessageDialog(null, "X COORDINATE IS TOO LARGE MUST BEE LESS THAN 1905",
										"ERROR", JOptionPane.ERROR_MESSAGE);
							} else if (y >= maxY) {
								JOptionPane.showMessageDialog(null, "Y COORDINATE IS TOO LARGE MUST BEE LESS THAN 885",
										"ERROR", JOptionPane.ERROR_MESSAGE);
							} else if (radius <= 0) {
								JOptionPane.showMessageDialog(null, "RADIUS MUST BE STRICTLY GREATER THAN 0", "ERROR",
										JOptionPane.ERROR_MESSAGE);
							} else {
								Point center = new Point(x, y);
								circle = new Circle(center, radius);
								if (colorChanged) {
									circle.setColor(circleColor);
									circle.setInnerColor(circleInnerColor);
								}
								dispose();
							}
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private static boolean isNumeric(String str) {
		int number;
		if (str == null || str == "") {
			return false;
		}
		try {
			number = Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public Color getColor() {
		return this.circleColor;
	}

	public Color getInnerColor() {
		return this.circleInnerColor;
	}

	public Circle getCircle() {
		return circle;
	}

}
