package termP_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class termP_server {

	public static void main(String[] args) throws IOException {

		final int MATH_PORT = 3333;
		ServerSocket listen;

		listen = new ServerSocket(MATH_PORT);

		while (true) {
			System.out.println(Users.size());
			Socket cli = listen.accept();

			if (Users.size() < 2) {
				clientThread ct = new clientThread(cli);
				Users.addUser(ct);
				ct.start();
			} else {		// 서버가 꽉 찼을 때
				PrintWriter writer = new PrintWriter(cli.getOutputStream(),
						true);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(cli.getInputStream()));
				writer.println("[full]");	// [full] 메시지를 클라이언트에 전송
				writer.flush();
			}
		}
	}
}

class Users {		// Users 구조체 (접속해 있는 클라이언트를 ArrayList로 관리)
	static ArrayList<clientThread> users = new ArrayList<clientThread>();

	static void addUser(clientThread ct) {
		users.add(ct);
	}

	static ArrayList<clientThread> getUsers() {
		return users;
	}

	static int size() {
		return users.size();
	}
}

class clientThread extends Thread {		// 클라이언트 쓰레드

	Socket s;
	BufferedReader reader;
	PrintWriter writer;
	ArrayList<clientThread> users;
	boolean rdy;
	String id;
	String[] exam = { "apple", "pear", "grape", "berry" };	// 문제
	static int i = 0;
	static int quser = 0;
	static int anser = 1;

	clientThread(Socket s) {
		this.s = s;
		rdy = false;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public PrintWriter getWriter() {
		return writer;
	}
	
	public void setRdy(boolean rdy) {
		this.rdy = rdy;
	}

	public boolean getRdy() {
		return rdy;
	}

	public String getID() {
		return id;
	}

	public void run() {
		try {
			writer = new PrintWriter(s.getOutputStream(), true);	// writer, reader연결
			reader = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			System.out.println("Accepted : " + s.getInetAddress());
			String tmp;

			while (true) {
				if ((tmp = reader.readLine()) != null) {
					// tmp = reader.readLine();
					System.out.println(tmp);
					users = Users.getUsers();
					if (tmp.equals("[connected]")) {	// 유저가 연결된 경우 유저에게서 받는 메시지
						id = reader.readLine();
						for (int i = 0; i < users.size(); i++) {
							users.get(i).getWriter().println("[userEntered]");
							users.get(i).setRdy(false);
							for (int j = 0; j < users.size(); j++) {
								users.get(i).getWriter().println("[users]");	// 지금 접속해있는 유저들의 아이디를 모두 전송
								System.out.println(users.get(j).getID());
								users.get(i).getWriter()
										.println(users.get(j).getID());
							}
						}
					} else if (tmp.equals("[ready]")) {		// 유저가 ready버튼을 누른 경우
						this.rdy = true;
						System.out.println(rdy);
						if (users.size() == 2) {
							if (users.get(0).getRdy() && users.get(1).getRdy()) {	// 모두 ready한 경우
								users.get(quser).getWriter()
										.println("[started]");
								users.get(quser).getWriter().flush();
								users.get(anser).getWriter()
										.println("[started]");
								users.get(anser).getWriter().flush();

								users.get(quser).getWriter()
										.println("[startF]");	// quser(먼저 접속한 사람) 에게 먼저 문제를 내도록 메시지 전송
								users.get(quser).getWriter().flush();
								users.get(anser).getWriter()	// anser(나중에 접속한 사람) 은 문제를 풀도록 메시지 전송
										.println("[startL]");
								users.get(anser).getWriter().flush();
								users.get(quser).getWriter().println("[exam]");		// quser에게 문제 전송
								users.get(quser).getWriter().flush();
								users.get(quser).getWriter().println(exam[i]);
								users.get(quser).getWriter().flush();
							}
						}
					} else if (tmp.equals("[answer]")) {	//  anser가 정답 확인 버튼을 누르는 경우 받는 메시지
						String ans = reader.readLine();
						if (ans.equals(exam[i])) {		// 정답을 맞춘경우 다음 문제로 넘어감
							i++;
							users.get(anser).getWriter().println("[correct]");
							users.get(anser).getWriter().flush();
							if (quser == 1) {	// quser와 anser를 바꿈
								quser = 0;
							} else if (quser == 0) {
								quser = 1;
							}
							if (anser == 1) {
								anser = 0;
							} else if (anser == 0) {
								anser = 1;
							}
							if (i > 3) {	// 모든 문제를 푼 경우
								for (int i = 0; i < users.size(); i++) {
									users.get(i).getWriter()
											.println("[finish]");	// 게임을 종료시키기 위해 [finish] 메시지를 클라이언트들에게 전송
									users.get(i).getWriter().flush();
									users.get(i).setRdy(false);		// ready 상태 초기화
								}
								i = 0;
							} else {
								users.get(quser).getWriter()
										.println("[startF]");	// quser에게 문제를 내도록 메시지 전송
								users.get(quser).getWriter().flush();
								users.get(anser).getWriter()
										.println("[startL]");	// anser에게 문제를 풀도록 메시지 전송
								users.get(anser).getWriter().flush();

								users.get(quser).getWriter().println("[exam]");
								users.get(quser).getWriter().flush();

								users.get(quser).getWriter().println(exam[i]);
								users.get(quser).getWriter().flush();
							}

						}
					} else if (tmp.equals("[chat]")) {		// 유저가 채팅을 한 경우 받는 메시지
						String chat = reader.readLine();	// 채팅 내용을 받아서
						for (int i = 0; i < users.size(); i++) {
							users.get(i).getWriter().println("[chat]");		// [chat] 메시지를 전송 한 후 
							users.get(i).getWriter().flush();
							users.get(i).getWriter()
									.println(id + "  :     " + chat);	// 채팅 내용을 모든 유저에게 전송
							users.get(i).getWriter().flush();
						}
					} else if (tmp.equals("[exit]")) {		// 유저가 나간 경우
						String name = reader.readLine();
						for (int i = 0; i < users.size(); i++) {
							if (users.get(i).getID().equals(name)) {
								users.remove(i);		// 나간 유저를 User class(ArrayList 구조체)에서 제거
							}
						}

						for (int i = 0; i < users.size(); i++) {
							users.get(i).setRdy(false);
							System.out.println(users.get(i).getID()+": userName\n"+users.get(i).getRdy());
							users.get(i).getWriter().println("[userEntered]");	// 접속해 있는 유저에게 유저 목록 대전 송
							for (int j = 0; j < users.size(); j++) {
								users.get(i).getWriter().println("[users]");
								System.out.println(users.get(j).getID());
								users.get(i).getWriter()
										.println(users.get(j).getID());
							}
						}
					} else {
						for (int i = 0; i < users.size(); i++) {
							users.get(i).getWriter().println(tmp);	// 클라이언트가 보내는 그림판에 대한 메시지들을 다른 클라이언트에게 재 전송
							users.get(i).getWriter().flush();
						}
					}
				}
			}

		} catch (Exception e) {
		}

	}

}