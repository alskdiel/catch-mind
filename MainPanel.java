package termP;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MainPanel {

	public static String myName;
	JFrame frame;
	GridBagLayout gridbag;
	GridBagConstraints constraint;
	Socket s;
	BufferedReader reader;
	public static PrintWriter writer;

	public MainPanel() {
		frame = new JFrame("Drawing Board");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		gridbag = new GridBagLayout();
		constraint = new GridBagConstraints();
		frame.getContentPane().setLayout(gridbag);

		PaintPanel pp = new PaintPanel(reader, writer);
		LeftPanel lp = new LeftPanel(s, reader, writer);

		frame.getContentPane().setLayout(gridbag);

		constraint.anchor = GridBagConstraints.CENTER;
		addPanel(frame, pp, 0, 0, 1, 1);
		addPanel(frame, lp, 1, 0, 1, 1);

		frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					writer.println("[exit]");
					writer.flush();
					writer.println(myName);
					writer.flush();
				} catch (Exception e4) {
				}
				System.exit(0);
			}
		});

	}

	public void addPanel(JFrame f, JPanel p, int gridx, int gridy, int width,
			int height) {

		constraint.gridx = gridx;
		constraint.gridy = gridy;
		constraint.gridwidth = width;
		constraint.gridheight = height;

		gridbag.setConstraints(p, constraint);
		f.getContentPane().add(p);
	}

	public static void setWriter(PrintWriter writer) {
		MainPanel.writer = writer;
	}
}

class LeftPanel extends JPanel {

	GridBagLayout gridbag;
	GridBagConstraints constraint;
	LoginPanel lp;
	UsersPanel up;
	public static JTextArea ta;
	JScrollPane sp;
	JTextField tb;
	public static JButton btn;
	static BufferedReader reader;
	static PrintWriter writer;

	LeftPanel(Socket s, BufferedReader reader, PrintWriter writer) {
		lp = new LoginPanel(s, reader, writer);
		up = new UsersPanel();
		gridbag = new GridBagLayout();
		constraint = new GridBagConstraints();
		setLayout(gridbag);

		constraint.anchor = GridBagConstraints.CENTER;
		addPanel(lp, 0, 0, 2, 1);
		addPanel(up, 0, 1, 2, 1);

		ta = new JTextArea(15, 25);
		ta.setEditable(false);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);

		sp = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// sp.setAutoscrolls(true);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		tb = new JTextField();
		tb.setPreferredSize(new Dimension(200, 30));
		btn = new JButton("전송");
		btn.setEnabled(false);
		addComp(sp, 0, 2, 2, 5);
		addComp(tb, 0, 7, 1, 1);
		addComp(btn, 1, 7, 1, 1);

		btn.addActionListener(new onClickListener());
		tb.addKeyListener(new onEnterListener());
	}

	private class onEnterListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_ENTER) { // 텍스트필드에서 엔터키 눌렀을 때
				try {
					if (!(tb.getText().equals(""))) {
						writer.println("[chat]"); // 채팅 내용 전송 시 전송하는 메시지
						writer.flush();
						writer.println(tb.getText()); // 채팅 내용 전송
						writer.flush();
						tb.setText("");
					}
				} catch (Exception e2) {
				}
			}
		}

		// TODO Auto-generated method stub

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

	private class onClickListener implements ActionListener { // 전송 버튼을 이용한 채팅
																// 전송

		@Override
		public void actionPerformed(ActionEvent e) {
			Object i = e.getSource();
			if (i == btn) {
				try {
					if (!(tb.getText().equals(""))) {
						writer.println("[chat]"); // 채팅 내용 전송 시 전송하는 메시지
						writer.flush();
						writer.println(tb.getText()); // 채팅 내용 전송
						writer.flush();
						tb.setText("");
					}
				} catch (Exception e2) {
				}
			}
		}
	}

	public static void setRW(BufferedReader reader, PrintWriter writer) {
		LeftPanel.reader = reader;
		LeftPanel.writer = writer;
	}

	public void addPanel(JPanel p, int gridx, int gridy, int width, int height) {

		constraint.gridx = gridx;
		constraint.gridy = gridy;
		constraint.gridwidth = width;
		constraint.gridheight = height;

		gridbag.setConstraints(p, constraint);
		add(p);
	}

	public void addComp(Component c, int gridx, int gridy, int width, int height) {

		constraint.gridx = gridx;
		constraint.gridy = gridy;
		constraint.gridwidth = width;
		constraint.gridheight = height;

		gridbag.setConstraints(c, constraint);
		add(c);
	}
}

class UsersPanel extends JPanel {
	public static JLabel users;

	UsersPanel() {
		users = new JLabel();

		users.setPreferredSize(new Dimension(100, 50));
		add(users);
	}
}

class LoginPanel extends JPanel {
	public static final int PORT_NUM = 3333;
	BufferedReader reader;
	PrintWriter writer;
	JTextField id;
	JTextField ip;
	JLabel id_info;
	JLabel ip_info;
	public static JButton connect;
	JLabel warn;
	GridBagLayout gridbag;
	GridBagConstraints constraint;
	Socket s;

