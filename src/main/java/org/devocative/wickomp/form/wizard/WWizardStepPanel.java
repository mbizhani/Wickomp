package org.devocative.wickomp.form.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.devocative.wickomp.WPanel;

import java.io.Serializable;
import java.util.List;

public abstract class WWizardStepPanel extends WPanel {
	protected WWizardStepPanel() {
		super("step");
	}

	protected abstract void onInit();

	@Override
	protected void onInitialize() {
		super.onInitialize();

		onInit();
	}

	public void onStepSubmit() {
	}

	public boolean onError(AjaxRequestTarget target, List<Serializable> errors) {
		return true;
	}

	public boolean onException(AjaxRequestTarget target, Exception e) {
		return true;
	}
}
