package org.devocative.wickomp.form.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.devocative.wickomp.WPanel;

import java.io.Serializable;
import java.util.List;

public abstract class WWizardStepPanel extends WPanel {
	private static final long serialVersionUID = -3820276087741941301L;

	protected WWizardStepPanel() {
		super("step");
	}

	protected abstract void onInit();

	@Override
	protected void onInitialize() {
		super.onInitialize();

		onInit();
	}

	public boolean onStepSubmit(AjaxRequestTarget target) {
		return true;
	}

	public boolean onError(AjaxRequestTarget target, List<Serializable> errors) {
		return true;
	}

	public boolean onException(AjaxRequestTarget target, Exception e) {
		return true;
	}
}