	LoginPanel(Socket s, BufferedReader reader, PrintWriter writer) {
		this.s = s;
		gridbag = new GridBagLayout();
		constraint = new GridBagConstraints();
		setLayout(gridbag);

		connect = new JButton("연결");
		id_info = new JLabel("이름 : ");
		id = new JTextField(10);
		ip_info = new JLabel("IP주소 : ");
		ip = new JTextField(10);
		connect = new JButton("연결");
		warn = new JLabel("");

		addComp(id_info, 0, 0, 1, 1);
		addComp(id, 1, 0, 1, 1);
		addComp(ip_info, 0, 1, 1, 1);
		addComp(ip, 1, 1, 1, 1);
		addComp(connect, 0, 2, 2, 1);
		addComp(warn, 0, 3, 2, 1);

		connect.addActionListener(new onClickListener());
	}

	private class onClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object i = e.getSource();
			if (i == connect) {
				try {
					if (id.getText().equals(null) || id.getText().equals("")) {
						warn.setText("이름을 입력해주세요");
					}
					if (ip.getText().equals(null) || ip.getText().equals("")) {
						warn.setText("아이피를 입력해주세요");}
					 else {
						try{
						s = new Socket(ip.getText(), PORT_NUM); // socket 연결
						warn.setText("");
						reader = new BufferedReader(new InputStreamReader( // reader
																			// 연결
								s.getInputStream()));
						writer = new PrintWriter(s.getOutputStream(), true); // writer
																				// 연결

						UsersPanel.users.setText("<html>접속자<br></html>"); // 유저판
																			// 초기화
						writer.println("[connected]"); // 연결되었음을 알리는 메시지 서버에 전송
						writer.flush();
						writer.println(id.getText());
						writer.flush();
						MainPanel.myName = id.getText();
						MainPanel.setWriter(writer); // 각 패널들의 reader, writer 설정
						DrawPanel.setWriter(writer, reader);
						BTN.setWriter(writer);
						LeftPanel.setRW(reader, writer);
						}catch(Exception e5){warn.setText("유효한 아이피를 입력해주세요");}

					}
				} catch (Exception e2) {
				}
			}

		}
	}

	public void addComp(Component c, int gridx, int gridy, int width, int height) {

		constraint.gridx = gridx;
		constraint.gridy = gridy;
		constraint.gridwidth = width;
		constraint.gridheight = height;

		gridbag.setConstraints(c, constraint);
		add(c);
	}
}

class PaintPanel extends JPanel {
	DrawPanel tp;
	topPanel top;
	BTN b;
	BufferedReader reader;
	PrintWriter writer;

	PaintPanel(BufferedReader reader, PrintWriter writer) {
		this.reader = reader;
		this.writer = writer;

		tp = new DrawPanel();
		new Thread(tp).start();
		// tp.start();

		top = new topPanel(tp);
		b = new BTN(tp);

		setLayout(new BorderLayout());
		add(top, BorderLayout.NORTH);
		add(tp, BorderLayout.CENTER);
		add(b, BorderLayout.SOUTH);
	}

}

class BTN extends JPanel {
	public static JButton b;
	public static JButton rdy;
	public static JLabel exam;
	JTextField ans;
	public static JButton ans_enter;
	DrawPanel tp;
	static PrintWriter writer;

	BTN(DrawPanel tp) {
		b = new JButton("clear");
		b.setEnabled(false);
		rdy = new JButton("ready");
		rdy.setEnabled(false);
		exam = new JLabel("");
		ans = new JTextField(10);
		ans_enter = new JButton("정답확인");
		ans_enter.setEnabled(false);
		this.tp = tp;
		add(b);
		add(rdy);
		add(exam);
		add(ans);
		add(ans_enter);

		b.addActionListener(new onClickListener());
		rdy.addActionListener(new onClickListener());
		ans_enter.addActionListener(new onClickListener());
	}

	public static void setWriter(PrintWriter writer) {
		BTN.writer = writer;
	}

	private class onClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object id = e.getSource();
			if (id == b) {
				tp.clear();
			} else if (id == rdy) {
				writer.println("[ready]");
				rdy.setEnabled(false);
				// DrawPanel.enable = true;
			} else if (id == ans_enter) {
				writer.println("[answer]");
				writer.flush();
				writer.println(ans.getText());
				writer.flush();
				ans.setText("");
				// DrawPanel.enable = true;
			}
		}
	}
}

class topPanel extends JPanel {

	DrawPanel tp;
	colorPanel colp;
	// pensizePanel pszp;
	polygonPanel plyp;

	topPanel(DrawPanel tp) {
		this.tp = tp;
		colp = new colorPanel(tp);
		// pszp = new pensizePanel(tp);
		plyp = new polygonPanel(tp);

		add(colp);
		// add(pszp);
		add(plyp);
	}
}

class colorPanel extends JPanel {
	JRadioButton red;
	JRadioButton blue;
	JRadioButton black;
	DrawPanel tp;
	JLabel l;
	ImageIcon blackim, redim, blueim;
	public static ButtonGroup group;

	colorPanel(DrawPanel tp) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.tp = tp;
		l = new JLabel("색깔");

		blackim = new ImageIcon("./black.png");
		redim = new ImageIcon("./red.png");
		blueim = new ImageIcon("./blue.png");
		black = new JRadioButton(blackim);
		red = new JRadioButton(redim);
		blue = new JRadioButton(blueim);
		group = new ButtonGroup();

		group.add(black);
		group.add(red);
		group.add(blue);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(l);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(black);
		add(red);
		add(blue);

