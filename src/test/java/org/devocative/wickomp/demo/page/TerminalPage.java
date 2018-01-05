package org.devocative.wickomp.demo.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.demo.BasePage;
import org.devocative.wickomp.demo.service.ssh.IAsyncResult;
import org.devocative.wickomp.demo.service.ssh.SSHMediator;
import org.devocative.wickomp.html.WAjaxLink;
import org.devocative.wickomp.html.WTerminal;

public class TerminalPage extends BasePage implements IAsyncResult {
	private static final long serialVersionUID = -5989144748063188666L;

	private WTerminal wTerminal;

	public TerminalPage() {

		wTerminal = new WTerminal("xterm") {
			private static final long serialVersionUID = 3958105834715956085L;

			@Override
			protected void onConnect(int cols, int rows, int width, int height) {
				System.out.println("TerminalPage.onConnect");
				SSHMediator.init("root", "192.168.40.131", "qazWSX@123", TerminalPage.this, cols, rows, width, height);
			}

			@Override
			protected void onResize(int cols, int rows, int width, int height) {
				SSHMediator.resize(cols, rows, width, height);
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
