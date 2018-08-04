package org.devocative.wickomp.form.wizard;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.devocative.wickomp.IExceptionToMessageHandler;
import org.devocative.wickomp.WDefaults;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.form.WAjaxButton;
import org.devocative.wickomp.html.WAjaxLink;
import org.devocative.wickomp.html.icon.FontAwesome;
import org.devocative.wickomp.opt.OLayoutDirection;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WWizardPanel extends WPanel {
	private static final long serialVersionUID = 5674981873642697757L;

	public enum ButtonBarPlace {TOP, BOTTOM}

	// ------------------------------

	private OWizard oWizard;
	private WizardButtonBar buttonBar;
	private WebMarkupContainer buttonBarContainer, content;
	private Label titleLbl;
	private boolean titleChanged = false;
	private String title;
	private IExceptionToMessageHandler exceptionToMessageHandler = WDefaults.getExceptionToMessageHandler();

	// ------------------------------

	public WWizardPanel(String id, OWizard oWizard) {
		this(id, oWizard, ButtonBarPlace.BOTTOM);
	}

	// Main Constructor
	public WWizardPanel(String id, OWizard oWizard, ButtonBarPlace place) {
		super(id);
		this.oWizard = oWizard;

		add(titleLbl = new Label("title", new PropertyModel<>(this, "title")));
		titleLbl
			.setOutputMarkupId(true)
			.setOutputMarkupPlaceholderTag(true);

		WebMarkupContainer top = new WebMarkupContainer("top");
		add(top);

		WebMarkupContainer bottom = new WebMarkupContainer("bottom");
		add(bottom);

		switch (place) {
			case TOP:
				top.add(buttonBar = new WizardButtonBar("topButtonBar", "buttonBarFragment", this));
				bottom.add(new WebMarkupContainer("bottomButtonBar"));
				buttonBarContainer = top;
				break;

			default:
				top.add(new WebMarkupContainer("topButtonBar"));
				bottom.add(buttonBar = new WizardButtonBar("bottomButtonBar", "buttonBarFragment", this));
				buttonBarContainer = bottom;
				break;
		}
		buttonBarContainer.setOutputMarkupId(true);

		content = new WebMarkupContainer("content");
		content.setOutputMarkupId(true);
		content.add(oWizard.getFirstStep());
		add(content);

		add(new FontAwesomeBehavior());
	}

	// ------------------------------

	public String getTitle() {
		return title;
	}

	public WWizardPanel setTitle(String title) {
		this.title = title;
		this.titleChanged = true;
		return this;
	}

	public WWizardPanel setTitleVisible(boolean visible) {
		titleLbl.setVisible(visible);
		this.titleChanged = true;
		return this;
	}

	public WWizardPanel setExceptionToMessageHandler(IExceptionToMessageHandler exceptionToMessageHandler) {
		this.exceptionToMessageHandler = exceptionToMessageHandler;
		return this;
	}

	public WWizardPanel setFinishConfirmationMessage(IModel<String> msg) {
		buttonBar.setFinishConfirmationMessage(msg);
		return this;
	}

	public WWizardPanel setCancelButtonVisible(boolean visible) {
		buttonBar.setCancelButtonVisible(visible);
		return this;
	}

	public WWizardPanel setCancelButtonLabel(IModel<String> label) {
		buttonBar.setCancelButtonLabel(label);
		return this;
	}

	public WWizardPanel setCancelConfirmationMessage(IModel<String> msg) {
		buttonBar.setCancelConfirmationMessage(msg);
		return this;
	}

	public WWizardPanel setFinishEnabled(boolean enabled) {
		buttonBar.setFinishEnabled(enabled);
		return this;
	}

	// ------------------------------

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

	protected void onCancel(AjaxRequestTarget target, String stepId) {
	}

	protected void onException(AjaxRequestTarget target, String stepId, Exception e) {
		if (e.getMessage() != null) {
			List<Serializable> error = new ArrayList<>();
			error.add(exceptionToMessageHandler.handleMessage(this, e));
			onError(target, stepId, error);
		}
	}

	// ------------------------------

	private void updateStep(WWizardStepPanel stepPanel, AjaxRequestTarget target) {
		content.replace(stepPanel);
		target.add(content);

		if (titleChanged) {
			target.add(titleLbl);
			titleChanged = false;
		}
	}

	// ---------------------------------------------------------
	// ------------------------------ WIZARD BUTTON BAR FRAGMENT

	private class WizardButtonBar extends Fragment {
		private static final long serialVersionUID = -7432284770157489018L;

		private WAjaxLink prev, cancel;
		private WAjaxButton next, finish;
		private Label cancelLabel;

		// ---------------

		public WizardButtonBar(String id, String markupId, MarkupContainer markupProvider) {
			super(id, markupId, markupProvider);

			prev = new WAjaxLink("prev") {
				private static final long serialVersionUID = 8698300849201984560L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					WWizardPanel.this.onPrevious(target, oWizard.getCurrentStepId());
					WWizardStepPanel step = oWizard.getPreviousStep();
					updateButtons(target);
					WWizardPanel.this.updateStep(step, target);
				}
			};

			next = new WAjaxButton("next", new ResourceModel("WWizardPanel.next")) {
				private static final long serialVersionUID = -4723527907345120965L;

				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					if (oWizard.getCurrentStep().onStepSubmit(target)) {
						WWizardPanel.this.onNext(target, oWizard.getCurrentStepId());

						WWizardStepPanel step = oWizard.getNextStep();
						updateButtons(target);
						WWizardPanel.this.updateStep(step, target);
					}
				}

				@Override
				protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
					if (oWizard.getCurrentStep().onError(target, errors)) {
						WWizardPanel.this.onError(target, oWizard.getCurrentStepId(), errors);
					}
				}

				@Override
				protected void onException(AjaxRequestTarget target, Exception e) {
					if (oWizard.getCurrentStep().onException(target, e)) {
						WWizardPanel.this.onException(target, oWizard.getCurrentStepId(), e);
					}
				}
			};

			finish = new WAjaxButton("finish", new ResourceModel("WWizardPanel.finish"), new FontAwesome("check-circle")) {
				private static final long serialVersionUID = -5032119350215093190L;

				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					if (oWizard.getCurrentStep().onStepSubmit(target)) {
						WWizardPanel.this.onFinish(target, oWizard.getCurrentStepId());
					}
				}

				@Override
				protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
					if (oWizard.getCurrentStep().onError(target, errors)) {
						WWizardPanel.this.onError(target, oWizard.getCurrentStepId(), errors);
					}
				}

				@Override
				protected void onException(AjaxRequestTarget target, Exception e) {
					if (oWizard.getCurrentStep().onException(target, e)) {
						WWizardPanel.this.onException(target, oWizard.getCurrentStepId(), e);
					}
				}
			};

			cancel = new WAjaxLink("cancel") {
				private static final long serialVersionUID = 2912841408299541902L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					WWizardPanel.this.onCancel(target, oWizard.getCurrentStepId());
				}
			};
			cancel.add(cancelLabel = new Label("cancelLabel", new ResourceModel("WWizardPanel.cancel")));

			add(prev.setEnabled(false));
			add(next);
			add(finish.setEnabled(false));
			add(cancel.setVisible(false));

			prev.add(new Label("prevLbl", new ResourceModel("WWizardPanel.previous")));
			if (getUserPreference().getLayoutDirection() == OLayoutDirection.RTL) {
				prev.add(new WebComponent("prevIco").add(new AttributeModifier("class", "fa fa-chevron-right")));
				next.setIcon(new FontAwesome("chevron-left"));
			} else {
				prev.add(new WebComponent("prevIco").add(new AttributeModifier("class", "fa fa-chevron-left")));
				next.setIcon(new FontAwesome("chevron-right"));
			}
		}

		// ---------------

		public WizardButtonBar setFinishConfirmationMessage(IModel<String> msg) {
			finish.setConfirmationMessage(msg);
			return this;
		}

		public WizardButtonBar setCancelButtonVisible(boolean visible) {
			cancel.setVisible(visible);
			return this;
		}

		public WizardButtonBar setCancelButtonLabel(IModel<String> label) {
			cancelLabel.setDefaultModel(label);
			return this;
		}

		public WizardButtonBar setCancelConfirmationMessage(IModel<String> msg) {
			cancel.setConfirmationMessage(msg);
			return this;
		}

		public WizardButtonBar setFinishEnabled(boolean enabled) {
			finish.setEnabled(enabled);
			return this;
		}

		// ---------------

		private void updateButtons(AjaxRequestTarget target) {
			prev.setEnabled(!oWizard.isFirst());
			next.setEnabled(!oWizard.isLast());
			if (!finish.isEnabled()) {
				finish.setEnabled(oWizard.isLast());
			}
			target.add(buttonBarContainer);
		}
	}
}
