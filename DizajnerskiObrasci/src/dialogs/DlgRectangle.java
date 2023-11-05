package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import geometry.Point;
import geometry.Rectangle;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgRectangle extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public JTextArea txtUpperX = new JTextArea();
	public JTextArea txtUpperY = new JTextArea();
	public JTextArea txtWidth = new JTextArea();
	public JTextArea txtHeight = new JTextArea();
	Rectangle rec = null;
	private boolean colorChanged = false;
	private boolean innerColorChanged = false;
	public JButton btnColor = new JButton("CHOSE COLOR");
	private Color recColor, recInnerColor;
	private int maxX = 1905;
	private int maxY = 885;
	public JButton btnInnerColor = new JButton("InnerColor");

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			DlgRectangle dialog = new DlgRectangle();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public DlgRectangle(Color color, Color innerColor) {
		recColor = color;
		recInnerColor = innerColor;
		getContentPane().setBackground(Color.PINK);
		setTitle("EDIT RECTANGLE");
		setModal(true);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 428, 0 };
		gridBagLayout.rowHeights = new int[] { 195, 39, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		contentPanel.setBackground(Color.PINK);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagConstraints gbc_contentPanel = new GridBagConstraints();
		gbc_contentPanel.fill = GridBagConstraints.BOTH;
		gbc_contentPanel.insets = new Insets(0, 0, 5, 0);
		gbc_contentPanel.gridx = 0;
		gbc_contentPanel.gridy = 0;
		getContentPane().add(contentPanel, gbc_contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblUpperX = new JLabel("X coordinate of UpperLeftPoint");
			GridBagConstraints gbc_lblUpperX = new GridBagConstraints();
			gbc_lblUpperX.insets = new Insets(0, 0, 5, 5);
			gbc_lblUpperX.anchor = GridBagConstraints.WEST;
			gbc_lblUpperX.gridx = 0;
			gbc_lblUpperX.gridy = 0;
			contentPanel.add(lblUpperX, gbc_lblUpperX);
		}
		{
			GridBagConstraints gbc_txtUpperX = new GridBagConstraints();
			gbc_txtUpperX.insets = new Insets(0, 0, 5, 0);
			gbc_txtUpperX.fill = GridBagConstraints.BOTH;
			gbc_txtUpperX.gridx = 1;
			gbc_txtUpperX.gridy = 0;
			JScrollPane scrollTxtUpperX = new JScrollPane(txtUpperX);
			contentPanel.add(scrollTxtUpperX, gbc_txtUpperX);
			txtUpperX.setLineWrap(true);
		}
		{
			JLabel lblUpperY = new JLabel("Y coordinate of UpperLeftPoint");
			GridBagConstraints gbc_lblUpperY = new GridBagConstraints();
			gbc_lblUpperY.insets = new Insets(0, 0, 5, 5);
			gbc_lblUpperY.gridx = 0;
			gbc_lblUpperY.gridy = 1;
			contentPanel.add(lblUpperY, gbc_lblUpperY);
		}
		{
			GridBagConstraints gbc_txtUpperY = new GridBagConstraints();
			gbc_txtUpperY.insets = new Insets(0, 0, 5, 0);
			gbc_txtUpperY.fill = GridBagConstraints.BOTH;
			gbc_txtUpperY.gridx = 1;
			gbc_txtUpperY.gridy = 1;
			JScrollPane scrollTxtUpperY = new JScrollPane(txtUpperY);
			contentPanel.add(scrollTxtUpperY, gbc_txtUpperY);
			txtUpperY.setLineWrap(true);
		}
		{
			JLabel lblWidth = new JLabel("Width");
			GridBagConstraints gbc_lblWidth = new GridBagConstraints();
			gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
			gbc_lblWidth.gridx = 0;
			gbc_lblWidth.gridy = 2;
			contentPanel.add(lblWidth, gbc_lblWidth);
		}
		{
			GridBagConstraints gbc_txtWidth = new GridBagConstraints();
			gbc_txtWidth.insets = new Insets(0, 0, 5, 0);
			gbc_txtWidth.fill = GridBagConstraints.BOTH;
			gbc_txtWidth.gridx = 1;
			gbc_txtWidth.gridy = 2;
			JScrollPane scrollTxtWidth = new JScrollPane(txtWidth);
			contentPanel.add(scrollTxtWidth, gbc_txtWidth);
			txtWidth.setLineWrap(true);
		}
		{
			JLabel lblHeight = new JLabel("Height");
			GridBagConstraints gbc_lblHeight = new GridBagConstraints();
			gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
			gbc_lblHeight.gridx = 0;
			gbc_lblHeight.gridy = 3;
			contentPanel.add(lblHeight, gbc_lblHeight);
		}
		{
			GridBagConstraints gbc_txtHeight = new GridBagConstraints();
			gbc_txtHeight.insets = new Insets(0, 0, 5, 0);
			gbc_txtHeight.fill = GridBagConstraints.BOTH;
			gbc_txtHeight.gridx = 1;
			gbc_txtHeight.gridy = 3;
			JScrollPane scrollTxtHeight = new JScrollPane(txtHeight);
			contentPanel.add(scrollTxtHeight, gbc_txtHeight);
			txtHeight.setLineWrap(true);
		}
		{
			btnColor.setForeground(Color.WHITE);
			btnColor.setBackground(recColor);
			btnColor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color temp = JColorChooser.showDialog(null, "Select a color", recColor);
					if(temp != null) {
						recColor = temp;
						btnColor.setBackground(recColor);
						colorChanged = true;
					}
				}
			});
			{
				btnInnerColor.setBackground(recInnerColor);
				btnInnerColor.setForeground(Color.BLACK);
				btnInnerColor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Color temp = JColorChooser.showDialog(null, "Select a inner color", recInnerColor);
						if(temp != null) {
							recInnerColor = temp;
							btnInnerColor.setBackground(recInnerColor);
							colorChanged = true;
						}
					}
				});
				GridBagConstraints gbc_btnInnerColor = new GridBagConstraints();
				gbc_btnInnerColor.anchor = GridBagConstraints.SOUTH;
				gbc_btnInnerColor.insets = new Insets(0, 0, 0, 5);
				gbc_btnInnerColor.gridx = 0;
				gbc_btnInnerColor.gridy = 4;
				contentPanel.add(btnInnerColor, gbc_btnInnerColor);
			}
			GridBagConstraints gbc_btnColor = new GridBagConstraints();
			gbc_btnColor.gridx = 1;
			gbc_btnColor.gridy = 4;
			contentPanel.add(btnColor, gbc_btnColor);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.CYAN);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 1;
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (txtUpperX.getText().isEmpty() || txtUpperY.getText().isEmpty()
								|| txtWidth.getText().isEmpty() || txtHeight.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "YOU MUST ENTER ALL DATA", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNmueric(txtUpperX.getText())) {
							JOptionPane.showMessageDialog(null, "X COORDINATE MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNmueric(txtUpperY.getText())) {
							JOptionPane.showMessageDialog(null, "Y COORDINATE MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNmueric(txtWidth.getText())) {
							JOptionPane.showMessageDialog(null, "WIDTH MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (!isNmueric(txtHeight.getText())) {
							JOptionPane.showMessageDialog(null, "HEIGHT MUST BE A NUMBER", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} else if (isNmueric(txtUpperX.getText()) && isNmueric(txtUpperY.getText())
								&& isNmueric(txtWidth.getText()) && isNmueric(txtHeight.getText())) {
							int x = Integer.parseInt(txtUpperX.getText());
							int y = Integer.parseInt(txtUpperY.getText());
							int width = Integer.parseInt(txtWidth.getText());
							int height = Integer.parseInt(txtHeight.getText());
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
							} else if (width <= 0) {
								JOptionPane.showMessageDialog(null, "WIDTH MUST BE STRICTLY GREATER THAN 0", "ERROR",
										JOptionPane.ERROR_MESSAGE);
							} else if (height <= 0) {
								JOptionPane.showMessageDialog(null, "HEIGHT MUST BE STRICTLY GREATER THAN 0", "ERROR",
										JOptionPane.ERROR_MESSAGE);
							} else {
								Point upperL = new Point(x, y);
								rec = new Rectangle(upperL, width, height);
								rec.setColor(recColor);
								rec.setInnerColor(recInnerColor);
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

	private static boolean isNmueric(String str) {
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
		return this.recColor;
	}
	
	public Color getInnerColor() {
		return this.recInnerColor;
	}

	public Rectangle getRectangle() {
		return rec;
	}
}