		black.setSelected(true);

		red.addActionListener(new onClickListener());
		blue.addActionListener(new onClickListener());
		black.addActionListener(new onClickListener());
	}

	private class onClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object id = e.getSource();
			if (id == red) {
				tp.setColor(new Color(255, 0, 0));
			} else if (id == blue) {
				tp.setColor(new Color(0, 0, 255));
			} else if (id == black) {
				tp.setColor(new Color(0, 0, 0));
			}
		}

	}
}

class polygonPanel extends JPanel {

	JRadioButton free;
	JRadioButton line;
	JRadioButton rect;
	JRadioButton elip;
	DrawPanel tp;
	Graphics g;
	JLabel l;
	ButtonGroup group;
	ImageIcon line1, line2, circle, rec;
	static String type;

	polygonPanel(DrawPanel tp) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.tp = tp;
		type = "free";

		l = new JLabel("타입");

		line1 = new ImageIcon("./line1.png");
		line2 = new ImageIcon("./line2.png");
		circle = new ImageIcon("./circle.png");
		rec = new ImageIcon("./rect.png");
		free = new JRadioButton(line1);
		line = new JRadioButton(line2);
		rect = new JRadioButton(rec);
		elip = new JRadioButton(circle);
		group = new ButtonGroup();

		group.add(free);
		group.add(line);
		group.add(rect);
		group.add(elip);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(l);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(free);
		add(line);
		add(rect);
		add(elip);

		free.setSelected(true);

		free.addActionListener(new onClickListener());
		line.addActionListener(new onClickListener());
		rect.addActionListener(new onClickListener());
		elip.addActionListener(new onClickListener());
	}

	public static String getType() {
		return type;
	}

	private class onClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object id = e.getSource();

			if (id == free) {
				type = "free";
			} else if (id == line) {
				// pensizePanel.group.clearSelection();
				type = "line";
			} else if (id == rect) {
				// pensizePanel.group.clearSelection();
				System.out.println("BBB");
				type = "rect";
			} else if (id == elip) {
				// pensizePanel.group.clearSelection();
				type = "elip";
			}
		}

	}
}

class pensizePanel extends JPanel {
	JRadioButton lig;
	JRadioButton mid;
	JRadioButton thi;
	DrawPanel tp;
	Graphics g;
	JLabel l;
	public static ButtonGroup group;
	static int size;

	pensizePanel(DrawPanel tp) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.tp = tp;
		size = 2;
		l = new JLabel("굵기");

		lig = new JRadioButton("얇게");
		mid = new JRadioButton("중간");
		thi = new JRadioButton("두껍게");
		group = new ButtonGroup();

		group.add(lig);
		group.add(mid);
		group.add(thi);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(l);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(lig);
		add(mid);
		add(thi);

		mid.setSelected(true);

		lig.addActionListener(new onClickListener());
		mid.addActionListener(new onClickListener());
		thi.addActionListener(new onClickListener());
	}

	public static int getPs() {
		return size;
	}

	private class onClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object id = e.getSource();
			if (id == lig) {
				size = 0;
			} else if (id == mid) {
				size = 2;
			} else if (id == thi) {
				size = 5;
			}
		}

	}
}

