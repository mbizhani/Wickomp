package org.devocative.wickomp.demo.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.devocative.wickomp.demo.BasePage;
import org.devocative.wickomp.form.WTextInput;
import org.devocative.wickomp.form.wizard.OWizard;
import org.devocative.wickomp.form.wizard.WWizardPanel;
import org.devocative.wickomp.form.wizard.WWizardStepPanel;
import org.devocative.wickomp.html.WMessager;
import org.devocative.wickomp.wrcs.EasyUIBehavior;

import java.io.Serializable;
import java.util.List;

public class WizardPage extends BasePage {
	private boolean skipThird = false;

	public WizardPage() {
		final OWizard oWizard = new OWizard();
		oWizard
			.addStep("A", new Step(1))
			.addStep("B", new Step(2))
			.addStep("C", new Step(3))
			.addStep("D", new Step(4))
			.addStep("E", new Step(5))
		;

		Form form = new Form("form");
		form.add(new CheckBox("skipThird", new PropertyModel<Boolean>(this, "skipThird")));
		add(form);

		form.add(new WWizardPanel("wizard", oWizard, WWizardPanel.ButtonBarPlace.TOP) {

				@Override
				protected void onPrevious(AjaxRequestTarget target, String stepId) {
					System.out.println("prev = " + stepId);
				}

				@Override
				protected void onNext(AjaxRequestTarget target, String stepId) {
					System.out.println("current = " + stepId);

					if ("A".equals(stepId)) {
						setTitle("Gandalf, The Grey");
					} else if ("B".equals(stepId)) {
						setTitle("Gandalf, The White");
					}
					if (skipThird) {
						if (stepId.equals("B")) {
							setStep("D");
						}
					} else {
						clearSkippedSteps();
					}
				}

				@Override
				protected void onFinish(AjaxRequestTarget target, String stepId) {
					target.appendJavaScript(String.format("alert('Finished: %s');", stepId));
				}

				@Override
				protected void onError(AjaxRequestTarget target, String stepId, List<Serializable> errors) {
					WMessager.show("Error", errors, target);
				}

				@Override
				protected void onCancel(AjaxRequestTarget target, String stepId) {
					WMessager.show("Msg", "Canceled: step = " + stepId, target);
				}
			}
				.setTitle("Gandalf, The Grey")
				.setCancelButtonVisible(true)
				.setCancelConfirmationMessage(new Model<>("Cancel?"))
				.setFinishConfirmationMessage(new Model<>("Finish?"))
		);

		add(new EasyUIBehavior());
	}

	private class Step extends WWizardStepPanel {
		private int i;
		private WTextInput txt;
		private boolean warnOn4 = false;

		public Step(int i) {
			this.i = i;
		}

		@Override
		protected void onInit() {
			add(new Label("lbl", String.valueOf(i)));
			add(txt = new WTextInput("txt", new Model<String>()));
			txt.setRequired(i == 3).setLabel(new Model<>("TXT " + i));
		}

		@Override
		public boolean onStepSubmit(AjaxRequestTarget target) {
			System.out.printf("onStepSubmit(%s): txt = %s\n", i, txt.getModelObject());
			if (i == 4 && !warnOn4) {
				warnOn4 = true;
				WMessager.show("Warn", "Test warn", target);
				return false;
			}

			return true;
		}
	}

}
