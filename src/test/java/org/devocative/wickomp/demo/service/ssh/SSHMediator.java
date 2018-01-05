package org.devocative.wickomp.demo.service.ssh;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;

public class SSHMediator {
	static final String SUDO_PROMPT = "[sudo] password for";

	static JSch jsch = new JSch();
	static Session session;
	static ChannelShell channel;

	static IAsyncResult asyncResult;
	static PrintStream commander;
	static InputStream in;
	static boolean isSudoPasswordMode = true;
	static ShellProcessor processor;

	// ------------------------------

	public static void init(String username, String host, String password, final IAsyncResult asyncResult, int cols, int rows, int width, int height) {
		SSHMediator.asyncResult = asyncResult;

		try {
			session = jsch.getSession(username, host, 22);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(30000);

			channel = (ChannelShell) session.openChannel("shell");
			channel.setPtyType("xterm");
			channel.setPtySize(cols, rows, width, height);
			processor = new ShellProcessor();

			in = channel.getInputStream();
			OutputStream out = channel.getOutputStream();

			channel.connect();
			commander = new PrintStream(out, true);

			Thread th = new Thread() {
				@Override
				public void run() {
					System.out.println("THREAD!");
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(in));

						while (true) {
							System.out.println("while (true)");
							char[] buff = new char[1024];
							int read;
							while ((read = br.read(buff)) != -1) {
								String line = new String(buff, 0, read);
								processor.onServerText(line);
								asyncResult.onMessage(line);

								if (line.startsWith(SUDO_PROMPT) && isSudoPasswordMode) {
									System.out.println("SUDO");
									send("qweasd@123");
									send(13);
								}
								//Thread.sleep(100);
							}

							if (channel.isClosed()) {
								System.out.println("channel closed");
								session.disconnect();
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			th.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void resize(int cols, int rows, int width, int height) {
		channel.setPtySize(cols, rows, width, height);
	}

	public static void send(String txt) {
		isSudoPasswordMode = !txt.startsWith(SUDO_PROMPT);

		try {
			processor.onClientText(txt);
			commander.print(txt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void send(int webSpecialKey) {
		try {
			processor.onClientSpecialKey(webSpecialKey);
			byte[] shellCode = EShellSpecialKey.findShellCode(webSpecialKey);
			if (shellCode != null) {
				commander.write(shellCode);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void close() {
		session.disconnect();
	}
}
