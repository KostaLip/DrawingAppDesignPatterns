package mvc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import java.awt.SystemColor;

public class DrawingFrame extends JFrame {

	DrawingView view = new DrawingView();
	DrawingController controller;
	
	JPanel pnlBtns = new JPanel();
	public JToggleButton tglBtnPoint = new JToggleButton("Point");
	public JToggleButton tglBtnLine = new JToggleButton("Line");
	public JToggleButton tglBtnRectangle = new JToggleButton("Rectangle");
	public JToggleButton tglBtnCircle = new JToggleButton("Circle");
	public JToggleButton tglBtnDonut = new JToggleButton("Donut");
	public JToggleButton tglBtnSelect = new JToggleButton("Select");
	public JButton btnDelete = new JButton("Delete");
	public JButton btnEdit = new JButton("Edit");
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final JButton btnColor = new JButton("COLOR");
	private final JButton btnInnerColor = new JButton("INNER COLOR");
	public Color color = Color.BLACK;
	public Color innerColor = Color.WHITE;
	private final JPanel pnlCommandList = new JPanel();
	public JTextArea commandList = new JTextArea(40,25);
	private JScrollPane scrollList = new JScrollPane(commandList);
	private JPanel pnlOptions = new JPanel();
	public JButton btnUndo = new JButton("UNDO");
	public JButton btnRedo = new JButton("REDO");
	public JButton btnToBack = new JButton("TO BACK");
	public JButton btnBringToBack = new JButton("BRING TO BACK");
	public JButton btnToFront = new JButton("TO FRONT");
	public JButton btnBringToFront = new JButton("BRING TO FRONT");
	public final JToggleButton tglBtnHexagon = new JToggleButton("Hexagon");
	
