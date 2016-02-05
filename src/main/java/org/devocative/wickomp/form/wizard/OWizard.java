package org.devocative.wickomp.form.wizard;

import org.devocative.wickomp.opt.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OWizard extends Options {
	private static Logger logger = LoggerFactory.getLogger(OWizard.class);

	private List<String> stepIds = new ArrayList<>();
	private List<WWizardStepPanel> stepsPanels = new ArrayList<>();
	private Set<String> skippedSteps = new HashSet<>();
	private int stepIndex = 0;

	public OWizard addStep(String stepId, WWizardStepPanel stepPanel) {
		stepIds.add(stepId);
		stepsPanels.add(stepPanel);
		return this;
	}

	public WWizardStepPanel getFirstStep() {
		stepIndex = 0;
		while (skippedSteps.contains(stepIds.get(stepIndex))) {
			stepIndex++;
		}
		WWizardStepPanel panel = stepsPanels.get(stepIndex);
		logger.debug("Wizard step index = {}", stepIndex);
		return panel;
	}

	public WWizardStepPanel getNextStep() {
		stepIndex++;
		while (skippedSteps.contains(stepIds.get(stepIndex))) {
			stepIndex++;
		}
		WWizardStepPanel panel = stepsPanels.get(stepIndex);
		logger.debug("Wizard step index = {}", stepIndex);
		return panel;
	}

	public WWizardStepPanel getPreviousStep() {
		stepIndex--;
		while (skippedSteps.contains(stepIds.get(stepIndex))) {
			stepIndex--;
		}
		WWizardStepPanel panel = stepsPanels.get(stepIndex);
		logger.debug("Wizard step index = {}", stepIndex);
		return panel;
	}

	public void setStep(String stepId) {
		int dest = stepIds.indexOf(stepId);
		for (int i = stepIndex + 1; i <= dest; i++) {
			skippedSteps.add(stepIds.get(i));
		}
		if (dest < (stepIds.size() - 1)) {
			stepIndex = dest;
		}

		logger.debug("Wizard, setStep({}) index = {}", stepId, stepIndex);
	}

	public void clearSkippedSteps() {
		skippedSteps.clear();
	}

	public String getCurrentStepId() {
		return stepIds.get(stepIndex);
	}

	public boolean isFirst() {
		return stepIndex == 0;
	}

	public boolean isLast() {
		int i = stepIndex + 1;
		while (i < stepIds.size() && skippedSteps.contains(stepIds.get(i))) {
			i++;
		}
		return i >= stepIds.size();
	}
}
