package org.devocative.wickomp.ssh;

public class ShellProcessor {
	private boolean doStart = false, clientSent = false, isPrompt = true;
	private StringBuilder cmdBuilder = new StringBuilder();
	private int lastSpecialKey, cursor = 0;

	// ------------------------------

	public void onClientText(String msg) {
		doStart = true;
		clientSent = true;
	}

	public void onClientSpecialKey(int specialKey) {
		doStart = true;
		lastSpecialKey = specialKey;
	}

	public synchronized void onServerText(String msg) {
		//System.out.printf("P[%s] SERVER: %s\n", isPrompt, msg);

		if (doStart) {
			if (isPrompt) {
				EShellSpecialKey lastOne = EShellSpecialKey.findShellSpecialKey(lastSpecialKey);
				switch (lastOne) {
					case UP:
					case DOWN:
						msg = msg.trim();
						//System.out.println("U/D: " + msg);
						cmdBuilder = new StringBuilder(msg);
						cursor = msg.length();
						break;

					case ENTER:
					case CTR_D:
						String cmd = lastOne != EShellSpecialKey.CTR_D ? cmdBuilder.toString() : "logout";
						System.out.println("### CMD > " + cmd);
						cmdBuilder = new StringBuilder();
						checkPrompt(msg);
						cursor = 0;
						break;

					case TAB:
						if (!msg.startsWith("\n") && !msg.startsWith("\r") && msg.trim().length() > 0) {
							cmdBuilder.append(msg);
						}
						break;

					case BACKSPACE:
						if (cursor > 0) {
							cmdBuilder.deleteCharAt(cursor - 1);
						}
						cursor--;
						break;

					case LEFT:
						if (cursor > 0) {
							cursor--;
						}
						break;

					case RIGHT:
						if (cursor < cmdBuilder.length()) {
							cursor++;
						}
						break;

					case HOME:
						cursor = 0;
						break;

					case END:
						cursor = cmdBuilder.length();
						break;

					case CTR_U:
						cmdBuilder = new StringBuilder();
						cursor = 0;
						break;

					default:
						if (clientSent) {
							cmdBuilder.insert(cursor, msg);
							cursor += msg.length();
						}
				}
			} else {
				checkPrompt(msg);
			}
		}

		lastSpecialKey = -1;
		clientSent = false;
	}

	private void checkPrompt(String msg) {
		String trim = msg.trim();
		isPrompt = trim.endsWith("$") || trim.endsWith("#");
	}
}
