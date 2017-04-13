package org.devocative.wickomp.page;

import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.html.WTerminal;
import org.devocative.wickomp.ssh.IAsyncResult;
import org.devocative.wickomp.ssh.SSHMediator;

public class TerminalPage extends BasePage implements IAsyncResult {
	private static final long serialVersionUID = -5989144748063188666L;

	private WTerminal wTerminal;

	public TerminalPage() {

		wTerminal = new WTerminal("xterm") {
			private static final long serialVersionUID = 3958105834715956085L;

			@Override
			protected void onConnect() {
				SSHMediator.init("root", "172.16.1.243", "qazwsx@123", TerminalPage.this);
			}

			@Override
			protected void onMessage(String key, Integer specialKey) {
				if (key != null) {
					SSHMediator.send(key);
				} else if (specialKey != null) {
					SSHMediator.send(specialKey);
				}
			}

			@Override
			protected void onClose() {
				SSHMediator.close();
			}
		};

		add(wTerminal);
	}

	@Override
	public void onMessage(String text) {
		wTerminal.push(text);
	}
}
