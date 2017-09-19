package org.devocative.wickomp.form;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.IValueMap;

import java.util.*;

class WSelectionList<T> extends AbstractChoice<Object, T> {
	private static final long serialVersionUID = 3845967585500936689L;

	private boolean multipleSelection;

	static MetaDataKey<Boolean> RETAIN_DISABLED_META_KEY = new MetaDataKey<Boolean>() {
		private static final long serialVersionUID = 1L;
	};


	public WSelectionList(String id, List<? extends T> choices, boolean multipleSelection) {
		this(id, null, choices, multipleSelection);
	}

	// Main Constructor
	public WSelectionList(String id, IModel<Object> model, List<? extends T> choices, boolean multipleSelection) {
		super(id, model, choices);
		this.multipleSelection = multipleSelection;
	}

	// --------------------- ACCESSORS

	public boolean isMultipleSelection() {
		return multipleSelection;
	}

	public WSelectionList<T> setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
		return this;
	}

	// --------------------- INTERNAL METHODS

	@Override
	protected String getModelValue() {
		if (multipleSelection) {
			final AppendingStringBuffer buffer = new AppendingStringBuffer();

			final Collection<T> selectedValues = (Collection<T>) getModelObject();

			if (selectedValues != null) {
				final List<? extends T> choices = getChoices();
				for (T object : selectedValues) {
					if (buffer.length() > 0) {
						buffer.append(VALUE_SEPARATOR);
					}
					int index = choices.indexOf(object);
					buffer.append(getChoiceRenderer().getIdValue(object, index));
				}
			}

			return buffer.toString();
		} else {
			final T object = (T) getModelObject();
			if (object != null) {
				int index = getChoices().indexOf(object);
				return getChoiceRenderer().getIdValue(object, index);
			} else {
				return "";
			}
		}
	}

	@Override
	protected boolean isSelected(T choiceOrObject, int index, String selected) {
		if (multipleSelection) {
			if (selected != null) {
				// Loop through ids
				for (final StringTokenizer tokenizer = new StringTokenizer(selected, VALUE_SEPARATOR); tokenizer.hasMoreTokens(); ) {
					final String id = tokenizer.nextToken();
					if (id.equals(getChoiceRenderer().getIdValue(choiceOrObject, index))) {
						return true;
					}
				}
			}
			return false;
		} else
			return (selected != null) && selected.equals(getChoiceRenderer().getIdValue(choiceOrObject, index));
	}

	@Override
	protected Object convertValue(String[] ids) throws ConversionException {
		if (multipleSelection) {
			if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0])) {
				return convertChoiceIdsToChoices(ids);
			} else {
				ArrayList<T> result = new ArrayList<>();
				addRetainedDisabled(result);
				return result.size() > 0 ? result : null;
			}
		} else {
			String tmp = ((ids != null) && (ids.length > 0)) ? ids[0] : null;
			return convertChoiceIdToChoice(tmp);
		}
	}

	@Override
	public final void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
		// Iterate through choices
		final List<? extends T> choices = getChoices();

		// Buffer to hold generated body
		final AppendingStringBuffer buffer = new AppendingStringBuffer((choices.size() + 1) * 70);

		// The selected value
		final String selected = getModelValue();

		buffer.append("<table>");
		// Loop through choices
		for (int index = 0; index < choices.size(); index++) {
			// Get next choice
			final T choice = choices.get(index);

			appendOptionHtml(buffer, choice, index, selected);
		}
		buffer.append("</table>");

		// Replace body
		replaceComponentTagBody(markupStream, openTag, buffer);
	}

	protected boolean isRetainDisabledSelected() {
		Boolean flag = getMetaData(RETAIN_DISABLED_META_KEY);
		return (flag != null && flag);
	}

	@Override
	protected void appendOptionHtml(final AppendingStringBuffer buffer, final T choice, int index, final String selected) {
		Object displayValue = getChoiceRenderer().getDisplayValue(choice);
		Class<?> objectClass = (displayValue == null ? null : displayValue.getClass());

		// Get label for choice
		String label = "";

		if (objectClass != null && objectClass != String.class) {
			final IConverter converter = getConverter(objectClass);
			label = converter.convertToString(displayValue, getLocale());
		} else if (displayValue != null) {
			label = displayValue.toString();
		}

		// If there is a display value for the choice, then we know that the
		// choice is automatic in some way. If label is /null/ then we know
		// that the choice is a manually created radio tag at some random
		// location in the page markup!
		if (label != null) {
			String id = getChoiceRenderer().getIdValue(choice, index);
			final String idAttr = getMarkupId() + "-" + id;

			boolean enabled = isEnabledInHierarchy() && !isDisabled(choice, index, selected);

			// Add radio tag
			buffer.append("<tr><td><input name=\"")
				.append(getInputName())
				.append('"');
			if (multipleSelection)
				buffer.append(" type=\"checkbox\"");
			else
				buffer.append(" type=\"radio\"");
			buffer.append((isSelected(choice, index, selected) ? " checked=\"checked\"" : ""))
				.append((enabled ? "" : " disabled=\"disabled\""))
				.append(" value=\"")
				.append(id)
				.append("\" id=\"")
				.append(idAttr)
				.append('"');

			// Should a roundtrip be made (have onSelectionChanged called)
			// when the option is clicked?
			if (!multipleSelection && wantOnSelectionChangedNotifications()) {
				CharSequence url = urlFor(IOnChangeListener.INTERFACE, new PageParameters());

				Form<?> form = findParent(Form.class);
				if (form != null) {
					buffer.append(" onclick=\"")
						.append(form.getJsForInterfaceUrl(url))
						.append(";\"");
				} else {
					// NOTE: do not encode the url as that would give
					// invalid JavaScript
					buffer.append(" onclick=\"window.location.href='")
						.append(url)
						.append((url.toString().indexOf('?') > -1 ? '&' : '?') + getInputName())
						.append('=')
						.append(id)
						.append("';\"");
				}
			}

			// Allows user to add attributes to the <input..> tag
			{
				IValueMap attrs = getAdditionalAttributes(index, choice);
				if (attrs != null) {
					for (Map.Entry<String, Object> attr : attrs.entrySet()) {
						buffer.append(' ')
							.append(attr.getKey())
							.append("=\"")
							.append(attr.getValue())
							.append('"');
					}
				}
			}

			if (getApplication().getDebugSettings().isOutputComponentPath()) {
				CharSequence path = getPageRelativePath();
				path = Strings.replaceAll(path, "_", "__");
				path = Strings.replaceAll(path, ":", "_");
				buffer.append(" wicketpath=\"")
					.append(path)
					.append("_input_")
					.append(index)
					.append('"');
			}

			buffer.append("/></td>");

			// Add label for radio button
			String display = label;
			if (localizeDisplayValues()) {
				display = getLocalizer().getString(label, this, label);
			}

			CharSequence escaped = display;
			if (getEscapeModelStrings()) {
				escaped = Strings.escapeMarkup(display);
			}

			buffer.append("<td><label for=\"")
				.append(idAttr)
				.append("\">")
				.append(escaped)
				.append("</label></td></tr>");
		}
	}

	protected IValueMap getAdditionalAttributes(final int index, final T choice) {
		return null;
	}

	// Multiple Selection
	protected List<T> convertChoiceIdsToChoices(String[] ids) {
		List<T> selectedValues = new ArrayList<T>();

		// If one or more ids is selected
		if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0])) {
			// Get values that could be selected
			final Map<String, T> choiceIds2choiceValues = createChoicesIdsMap();

			// Loop through selected indices
			for (String id : ids) {
				if (choiceIds2choiceValues.containsKey(id)) {
					selectedValues.add(choiceIds2choiceValues.get(id));
				}
			}
		}
		addRetainedDisabled(selectedValues);

		return selectedValues;

	}

	// Single Selection
	protected T convertChoiceIdToChoice(String id) {
		final List<? extends T> choices = getChoices();
		final IChoiceRenderer<? super T> renderer = getChoiceRenderer();
		for (int index = 0; index < choices.size(); index++) {
			// Get next choice
			final T choice = choices.get(index);
			if (renderer.getIdValue(choice, index).equals(id)) {
				return choice;
			}
		}
		return null;
	}

	protected boolean wantOnSelectionChangedNotifications() {
		return false;
	}

	private Map<String, T> createChoicesIdsMap() {
		final List<? extends T> choices = getChoices();

		final Map<String, T> choiceIds2choiceValues = new HashMap<>(choices.size(), 1);

		for (int index = 0; index < choices.size(); index++) {
			// Get next choice
			final T choice = choices.get(index);
			choiceIds2choiceValues.put(getChoiceRenderer().getIdValue(choice, index), choice);
		}
		return choiceIds2choiceValues;
	}

	private void addRetainedDisabled(List<T> selectedValues) {
		if (isRetainDisabledSelected()) {
			Collection<T> unchangedModel = (Collection<T>) getModelObject();
			String selected;
			{
				StringBuilder builder = new StringBuilder();
				for (T t : unchangedModel) {
					builder.append(t);
					builder.append(";");
				}
				selected = builder.toString();
			}
			List<? extends T> choices = getChoices();
			for (int i = 0; i < choices.size(); i++) {
				final T choice = choices.get(i);
				if (isDisabled(choice, i, selected)) {
					if (unchangedModel.contains(choice)) {
						if (!selectedValues.contains(choice)) {
							selectedValues.add(choice);
						}
					}
				}
			}
		}
	}
}
