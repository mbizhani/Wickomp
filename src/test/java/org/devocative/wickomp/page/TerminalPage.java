package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.html.WAjaxLink;
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
				System.out.println("TerminalPage.onConnect");
				SSHMediator.init("ares", "192.168.40.131", "qweasd@123", TerminalPage.this);
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
		wTerminal.setVisible(false);
		wTerminal.setOutputMarkupId(true);
		wTerminal.setOutputMarkupPlaceholderTag(true);
		add(wTerminal);

		add(new WAjaxLink("createTerminal", new Model<>("Terminal")) {
			private static final long serialVersionUID = -5885155363799887584L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				wTerminal.setVisible(true);
				target.add(wTerminal);
			}
		});
	}

	@Override
	public void onMessage(String text) {
		wTerminal.push(text);
	}
}