class DrawPanel extends Canvas implements MouseListener, MouseMotionListener,
		Runnable { // 메세지를 받기 위해 Runnable interface implements

	int rx, ry, rx2 = -1, ry2;
	int rx_R, ry_R, rx2_R = -1, ry2_R;
	int h, w;
	Color back = Color.white;
	private boolean enable = false;
	private boolean CLEAR = false;
	// Socket s;
	static BufferedReader reader;
	static PrintWriter writer;

	// ObjectOutputStream writer;
	ArrayList<myPoint> pt_F;
	ArrayList<myPoint> pt_L_fir;
	ArrayList<myPoint> pt_L_lst;
	ArrayList<myPoint> pt_R_fir;
	ArrayList<myPoint> pt_R_lst;
	ArrayList<myPoint> pt_E_fir;
	ArrayList<myPoint> pt_E_lst;

	ArrayList<myPoint> pt_F_R;
	ArrayList<myPoint> pt_L_fir_R;
	ArrayList<myPoint> pt_L_lst_R;
	ArrayList<myPoint> pt_R_fir_R;
	ArrayList<myPoint> pt_R_lst_R;
	ArrayList<myPoint> pt_E_fir_R;
	ArrayList<myPoint> pt_E_lst_R;

	Color sel = Color.BLACK;

	DrawPanel() {
		try {

			// writer = new ObjectOutputStream(s.getOutputStream());
		} catch (Exception e) {
		}

		setPreferredSize(new Dimension(650, 500));
		setBackground(Color.white);
		Dimension d = getSize();
		h = d.height;
		w = d.width;

		addMouseListener(this);
		addMouseMotionListener(this);

		pt_F = new ArrayList<myPoint>();
		pt_L_fir = new ArrayList<myPoint>();
		pt_L_lst = new ArrayList<myPoint>();
		pt_R_fir = new ArrayList<myPoint>();
		pt_R_lst = new ArrayList<myPoint>();
		pt_E_fir = new ArrayList<myPoint>();
		pt_E_lst = new ArrayList<myPoint>();

		pt_F_R = new ArrayList<myPoint>();
		pt_L_fir_R = new ArrayList<myPoint>();
		pt_L_lst_R = new ArrayList<myPoint>();
		pt_R_fir_R = new ArrayList<myPoint>();
		pt_R_lst_R = new ArrayList<myPoint>();
		pt_E_fir_R = new ArrayList<myPoint>();
		pt_E_lst_R = new ArrayList<myPoint>();
	}

	public void setColor(Color c) {
		sel = c;
	}

	public static void setWriter(PrintWriter writer, BufferedReader reader) { // 읽고
																				// 쓰기위한
																				// writer,
																				// reader
																				// 설
		DrawPanel.writer = writer;
		DrawPanel.reader = reader;
	}

	public void mousePressed(MouseEvent e) {
		// writer.println("[pressed]");
		// writer.flush();
		if (enable == true) {
			try {
				if (polygonPanel.getType() == "free") {
					rx2 = e.getX();
					ry2 = e.getY();
					myPoint tmp = new myPoint(e.getPoint(), sel);
					pt_F.add(tmp);
					writer.println("[pressedFree]"); // mousePressed 시에 해당 메시지
														// 전송(자유곡선)
					writer.flush();
					writer.println(tmp);
					writer.flush();
				} else {
					rx = e.getX();
					ry = e.getY();
					myPoint tmp = new myPoint(e.getPoint(), sel);

					if (polygonPanel.getType() == "line") { // mousePressed 시에
															// 해당 메시지 전송(직선)
						pt_L_fir.add(tmp);
						writer.println("[pressedLine]");
						writer.flush();
					} else if (polygonPanel.getType() == "rect") { // mousePressed
																	// 시에 해당 메시지
																	// 전송(사각형)
						pt_R_fir.add(tmp);
						writer.println("[pressedRect]");
						writer.flush();
					} else if (polygonPanel.getType() == "elip") { // mousePressed
																	// 시에 해당 메시지
																	// 전송(원)
						pt_E_fir.add(tmp);
						writer.println("[pressedElip]");
						writer.flush();
					}
					writer.println(tmp);
					writer.flush();
				}

			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (enable == true) {
			try {
				if (polygonPanel.getType() == "free") {
					Point tmp = new Point();
					tmp.x = -1;
					tmp.y = -1;
					myPoint tmpP = new myPoint(tmp, sel);
					pt_F.add(tmpP);
					writer.println("[releasedFree]"); // mouseReleased 시에 해당 메시지
														// 전송(자유곡선)
					writer.flush();
					writer.println(tmpP);
					writer.flush();

				} else {
					myPoint tmp = new myPoint(e.getPoint(), sel);
					if (polygonPanel.getType() == "line") {
						pt_L_lst.add(tmp);
						// repaint();
						writer.println("[releasedLine]"); // mouseReleased 시에 해당
															// 메시지 전송(직선)
						writer.flush();
						writer.println(tmp);
						writer.flush();

					} else if (polygonPanel.getType() == "rect") { // mouseReleased
																	// 시에 해당 메시지
																	// 전송(사각형)
						pt_R_lst.add(tmp);
						// repaint();
						writer.println("[releasedRect]");
						writer.flush();
						writer.println(tmp);
						writer.flush();

					} else if (polygonPanel.getType() == "elip") { // mouseReleased
																	// 시에 해당 메시지
																	// 전송(원)
						pt_E_lst.add(tmp);
						// repaint();
						writer.println("[releasedElip]");
						writer.flush();
						writer.println(tmp);
						writer.flush();

					}

				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (enable == true) {
			try {
				if (polygonPanel.getType() == "free") {
					rx = rx2;
					ry = ry2;
					rx2 = e.getX();
					ry2 = e.getY();
					myPoint tmp = new myPoint(e.getPoint(), sel);
					pt_F.add(tmp);
					writer.println("[draggedFree]"); // mouseDragged 시에 해당 메시지
														// 전송(자유곡선)
					writer.flush();
					writer.println(tmp); // myPoint class를 문자열 형태로 전
					writer.flush();
					if (rx >= 0) {
						repaint();
					}

				} else if (polygonPanel.getType() == "line") {
					rx2 = e.getX();
					ry2 = e.getY();
					repaint();
					writer.println("[draggedLine]"); // mouseDragged 시에 해당 메시지
														// 전송(직선)
					writer.flush();
					myPoint tmp = new myPoint(e.getPoint(), sel);
					writer.println(tmp);
					writer.flush();
				} else if (polygonPanel.getType() == "rect") {
					rx2 = e.getX();
					ry2 = e.getY();
					repaint();
					writer.println("[draggedRect]"); // mouseDragged 시에 해당 메시지
														// 전송(사각형)
					writer.flush();
					myPoint tmp = new myPoint(e.getPoint(), sel);
					writer.println(tmp);
					writer.flush();
				} else if (polygonPanel.getType() == "elip") {
					rx2 = e.getX();
					ry2 = e.getY();
					repaint();
					writer.println("[draggedElip]"); // mouseDragged 시에 해당 메시지
														// 전송(원)
					writer.flush();
					myPoint tmp = new myPoint(e.getPoint(), sel);
					writer.println(tmp);
					writer.flush();
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	/*
	 * public void update(Graphics g) { if (CLEAR == false) { paint(g); } else {
	 * g.clearRect(0, 0, getWidth(), getHeight()); CLEAR = false; } }
	 */

	public void paint(Graphics g) {

		if (pt_F.size() != 0) {
			for (int i = 0; i < pt_F.size()-1 ; i++) {
				if (pt_F.get(i).getPoint().x == -1) {
					i++;
				} else {
					if (pt_F.get(i + 1).getPoint().x == -1) {
						i++;
					} else {
						g.setColor(pt_F.get(i).getColor());
						g.drawLine(pt_F.get(i).getPoint().x, pt_F.get(i)
								.getPoint().y, pt_F.get(i + 1).getPoint().x,
								pt_F.get(i + 1).getPoint().y);
					}
				}
			}
		}

		if (pt_L_lst.size() != 0) {
			for (int i = 0; i < pt_L_lst.size() ; i++) {
				g.setColor(pt_L_fir.get(i).getColor());
				g.drawLine(pt_L_fir.get(i).getPoint().x, pt_L_fir.get(i)
						.getPoint().y, pt_L_lst.get(i).getPoint().x, pt_L_lst
						.get(i).getPoint().y);
			}
		}
		if (pt_R_lst.size() != 0) {

			for (int i = 0; i < pt_R_lst.size(); i++) {
				int w = Math.abs(pt_R_lst.get(i).getPoint().x
						- pt_R_fir.get(i).getPoint().x);
				int h = Math.abs(pt_R_lst.get(i).getPoint().y
						- pt_R_fir.get(i).getPoint().y);
				int x_start = (pt_R_lst.get(i).getPoint().x < pt_R_fir.get(i)
						.getPoint().x) ? pt_R_lst.get(i).getPoint().x
						: pt_R_fir.get(i).getPoint().x;
				int y_start = (pt_R_fir.get(i).getPoint().y < pt_R_lst.get(i)
						.getPoint().y) ? pt_R_fir.get(i).getPoint().y
						: pt_R_lst.get(i).getPoint().y;
				g.setColor(pt_R_fir.get(i).getColor());
				g.drawRect(x_start, y_start, w, h);
			}
		}

		if (pt_E_lst.size() != 0) {
			for (int i = 0; i < pt_E_lst.size(); i++) {
				int w = Math.abs(pt_E_lst.get(i).getPoint().x
						- pt_E_fir.get(i).getPoint().x);
				int h = Math.abs(pt_E_lst.get(i).getPoint().y
						- pt_E_fir.get(i).getPoint().y);
				int x_start = (pt_E_lst.get(i).getPoint().x < pt_E_fir.get(i)
						.getPoint().x) ? pt_E_lst.get(i).getPoint().x
						: pt_E_fir.get(i).getPoint().x;
				int y_start = (pt_E_fir.get(i).getPoint().y < pt_E_lst.get(i)
						.getPoint().y) ? pt_E_fir.get(i).getPoint().y
						: pt_E_lst.get(i).getPoint().y;
				g.setColor(pt_E_fir.get(i).getColor());
				g.drawOval(x_start, y_start, w, h);
			}
		}

		g.setColor(sel);

		if (polygonPanel.getType() == "free") {
			g.drawLine(rx, ry, rx2, ry2);
		} else if (polygonPanel.getType() == "line") {
			g.drawLine(rx, ry, rx2, ry2);
		} else if (polygonPanel.getType() == "rect") {
			g.drawRect(rx2 < rx ? rx2 : rx, ry < ry2 ? ry : ry2,
					Math.abs(rx2 - rx), Math.abs(ry2 - ry));
		} else if (polygonPanel.getType() == "elip") {
			g.drawOval(rx2 < rx ? rx2 : rx, ry < ry2 ? ry : ry2,
					Math.abs(rx2 - rx), Math.abs(ry2 - ry));
		}

	}

	public void clear() { // clear 버튼을 위한 메쏘드
		CLEAR = true;
		rx = 0;
		ry = 0;
		rx2 = -1;
		ry2 = 0;
		pt_F.clear();
		pt_L_fir.clear();
		pt_L_lst.clear();
		pt_E_fir.clear();
		pt_E_lst.clear();
		pt_R_fir.clear();
		pt_R_lst.clear();
		repaint();
		Point point = new Point(0, 0);
		Color col = new Color(0, 0, 0);
		writer.println("[CLEAR]");
		writer.flush();
		myPoint tmp = new myPoint(point, col);
		writer.println(tmp);
		writer.flush();
	}

	public void run() { // 메세지를 받는 Thread
		while (true) {
			try {
				String start;
				String type;
				String myp;
				if ((start = reader.readLine()) != null) {
					if (start.equals("[CLEAR]")) { // 마우스 이벤트 전송 - clear_button
						myp = reader.readLine(); // myPoint class 형태로 다시 복원
						String[] tmpp = myp.split("/"); // 좌표와 Color를 분리
						String[] xANDy = tmpp[0].split(","); // 좌표 복원
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(","); // Color복원

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col); // 복원된 좌표와 Color를
															// 이용하여 myPoint생성
						mouseClicked_clear(tmp);
					}
					if (start.equals("[pressedFree]")) { // 마우스 이벤트 전송 -
															// pressed(자유곡선)
						myp = reader.readLine(); // (위와 같은 과정)
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mousePressed_free(tmp);
					} else if (start.equals("[pressedLine]")) { // 마우스 이벤트 전송 -
																// pressed(직선)
						myp = reader.readLine(); // (위와 같은 과정)
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mousePressed_line(tmp);
					} else if (start.equals("[pressedRect]")) { // 마우스 이벤트 전송 -
																// pressed(사각형)
						myp = reader.readLine(); // (위와 같은 과정)
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mousePressed_rect(tmp);
					} else if (start.equals("[pressedElip]")) { // 마우스 이벤트 전송
																// -pressed(원)
						System.out.println("[pressedElip]"); // (위와 같은 과정)
						myp = reader.readLine();
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mousePressed_elip(tmp);
					} else if (start.equals("[releasedFree]")) { // 마우스 이벤트 전송 -
																	// released(자유곡선)
						myp = reader.readLine(); // (위와 같은 과정)
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mouseReleased_free(tmp);
					} else if (start.equals("[releasedLine]")) { // 마우스 이벤트 전송 -
																	// released(직선)
						myp = reader.readLine(); // (위와 같은 과정)
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mouseReleased_line(tmp);
					} else if (start.equals("[releasedRect]")) { // 마우스 이벤트 전송 -
																	// released(사각형)
						myp = reader.readLine(); // (위와 같은 과정)
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mouseReleased_rect(tmp);
					} else if (start.equals("[releasedElip]")) { // 마우스 이벤트 전송 -
																	// released(원)
						System.out.println("[releasedElip]"); // (위와 같은 과정)
						myp = reader.readLine();
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mouseReleased_elip(tmp);
					} else if (start.equals("[draggedFree]")) { // 마우스 이벤트 전송 -
																// dragged(자유곡선)
						myp = reader.readLine(); // (위와 같은 과정)
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mouseDragged_free(tmp);
					} else if (start.equals("[draggedLine]")) { // 마우스 이벤트 전송 -
																// dragged(직선)
						myp = reader.readLine(); // (위와 같은 과정)
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mouseDragged_line(tmp);
					} else if (start.equals("[draggedRect]")) { // 마우스 이벤트 전송 -
																// dragged(사각형)
						myp = reader.readLine(); // (위와 같은 과정)
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						mouseDragged_rect(tmp);
					} else if (start.equals("[draggedElip]")) { // 마우스 이벤트 전송 -
																// dragged(원)
						System.out.println("[draggedElip]"); // (위와 같은 과정)
						myp = reader.readLine();
						String[] tmpp = myp.split("/");
						String[] xANDy = tmpp[0].split(",");
						Point pt = new Point();
						pt.x = Integer.parseInt(xANDy[0]);
						pt.y = Integer.parseInt(xANDy[1]);
						String cols = tmpp[1].substring(15,
								tmpp[1].length() - 1);
						String[] RGBs = cols.split(",");

						int r = Integer.parseInt(RGBs[0].substring(2,
								RGBs[0].length()));
						int g = Integer.parseInt(RGBs[1].substring(2,
								RGBs[1].length()));
						int b = Integer.parseInt(RGBs[2].substring(2,
								RGBs[2].length()));

						Color col = new Color(r, g, b);
						// System.out.println(col);
						myPoint tmp = new myPoint(pt, col);
						System.out.println(tmp);
						mouseDragged_elip(tmp);
					} else if (start.equals("[startF]")) { // 먼저 시작하는 사람이 받는 메시지
						BTN.ans_enter.setEnabled(false);
						enable = true; // 그림그리기 enable
						BTN.rdy.setEnabled(false);
						BTN.b.setEnabled(true);
						JOptionPane.showMessageDialog(null, "당신차례입니다");

						pt_F.clear(); // 저장된 좌표 초기화
						pt_L_fir.clear();
						pt_L_lst.clear();
						pt_R_fir.clear();
						pt_R_lst.clear();
						pt_E_fir.clear();
						pt_E_lst.clear();

						pt_F_R.clear();
						pt_L_fir_R.clear();
						pt_L_lst_R.clear();
						pt_R_fir_R.clear();
						pt_R_lst_R.clear();
						pt_E_fir_R.clear();
						pt_E_lst_R.clear();
						repaint();

					} else if (start.equals("[startL]")) { // 나중에 시작하는 사람이 받는
															// 메시지
						BTN.ans_enter.setEnabled(true);
						enable = false;
						BTN.exam.setText("");
						BTN.rdy.setEnabled(false);
						BTN.b.setEnabled(false);

						pt_F.clear(); // 좌표 초기화
						pt_L_fir.clear();
						pt_L_lst.clear();
						pt_R_fir.clear();
						pt_R_lst.clear();
						pt_E_fir.clear();
						pt_E_lst.clear();

						pt_F_R.clear();
						pt_L_fir_R.clear();
						pt_L_lst_R.clear();
						pt_R_fir_R.clear();
						pt_R_lst_R.clear();
						pt_E_fir_R.clear();
						pt_E_lst_R.clear();

						rx = -1; // 수동으로 좌표 초기화
						ry = -1;
						rx2 = -1;
						ry2 = -1;
						rx_R = -1;
						ry_R = -1;
						rx2_R = -1;
						ry2_R = -1;

						repaint();

					} else if (start.equals("[exam]")) { // 문제 전달 시 받는 메시지
						String tmp = reader.readLine();
						BTN.exam.setText("문제 : " + tmp);
					} else if (start.equals("[correct]")) { // 정답 맞췄을 시 받는 메시지
						JOptionPane.showMessageDialog(null, "정답입니다");
					} else if (start.equals("[started]")) { // 게임 시작 메시지
						JOptionPane.showMessageDialog(null, "게임 시작");
					} else if (start.equals("[userEntered]")) { // 새로운 유저 접속 시
																// 받는 메시지
						LoginPanel.connect.setEnabled(false);
						BTN.exam.setText("");
						BTN.rdy.setEnabled(true);
						LeftPanel.btn.setEnabled(true);
						enable = false;
						UsersPanel.users.setText("<html>접속자<br></html>"); // 유저판
																			// 초기화
					} else if (start.equals("[users]")) { // [userEntered] 메시지
															// 이후에 받는 메시지
						String[] tmp = UsersPanel.users.getText().split( // 초기화
																			// 된
																			// 유저판에
																			// 새롭게
																			// 유저목록
																			// 작성
								"<html>");
						String[] users = tmp[1].split("</html>");
						UsersPanel.users.setText("<html>" + users[0]
								+ reader.readLine() + "<br></html>");
					} else if (start.equals("[chat]")) { // 채팅 받을 때 받는 메시지
						String tmp = LeftPanel.ta.getText(); // 기존 내용 + 받은 내용
																// set
						tmp += reader.readLine() + "\n";
						LeftPanel.ta.setText(tmp);
						LeftPanel.ta.setCaretPosition(LeftPanel.ta
								.getDocument().getLength()); // 커서를 맨 마지막 위치로
					} else if (start.equals("[full]")) { // 서버가 꽉 찬 경우 받는 메시지
						JOptionPane.showMessageDialog(null,
								"빈 자리가 없습니다.\n나중에 다시 접속해 주세요.");
						LoginPanel.connect.setEnabled(false);
						BTN.rdy.setEnabled(false);
						LeftPanel.btn.setEnabled(false);
					} else if (start.equals("[finish]")) { // 게임 종료 시 받는 메시지
						JOptionPane.showMessageDialog(null, "게임 끝.");
						LoginPanel.connect.setEnabled(false); // 초기상태로 되돌림
						BTN.rdy.setEnabled(true);
						LeftPanel.btn.setEnabled(true);
						BTN.b.setEnabled(false);
						BTN.exam.setText("");
						enable = false;

					}

				}
			} catch (Exception e) {
			}
		}
	}

	/******************* 마우스 이벤트를 수신하고 그림판을 따라 그리기 위한 메쏘드들 ******************************/
	public void mousePressed_free(myPoint mp) {
		rx2_R = mp.getPoint().x;
		ry2_R = mp.getPoint().y;
		myPoint tmp = mp;
		pt_F_R.add(tmp);
	}

	public void mousePressed_line(myPoint mp) {
		rx_R = mp.getPoint().x;
		ry_R = mp.getPoint().y;
		myPoint tmp = mp;
		pt_L_fir_R.add(tmp);
	}

	public void mousePressed_rect(myPoint mp) {
		rx_R = mp.getPoint().x;
		ry_R = mp.getPoint().y;
		myPoint tmp = mp;
		pt_R_fir_R.add(tmp);
	}

	public void mousePressed_elip(myPoint mp) {
		rx_R = mp.getPoint().x;
		ry_R = mp.getPoint().y;
		myPoint tmp = mp;
		pt_E_fir_R.add(tmp);
	}

	public void mouseReleased_free(myPoint mp) {
		Point tmp = new Point();
		tmp.x = -1;
		tmp.y = -1;
		myPoint tmpP = new myPoint(tmp, mp.getColor());
		pt_F_R.add(tmpP);
		paintT(mp);
	}

	public void mouseReleased_line(myPoint mp) {
		myPoint tmp = mp;
		pt_L_lst_R.add(tmp);
		paintT(mp);
	}

	public void mouseReleased_rect(myPoint mp) {
		myPoint tmp = mp;
		pt_R_lst_R.add(tmp);
		paintT(mp);
	}

	public void mouseReleased_elip(myPoint mp) {
		System.out.println("mouseReleased_elip");
		myPoint tmp = mp;
		pt_E_lst_R.add(tmp);
		paintT(mp);
	}

	public void mouseDragged_free(myPoint mp) {
		rx_R = rx2_R;
		ry_R = ry2_R;
		rx2_R = mp.getPoint().x;
		ry2_R = mp.getPoint().y;
		myPoint tmp = mp;
		pt_F_R.add(tmp);
		if (rx_R >= 0) {
			paintT(mp);
			paintN_free(mp);
		}
	}

	public void mouseDragged_line(myPoint mp) {
		rx2_R = mp.getPoint().x;
		ry2_R = mp.getPoint().y;
		paintT(mp);
		repaint();
		paintN_line(mp);
	}

	public void mouseDragged_rect(myPoint mp) {
		rx2_R = mp.getPoint().x;
		ry2_R = mp.getPoint().y;
		paintT(mp);
		repaint();
		paintN_rect(mp);
	}

	public void mouseDragged_elip(myPoint mp) {
		System.out.println("mouseDragged_elip");
		rx2_R = mp.getPoint().x;
		ry2_R = mp.getPoint().y;
		paintT(mp);
		repaint();
		paintN_elip(mp);
	}

	public void mouseClicked_clear(myPoint mp) { // Clicked_clear 시에 모든 좌표 및
		System.out.println("mouseClicked_clear"); // ArrayList<myPoint> 모두 초기
		rx_R = mp.getPoint().x;
		System.out.println(rx_R);
		ry_R = mp.getPoint().y;
		rx2_R = -1;
		ry2_R = 0;
		pt_F_R.clear();
		pt_L_fir_R.clear();
		pt_L_lst_R.clear();
		pt_E_fir_R.clear();
		pt_E_lst_R.clear();
		pt_R_fir_R.clear();
		pt_R_lst_R.clear();
		repaint();
		CLEAR = false;
	}

	public void paintT(myPoint mp) {
		Graphics g = getGraphics();

		if (pt_F_R.size() != 0) {
			for (int i = 0; i < pt_F_R.size() - 1; i++) {
				if (pt_F_R.get(i).getPoint().x == -1) {
					i++;
				} else {
					if (pt_F_R.get(i + 1).getPoint().x == -1) {
						i++;
					} else {
						g.setColor(pt_F_R.get(i).getColor());
						g.drawLine(pt_F_R.get(i).getPoint().x, pt_F_R.get(i)
								.getPoint().y, pt_F_R.get(i + 1).getPoint().x,
								pt_F_R.get(i + 1).getPoint().y);
					}
				}
			}
		}

		if (pt_L_lst_R.size() != 0) {
			for (int i = 0; i < pt_L_lst_R.size(); i++) {
				g.setColor(pt_L_fir_R.get(i).getColor());
				g.drawLine(pt_L_fir_R.get(i).getPoint().x, pt_L_fir_R.get(i)
						.getPoint().y, pt_L_lst_R.get(i).getPoint().x,
						pt_L_lst_R.get(i).getPoint().y);
			}
		}
		if (pt_R_lst_R.size() != 0) {
			for (int i = 0; i < pt_R_lst_R.size(); i++) {
				int w = Math.abs(pt_R_lst_R.get(i).getPoint().x
						- pt_R_fir_R.get(i).getPoint().x);
				int h = Math.abs(pt_R_lst_R.get(i).getPoint().y
						- pt_R_fir_R.get(i).getPoint().y);
				int x_start = (pt_R_lst_R.get(i).getPoint().x < pt_R_fir_R.get(
						i).getPoint().x) ? pt_R_lst_R.get(i).getPoint().x
						: pt_R_fir_R.get(i).getPoint().x;
				int y_start = (pt_R_fir_R.get(i).getPoint().y < pt_R_lst_R.get(
						i).getPoint().y) ? pt_R_fir_R.get(i).getPoint().y
						: pt_R_lst_R.get(i).getPoint().y;
				g.setColor(pt_R_fir_R.get(i).getColor());
				g.drawRect(x_start, y_start, w, h);
			}
		}

		if (pt_E_lst_R.size() != 0) {
			for (int i = 0; i < pt_E_lst_R.size(); i++) {
				int w = Math.abs(pt_E_lst_R.get(i).getPoint().x
						- pt_E_fir_R.get(i).getPoint().x);
				int h = Math.abs(pt_E_lst_R.get(i).getPoint().y
						- pt_E_fir_R.get(i).getPoint().y);
				int x_start = (pt_E_lst_R.get(i).getPoint().x < pt_E_fir_R.get(
						i).getPoint().x) ? pt_E_lst_R.get(i).getPoint().x
						: pt_E_fir_R.get(i).getPoint().x;
				int y_start = (pt_E_fir_R.get(i).getPoint().y < pt_E_lst_R.get(
						i).getPoint().y) ? pt_E_fir_R.get(i).getPoint().y
						: pt_E_lst_R.get(i).getPoint().y;
				g.setColor(pt_E_fir_R.get(i).getColor());
				g.drawOval(x_start, y_start, w, h);
			}
		}

	}

	public void paintN_free(myPoint mp) {

		Graphics g = getGraphics();
		g.setColor(mp.getColor());

		g.drawLine(rx_R, ry_R, rx2_R, ry2_R);
	}

	public void paintN_line(myPoint mp) {
		Graphics g = getGraphics();
		g.setColor(mp.getColor());

		g.drawLine(rx_R, ry_R, rx2_R, ry2_R);
	}

	public void paintN_rect(myPoint mp) {
		Graphics g = getGraphics();
		g.setColor(mp.getColor());

		g.drawRect((rx2_R < rx_R) ? rx2_R : rx_R,
				(ry_R < ry2_R) ? ry_R : ry2_R, Math.abs(rx_R - rx2_R),
				Math.abs(ry_R - ry2_R));
	}

	public void paintN_elip(myPoint mp) {
		System.out.println("paintN_elip");
		Graphics g = getGraphics();

		g.setColor(mp.getColor());

		g.drawOval((rx2_R < rx_R) ? rx2_R : rx_R,
				(ry_R < ry2_R) ? ry_R : ry2_R, Math.abs(rx_R - rx2_R),
				Math.abs(ry_R - ry2_R));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/******************************************************************************/

}

class myPoint { // 그리고 있는 좌표, 색을 상대방에게 전달하기 위한 클래
	Point pt;
	Color col;

	myPoint(Point pt, Color col) {
		this.pt = pt;
		this.col = col;
	}

	public Point getPoint() {
		return pt;
	}

	public Color getColor() {
		return col;
	}

	public String toString() {
		return pt.x + "," + pt.y + "/" + col; // x,y/color 포맷으로 전송
	}
}