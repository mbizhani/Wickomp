package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.devocative.wickomp.BasePage;
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
		});

		add(new EasyUIBehavior());
	}

	private class Step extends WWizardStepPanel {
		private int i;

		public Step(int i) {
			super();
			this.i = i;
		}

		@Override
		protected void onInit() {
			add(new Label("lbl", String.valueOf(i)));
			add(new WTextInput("txt", new Model<String>()).setRequired(i == 3).setLabel(new Model<>("TXT " + i)));
		}
	}

}
