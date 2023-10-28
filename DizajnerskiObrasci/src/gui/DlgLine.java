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

import geometry.Line;
import geometry.Point;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgLine extends JDialog {

	private final JPanel contentPanel = new JPanel();
	protected JTextArea txtStartX = new JTextArea();
	protected JTextArea txtStartY = new JTextArea();
	protected JTextArea txtEndX = new JTextArea();
	protected JTextArea txtEndY = new JTextArea();
	Color lineColor;
	private Line line = null;
	private boolean colorChanged = true;
	protected JButton btnColor = new JButton("CHOSE COLOR");
	private int maxX = 1905;
	private int maxY = 885;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			DlgLine dialog = new DlgLine();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public DlgLine(Color color) {
		lineColor = color;
		setTitle("EDIT LINE");
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.PINK);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblStartX = new JLabel("X coordinate of StartPoint");
			GridBagConstraints gbc_lblStartX = new GridBagConstraints();
			gbc_lblStartX.insets = new Insets(0, 0, 5, 5);
			gbc_lblStartX.gridx = 0;
			gbc_lblStartX.gridy = 0;
			contentPanel.add(lblStartX, gbc_lblStartX);
		}
		{
			GridBagConstraints gbc_txtStartX = new GridBagConstraints();
			gbc_txtStartX.insets = new Insets(0, 0, 5, 0);
			gbc_txtStartX.fill = GridBagConstraints.BOTH;
			gbc_txtStartX.gridx = 2;
			gbc_txtStartX.gridy = 0;
			JScrollPane scrollTxtStartX = new JScrollPane(txtStartX);
			contentPanel.add(scrollTxtStartX, gbc_txtStartX);
			txtStartX.setLineWrap(true);
		}
		{
			JLabel lblStartY = new JLabel("Y coordinate of StartPoint");
			GridBagConstraints gbc_lblStartY = new GridBagConstraints();
			gbc_lblStartY.insets = new Insets(0, 0, 5, 5);
			gbc_lblStartY.gridx = 0;
			gbc_lblStartY.gridy = 1;
			contentPanel.add(lblStartY, gbc_lblStartY);
		}
		{
			GridBagConstraints gbc_txtStartY = new GridBagConstraints();
			gbc_txtStartY.insets = new Insets(0, 0, 5, 0);
			gbc_txtStartY.fill = GridBagConstraints.BOTH;
			gbc_txtStartY.gridx = 2;
			gbc_txtStartY.gridy = 1;
			JScrollPane scrollTxtStartY = new JScrollPane(txtStartY);
			contentPanel.add(scrollTxtStartY, gbc_txtStartY);
			txtStartY.setLineWrap(true);
		}
		{
			JLabel lblEndX = new JLabel("X coordinate of EndPoint");
			GridBagConstraints gbc_lblEndX = new GridBagConstraints();
			gbc_lblEndX.insets = new Insets(0, 0, 5, 5);
			gbc_lblEndX.gridx = 0;
			gbc_lblEndX.gridy = 2;
			contentPanel.add(lblEndX, gbc_lblEndX);
		}
		{
			GridBagConstraints gbc_txtEndX = new GridBagConstraints();
			gbc_txtEndX.insets = new Insets(0, 0, 5, 0);
			gbc_txtEndX.fill = GridBagConstraints.BOTH;
			gbc_txtEndX.gridx = 2;
			gbc_txtEndX.gridy = 2;
			JScrollPane scrollTxtEndX = new JScrollPane(txtEndX);
			contentPanel.add(scrollTxtEndX, gbc_txtEndX);
			txtEndX.setLineWrap(true);
		}
		{
			JLabel lblEndY = new JLabel("Y coordinate of EndPoint");
			GridBagConstraints gbc_lblEndY = new GridBagConstraints();
			gbc_lblEndY.insets = new Insets(0, 0, 5, 5);
			gbc_lblEndY.anchor = GridBagConstraints.WEST;
			gbc_lblEndY.gridx = 0;
			gbc_lblEndY.gridy = 3;
			contentPanel.add(lblEndY, gbc_lblEndY);
		}
		{
			GridBagConstraints gbc_txtEndY = new GridBagConstraints();
			gbc_txtEndY.insets = new Insets(0, 0, 5, 0);
			gbc_txtEndY.fill = GridBagConstraints.BOTH;
			gbc_txtEndY.gridx = 2;
			gbc_txtEndY.gridy = 3;
			JScrollPane scrollTxtEndY = new JScrollPane(txtEndY);
			contentPanel.add(scrollTxtEndY, gbc_txtEndY);
			txtEndY.setLineWrap(true);
		}
		{
			btnColor.setForeground(Color.WHITE);
			btnColor.setBackground(lineColor);
			btnColor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color temp = JColorChooser.showDialog(null, "Select a color", lineColor);
					if(temp != null) {
						lineColor = temp;
						btnColor.setBackground(lineColor);
						colorChanged = true;
					}
				}
			});
			GridBagConstraints gbc_btnColor = new GridBagConstraints();
			gbc_btnColor.gridx = 2;
			gbc_btnColor.gridy = 4;
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
						if (txtStartX.getText().isEmpty() || txtStartY.getText().isEmpty()
								|| txtEndX.getText().isEmpty() || txtEndY.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "YOU MUST ENTER ALL DATA", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNumeric(txtStartX.getText())) {
							JOptionPane.showMessageDialog(null, "X COORDINATE OF START POINT MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNumeric(txtStartY.getText())) {
							JOptionPane.showMessageDialog(null, "Y COORDINATE OF START POINT MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNumeric(txtEndX.getText())) {
							JOptionPane.showMessageDialog(null, "X COORDINATE OF END POINT MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNumeric(txtEndY.getText())) {
							JOptionPane.showMessageDialog(null, "Y COORDINATE OF END POINT MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (isNumeric(txtStartX.getText()) && isNumeric(txtStartY.getText())
								&& isNumeric(txtEndX.getText()) && isNumeric(txtEndY.getText())) {
							int x1 = Integer.parseInt(txtStartX.getText());
							int y1 = Integer.parseInt(txtStartY.getText());
							int x2 = Integer.parseInt(txtEndX.getText());
							int y2 = Integer.parseInt(txtEndY.getText());
							if (x1 < 0) {
								JOptionPane.showMessageDialog(null,
										"X COORDINATE OF START POINT MUST BE GREATER THAN 0", "ERROR",
										JOptionPane.ERROR_MESSAGE);
							} else if (y1 < 0) {
								JOptionPane.showMessageDialog(null,
										"Y COORDINATE OF START POINT MUST BE GREATER THAN 0", "ERROR",
										JOptionPane.ERROR_MESSAGE);
							} else if (x2 < 0) {
								JOptionPane.showMessageDialog(null, "X COORDINATE OF END POINT MUST BE GREATER THAN 0",
										"ERROR", JOptionPane.ERROR_MESSAGE);
							} else if (y2 < 0) {
								JOptionPane.showMessageDialog(null, "Y COORDINATE OF END POINT MUST BE GREATER THAN 0",
										"ERROR", JOptionPane.ERROR_MESSAGE);
							} else if (x1 >= maxX && x2 >= maxX) {
								JOptionPane.showMessageDialog(null, "X COORDINATE IS TOO LARGE MUST BEE LESS THAN 1905",
										"ERROR", JOptionPane.ERROR_MESSAGE);
							} else if (y1 >= maxY && y2 >= maxY) {
								JOptionPane.showMessageDialog(null, "Y COORDINATE IS TOO LARGE MUST BEE LESS THAN 885",
										"ERROR", JOptionPane.ERROR_MESSAGE);
							} else {
								Point startPoint = new Point(x1, y1);
								Point endPoint = new Point(x2, y2);
								line = new Line(startPoint, endPoint);
								if (colorChanged) {
									line.setColor(lineColor);
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

	public Line getLine() {
		return this.line;
	}

	public Color getColor() {
		return this.lineColor;
	}

}
