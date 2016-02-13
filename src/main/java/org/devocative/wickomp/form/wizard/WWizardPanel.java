package org.devocative.wickomp.form.wizard;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.form.WAjaxButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WWizardPanel extends WPanel {
	public enum ButtonBarPlace {TOP, BOTTOM}

	private OWizard oWizard;
	private WebMarkupContainer buttonBar, content;

	public WWizardPanel(String id, OWizard oWizard) {
		this(id, oWizard, ButtonBarPlace.BOTTOM);
	}

	// Main Constructor
	public WWizardPanel(String id, OWizard oWizard, ButtonBarPlace place) {
		super(id);
		this.oWizard = oWizard;

		WebMarkupContainer top = new WebMarkupContainer("top");
		add(top);

		WebMarkupContainer bottom = new WebMarkupContainer("bottom");
		add(bottom);

		switch (place) {
			case TOP:
				top.add(new WizardButtonBar("topButtonBar", "buttonBarFragment", this));
				bottom.add(new WebMarkupContainer("bottomButtonBar"));
				buttonBar = top;
				break;

			case BOTTOM:
				top.add(new WebMarkupContainer("topButtonBar"));
				bottom.add(new WizardButtonBar("bottomButtonBar", "buttonBarFragment", this));
				buttonBar = bottom;
				break;
		}
		buttonBar.setOutputMarkupId(true);

		content = new WebMarkupContainer("content");
		content.setOutputMarkupId(true);
		content.add(oWizard.getFirstStep());
		add(content);
	}

	protected void clearSkippedSteps() {
		oWizard.clearSkippedSteps();
	}

	protected void setStep(String stepId) {
		oWizard.setStep(stepId);
	}

	protected void onPrevious(AjaxRequestTarget target, String stepId) {
	}

	protected void onNext(AjaxRequestTarget target, String stepId) {
	}

	protected void onFinish(AjaxRequestTarget target, String stepId) {
	}

	protected void onError(AjaxRequestTarget target, String stepId, List<Serializable> errors) {
	}

	protected void onException(AjaxRequestTarget target, String stepId, Exception e) {
		if (e.getMessage() != null) {
			List<Serializable> error = new ArrayList<>();
			error.add(getString(e.getMessage(), null, e.getMessage()));
			onError(target, stepId, error);
		}
	}

	private void updateStep(WWizardStepPanel stepPanel, AjaxRequestTarget target) {
		content.replace(stepPanel);
		target.add(content);
	}

	// ---------------------------------------------------------
	// ------------------------------ WIZARD BUTTON BAR FRAGMENT

	private class WizardButtonBar extends Fragment {
		private AjaxLink prev;
		private WAjaxButton next, finish;

		public WizardButtonBar(String id, String markupId, MarkupContainer markupProvider) {
			super(id, markupId, markupProvider);

			prev = new AjaxLink("prev") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					WWizardPanel.this.onPrevious(target, oWizard.getCurrentStepId());
					WWizardStepPanel step = oWizard.getPreviousStep();
					updateButtons(target);
					WWizardPanel.this.updateStep(step, target);
				}
			};

			next = new WAjaxButton("next") {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					WWizardPanel.this.onNext(target, oWizard.getCurrentStepId());
					WWizardStepPanel step = oWizard.getNextStep();
					updateButtons(target);
					WWizardPanel.this.updateStep(step, target);
				}

				@Override
				protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
					WWizardPanel.this.onError(target, oWizard.getCurrentStepId(), errors);
				}

				@Override
				protected void onException(AjaxRequestTarget target, Exception e) {
					WWizardPanel.this.onException(target, oWizard.getCurrentStepId(), e);
				}
			};

			finish = new WAjaxButton("finish") {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					WWizardPanel.this.onFinish(target, oWizard.getCurrentStepId());
				}

				@Override
				protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
					WWizardPanel.this.onError(target, oWizard.getCurrentStepId(), errors);
				}

				@Override
				protected void onException(AjaxRequestTarget target, Exception e) {
					WWizardPanel.this.onException(target, oWizard.getCurrentStepId(), e);
				}
			};

			add(prev.setEnabled(false));
			add(next);
			add(finish.setEnabled(false));

			prev.add(new AttributeModifier("value", getLocalizer().getString("label.wizard.previous", this, "Previous")));
			next.add(new AttributeModifier("value", getLocalizer().getString("label.wizard.next", this, "Next")));
			finish.add(new AttributeModifier("value", getLocalizer().getString("label.wizard.finish", this, "Finish")));
		}

		private void updateButtons(AjaxRequestTarget target) {
			prev.setEnabled(!oWizard.isFirst());
			next.setEnabled(!oWizard.isLast());
			if (!finish.isEnabled()) {
				finish.setEnabled(oWizard.isLast());
			}
			target.add(buttonBar);
		}
	}
}