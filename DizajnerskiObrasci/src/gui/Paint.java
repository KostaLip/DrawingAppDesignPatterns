package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import gui.Drawing;
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;

import java.awt.GridBagLayout;
import javax.swing.JToggleButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Paint extends JFrame {

	private JPanel contentPane = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	Border blackline = BorderFactory.createLineBorder(Color.black);
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private Drawing drawingPanel = new Drawing();
	private JToggleButton tglBtnPoint = new JToggleButton("Point");
	private JToggleButton tglBtnLine = new JToggleButton("Line");
	private JToggleButton tglBtnRectangle = new JToggleButton("Rectangle");
	private JToggleButton tglBtnCircle = new JToggleButton("Circle");
	private JToggleButton tglBtnDonut = new JToggleButton("Donut");
	private JToggleButton tglBtnSelect = new JToggleButton("Select");
	private JButton btnDelete = new JButton("Delete");
	private JButton btnEdit = new JButton("Edit");
	private Point startPoint;
	private Point endPoint;
	private final JButton btnColor = new JButton("COLOR");
	private final JButton btnInnerColor = new JButton("INNER COLOR");
	public Color color = Color.BLACK;
	public Color innerColor = Color.WHITE;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Paint frame = new Paint();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @return
	 */
	public Paint() {
		setTitle("Kosta Bjelogrlic IT31/2021");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel pnlBtns = new JPanel();
		pnlBtns.setBackground(Color.PINK);
		contentPane.add(pnlBtns, BorderLayout.NORTH);
		GridBagLayout gbl_pnlBtns = new GridBagLayout();
		gbl_pnlBtns.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_pnlBtns.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_pnlBtns.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_pnlBtns.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pnlBtns.setLayout(gbl_pnlBtns);

		JLabel lblShapes = new JLabel("Shapes");
		GridBagConstraints gbc_lblShapes = new GridBagConstraints();
		gbc_lblShapes.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblShapes.insets = new Insets(0, 0, 5, 5);
		gbc_lblShapes.gridx = 0;
		gbc_lblShapes.gridy = 0;
		pnlBtns.add(lblShapes, gbc_lblShapes);
		lblShapes.setPreferredSize(new Dimension(140, 50));

		buttonGroup.add(tglBtnPoint);
		tglBtnPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				drawingPanel.setAllFalse();
			}
		});
		tglBtnPoint.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnPoint = new GridBagConstraints();
		gbc_tglBtnPoint.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnPoint.gridx = 1;
		gbc_tglBtnPoint.gridy = 0;
		pnlBtns.add(tglBtnPoint, gbc_tglBtnPoint);
		tglBtnPoint.setPreferredSize(new Dimension(100, 50));

		buttonGroup.add(tglBtnLine);
		tglBtnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawingPanel.setAllFalse();
			}
		});
		tglBtnLine.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnLine = new GridBagConstraints();
		gbc_tglBtnLine.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnLine.gridx = 4;
		gbc_tglBtnLine.gridy = 0;
		pnlBtns.add(tglBtnLine, gbc_tglBtnLine);
		tglBtnLine.setPreferredSize(new Dimension(100, 50));

		buttonGroup.add(tglBtnRectangle);
		tglBtnRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawingPanel.setAllFalse();
			}
		});
		tglBtnRectangle.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnRectangle = new GridBagConstraints();
		gbc_tglBtnRectangle.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnRectangle.gridx = 7;
		gbc_tglBtnRectangle.gridy = 0;
		pnlBtns.add(tglBtnRectangle, gbc_tglBtnRectangle);
		tglBtnRectangle.setPreferredSize(new Dimension(100, 50));

		buttonGroup.add(tglBtnCircle);
		tglBtnCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawingPanel.setAllFalse();
			}
		});
		tglBtnCircle.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnCircle = new GridBagConstraints();
		gbc_tglBtnCircle.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnCircle.gridx = 10;
		gbc_tglBtnCircle.gridy = 0;
		pnlBtns.add(tglBtnCircle, gbc_tglBtnCircle);
		tglBtnCircle.setPreferredSize(new Dimension(100, 50));

		buttonGroup.add(tglBtnDonut);
		tglBtnDonut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawingPanel.setAllFalse();
			}
		});
		tglBtnDonut.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tglBtnDonut = new GridBagConstraints();
		gbc_tglBtnDonut.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnDonut.gridx = 13;
		gbc_tglBtnDonut.gridy = 0;
		pnlBtns.add(tglBtnDonut, gbc_tglBtnDonut);
		tglBtnDonut.setPreferredSize(new Dimension(100, 50));
		
		GridBagConstraints gbc_btnColor = new GridBagConstraints();
		gbc_btnColor.insets = new Insets(0, 0, 5, 0);
		gbc_btnColor.gridx = 16;
		gbc_btnColor.gridy = 0;
		btnColor.setPreferredSize(new Dimension(100, 50));
		btnColor.setBackground(color);
		btnColor.setForeground(Color.WHITE);
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color temp;
				temp = JColorChooser.showDialog(null, "Select a color", color);
				if(temp != null) {
					color = temp;
					btnColor.setBackground(color);
				}
			}
		});
		pnlBtns.add(btnColor, gbc_btnColor);

		JLabel lblOptions = new JLabel("Options");
		GridBagConstraints gbc_lblOptions = new GridBagConstraints();
		gbc_lblOptions.anchor = GridBagConstraints.WEST;
		gbc_lblOptions.insets = new Insets(0, 0, 5, 5);
		gbc_lblOptions.gridx = 0;
		gbc_lblOptions.gridy = 1;
		pnlBtns.add(lblOptions, gbc_lblOptions);

		tglBtnSelect.setBackground(Color.GRAY);
		buttonGroup.add(tglBtnSelect);
		GridBagConstraints gbc_tglBtnSelect = new GridBagConstraints();
		gbc_tglBtnSelect.anchor = GridBagConstraints.NORTH;
		gbc_tglBtnSelect.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnSelect.gridx = 4;
		gbc_tglBtnSelect.gridy = 1;
		pnlBtns.add(tglBtnSelect, gbc_tglBtnSelect);
		tglBtnSelect.setPreferredSize(new Dimension(110, 30));

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPointsToNull();
				if (drawingPanel.isShapesEmpty()) {
					JOptionPane.showMessageDialog(null, "THERE ARE NO DRAWN SHAPES", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				} else if (drawingPanel.getSelectedShape() == null) {
					JOptionPane.showMessageDialog(null, "THERE ARE NO SELECTED SHAPES", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int option = JOptionPane.showConfirmDialog(null, "DO YOU WANT TO DELETE SELECTED SHAPE");
					if (option == JOptionPane.YES_OPTION) {
						drawingPanel.getShapes().remove(drawingPanel.getSelectedShape());
						repaint();
						drawingPanel.setSelectedShape();
						drawingPanel.setAllFalse();
					}
				}
			}
		});
		btnDelete.setBackground(Color.GRAY);
		buttonGroup.add(btnDelete);
		GridBagConstraints gbc_tglBtnDelete = new GridBagConstraints();
		gbc_tglBtnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnDelete.gridx = 7;
		gbc_tglBtnDelete.gridy = 1;
		pnlBtns.add(btnDelete, gbc_tglBtnDelete);
		btnDelete.setPreferredSize(new Dimension(110, 30));
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setPointsToNull();
				if (drawingPanel.isShapesEmpty()) {
					JOptionPane.showMessageDialog(null, "THERE ARE NO DRAWN SHAPES", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				} else if (drawingPanel.getSelectedShape() == null) {
					JOptionPane.showMessageDialog(null, "THERE ARE NO SELECTED SHAPES", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (drawingPanel.getSelectedShape() instanceof Point) {
						Point p = (Point) drawingPanel.getSelectedShape();
						Color pointColor = p.getColor();
						DlgPoint dlgPoint = new DlgPoint(pointColor);
						dlgPoint.txtX.setText(Integer.toString(p.getX()));
						dlgPoint.txtY.setText(Integer.toString(p.getY()));
						dlgPoint.btnColor.setBackground(p.getColor());
						dlgPoint.setVisible(true);
						if (dlgPoint.getPoint() != null) {
							p.setX(dlgPoint.getPoint().getX());
							p.setY(dlgPoint.getPoint().getY());
							p.setColor(dlgPoint.getColor());
							repaint();
							drawingPanel.setAllFalse();
						}
					} else if (drawingPanel.getSelectedShape() instanceof Rectangle) {
						Rectangle r = (Rectangle) drawingPanel.getSelectedShape();
						Color recColor = r.getColor();
						Color recInnerColor = r.getInnerColor();
						DlgRectangle dlgRectangle = new DlgRectangle(recColor, recInnerColor);
						dlgRectangle.txtUpperX.setText(Integer.toString(r.getUpperLeftPoint().getX()));
						dlgRectangle.txtUpperY.setText(Integer.toString(r.getUpperLeftPoint().getY()));
						dlgRectangle.txtWidth.setText(Integer.toString(r.getWidth()));
						dlgRectangle.txtHeight.setText(Integer.toString(r.getHeight()));
						dlgRectangle.btnColor.setBackground(r.getColor());
						dlgRectangle.btnInnerColor.setBackground(r.getInnerColor());
						dlgRectangle.setVisible(true);
						if (dlgRectangle.getRectangle() != null) {
							Rectangle r2 = dlgRectangle.getRectangle();
							Point p = new Point(r2.getUpperLeftPoint().getX(), r2.getUpperLeftPoint().getY());
							r.setUpperLeftPoint(p);
							r.setWidth(r2.getWidth());
							r.setHeight(r2.getHeight());
							r.setColor(dlgRectangle.getColor());
							r.setInnerColor(dlgRectangle.getInnerColor());
							repaint();
							drawingPanel.setAllFalse();
						}
					} else if (drawingPanel.getSelectedShape() instanceof Line) {
						Line l = (Line) drawingPanel.getSelectedShape();
						Color lineColor = l.getColor();
						DlgLine dlgLine = new DlgLine(lineColor);
						dlgLine.txtStartX.setText(Integer.toString(l.getStartPoint().getX()));
						dlgLine.txtStartY.setText(Integer.toString(l.getStartPoint().getY()));
						dlgLine.txtEndX.setText(Integer.toString(l.getEndPoint().getX()));
						dlgLine.txtEndY.setText(Integer.toString(l.getEndPoint().getY()));
						dlgLine.btnColor.setBackground(lineColor);
						dlgLine.setVisible(true);
						if (dlgLine.getLine() != null) {
							Line l2 = dlgLine.getLine();
							Point startPoint = new Point(l2.getStartPoint().getX(), l2.getStartPoint().getY());
							Point endPoint = new Point(l2.getEndPoint().getX(), l2.getEndPoint().getY());
							l.setStartPoint(startPoint);
							l.setEndPoint(endPoint);
							l.setColor(dlgLine.getColor());
							repaint();
							drawingPanel.setAllFalse();
						}
					} else if (drawingPanel.getSelectedShape() instanceof Donut) {
						Donut d = (Donut) drawingPanel.getSelectedShape();
						Color donutColor = d.getColor();
						Color donutInnerColor = d.getInnerColor();
						DlgDonut dlgDonut = new DlgDonut(donutColor, donutInnerColor);
						dlgDonut.txtCentarX.setText(Integer.toString(d.getCenter().getX()));
						dlgDonut.txtCentarY.setText(Integer.toString(d.getCenter().getY()));
						dlgDonut.txtRadius.setText(Integer.toString(d.getRadius()));
						dlgDonut.txtInnerRadius.setText(Integer.toString(d.getInnerRadius()));
						dlgDonut.btnColor.setBackground(donutColor);
						dlgDonut.btnInnerColor.setBackground(donutInnerColor);
						dlgDonut.setVisible(true);
						if (dlgDonut.getDonut() != null) {
							Donut d2 = dlgDonut.getDonut();
							Point p2 = new Point(d2.getCenter().getX(), d2.getCenter().getY());
							d.setCenter(p2);
							d.setRadius(d2.getRadius());
							d.setInnerRadius(d2.getInnerRadius());
							d.setColor(dlgDonut.getColor());
							d.setInnerColor(dlgDonut.getInnerColor());
							repaint();
							drawingPanel.setAllFalse();
						}
					} else if (drawingPanel.getSelectedShape() instanceof Circle) {
						Circle c = (Circle) drawingPanel.getSelectedShape();
						Color circleColor = c.getColor();
						Color circleInnerColor = c.getInnerColor();
						DlgCircle dlgCircle = new DlgCircle(circleColor, circleInnerColor);
						dlgCircle.txtCenterX.setText(Integer.toString(c.getCenter().getX()));
						dlgCircle.txtCenterY.setText(Integer.toString(c.getCenter().getY()));
						dlgCircle.txtRadius.setText(Integer.toString(c.getRadius()));
						dlgCircle.btnColor.setBackground(circleColor);
						dlgCircle.btnInnerColor.setBackground(circleInnerColor);
						dlgCircle.setVisible(true);
						if (dlgCircle.getCircle() != null) {
							Circle c2 = dlgCircle.getCircle();
							Point p1 = new Point(c2.getCenter().getX(), c2.getCenter().getY());
							c.setCenter(p1);
							c.setRadius(c2.getRadius());
							c.setColor(dlgCircle.getColor());
							c.setInnerColor(dlgCircle.getInnerColor());
							repaint();
							drawingPanel.setAllFalse();
						}
					}
				}
			}
		});

		btnEdit.setBackground(Color.GRAY);
		buttonGroup.add(btnEdit);
		GridBagConstraints gbc_tglBtnEdit = new GridBagConstraints();
		gbc_tglBtnEdit.insets = new Insets(0, 0, 5, 5);
		gbc_tglBtnEdit.gridx = 10;
		gbc_tglBtnEdit.gridy = 1;
		pnlBtns.add(btnEdit, gbc_tglBtnEdit);
		btnEdit.setPreferredSize(new Dimension(110, 30));
		
		GridBagConstraints gbc_btnInnerColor = new GridBagConstraints();
		gbc_btnInnerColor.insets = new Insets(0, 0, 5, 0);
		gbc_btnInnerColor.gridx = 16;
		gbc_btnInnerColor.gridy = 1;
		btnInnerColor.setPreferredSize(new Dimension(120, 30));
		btnInnerColor.setBackground(innerColor);
		btnInnerColor.setForeground(Color.BLACK);
		btnInnerColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color temp;
				temp = JColorChooser.showDialog(null, "Chose a inner color", innerColor);
				if(temp != null) {
					innerColor = temp;
					btnInnerColor.setBackground(innerColor);
				}
			}
		});
		pnlBtns.add(btnInnerColor, gbc_btnInnerColor);

		drawingPanel.addMouseListener(pnlDrawingClickLitener());
		contentPane.add(drawingPanel, BorderLayout.CENTER);
		drawingPanel.setBorder(blackline);
	}

	private MouseAdapter pnlDrawingClickLitener() {
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (tglBtnPoint.isSelected()) {
					setPointsToNull();
					Point clickPoint = new Point(e.getX(), e.getY());
					clickPoint.setColor(color);
					drawingPanel.addShape(clickPoint);
				} else if (tglBtnLine.isSelected()) {
					if(startPoint == null && endPoint == null) {
						startPoint = new Point(e.getX(), e.getY());
					} else if(startPoint != null && endPoint == null) {
						endPoint = new Point(e.getX(), e.getY());
						Line line = new Line(startPoint, endPoint);
						line.setColor(color);
						startPoint = null;
						endPoint = null;
						drawingPanel.addShape(line);
					}
				} else if (tglBtnSelect.isSelected()) {
					setPointsToNull();
					drawingPanel.select(e.getX(), e.getY());
				} else if (tglBtnRectangle.isSelected()) {
					setPointsToNull();
					DlgRectangle dlgRectangle = new DlgRectangle(color, innerColor);
					dlgRectangle.txtUpperX.setText(Integer.toString(e.getX()));
					dlgRectangle.txtUpperY.setText(Integer.toString(e.getY()));
					dlgRectangle.setVisible(true);
					if (dlgRectangle.getRectangle() != null) {
						drawingPanel.addShape(dlgRectangle.getRectangle());
					}
				} else if (tglBtnCircle.isSelected()) {
					setPointsToNull();
					DlgCircle dlgCircle = new DlgCircle(color, innerColor);
					dlgCircle.txtCenterX.setText(Integer.toString(e.getX()));
					dlgCircle.txtCenterY.setText(Integer.toString(e.getY()));
					dlgCircle.setVisible(true);
					if (dlgCircle.getCircle() != null) {
						drawingPanel.addShape(dlgCircle.getCircle());
					}
				} else if (tglBtnDonut.isSelected()) {
					setPointsToNull();
					DlgDonut dlgDonut = new DlgDonut(color, innerColor);
					dlgDonut.txtCentarX.setText(Integer.toString(e.getX()));
					dlgDonut.txtCentarY.setText(Integer.toString(e.getY()));
					dlgDonut.setVisible(true);
					if (dlgDonut.getDonut() != null) {
						drawingPanel.addShape(dlgDonut.getDonut());
					}
				}
			}
		};
	};
	
	private void setPointsToNull() {
		this.endPoint = null;
		this.startPoint = null;
	}
	
}

