package org.devocative.wickomp.demo.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.demo.BasePage;
import org.devocative.wickomp.demo.service.ssh.IAsyncResult;
import org.devocative.wickomp.demo.service.ssh.SSHMediator;
import org.devocative.wickomp.demo.vo.TerminalConnInfo;
import org.devocative.wickomp.form.WAjaxButton;
import org.devocative.wickomp.form.WNumberInput;
import org.devocative.wickomp.form.WTextInput;
import org.devocative.wickomp.html.WTerminal;

import java.io.Serializable;

public class TerminalPage extends BasePage implements IAsyncResult {
	private static final long serialVersionUID = -5989144748063188666L;

	private WTerminal wTerminal;
	private TerminalConnInfo info = new TerminalConnInfo();

	public TerminalPage() {
		Form<TerminalConnInfo> form = new Form<>("form", new CompoundPropertyModel<>(info));
		form.add(new WTextInput("address"));
		form.add(new WNumberInput("port", Integer.class));
		form.add(new WTextInput("username"));
		form.add(new WTextInput("password"));
		form.add(new WAjaxButton("createTerminal", new Model<>("Connect")) {
			private static final long serialVersionUID = 4420737213111016164L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				wTerminal.setVisible(true);
				target.add(wTerminal);
			}
		});
		add(form);

		wTerminal = new WTerminal("xterm") {
			private static final long serialVersionUID = 3958105834715956085L;

			@Override
			protected void onConnect(int cols, int rows, int width, int height) {
				System.out.println("TerminalPage.onConnect");
				SSHMediator.init(info.getAddress(), info.getPort(), info.getUsername(), info.getPassword(),
					TerminalPage.this, cols, rows, width, height);
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
	}

	@Override
	public void onMessage(String text) {
		wTerminal.push(text);
	}

	private static class ConnectionInfo implements Serializable {

		private static final long serialVersionUID = -7556096549774022927L;
	}
}
