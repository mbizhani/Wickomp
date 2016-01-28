package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.form.*;
import org.devocative.wickomp.form.code.OCode;
import org.devocative.wickomp.form.code.OCodeMode;
import org.devocative.wickomp.form.code.WCodeInput;
import org.devocative.wickomp.vo.Field;
import org.devocative.wickomp.vo.KeyValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class FormPage extends BasePage {

	public FormPage() {
		simpleForm();
		dynamicForm();
	}

	private void dynamicForm() {
		List<Field> fields = new ArrayList<>();
		fields.add(new Field("Name", "name", Field.Type.String));
		fields.add(new Field("Age", "age", Field.Type.Integer));
		fields.add(new Field("Weight", "weight", Field.Type.Real));

		final Map<String, Serializable> map = new HashMap<>();
		map.put("name", "Joe");
		map.put("age", 123456);

		Form<Map<String, Serializable>> dynamicForm = new Form<>("dynamicForm", new CompoundPropertyModel<>(map));

		dynamicForm.add(new ListView<Field>("fields", fields) {
			@Override
			protected void populateItem(ListItem<Field> item) {
				Field field = item.getModelObject();
				item.add(new Label("label", field.getTitle()));
				//item.add(new TextField<String>("field"));

				RepeatingView view = new RepeatingView("field");
				switch (field.getType()) {

					case String:
						view.add(new WTextInput(field.getName()));
						break;

					case Integer:
						view.add(new WNumberInput(field.getName(), Long.class).setThousandSeparator(","));
						break;

					case Real:
						view.add(new WNumberInput(field.getName(), BigDecimal.class).setPrecision(4).setThousandSeparator(","));
						break;

					case Boolean:
						break;
				}

				item.add(view);
			}
		});

		dynamicForm.add(new Button("save") {

			@Override
			public void onSubmit() {
				System.out.println(map);
			}
		});
		add(dynamicForm);
	}

	private String sql;

	private void simpleForm() {
		List<KeyValue> list = new ArrayList<>();
		list.add(new KeyValue("A", "Alef"));
		list.add(new KeyValue("B", "Be"));
		list.add(new KeyValue("C", "Cee"));
		list.add(new KeyValue("D", "Dee"));


		final WSelectionInput child, parentSI;
		final Map<String, Serializable> map = new HashMap<>();
		map.put("name", "Joe");
		map.put("child", "B.1");

		Form<Map<String, Serializable>> form = new Form<>("form", new CompoundPropertyModel<>(map));
		form.add(new TextField<String>("name"));
		form.add(new WNumberRangeInput("age", Integer.class).setThousandSeparator(","));
		form.add(new WSelectionInput("eduSingle", list, false));
		form.add(new WSelectionInput("eduMultiple", list, true));
		form.add(new DropDownChoice<>("eduDD", list));
		form.add(new WDateInput("birthdate").setTimePartVisible(true));
		form.add(new WBooleanInput("alive"));
		//form.add(new WDateRangeInput("dateRange"));
		form.add(parentSI = new WSelectionInput("parent", Arrays.asList("A", "B", "C"), false));
		form.add(child = new WSelectionInput("child", Arrays.asList("B.1"), false));
		form.add(new WCodeInput("sql", new PropertyModel<String>(this, "sql"), new OCode(OCodeMode.SQL)));
		form.add(new Button("save") {
			//		form.add(new AjaxButton("save") {
			public void onSubmit() {
				theSubmit();
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				theSubmit();
			}

			private void theSubmit() {
				System.out.println("MAP {{");
				for (Map.Entry<String, Serializable> entry : map.entrySet()) {
					System.out.printf("\t%s = %s (%s)\n", entry.getKey(), entry.getValue(),
						entry.getValue() != null ? entry.getValue().getClass().getName() : "-");
				}
				System.out.println("}}");

				System.out.println("sql = " + sql);
			}
		});
		add(form);

		parentSI.addToChoices(new WSelectionInputAjaxUpdatingBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Object parentSel = getComponent().getDefaultModelObject();
				child.updateChoices(target, Arrays.asList(
					String.format("%s.1", parentSel),
					String.format("%s.2", parentSel),
					String.format("%s.3", parentSel)
				));
			}
		});
	}
}
