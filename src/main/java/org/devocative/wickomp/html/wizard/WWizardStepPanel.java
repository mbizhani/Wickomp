package org.devocative.wickomp.html.wizard;

import org.devocative.wickomp.WPanel;

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
}