	public DrawingFrame() {
		view.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		//PNL BTNS
		view.setBackground(Color.WHITE);
		pnlBtns.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		pnlBtns.setBackground(SystemColor.inactiveCaption);
		GridBagLayout gbl_pnlBtns = new GridBagLayout();
		gbl_pnlBtns.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_pnlBtns.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_pnlBtns.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_pnlBtns.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pnlBtns.setLayout(gbl_pnlBtns);
		
		//LABELA SHAPES
		JLabel lblShapes = new JLabel("Shapes");
		GridBagConstraints gbc_lblShapes = new GridBagConstraints();
		gbc_lblShapes.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblShapes.insets = new Insets(0, 0, 5, 5);
		gbc_lblShapes.gridx = 0;
		gbc_lblShapes.gridy = 0;
		pnlBtns.add(lblShapes, gbc_lblShapes);
		lblShapes.setPreferredSize(new Dimension(140, 50));

		//TOGLE BTN POINT
		buttonGroup.add(tglBtnPoint);
		tglBtnPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.setPointsToNull();
			}
		});
		tglBtnPoint.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnPoint = new GridBagConstraints();
		gbc_tglBtnPoint.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnPoint.gridx = 1;
		gbc_tglBtnPoint.gridy = 0;
		pnlBtns.add(tglBtnPoint, gbc_tglBtnPoint);
		tglBtnPoint.setPreferredSize(new Dimension(100, 50));

		//TOGLE BTN LINE
		buttonGroup.add(tglBtnLine);
		tglBtnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setPointsToNull();
			}
		});
		tglBtnLine.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnLine = new GridBagConstraints();
		gbc_tglBtnLine.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnLine.gridx = 4;
		gbc_tglBtnLine.gridy = 0;
		pnlBtns.add(tglBtnLine, gbc_tglBtnLine);
		tglBtnLine.setPreferredSize(new Dimension(100, 50));

		//TOGLE BTN RECTANGLE
		buttonGroup.add(tglBtnRectangle);
		tglBtnRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setPointsToNull();
			}
		});
		tglBtnRectangle.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnRectangle = new GridBagConstraints();
		gbc_tglBtnRectangle.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnRectangle.gridx = 7;
		gbc_tglBtnRectangle.gridy = 0;
		pnlBtns.add(tglBtnRectangle, gbc_tglBtnRectangle);
		tglBtnRectangle.setPreferredSize(new Dimension(100, 50));

		//TOGLE BTN CIRCLE
		buttonGroup.add(tglBtnCircle);
		tglBtnCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setPointsToNull();
			}
		});
		tglBtnCircle.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnCircle = new GridBagConstraints();
		gbc_tglBtnCircle.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnCircle.gridx = 10;
		gbc_tglBtnCircle.gridy = 0;
		pnlBtns.add(tglBtnCircle, gbc_tglBtnCircle);
		tglBtnCircle.setPreferredSize(new Dimension(100, 50));

		//TOGLE BTN DONUT
		buttonGroup.add(tglBtnDonut);
		tglBtnDonut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setPointsToNull();
			}
		});
		tglBtnDonut.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnDonut = new GridBagConstraints();
		gbc_tglBtnDonut.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnDonut.gridx = 13;
		gbc_tglBtnDonut.gridy = 0;
		pnlBtns.add(tglBtnDonut, gbc_tglBtnDonut);
		tglBtnDonut.setPreferredSize(new Dimension(100, 50));
		
		//BTN COLOR
		GridBagConstraints gbc_btnColor = new GridBagConstraints();
		gbc_btnColor.insets = new Insets(0, 0, 5, 0);
		gbc_btnColor.gridx = 17;
		gbc_btnColor.gridy = 0;
		btnColor.setPreferredSize(new Dimension(100, 50));
		btnColor.setBackground(color);
		btnColor.setForeground(Color.WHITE);
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.setPointsToNull();
				Color temp;
				temp = JColorChooser.showDialog(null, "Select a color", color);
				if(temp != null) {
					color = temp;
					btnColor.setBackground(color);
				}
			}
		});
		
		buttonGroup.add(tglBtnHexagon);
		GridBagConstraints gbc_tglBtnHexagon = new GridBagConstraints();
		gbc_tglBtnHexagon.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnHexagon.gridx = 16;
		gbc_tglBtnHexagon.gridy = 0;
		tglBtnHexagon.setBackground(Color.LIGHT_GRAY);
		tglBtnHexagon.setPreferredSize(new Dimension(100, 50));
		pnlBtns.add(tglBtnHexagon, gbc_tglBtnHexagon);
		pnlBtns.add(btnColor, gbc_btnColor);
		
		//LABELA OPTIONS
		JLabel lblOptions = new JLabel("Options");
		GridBagConstraints gbc_lblOptions = new GridBagConstraints();
		gbc_lblOptions.anchor = GridBagConstraints.WEST;
		gbc_lblOptions.insets = new Insets(0, 0, 5, 5);
		gbc_lblOptions.gridx = 0;
		gbc_lblOptions.gridy = 1;
		pnlBtns.add(lblOptions, gbc_lblOptions);

		//TOGLE BTN SELECT
		tglBtnSelect.setBackground(Color.GRAY);
		buttonGroup.add(tglBtnSelect);
		GridBagConstraints gbc_tglBtnSelect = new GridBagConstraints();
		gbc_tglBtnSelect.anchor = GridBagConstraints.NORTH;
		gbc_tglBtnSelect.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnSelect.gridx = 4;
		gbc_tglBtnSelect.gridy = 1;
		pnlBtns.add(tglBtnSelect, gbc_tglBtnSelect);
		tglBtnSelect.setPreferredSize(new Dimension(110, 30));
		tglBtnSelect.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.deleteSelectedShape();
			}
		});
		
		//TOGLE BTN DELETE
		btnDelete.setBackground(Color.GRAY);
		buttonGroup.add(btnDelete);
		GridBagConstraints gbc_tglBtnDelete = new GridBagConstraints();
		gbc_tglBtnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnDelete.gridx = 7;
		gbc_tglBtnDelete.gridy = 1;
		pnlBtns.add(btnDelete, gbc_tglBtnDelete);
		btnDelete.setPreferredSize(new Dimension(110, 30));
		btnDelete.setEnabled(false);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.editSelectedShape();
			}
		});

		//TOGLE BTN EDIT
		btnEdit.setBackground(Color.GRAY);
		buttonGroup.add(btnEdit);
		GridBagConstraints gbc_tglBtnEdit = new GridBagConstraints();
		gbc_tglBtnEdit.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnEdit.gridx = 10;
		gbc_tglBtnEdit.gridy = 1;
		pnlBtns.add(btnEdit, gbc_tglBtnEdit);
		btnEdit.setPreferredSize(new Dimension(110, 30));
		btnEdit.setEnabled(false);
		
		//BTN INNERCOLOR
		GridBagConstraints gbc_btnInnerColor = new GridBagConstraints();
		gbc_btnInnerColor.insets = new Insets(0, 0, 5, 0);
		gbc_btnInnerColor.gridx = 17;
		gbc_btnInnerColor.gridy = 1;
		btnInnerColor.setPreferredSize(new Dimension(120, 30));
		btnInnerColor.setBackground(innerColor);
		btnInnerColor.setForeground(Color.BLACK);
		btnInnerColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setPointsToNull();
				Color temp;
				temp = JColorChooser.showDialog(null, "Chose a inner color", innerColor);
				if(temp != null) {
					innerColor = temp;
					btnInnerColor.setBackground(innerColor);
				}
			}
		});
		pnlBtns.add(btnInnerColor, gbc_btnInnerColor);
		
		//ADDING VIEW AND BtnPane to ContentPane
		getContentPane().add(view, BorderLayout.CENTER);
		getContentPane().add(pnlBtns, BorderLayout.NORTH);
		pnlCommandList.setBackground(SystemColor.activeCaption);
		
		//ADDING COMMANDLIST ON WEST
		commandList.setEditable(false);
		pnlCommandList.add(scrollList);
		pnlCommandList.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		getContentPane().add(pnlCommandList, BorderLayout.WEST);
		pnlOptions.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		pnlOptions.setBackground(SystemColor.inactiveCaption);
		
		//ADDING OPTIONS ON SOUTH 
		getContentPane().add(pnlOptions, BorderLayout.SOUTH);
		//BTN UNDO
		btnUndo.setBackground(Color.GRAY);
		btnUndo.setPreferredSize(new Dimension(110, 30));
		btnUndo.setEnabled(false);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
			}
		});
		btnBringToBack.setEnabled(false);
		btnBringToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToBack(false);
			}
		});
		btnBringToBack.setBackground(Color.GRAY);
		
		pnlOptions.add(btnBringToBack);
		btnToBack.setEnabled(false);
		btnToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.toBack(false);
			}
		});
		btnToBack.setBackground(Color.GRAY);
		
		pnlOptions.add(btnToBack);
		pnlOptions.add(btnUndo);
		
		//BTN REDO
		btnRedo.setBackground(Color.GRAY);
		btnRedo.setPreferredSize(new Dimension(110,30));
		btnRedo.setEnabled(false);
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.redo();
			}
		});
		pnlOptions.add(btnRedo);
		btnToFront.setEnabled(false);
		btnToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.toFront(false);
			}
		});
		btnToFront.setBackground(Color.GRAY);
		
		pnlOptions.add(btnToFront);
		btnBringToFront.setEnabled(false);
		btnBringToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToFront(false);
			}
		});
		btnBringToFront.setBackground(Color.GRAY);
		
		pnlOptions.add(btnBringToFront);
	}
	
	public DrawingView getView() {
		return this.view;
	}
	
	public void setController(DrawingController controller) {
		this.controller = controller;
		view.addMouseListener(controller.viewMouseListener());
	}
}